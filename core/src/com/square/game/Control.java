package com.square.game;

import com.badlogic.gdx.Gdx;

import java.util.Vector;

/**
 * Klasa obsługująca polecenia gracza.
 */
public class Control {

    private ControlMode mode;
    private int selectedItem;
    private Gui currentGui;
    private boolean refreshGame = false;

    private int editItem = 1;

    boolean editMode = false;

    int startingBlock= 9;

    Render render;
    private NodeManager nodeMan;

    private Vector<InvItem> items;

    /**
     * Konstruktor klasy Control.
     *
     * @param render    Obiekt odpowiedzialny za render.
     * @param nodeMan   Menadżer bloków.
     */
    public Control(Render render, NodeManager nodeMan)
    {
        selectedItem = 0;
        mode = ControlMode.NONE;
        this.render = render;
        this.nodeMan = nodeMan;
    }

    /**
     * Ustaw konkretny interfejs użytkownika.
     *
     * @param gui interfejs użytkownika.
     */
    public void setGui(Gui gui)
    {
        currentGui = gui;
    }

    /**
     * Ustaw wektor przedmiotów.
     *
     * @param its Przedmioty.
     */
    void setStartingItems(Vector<InvItem> its)
    {
        items = its;
    }


    /**
     * Klasa wewnętrzna zawierająca reakcje na wydarzenie przedmiotów ekwipunku.
     *
     */
    public class ButtonActionNode implements ButtonAction {

        int index;

        /**
         * Konstruktor klasy ButtonActionNode.
         *
         * @param index Indeks przedmiotu ekwipunktu.
         */
        ButtonActionNode(int index)
        {
            this.index = index;
        }


        @Override
        public void onPress(Button but, float time) {
        }

        @Override
        public void onTap(Button but, float time) {
        }

        /**
         * Wybierz ten przedmiot ekwipunku po puszczeniu przycisku.
         *
         * @param but Przycisk.
         * @param time Do usunięcia.
         */
        @Override
        public void onTapRelease(Button but, float time) {
            selectedItem = index;
            setMode(ControlMode.ADD);
        }
    }

    /**
     * Załaduj intefejs użytkownika do ekwipunku.
     */
    void loadInventoryGui()
    {
        currentGui.clear();

        Button buttonPlay;

        buttonPlay = new Button(Render.TEXTURE_BUTTON_PLAY, 16, Gdx.graphics.getHeight()-16-96, 96, 96);
        buttonPlay.setAction(new ButtonAction() {
            @Override
            public void onPress(Button but, float time) {
            }

            @Override
            public void onTap(Button but, float time) {
            }

            @Override
            public void onTapRelease(Button but, float time) {

                setMode(ControlMode.RUN);
            }
        });

        Button button_remove;

        button_remove = new Button(Render.TEXTURE_BUTTON_REMOVE, 16 + 96 + 16, Gdx.graphics.getHeight()-16-96, 96, 96);
        button_remove.setAction(new ButtonAction() {
            @Override
            public void onPress(Button but, float time) {
            }

            @Override
            public void onTap(Button but, float time) {
            }

            @Override
            public void onTapRelease(Button but, float time) {
                setMode(ControlMode.REMOVE);
            }
        });

        currentGui.addElement(buttonPlay);
        currentGui.addElement(button_remove);

        currentGui.addElement(new Button(Render.TEXTURE_BUTTON_EXIT, Gdx.graphics.getWidth() - 16 - 96, Gdx.graphics.getHeight() - 16 - 96, 96, 96).setAction(new ButtonAction() {
            @Override
            public void onPress(Button but, float time) {

            }

            @Override
            public void onTap(Button but, float time) {

            }

            @Override
            public void onTapRelease(Button but, float time) {
                Gui.setGui(Gui.GUI_MENU);
            }
        }));

        for(int i = 0; i < items.size(); i++)
        {
            Button button_node;

            button_node = new Button(nodeMan.getNode(items.get(i).type).getTexture(), 32 + 96 + (i+1)*(96+16), Gdx.graphics.getHeight()-16-96, 96, 96);
            button_node.setAction(new ButtonActionNode(i));

            currentGui.addElement(button_node);
        }

        if(editMode)
        {
            currentGui.addElement(new Button(Render.TEXTURE_PLUS, 16, 16, 96, 96).setAction(new ButtonAction() {
                @Override
                public void onPress(Button but, float time) {

                }

                @Override
                public void onTap(Button but, float time) {

                }

                @Override
                public void onTapRelease(Button but, float time) {
                    addItem(editItem, 1);
                }
            }));

            currentGui.addElement(new Button(nodeMan.getNode(editItem).getTexture(), 32 + 96, 16, 96, 96).setAction(new ButtonAction() {
                @Override
                public void onPress(Button but, float time) {

                }

                @Override
                public void onTap(Button but, float time) {

                }

                @Override
                public void onTapRelease(Button but, float time) {
                    editItem++;
                    if(editItem >= nodeMan.getSize()) editItem = 1;
                    but.setTextureNormal(nodeMan.getNode(editItem).getTexture());
                    but.setTexturePressed(nodeMan.getNode(editItem).getTexture());
                    but.setTexture(nodeMan.getNode(editItem).getTexture());
                }
            }));
        }
    }

