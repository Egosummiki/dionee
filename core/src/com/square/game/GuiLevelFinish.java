package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by Mikolaj on 25.10.2015.
 */
public class GuiLevelFinish extends Gui {

    private class action_next implements ButtonAction
    {
        LevelManager levelMan;

        public action_next(LevelManager lvl)
        {
            levelMan = lvl;
        }

        @Override
        public void onPress(Button but, float time) {

        }

        @Override
        public void onTap(Button but, float time) {
            but.setTexture(Render.TEXTURE_PLAY_SMALL_PRESS);
        }

        @Override
        public void onTapRelease(Button but, float time) {
            but.setTexture(Render.TEXTURE_PLAY_SMALL);

            setAnimationForEach(GuiElement.ani_type.SLIDE_TO_BOTTOM, 300, new GuiElement.ani_callback() {
                @Override
                public void onAnimationEnd() {
                    Gui.setGui(Gui.GUI_GAME);
                    levelMan.loadNextLevel();
                }
            });
        }
    }

    private class action_repeat implements ButtonAction
    {
        LevelManager levelMan;

        public action_repeat(LevelManager lvl)
        {
            levelMan = lvl;
        }

        @Override
        public void onPress(Button but, float time) {

        }

        @Override
        public void onTap(Button but, float time) {
            but.setTexture(Render.TEXTURE_BUTTON_REPLAY);
        }

        @Override
        public void onTapRelease(Button but, float time) {
            but.setTexture(Render.TEXTURE_BUTTON_REPLAY_PRESS);

            setAnimationForEach(GuiElement.ani_type.SLIDE_TO_BOTTOM, 300, new GuiElement.ani_callback() {
                @Override
                public void onAnimationEnd() {
                    Gui.setGui(Gui.GUI_GAME);
                    levelMan.reload();
                }
            });
        }
    }

    public GuiLevelFinish(Render ren, LevelManager lman)
    {
        int sx = (Gdx.graphics.getWidth()/2)-176;
        int sy = (Gdx.graphics.getHeight()/2)-144;

        addElement(new GuiImage(Render.TEXTURE_BACK_LVL_CLEARED, sx, sy, 352, 288));
        //addElement(new GuiImage(Render.TEXTURE_LEVEL_DONE, sx + 10, sy + 144, 333, 61));
        addElement(new Button(Render.TEXTURE_PLAY_SMALL, (Gdx.graphics.getWidth()/2)+16, sy+16, 64, 64).setAction(new action_next(lman)));
        addElement(new Button(Render.TEXTURE_BUTTON_REPLAY, (Gdx.graphics.getWidth()/2)-16-64, sy+16, 64, 64).setAction(new action_repeat(lman)));
    }

    @Override
    public void onSet()
    {
        Game.bgr = new BackgroundTexture().setTexture(Generate.darkPixmap(Generate.getScreenshot(), 0.5f)/*new Texture(Generate.getScreenshot())*/);
        resetAll();
        setAnimationForEach(GuiElement.ani_type.SLIDE_FROM_BOTTOM, 300);
    }
}
