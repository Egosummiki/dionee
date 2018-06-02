package com.square.game;


import com.badlogic.gdx.graphics.Texture;

/**
 * Klasa odpowiedzialna za wyświetlanie konkretnego tła.
 */
public class BackgroundTexture implements Background {

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
    public void update(float time) {

    }

    @Override
    public void draw(Render ren)
    {
        ren.getBatch().draw(tex, 0, 0);
    }




}
