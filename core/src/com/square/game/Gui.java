package com.square.game;

import java.util.Vector;

/**
 * Created by Mikolaj on 11.10.2015.
 */
public class Gui {

    public static Gui current_gui;
    public static Gui game;
    public static Gui menu;
    public static Gui levels;
    public static Gui level_cleared;
    public static Gui tutorial;
    public static Gui settings;

    public static final int GUI_NONE = 0;
    public static final int GUI_GAME = 1;
    public static final int GUI_MENU = 2;
    public static final int GUI_LEVELS = 3;
    public static final int GUI_LEVEL_CLEARED = 4;
    public static final int GUI_TUTORIAL = 5;
    public static final int GUI_SETTINGS = 6;

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

    public void onSet()
    {
    }

    public void onUnset()
    {

    }

    protected void resetAll()
    {
        for(int i = 0; i < elements.size(); i++)
        {
            elements.get(i).resetPosition();
        }
    }

    protected void setAnimationForEach(GuiElement.AnimationType type, int time)
    {
        int most = 0;
        int cur_val = 0;

        if(type == GuiElement.AnimationType.SLIDE_FROM_LEFT || type == GuiElement.AnimationType.SLIDE_TO_LEFT)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                if(elements.get(i).getX() + elements.get(i).getWidth() > cur_val)
                {
                    most = i;
                    cur_val = elements.get(i).getX() + elements.get(i).getWidth();
                }
            }

            for(int i = 0; i < elements.size(); i++)
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

            for(int i = 0; i < elements.size(); i++)
            {
                elements.get(i).setAnimation(type, elements.get(i).getY() - cur_val, time);
            }
        }
    }

    protected void setAnimationExcluding(GuiElement.AnimationType type, int time, int[] excluding)
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

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
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

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
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

    protected void setAnimationForEach(GuiElement.AnimationType type, int time, GuiElement.AnimationCallback callback)
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

    protected void setAnimationExcluding(GuiElement.AnimationType type, int time, int[] excluding, GuiElement.AnimationCallback callback)
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

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
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

                for(int e_i = 0; e_i < excluding.length; e_i++)
                {
                    if(i == excluding[e_i])
                    {
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

    protected Vector<GuiElement> elements;

    public Gui(){
        elements = new Vector<GuiElement>();
    }

    public int getSize()
    {
        return elements.size();
    }

    public int addElement(GuiElement e)
    {
        elements.add(e);

        return elements.size()-1;
    }

    public void removeElement(int i)
    {
        elements.remove(i);
    }

    public void clear()
    {
        elements.clear();
    }

    public void draw(Render ren)
    {
        for(int i = 0; i < elements.size(); i++)
        {
            elements.get(i).draw(ren);
        }
    }

    public void update(float time)
    {
        for(int i = 0; i < elements.size(); i++)
        {
            elements.get(i).update(time);
        }
    }
}
