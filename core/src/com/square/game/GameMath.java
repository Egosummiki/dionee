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
        float xDiff = vec2.x - vec1.x;
        float yDiff = vec2.y - vec1.y;
        return (float)Math.sqrt(xDiff*xDiff + yDiff*yDiff);
    }

    public static Vector2 linearTest(HitLine line1, HitLine line2)
    {
        float denominator = (line1.getA().x - line1.getB().x)*(line2.getA().y - line2.getB().y) -
                (line1.getA().y - line1.getB().y)*(line2.getA().x - line2.getB().x);

        float mult1 = line1.getA().x * line1.getB().y - line1.getA().y * line1.getB().x;
        float mult2 = line2.getA().x * line2.getB().y - line2.getA().y * line2.getB().x;

        Vector2 common = new Vector2(
                ((mult1 * (line2.getA().x - line2.getB().x)) - (mult2 * (line1.getA().x - line1.getB().x)))/denominator,
                ((mult1 * (line2.getA().y - line2.getB().y)) - (mult2 * (line1.getA().y - line1.getB().y)))/denominator);

        float aDiff = distance(line1.getA(), line1.getB()) - distance(line1.getA(), common) - distance(common, line1.getB());
        float bDiff = distance(line2.getA(), line2.getB()) - distance(line2.getA(), common) - distance(common, line2.getB());

        if(Math.abs(aDiff) < 0.01f && Math.abs(bDiff) < 0.01f)
        {
            return common;
        }

        return null;
    }
}
