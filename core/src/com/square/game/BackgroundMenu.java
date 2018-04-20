package com.square.game;

/**
 * Created by Mikolaj on 27.10.2015.
 */
public class BackgroundMenu extends Background {

    public BackgroundMenu()
    {
    }

    @Override
    public void update(float time)
    {
    }

    @Override
    public void draw(Render ren)
    {
        ren.draw(Render.TEXTURE_BACKGROUND_MENU, 0, 0);
    }
}
