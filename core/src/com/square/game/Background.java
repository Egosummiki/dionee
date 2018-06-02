package com.square.game;

/**
 * Interfejs odpowiada za wyświetlania tła gry, lub menu.
 */
public interface Background {

    /**
     * Metoda update, wywoływana co cykl logiki gry.
     *
     * @param time Do usunięcia.
     */
    void update(float time);

    /**
     * Metoda render, wywoływana co cylk rysowania gry.
     *
     * @param ren Obiekt klasy render.
     */
    void draw(Render ren);

}
