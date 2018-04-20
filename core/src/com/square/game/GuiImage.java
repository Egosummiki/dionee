package com.square.game;

/**
 * Created by Mikolaj on 18.10.2015.
 */
public class GuiImage extends GuiElement {

    int texture;

    public GuiImage(int _texture, int _x, int _y, int _width, int _height)
    {
        super(_x, _y, _width, _height);
        texture = _texture;
    }

    @Override
    public void draw(Render ren)
    {
        ren.drawScale(texture, x, y, width, height);
    }
}
