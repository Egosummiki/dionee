package com.square.game;

/**
 * Created by Mikolaj on 23.10.2015.
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
