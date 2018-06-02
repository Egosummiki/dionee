package com.square.game;

import com.badlogic.gdx.Gdx;

import java.util.Vector;

/**
 * Created by Mikolaj on 10.10.2015.
 *
 * Klasa obsługująca polecenia gracza podczas gry.
 */

public class Control {

    private ControlMode mode;
    private int selectedItem;
    public Gui currentGui;
    private boolean refreshGame = false;
    private boolean loadButtons = true;

    private int editItem = 1;

    public boolean editMode = false;

    public int startingBlock= 9;

    Render render;
    NodeManager nodeMan;

    private Vector<InvItem> items;

    public Control(Render render, NodeManager nodeMan)
    {
        selectedItem = 0;
        mode = ControlMode.NONE;
        this.render = render;
        this.nodeMan = nodeMan;
    }

    public void setGui(Gui g)
    {
        currentGui = g;
    }

    public void setStartingItems(Vector<InvItem> its)
    {
        items = its;
    }


    public class buttonAction_node implements ButtonAction {

        int index;

        public buttonAction_node(int _i)
        {
            index = _i;
        }


        @Override
        public void onPress(Button but, float time) {
        }

        @Override
        public void onTap(Button but, float time) {
        }

        @Override
        public void onTapRelease(Button but, float time) {
            selectedItem = index;
            setMode(ControlMode.ADD);
        }
    }

    public void loadInvGui()
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
            button_node.setAction(new buttonAction_node(i));

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

    public void setMode(ControlMode newMode)
    {
        if(newMode != ControlMode.RUN)
        {
            if(mode == ControlMode.RUN)
            {
                loadInvGui();
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

    public void addItem(int t, int a)
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
        loadInvGui();
    }

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
                        loadInvGui();
                    }

                    return true;
                }

            }
        }

        return false;
    }

    public boolean takeItemId(int i, int a)
    {
        if(a<1) return false;

        if(items.get(i).amount - a < 0) return false;

        items.get(i).amount -= a;

        if(items.get(i).amount == 0)
        {
            items.remove(i);

            setMode(ControlMode.NONE);
            loadInvGui();
        }

        return true;
    }

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

    public void update(float time, EntityManager entityMan, LevelMap gameMap, int block_sz)
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
                Entity en = new Entity(Render.TEXTURE_ENTITY_TEST, -2*block_sz, (startingBlock+1)*block_sz, block_sz);
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
                    int block_x = (int)Math.floor(Gdx.input.getX() / block_sz);
                    int block_y = (int)Math.floor((Gdx.graphics.getHeight() - Gdx.input.getY()) / block_sz);

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
                    int block_x = (int)Math.floor(Gdx.input.getX() / block_sz);
                    int block_y = (int)Math.floor((Gdx.graphics.getHeight() - Gdx.input.getY()) / block_sz);

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
