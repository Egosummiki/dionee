package com.square.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mikolaj on 01.11.2015.
 */
public class GuiTutorial extends Gui {

    public static int texture = 0;
    public static String text = "";
    public static Vector2 text_sz;

    public class btn_skip implements ButtonAction {

        LevelManager levelMan;

        public btn_skip(LevelManager lvlMan)
        {
            levelMan = lvlMan;
        }

        @Override
        public void onPress(Button but, float time) {

        }

        @Override
        public void onTap(Button but, float time) {
        }

        @Override
        public void onTapRelease(Button but, float time) {
            TutorialQueue.stop();
            SaveData.setTutorialPassed();
            Gui.setGui(Gui.GUI_GAME);
            levelMan.loadLevel(0);
        }
    }

    public class btn_cont implements ButtonAction {

        LevelManager levelMan;

        public btn_cont(LevelManager lvlMan)
        {
            levelMan = lvlMan;
        }

        @Override
        public void onPress(Button but, float time) {

        }

        @Override
        public void onTap(Button but, float time) {
        }

        @Override
        public void onTapRelease(Button but, float time) {
            TutorialQueue.apply_next();
        }
    }

    @Override
    public void onSet()
    {
        Game.bgr = new Background();
    }

    public GuiTutorial(LevelManager lvlMan)
    {
        super();

        addElement(new GuiImage(texture, 0, 96, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-96-64));
        addElement(new Button(Render.TEXTURE_BUTTON_SKIP, Render.TEXTURE_BUTTON_SKIP_PRESSED, 16, 32, 64, 32).setAction(new btn_skip(lvlMan)));
        addElement(new Button(Render.TEXTURE_BUTTON_CONT, Render.TEXTURE_BUTTON_CONT_PRESSED, Gdx.graphics.getWidth() - 128 - 16, 32, 128, 32).setAction(new btn_cont(lvlMan)));
    }

    public void setPicture(int tex)
    {
        texture = tex;
        elements.set(0, new GuiImage(texture, 0, 96, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-96-64));
    }

    public void setText(String _text)
    {
        text = _text;
        text_sz = null;
    }

    @Override
    public void draw(Render ren)
    {
        super.draw(ren);
        if(text_sz == null) text_sz = ren.getTextSmallSize(text);
        ren.drawTextSmall(text, (int)(Gdx.graphics.getWidth()/2 - text_sz.x/2) - 32, (int)(48 - text_sz.y/2) + 16);
    }
}
