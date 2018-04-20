package com.square.game;

/**
 * Created by Mikolaj on 29.10.2015.
 */

import com.badlogic.gdx.graphics.Texture;

public class BackgroundTexture extends Background {

    Texture tex;

    public BackgroundTexture()
    {

    }

    public BackgroundTexture setTexture(Texture _tex)
    {
        tex = _tex;
        return this;
    }

    @Override
    public void draw(Render ren)
    {
        ren.getBatch().draw(tex, 0, 0);
    }


}
