package com.square.game;

/**
 * Klasa stanowi element interfejsu użytkownika wyświetlający wybraną teksturę.
 */
public class GuiImage extends GuiElement {

    int texture;

    GuiImage(int _texture, int _x, int _y, int _width, int _height)
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
