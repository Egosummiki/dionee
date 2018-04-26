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

    public static Vector2 linearTest(HitLine line1, HitLine line2)
    {
        if(line1.getA().x == line1.getB().x || line2.getA().x == line2.getB().x) return null;

        float aDen = (line1.getB().x - line1.getA().x);
        float aA = (line1.getB().y - line1.getA().y) / aDen;
        float aB = (line1.getA().y* line1.getB().x - line1.getB().y* line1.getA().x) / aDen;

        float bDen = (line2.getB().x - line2.getA().x);
        float bA = (line2.getB().y - line2.getA().y) / bDen;
        float bB = (line2.getA().y* line2.getB().x - line2.getB().y* line2.getA().x) / bDen;

        float commonDen = (aA - bA);
        if(commonDen == 0.0f) return null; //return aB == bB;

        Vector2 common = new Vector2( (bB - aB) / commonDen, (bB*aA - aB*bA) / commonDen );

        float aDiff = Math.abs(distance(line1.getA(), line1.getB()) - distance(line1.getA(), common) - distance(common, line1.getB()));
        float bDiff = Math.abs(distance(line2.getA(), line2.getB()) - distance(line2.getA(), common) - distance(common, line2.getB()));

        if(aDiff < 0.001f && bDiff < 0.001f)
        {
            return common;
        }

        return null;
    }
}
