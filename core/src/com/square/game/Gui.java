package com.square.game;

import java.util.Vector;

/**
 * Klasa obsługująca graficzny interfejs użytkownika.
 */
public class Gui {

    static Gui current_gui;
    public static Gui game;
    static Gui menu;
    static Gui levels;
    static Gui level_cleared;
    static Gui tutorial;
    static Gui settings;

    private static final int GUI_NONE = 0;
    static final int GUI_GAME = 1;
    static final int GUI_MENU = 2;
    static final int GUI_LEVELS = 3;
    static final int GUI_LEVEL_CLEARED = 4;
    static final int GUI_TUTORIAL = 5;
    static final int GUI_SETTINGS = 6;

    /**
     * Ustaw interfejs użytkownika.
     *
     * @param id    Id interfejsu.
     */
    public static void setGui(int id)
    {
        if(current_gui != null) current_gui.onUnset();

        switch (id)
        {
            case GUI_NONE: current_gui = null; break;
            case GUI_GAME: current_gui = game; break;
            case GUI_MENU: current_gui = menu; break;
            case GUI_LEVELS: current_gui = levels; break;
            case GUI_LEVEL_CLEARED: current_gui = level_cleared; break;
            case GUI_TUTORIAL: current_gui = tutorial; break;
            case GUI_SETTINGS: current_gui = settings; break;
        }

        if(current_gui != null) current_gui.onSet();
    }

    /**
     * Kiedy interfejs zostanie ustawiony.
     */
    public void onSet()
    {
    }

    /**
     * Kiedy interfejs przestanie być aktywny.
     */
    public void onUnset()
    {

    }

    /**
     * Ustaw domyślne ustawienie interfejstu
     */
    void resetAll()
    {
        for (GuiElement element : elements) {
            element.resetPosition();
        }
    }

    /**
     * Dla każdego elementu interfejsu ustaw animację.
     *
     * @param type  Rodzaj animacji.
     * @param time  Długość trwania.
     */
    void setAnimationForEach(GuiElement.AnimationType type, int time)
    {
        int cur_val = 0;

        if(type == GuiElement.AnimationType.SLIDE_FROM_LEFT || type == GuiElement.AnimationType.SLIDE_TO_LEFT)
        {
            for (GuiElement element : elements) {
                if (element.getX() + element.getWidth() > cur_val) {
                    cur_val = element.getX() + element.getWidth();
                }
            }

            for (GuiElement element : elements) {
                element.setAnimation(type, element.getX() - cur_val, time);
            }
        } else if(type == GuiElement.AnimationType.SLIDE_FROM_BOTTOM || type == GuiElement.AnimationType.SLIDE_TO_BOTTOM)
        {
            for (GuiElement element : elements) {
                if (element.getY() + element.getHeight() > cur_val) {
                    cur_val = element.getY() + element.getHeight();
                }
            }

            for (GuiElement element : elements) {
                element.setAnimation(type, element.getY() - cur_val, time);
            }
        }
    }

