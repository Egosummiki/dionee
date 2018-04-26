package com.square.game;

import com.badlogic.gdx.math.Vector2;
/**
 * Created by ego on 4/26/18.
 */

public class HitLine {
    private Vector2 a;
    private Vector2 b;

    public HitLine(Vector2 a, Vector2 b)
    {
        this.a = a;
        this.b = b;
    }

    public Vector2 getA()
    {
        return a;
    }

    public Vector2 getB()
    {
        return b;
    }
}
