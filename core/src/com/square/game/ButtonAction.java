package com.square.game;

/**
 * Interfejs zdarzenia przycisku.
 */
public interface ButtonAction {


    /**
     * Kiedy przycisk zostanie przyciśnięty.
     *
     * @param but Pzycisk.
     * @param time Do usunięcia.
     */
    void onPress(Button but, float time);
    void onTap(Button but, float time);
    void onTapRelease(Button but, float time);
}
