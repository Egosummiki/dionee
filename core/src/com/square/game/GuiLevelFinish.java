package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Klasa reprezentuje interfejs użytkownika wyświetlany po przejściu poziomu.
 */
public class GuiLevelFinish extends Gui {

    /**
     * Klasa wewnętrzna odpowiedzialna za wykonanie akcji przejścia do następnego poziomu.
     */
    private class ActionNext implements ButtonAction
    {
        LevelManager levelMan;

        ActionNext(LevelManager lvl)
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

            setAnimationForEach(GuiElement.AnimationType.SLIDE_TO_BOTTOM, 300, new GuiElement.AnimationCallback() {
                @Override
                public void onAnimationEnd() {
                    Gui.setGui(Gui.GUI_GAME);
                    levelMan.loadNextLevel();
                }
            });
        }
    }

    /**
     * Klasa wewnętrzna odpowiedzialna za wykonanie akcji załadowania poziomu ponownie.
     */
    private class ActionRepeat implements ButtonAction
    {
        LevelManager levelMan;

        ActionRepeat(LevelManager lvl)
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

            setAnimationForEach(GuiElement.AnimationType.SLIDE_TO_BOTTOM, 300, new GuiElement.AnimationCallback() {
                @Override
                public void onAnimationEnd() {
                    Gui.setGui(Gui.GUI_GAME);
                    levelMan.reload();
                }
            });
        }
    }

    GuiLevelFinish(Render ren, LevelManager lman)
    {
        int sx = (int) (Gdx.graphics.getWidth() * 0.2f);
        int sy = (int) (Gdx.graphics.getHeight() * 0.2f);

        addElement(new GuiImage(Render.TEXTURE_BACK_LVL_CLEARED, sx, sy, (int) (Gdx.graphics.getWidth() * 0.6f), (int) (Gdx.graphics.getHeight()*0.6f) ));
        addElement(new Button(Render.TEXTURE_PLAY_SMALL, (Gdx.graphics.getWidth()/2)+42, sy+42, 128, 128).setAction(new ActionNext(lman)));
        addElement(new Button(Render.TEXTURE_BUTTON_REPLAY, (Gdx.graphics.getWidth()/2)-42-128, sy+42, 128, 128).setAction(new ActionRepeat(lman)));
    }

    @Override
    public void onSet()
    {
        Game.background = new BackgroundTexture().setTexture(Generate.darkPixmap(Generate.getScreenshot(), 0.8f));
        resetAll();
        setAnimationForEach(GuiElement.AnimationType.SLIDE_FROM_BOTTOM, 300);
    }
}
