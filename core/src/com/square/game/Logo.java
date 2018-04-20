package com.square.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Mikolaj on 19.11.2015.
 */
public class Logo extends GuiElement {

    public Logo(int _x, int _y)
    {
        super(_x, _y, 512, 145);
    }

    @Override
    public void draw(Render ren)
    {
        SpriteBatch batch = ren.getBatch();

        int tm = (int)(System.currentTimeMillis() % 10000);

        int ts = 0;

        if(tm < 2500) ts = 0; else
        if(tm < 5000) ts = tm-2500; else
        if(tm < 7500) ts = 2500; else ts = 10000 - tm;

        //int ts = tm >= 2500 ? 5000-tm : tm;

        float p = (float)ts/2500.0f;
        float q = 1.0f - p;

        batch.setColor(1, 1, 1, p);

        batch.draw(ren.getTexture(Render.TEXTURE_LOGO), x, y, width, height);

        batch.setColor(1, 1, 1, q);

        batch.draw(ren.getTexture(Render.TEXTURE_LOGO_LIGHT), x, y, width, height);

        batch.setColor(1, 1, 1, 1);

    }
}
