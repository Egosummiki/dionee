package com.square.game;

/**
 * Element interfejsu użytkownika stanowiący tekst.
 */
public class GuiText extends GuiElement {

    String text;

    public GuiText(String _text, int _x, int _y, Render ren)
    {
        super(_x, _y, (int)ren.getTextSize(_text).x, (int)ren.getTextSize(_text).y);
        text = _text;
    }

    @Override
    public void draw(Render ren)
    {
        ren.drawText(text, x, y);
    }
}
