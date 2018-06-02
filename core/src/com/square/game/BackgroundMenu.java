package com.square.game;

/**
 * Klasa wyświetlająca tło podczas obecności menu gry.
 */
public class BackgroundMenu implements Background {

    BackgroundMenu()
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