    private boolean hitMapGenerated = false;

    /**
     * Ustaw tryb gry (Modyfikacja mapy, przejście jednostek)
     *
     * @param newMode Nowy tryb gry.
     */
    void setMode(ControlMode newMode)
    {
        if(newMode != ControlMode.RUN)
        {
            if(mode == ControlMode.RUN)
            {
                loadInventoryGui();
            }

            mode = newMode;
        } else
        {
            hitMapGenerated = false;
            mode = newMode;
            currentGui.clear();

            Button button_refresh;

            button_refresh = new Button(Render.TEXTURE_BUTTON_REFRESH, 16, Gdx.graphics.getHeight()-16-96, 96, 96);
            button_refresh.setAction(new ButtonAction() {
                @Override
                public void onPress(Button but, float time) {
                }

                @Override
                public void onTap(Button but, float time) {
                }

                @Override
                public void onTapRelease(Button but, float time) {

                    setMode(ControlMode.NONE);
                    refreshGame = true;

                }
            });

            currentGui.addElement(button_refresh);
        }
    }

    /**
     * Dodaj przedmiot do ekwipunku.
     *
     * @param t Id przedmiotu
     * @param a Ilość
     */
    private void addItem(int t, int a)
    {
        if (a < 1) return;

        for(int i = 0; i < items.size(); i++)
        {
            if(items.get(i).type == t)
            {
                items.get(i).amount += a;
                return;
            }
        }

        items.add(new InvItem(t, a));
        loadInventoryGui();
    }

    /**
     * Zabierz przedmiot
     *
     * @param t
     * @param a
     * @return
     */
    public boolean takeItem(int t, int a)
    {
        if(a<1) return false;

        for(int i = 0; i < items.size(); i++)
        {
            if(items.get(i).type == t)
            {
                if(items.get(i).amount - a < 0)
                    return false;
                else
                {
                    items.get(i).amount -= a;

                    if(items.get(i).amount == 0)
                    {
                        items.remove(i);

                        setMode(ControlMode.NONE);
                        loadInventoryGui();
                    }

                    return true;
                }

            }
        }

        return false;
    }

    /**
     * Zabierz przedmiot z ekwipunktu..
     *
     * @param i Pozycja przedmiotu w ekwipunku.
     * @param a Ilość.
     * @return
     */
    private boolean takeItemId(int i, int a)
    {
        if(a<1) return false;

        if(items.get(i).amount - a < 0) return false;

        items.get(i).amount -= a;

        if(items.get(i).amount == 0)
        {
            items.remove(i);

            setMode(ControlMode.NONE);
            loadInventoryGui();
        }

        return true;
    }

