package com.square.game;

import com.badlogic.gdx.math.Vector2;
/**
 * Created by ego on 4/26/18.
 *
 * Klasa funkcjonuje jako element hitmapy. Reprezentuje linię.
* */
class HitLine {

    private Vector2 a;
    private Vector2 b;
    private boolean side;

    HitLine(Vector2 a, Vector2 b, boolean side)
    {
        this.a = a;
        this.b = b;
        this.side = side;
    }

    Vector2 getA()
    {
        return a;
    }

    Vector2 getB()
    {
        return b;
    }

    boolean getSide() {
        return side;
    }
}
