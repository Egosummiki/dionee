package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by Mikolaj on 23.10.2015.
 */
public class GuiLevels extends Gui {

    LevelManager levelMan;

    private class buttonAction_load_level implements ButtonAction {
        int level;
        LevelManager levelMan;
        long press_time = 0;

        public buttonAction_load_level(int l, LevelManager lman)
        {
            level = l;
            levelMan = lman;
        }

        @Override
        public void onPress(Button but, float time) {
            if(press_time != 0 && System.currentTimeMillis() - press_time >= 3000)
            {
                press_time = 0;
                elements.get(1).setAnimation(GuiElement.ani_type.SLIDE_TO_LEFT, 300);
                setAnimationExcluding(GuiElement.ani_type.SLIDE_TO_BOTTOM, 300, new int[]{1}, new GuiElement.ani_callback() {
                    @Override
                    public void onAnimationEnd() {
                        levelMan.setEditMode(true);
                        Gui.setGui(Gui.GUI_GAME);
                        levelMan.loadLevel(level);
                    }
                });
            }
        }

        @Override
        public void onTap(Button but, float time) {
            press_time = System.currentTimeMillis();
        }

        @Override
        public void onTapRelease(Button but, float time) {
            press_time = 0;

            elements.get(1).setAnimation(GuiElement.ani_type.SLIDE_TO_LEFT, 300);
            setAnimationExcluding(GuiElement.ani_type.SLIDE_TO_BOTTOM, 300, new int[]{1}, new GuiElement.ani_callback() {
                @Override
                public void onAnimationEnd() {
                    levelMan.setEditMode(false);
                    Gui.setGui(Gui.GUI_GAME);
                    levelMan.loadLevel(level);
                }
            });



        }
    }

    public GuiLevels(Render ren, LevelManager lman)
    {
        super();

        levelMan = lman;

        int sx = (Gdx.graphics.getWidth()/2)-176;
        int sy = (Gdx.graphics.getHeight()/2)-144;

        addElement(new GuiImage(Render.TEXTURE_GUI_LEVELMENU_BACK, sx, sy, 352, 288));

        addElement(new Button(Render.TEXTURE_GOBACK, 16, Gdx.graphics.getHeight() - 80, 64, 64).setAction(new ButtonAction() {
            @Override
            public void onPress(Button but, float time) {

            }

            @Override
            public void onTap(Button but, float time) {

            }

            @Override
            public void onTapRelease(Button but, float time) {
                elements.get(1).setAnimation(GuiElement.ani_type.SLIDE_TO_LEFT, 300);
                setAnimationExcluding(GuiElement.ani_type.SLIDE_TO_BOTTOM, 300, new int[]{1}, new GuiElement.ani_callback() {
                    @Override
                    public void onAnimationEnd() {
                        Gui.setGui(Gui.GUI_MENU);
                    }
                });
            }
        }));

        for(int i = 0; i < 20; i++)
        {
            if(i <= SaveData.getMaxLevel())
            {
                addElement(new Button(Render.TEXTURE_GUI_LEVELMENU_BUTTON, Render.TEXTURE_GUI_LEVELMENU_BUTTON_PRESS, sx + 16 + 64 * (i % 5), sy + 16 + 64 * (3 - i / 5), 56, 56).setAction(new buttonAction_load_level(i, levelMan)));
                addElement(new GuiText(Integer.toString(i+1), sx + 16 + 64 * (i % 5) + 32, sy + 16 + 64 * (3 - i / 5) + 32, ren));
            } else
            {
                addElement(new GuiImage(Render.TEXTURE_GUI_LEVELMENU_LOCKED, sx + 16 + 64*(i%5), sy + 16 + 64*(3 - i/5), 56, 56));
            }
        }
    }

    @Override
    public void onSet()
    {
        resetAll();
        setAnimationExcluding(GuiElement.ani_type.SLIDE_FROM_BOTTOM, 300, new int[]{1});
        elements.get(1).setAnimation(GuiElement.ani_type.SLIDE_FROM_LEFT, 300);
    }

    @Override
    public void update(float time)
    {
        super.update(time);
    }
}
