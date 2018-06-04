package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by Mikolaj on 18.10.2015.
 */
public class GuiMenu extends Gui {

    LevelManager levelMan;

    private class buttonAction_play implements ButtonAction {
        LevelManager levelMan;

        public buttonAction_play(LevelManager l)
        {
            levelMan = l;
        }

        @Override
        public void onPress(Button but, float time) {
        }

        @Override
        public void onTap(Button but, float time) {
        }

        @Override
        public void onTapRelease(Button but, float time) {

            elements.get(4).setAnimation(GuiElement.AnimationType.SLIDE_TO_RIGHT, 300);

            if(levelMan.getCurrentLevel() > -1)
            {
                setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_LEFT, 300, new int[] {4}, new GuiElement.AnimationCallback() {
                    @Override
                    public void onAnimationEnd() {
                        Gui.setGui(Gui.GUI_GAME);
                    }
                });
            } else
            {
                if(!SaveData.tutorialPassed())
                {
                    setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_LEFT, 300, new int[] {4}, new GuiElement.AnimationCallback() {
                        @Override
                        public void onAnimationEnd() {
                            levelMan.setEditMode(false);
                            TutorialQueue.start();
                        }
                    });
                } else
                {
                    setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_LEFT, 300, new int[] {4}, new GuiElement.AnimationCallback() {
                        @Override
                        public void onAnimationEnd() {
                            levelMan.setEditMode(false);
                            Gui.setGui(Gui.GUI_GAME);
                            levelMan.loadLevel(SaveData.getMaxLevel());
                        }
                    });
                }
            }

        }
    }

    private class buttonAction_settings implements ButtonAction {
        public buttonAction_settings()
        {
        }

        @Override
        public void onPress(Button but, float time) {
        }

        @Override
        public void onTap(Button but, float time) {
        }

        @Override
        public void onTapRelease(Button but, float time) {

            setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_LEFT, 300, new int[] {4}, new GuiElement.AnimationCallback() {
                @Override
                public void onAnimationEnd() {
                    Gui.setGui(Gui.GUI_SETTINGS);
                }
            });

            elements.get(4).setAnimation(GuiElement.AnimationType.SLIDE_TO_RIGHT, 300);
        }
    }

    private class buttonAction_levels implements ButtonAction {
        public buttonAction_levels()
        {
        }

        @Override
        public void onPress(Button but, float time) {
        }

        @Override
        public void onTap(Button but, float time) {
        }

        @Override
        public void onTapRelease(Button but, float time) {
            if(!TutorialQueue.isRunning())
            {
                setAnimationExcluding(GuiElement.AnimationType.SLIDE_TO_LEFT, 300, new int[] {4}, new GuiElement.AnimationCallback() {
                    @Override
                    public void onAnimationEnd() {
                        Gui.setGui(Gui.GUI_LEVELS);
                    }
                });

                elements.get(4).setAnimation(GuiElement.AnimationType.SLIDE_TO_RIGHT, 300);
            }
        }
    }

    public GuiMenu(Render ren, LevelManager lman)
    {
        super();

        levelMan = lman;

        addElement(new GuiImage(Render.TEXTURE_MENU_BACK, 0, (int) (0.5f * Gdx.graphics.getHeight() - 256.0f), 160, 512));
        addElement(new Button(Render.TEXTURE_BUTTON_RUN, Render.TEXTURE_BUTTON_RUN_PRESSED, 70, Gdx.graphics.getHeight() / 2 - 64, 128, 128).setAction(new buttonAction_play(levelMan)));
        addElement(new Button(Render.TEXTURE_BUTTON_OPTIONS, Render.TEXTURE_BUTTON_OPTIONS_PRESSED, 22, Gdx.graphics.getHeight() / 2 - 208, 96, 96).setAction(new buttonAction_settings()));
        addElement(new Button(Render.TEXTURE_BUTTON_LEVELS, Render.TEXTURE_BUTTON_LEVELS_PRESSED, 32, Gdx.graphics.getHeight() / 2 + 106, 96, 96).setAction(new buttonAction_levels()));
        addElement(new Logo((int) (0.5f * Gdx.graphics.getWidth() - 256.0f) + 160, (int) (0.5f * Gdx.graphics.getHeight() - 72.0f)));
    }

    @Override
    public void onSet()
    {
        resetAll();
        //setAnimationForEach(GuiElement.AnimationType.SLIDE_FROM_LEFT, 300);
        setAnimationExcluding(GuiElement.AnimationType.SLIDE_FROM_LEFT, 300, new int[] {4});
        elements.get(4).setAnimation(GuiElement.AnimationType.SLIDE_FROM_RIGHT, 300);

        GameMusic.onMenu();

        //if(TutorialQueue.isRunning()) TutorialQueue.stop();
    }

    @Override
    public void update(float time)
    {
        super.update(time);
    }


}
