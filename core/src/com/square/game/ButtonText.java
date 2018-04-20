package com.square.game;

/**
 * Created by Mikolaj on 18.10.2015.
 */
public class ButtonText extends Button {

    String text;

    public ButtonText(String _text, int _x, int _y, Render ren)
    {
        super(0, _x, _y, (int)ren.getTextSize(_text).x, (int)ren.getTextSize(_text).y);
        text = _text;
    }

    @Override
    public void draw(Render ren)
    {
        ren.drawText(text, x, y);
    }
}