    /**
     * Ustaw animację dla każdego elementu interfejsu.
     *
     * @param type          Rodzaj animacji.
     * @param time          Czas trwania animacji.
     * @param excluding     Id elementów, które nie mają być wliczane do animacji.
     */
    void setAnimationExcluding(GuiElement.AnimationType type, int time, int[] excluding)
    {
        int cur_val = 0;

        if(type == GuiElement.AnimationType.SLIDE_FROM_LEFT || type == GuiElement.AnimationType.SLIDE_TO_LEFT)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
                        cont = false;
                    }
                }

                if(cont)
                {
                    if(elements.get(i).getX() + elements.get(i).getWidth() > cur_val)
                    {
                        cur_val = elements.get(i).getX() + elements.get(i).getWidth();
                    }
                }
            }

            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for (int anExcluding : excluding) {
                    if (i == anExcluding) {
                        cont = false;
                    }
                }

                if(cont) elements.get(i).setAnimation(type, elements.get(i).getX() - cur_val, time);
            }
        } else if(type == GuiElement.AnimationType.SLIDE_FROM_BOTTOM || type == GuiElement.AnimationType.SLIDE_TO_BOTTOM)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
                        cont = false;
                    }
                }

                if(cont)
                {
                    if(elements.get(i).getY() + elements.get(i).getHeight() > cur_val)
                    {
                        cur_val = elements.get(i).getY() + elements.get(i).getHeight();
                    }
                }


            }

            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for (int anExcluding : excluding) {
                    if (i == anExcluding) {
                        cont = false;
                    }
                }

                if(cont)
                {
                    elements.get(i).setAnimation(type, elements.get(i).getY() - cur_val, time);
                }
            }
        }
    }

    /**
     * Ustaw animację dla każdego elementu interfejsu.
     *
     * @param type          Rodzaj animacji.
     * @param time          Czas trwania animacji.
     * @param callback      Reakcja na zdarzenie zakończenia animacji.
     */
    void setAnimationForEach(GuiElement.AnimationType type, int time, GuiElement.AnimationCallback callback)
    {
        int cur_val = 0;

        if(type == GuiElement.AnimationType.SLIDE_FROM_LEFT || type == GuiElement.AnimationType.SLIDE_TO_LEFT)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                if(elements.get(i).getX() + elements.get(i).getWidth() > cur_val)
                {
                    cur_val = elements.get(i).getX() + elements.get(i).getWidth();
                }
            }

            elements.get(0).setAnimation(type, time, elements.get(0).getX() - cur_val, callback);

            for(int i = 1; i < elements.size(); i++)
            {
                elements.get(i).setAnimation(type, elements.get(i).getX() - cur_val, time);
            }
        } else if(type == GuiElement.AnimationType.SLIDE_FROM_BOTTOM || type == GuiElement.AnimationType.SLIDE_TO_BOTTOM)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                if(elements.get(i).getY() + elements.get(i).getHeight() > cur_val)
                {
                    cur_val = elements.get(i).getY() + elements.get(i).getHeight();
                }
            }

            elements.get(0).setAnimation(type, time, elements.get(0).getY() - cur_val, callback);

            for(int i = 1; i < elements.size(); i++)
            {
                elements.get(i).setAnimation(type, elements.get(i).getY() - cur_val, time);
            }
        }


    }

    /**
     * Ustaw animację dla każdego elementu interfejsu.
     *
     * @param type          Rodzaj animacji.
     * @param time          Czas trwania animacji.
     * @param excluding     Id elementów interfejsu, których animacja ma niedotyczyć.
     * @param callback      Reakcja na zdarzenie zakończenia animacji.
     */
    void setAnimationExcluding(GuiElement.AnimationType type, int time, int[] excluding, GuiElement.AnimationCallback callback)
    {
        int cur_val = 0;

        if(type == GuiElement.AnimationType.SLIDE_FROM_LEFT || type == GuiElement.AnimationType.SLIDE_TO_LEFT)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
                        cont = false;
                    }
                }

                if(cont)
                {
                    if(elements.get(i).getX() + elements.get(i).getWidth() > cur_val)
                    {
                        cur_val = elements.get(i).getX() + elements.get(i).getWidth();
                    }
                }
            }

            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for (int anExcluding : excluding) {
                    if (i == anExcluding) {
                        cont = false;
                    }
                }

                if(cont)
                {
                    if(i == 0)
                    {
                        elements.get(0).setAnimation(type, time, elements.get(0).getX() - cur_val, callback);
                    } else
                    {
                        elements.get(i).setAnimation(type, elements.get(i).getX() - cur_val, time);
                    }
                }
            }
        } else if(type == GuiElement.AnimationType.SLIDE_FROM_BOTTOM || type == GuiElement.AnimationType.SLIDE_TO_BOTTOM)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
                        cont = false;
                    }
                }

                if(cont)
                {
                    if(elements.get(i).getY() + elements.get(i).getHeight() > cur_val)
                    {
                        cur_val = elements.get(i).getY() + elements.get(i).getHeight();
                    }
                }


            }

            for(int i = 0; i < elements.size(); i++)
            {
                boolean cont = true;

                for (int anExcluding : excluding) {
                    if (i == anExcluding) {
                        cont = false;
                    }
                }

                if(cont)
                {
                    if(i == 0)
                    {
                        elements.get(0).setAnimation(type, time, elements.get(0).getY() - cur_val, callback);
                    } else
                    {
                        elements.get(i).setAnimation(type, elements.get(i).getY() - cur_val, time);
                    }

                }
            }
        }
    }

    Vector<GuiElement> elements;

    /**
     * Konstruktor klasy GUI.
     */
    public Gui(){
        elements = new Vector<GuiElement>();
    }

    /**
     * Podaj ilość elementów interfejsu.
     *
     * @return Ilość elementów interfejsu.
     */
    public int getSize()
    {
        return elements.size();
    }

    /**
     * Dodaj element interfejsu.
     *
     * @param e     Element interfejsu.
     */
    void addElement(GuiElement e)
    {
        elements.add(e);
    }

    /**
     * Usuń wszystkie elementu interfejsu.
     */
    void clear()
    {
        elements.clear();
    }

    /**
     * Metoda wykonywana co cylk rysowania gry.
     *
     * @param ren   Obiekt klasy render.
     */
    public void draw(Render ren)
    {
        for (GuiElement element : elements) {
            element.draw(ren);
        }
    }

    /**
     * Metoda wykonywana co cylk logiki gry.
     *
     * @param time  Do usunięcia.
     */
    public void update(float time)
    {
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).update(time);
        }
    }
}
