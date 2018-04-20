package com.square.game;

/**
 * Created by Mikolaj on 11.10.2015.
 */
public interface ButtonAction {


    void onPress(Button but, float time);
    void onTap(Button but, float time);
    void onTapRelease(Button but, float time);
}