    /**
     * Metoda wywoływana co cylk rysowania.
     *
     * @param ren Obiekt klasy render.
     */
    public void draw(Render ren)
    {
        if(mode == ControlMode.REMOVE)
        {
            ren.drawScale(Render.TEXTURE_HIGHLIGHT, 32+96, Gdx.graphics.getHeight()-16-96-24, 96, 16);
        } else if(mode == ControlMode.ADD)
        {
            ren.drawScale(Render.TEXTURE_HIGHLIGHT, 48+96+96 + selectedItem *(96+16), Gdx.graphics.getHeight()-16-96-24, 96, 16);
        }


        if(mode != ControlMode.RUN)
        {
            for(int i = 0; i < items.size(); i++)
            {
                ren.drawText(Integer.toString(items.get(i).amount),  80 + 2*(32 + 96) + i*(96+16)-16*((int)Math.floor(Math.log10(items.get(i).amount))+1), Gdx.graphics.getHeight()-16-96+30);
            }
        }

    }

    private long time_offset = 0;

    /**
     * Metoda wywoływana co cylk logiki gry.
     *
     * @param time              Do usunięcia.
     * @param entityMan         Menadżer jednostek.
     * @param gameMap           Mapa gry.
     * @param blockDimension    Wymiar pojedynczego bloku.
     */
    public void update(float time, EntityManager entityMan, LevelMap gameMap, int blockDimension)
    {
        if(refreshGame)
        {
            refreshGame = false;
            entityMan.killAll();
            gameMap.sendReset();
        }

        if(mode == ControlMode.RUN)
        {
            if(!hitMapGenerated)
            {
                hitMapGenerated = true;
                gameMap.generateHitMap();
            }
            if(System.currentTimeMillis() - time_offset > 1000 && entityMan.getReleasedEntities() < 5)
            {
                time_offset = System.currentTimeMillis();
                Entity en = new Entity(Render.TEXTURE_ENTITY_TEST, -2*blockDimension, (startingBlock+1)*blockDimension, blockDimension);
                en.setRightDirection();
                entityMan.spawn(en);
            }
        } else
        {
            entityMan.resetReleasedEntities();

            if(mode == ControlMode.ADD)
            {

                if(Gdx.input.isTouched())
                {
                    int block_x = (int)Math.floor(Gdx.input.getX() / blockDimension);
                    int block_y = (int)Math.floor((Gdx.graphics.getHeight() - Gdx.input.getY()) / blockDimension);

                    if((gameMap.getNode(block_x, block_y) == 0 || editMode) && block_y < gameMap.getHeight()-3 && (!editMode || block_x > 3 || block_y > 1))
                    {
                        if((nodeMan.getNode((short)items.get(selectedItem).type).canBePlacedOn(gameMap, block_x, block_y) || editMode) && items.get(selectedItem).amount > 0)
                        {
                            gameMap.setNode(block_x, block_y, (short)items.get(selectedItem).type);

                            if(!editMode) takeItemId(selectedItem, 1);
                        }
                    }


                }
            } else if(mode == ControlMode.REMOVE)
            {
                if(Gdx.input.isTouched())
                {
                    int block_x = (int)Math.floor(Gdx.input.getX() / blockDimension);
                    int block_y = (int)Math.floor((Gdx.graphics.getHeight() - Gdx.input.getY()) / blockDimension);

                    if(((gameMap.isRemovable(block_x, block_y) && (gameMap.getNode(block_x-1 ,block_y) == 0 || gameMap.getNode(block_x ,block_y-1) == 0 ||
                            gameMap.getNode(block_x+1 ,block_y) == 0 || gameMap.getNode(block_x ,block_y+1) == 0)) || editMode) &&  (!editMode || block_x > 3 || block_y > 1))
                    {
                        if(!editMode) addItem(gameMap.getNode(block_x,block_y), 1);
                        gameMap.setNode(block_x, block_y, (short)0);
                    }

                }
            }
        }

    }
}
