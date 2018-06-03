package com.square.game;

/**
 * Klasa odpowiada przyciskowi z tekstem.
 */
public class ButtonText extends Button {

    String text;

    /**
     * Konstruktor klasy ButtonText.
     *
     * @param text  Tekst przycisku.
     * @param x     Położenie x przycisku.
     * @param y     Położenie y przycisku.
     * @param ren   Obiekt odpowiedzialny za render.
     */
    public ButtonText(String text, int x, int y, Render ren)
    {
        super(0, x, y, (int)ren.getTextSize(text).x, (int)ren.getTextSize(text).y);
        this.text = text;
    }

    /**
     * Metoda wywoływana co cylk rysowania.
     *
     * @param ren Obiekt klasy Render.
     */
    @Override
    public void draw(Render ren)
    {
        ren.drawText(text, x, y);
    }
}
