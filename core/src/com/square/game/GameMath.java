package com.square.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mikolaj on 09.10.2015.
 */
public class GameMath {

    public final static float pi = 3.14159265359f;
    public final static float tau = 6.28318530718f;

    public final static float gravitational_constant = 0.2f;

    public static float distance(Vector2 vec1, Vector2 vec2)
    {
        return (float)Math.sqrt(Math.pow(vec2.x - vec1.x, 2) + Math.pow(vec2.y - vec1.y, 2));
    }

    public static boolean linearTest(Vector2 a1, Vector2 a2, Vector2 b1, Vector2 b2)
    {
        if(a1.x == a2.x || b1.x == b2.x) return false;

        float aDen = (a2.x - a1.x);
        float aA = (a2.y - a1.y) / aDen;
        float aB = (a1.y*a2.x - a2.y*a1.x) / aDen;

        float bDen = (b2.x - b1.x);
        float bA = (b2.y - b1.y) / bDen;
        float bB = (b1.y*b2.x - b2.y*b1.x) / bDen;

        float commonDen = (aA - bA);
        if(commonDen == 0.0f) return aB == bB;

        Vector2 common = new Vector2( (bB - aB) / commonDen, (bB*aA - aB*bA) / commonDen );

        float aDiff = Math.abs(distance(a1, a2) - distance(a1, common) - distance(common, a2));
        float bDiff = Math.abs(distance(b1, b2) - distance(b1, common) - distance(common, b2));

        return aDiff < 0.001f && bDiff < 0.001f;
    }
}
