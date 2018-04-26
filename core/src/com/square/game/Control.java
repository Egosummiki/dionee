package com.square.game;

import com.badlogic.gdx.Gdx;

import java.util.Vector;

/**
 * Created by Mikolaj on 10.10.2015.
 */

public class Control {

    private ControlMode mode;
    private int item_sel;
    public Gui current_gui;
    private boolean refresh_game = false;
    private boolean load_buttons = true;

    private int edit_itm = 1;

    public boolean edit_mode = false;

    public int starting_bl = 9;

    Render textureRender;
    NodeManager nodeMan;

    private Vector<InvItem> items;

    public Control(Render ren, NodeManager nd)
    {
        item_sel = 0;
        mode = ControlMode.NONE;
        textureRender = ren;
        nodeMan = nd;
    }

    public void setGui(Gui g)
    {
        current_gui = g;
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
            item_sel = index;
            setMode(ControlMode.ADD);
        }
    }

    public void loadInvGui()
    {
        current_gui.clear();

        Button button_play;

        button_play = new Button(Render.TEXTURE_BUTTON_PLAY, 16, Gdx.graphics.getHeight()-16-48, 48, 48);
        button_play.setAction(new ButtonAction() {
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

        button_remove = new Button(Render.TEXTURE_BUTTON_REMOVE, 89, Gdx.graphics.getHeight()-16-48, 48, 48);
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

        current_gui.addElement(button_play);
        current_gui.addElement(button_remove);

        current_gui.addElement(new Button(Render.TEXTURE_BUTTON_EXIT, Gdx.graphics.getWidth() - 16 - 48, Gdx.graphics.getHeight() - 16 - 48, 48, 48).setAction(new ButtonAction() {
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

            button_node = new Button(nodeMan.getNode(items.get(i).type).getTexture(), 89 + (i+1)*(48+8), Gdx.graphics.getHeight()-16-48, 48, 48);
            button_node.setAction(new buttonAction_node(i));

            current_gui.addElement(button_node);
        }

        if(edit_mode)
        {
            current_gui.addElement(new Button(Render.TEXTURE_PLUS, 16, 16, 48, 48).setAction(new ButtonAction() {
                @Override
                public void onPress(Button but, float time) {

                }

                @Override
                public void onTap(Button but, float time) {

                }

                @Override
                public void onTapRelease(Button but, float time) {
                    addItem(edit_itm, 1);
                }
            }));

            current_gui.addElement(new Button(nodeMan.getNode(edit_itm).getTexture(), 32 + 48, 16, 48, 48).setAction(new ButtonAction() {
                @Override
                public void onPress(Button but, float time) {

                }

                @Override
                public void onTap(Button but, float time) {

                }

                @Override
                public void onTapRelease(Button but, float time) {
                    edit_itm++;
                    if(edit_itm >= nodeMan.getSize()) edit_itm = 1;
                    but.setTextureNormal(nodeMan.getNode(edit_itm).getTexture());
                    but.setTexturePressed(nodeMan.getNode(edit_itm).getTexture());
                    but.setTexture(nodeMan.getNode(edit_itm).getTexture());
                }
            }));
        }
    }

    public void setMode(ControlMode new_mode)
    {
        if(new_mode != ControlMode.RUN)
        {
            if(mode == ControlMode.RUN)
            {
                loadInvGui();
            }

            mode = new_mode;
        } else
        {
            mode = new_mode;
            current_gui.clear();

            Button button_refresh;

            button_refresh = new Button(Render.TEXTURE_BUTTON_REFRESH, 16, Gdx.graphics.getHeight()-16-48, 48, 48);
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
                    refresh_game = true;

                }
            });

            current_gui.addElement(button_refresh);
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
            ren.draw(Render.TEXTURE_HIGHLIGHT, 89, Gdx.graphics.getHeight()-16-48-12);
        } else if(mode == ControlMode.ADD)
        {
            ren.draw(Render.TEXTURE_HIGHLIGHT, 145 + item_sel*56, Gdx.graphics.getHeight()-16-48-12);
        }


        if(mode != ControlMode.RUN)
        {
            for(int i = 0; i < items.size(); i++)
            {
                ren.drawText(Integer.toString(items.get(i).amount), 191 + i*56-16*((int)Math.floor(Math.log10(items.get(i).amount))+1), Gdx.graphics.getHeight()-16-48+30);
            }
        }

    }

    long time_offset = 0;


    public void update(float time, EntityManager entityMan, Map gameMap, int block_sz)
    {
        if(refresh_game)
        {
            refresh_game = false;
            entityMan.killAll();
            gameMap.sendReset();
        }

        if(mode == ControlMode.RUN)
        {
            if(System.currentTimeMillis() - time_offset > 1000 && entityMan.getRealsedEntities() < 5)
            {
                time_offset = System.currentTimeMillis();
                Entity en = new Entity(Render.TEXTURE_ENTITY_TEST, -block_sz, starting_bl*block_sz+3.0f + 1, block_sz);
                en.setRightDirection();
                entityMan.spawn(en);
            }
        } else
        {
            entityMan.resetRealsedEntites();

            if(mode == ControlMode.ADD)
            {

                if(Gdx.input.isTouched())
                {
                    int block_x = (int)Math.floor(Gdx.input.getX() / block_sz);
                    int block_y = (int)Math.floor((Gdx.graphics.getHeight() - Gdx.input.getY()) / block_sz);

                    if((gameMap.getNode(block_x, block_y) == 0 || edit_mode) && block_y < gameMap.getHeight()-3 && (!edit_mode || block_x > 3 || block_y > 1))
                    {
                        if((nodeMan.getNode((short)items.get(item_sel).type).canBePlacedOn(gameMap, block_x, block_y) || edit_mode) && items.get(item_sel).amount > 0)
                        {
                            gameMap.setNode(block_x, block_y, (short)items.get(item_sel).type);

                            if(!edit_mode) takeItemId(item_sel, 1);
                        }
                    }


                }
            } else if(mode == ControlMode.REMOVE)
            {
                if(Gdx.input.isTouched())
                {
                    int block_x = (int)Math.floor(Gdx.input.getX() / block_sz);
                    int block_y = (int)Math.floor((Gdx.graphics.getHeight() - Gdx.input.getY()) / block_sz);

                    if(((gameMap.isRemoveable(block_x, block_y) && (gameMap.getNode(block_x-1 ,block_y) == 0 || gameMap.getNode(block_x ,block_y-1) == 0 ||
                            gameMap.getNode(block_x+1 ,block_y) == 0 || gameMap.getNode(block_x ,block_y+1) == 0)) || edit_mode) &&  (!edit_mode || block_x > 3 || block_y > 1))
                    {
                        if(!edit_mode) addItem(gameMap.getNode(block_x,block_y), 1);
                        gameMap.setNode(block_x, block_y, (short)0);
                    }

                }
            }
        }

    }
}
