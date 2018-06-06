package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Gui pozwalające na wybór poziomu gry.
 */
public class GuiLevels extends Gui {

    LevelManager levelMan;

    /**
     * Klasa wewnętrzna odpowiedzialna za ładowanie wybranego poziomu.
     */
    private class buttonAction_load_level implements ButtonAction {
        int level;
        LevelManager levelMan;
        long press_time = 0;

        buttonAction_load_level(int l, LevelManager lman)
        {
            level = l;
            levelMan = lman;
        }

        @Override
        public void onPress(Button but, float time) {
            if(press_time != 0 && System.currentTimeMillis() - press_time >= 3000)
            {
                press_time = 0;
                elements.get(1).setAnimation(GuiElement.AnimationType.SLIDE_TO_LEFT, 300);
                setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_BOTTOM, 300, new int[]{1}, new GuiElement.AnimationCallback() {
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

            elements.get(1).setAnimation(GuiElement.AnimationType.SLIDE_TO_LEFT, 300);
            setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_BOTTOM, 300, new int[]{1}, new GuiElement.AnimationCallback() {
                @Override
                public void onAnimationEnd() {
                    levelMan.setEditMode(false);
                    Gui.setGui(Gui.GUI_GAME);
                    levelMan.loadLevel(level);
                }
            });



        }
    }

    GuiLevels(Render ren, LevelManager lman)
    {
        super();

        levelMan = lman;

        int sx = (int)(Gdx.graphics.getWidth()*0.2f);
        int sy = (int)(Gdx.graphics.getHeight()*0.2f);

        addElement(new GuiImage(Render.TEXTURE_GUI_LEVELMENU_BACK, sx, sy, (int) (Gdx.graphics.getWidth() * 0.6f), (int) (Gdx.graphics.getHeight() * 0.6f)));

        addElement(new Button(Render.TEXTURE_GOBACK, 16, Gdx.graphics.getHeight() - 128, 96, 96).setAction(new ButtonAction() {
            @Override
            public void onPress(Button but, float time) {

            }

            @Override
            public void onTap(Button but, float time) {

            }

            @Override
            public void onTapRelease(Button but, float time) {
                elements.get(1).setAnimation(GuiElement.AnimationType.SLIDE_TO_LEFT, 300);
                setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_BOTTOM, 300, new int[]{1}, new GuiElement.AnimationCallback() {
                    @Override
                    public void onAnimationEnd() {
                        Gui.setGui(Gui.GUI_MENU);
                    }
                });
            }
        }));

        for(int i = 0; i < 28; i++)
        {
            if(i <= SaveData.getMaxLevel())
            {
                addElement(new Button(Render.TEXTURE_GUI_LEVELMENU_BUTTON, Render.TEXTURE_GUI_LEVELMENU_BUTTON_PRESS, sx + 32 + 12 + 156 * (i % 7), sy + 16 + 12 + 156 * (3 - i / 7), 128, 128).setAction(new buttonAction_load_level(i, levelMan)));
                addElement(new GuiText(Integer.toString(i+1), sx + 16 + 12 + 156 * (i % 7) + 72, sy + 16 + 12 + 156 * (3 - i / 7) + 72, ren));
            } else
            {
                addElement(new GuiImage(Render.TEXTURE_GUI_LEVELMENU_LOCKED, sx + 32 + 12 + 156*(i%7), sy + 16 + 12 + 156*(3 - i/7), 128, 128));
            }
        }
    }

    @Override
    public void onSet()
    {
        resetAll();
        setAnimationExcluding(GuiElement.AnimationType.SLIDE_FROM_BOTTOM, 300, new int[]{1});
        elements.get(1).setAnimation(GuiElement.AnimationType.SLIDE_FROM_LEFT, 300);
    }

    @Override
    public void update(float time)
    {
        super.update(time);
    }
}
