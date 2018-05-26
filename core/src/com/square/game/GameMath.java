package com.square.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mikolaj on 09.10.2015.
 */
public class GameMath {

    public final static float pi = 3.14159265359f;
    public final static float pi_over_4 = 0.7853982f;
    public final static float tau = 6.28318530718f;

    public final static float gravitationalConstant = 0.2f;

    public static float distance(Vector2 vec1, Vector2 vec2)
    {
        float xDiff = vec2.x - vec1.x;
        float yDiff = vec2.y - vec1.y;
        return (float)Math.sqrt(xDiff*xDiff + yDiff*yDiff);
    }

    private static boolean betweenTest(HitLine line, Vector2 point)
    {
        if(line.getA().x != line.getB().x)
        {
            if(line.getA().x < line.getB().x)
            {
                return line.getA().x < point.x && line.getB().x > point.x;
            }
            return line.getA().x > point.x && line.getB().x < point.x;
        }
        if(line.getA().y < line.getB().y)
        {
            return line.getA().y < point.y && line.getB().y > point.y;
        }
        return line.getA().y > point.y && line.getB().y < point.y;

    }

    private static Vector2 common;

    public static void initCommon()
    {
        common = new Vector2(0.0f, 0.0f);
    }

    public static Vector2 linearTest(HitLine line1, HitLine line2)
    {
        if(line1.getA().x == line1.getB().x)
        {
            common.x = line1.getA().x;
            common.y = ( (line2.getB().y - line2.getA().y)*line1.getA().x + (line2.getA().y * line2.getB().x - line2.getB().y * line2.getA().x) )
                            / (line2.getB().x - line2.getA().x);
        } else if(line1.getA().y == line1.getB().y)
        {
            common.x = ( (line2.getB().x - line2.getA().x)*line1.getA().y + (line2.getB().y * line2.getA().x - line2.getA().y * line2.getB().x) )
                            / (line2.getB().y - line2.getA().y);
            common.y = line1.getA().y;
        } else
        {
            float denominator = (line1.getA().x - line1.getB().x)*(line2.getA().y - line2.getB().y) -
                    (line1.getA().y - line1.getB().y)*(line2.getA().x - line2.getB().x);

            if(denominator == 0) return null;

            float mult1 = line1.getA().x * line1.getB().y - line1.getA().y * line1.getB().x;
            float mult2 = line2.getA().x * line2.getB().y - line2.getA().y * line2.getB().x;

            common.x = ((mult1 * (line2.getA().x - line2.getB().x)) - (mult2 * (line1.getA().x - line1.getB().x)))/denominator;
            common.y = ((mult1 * (line2.getA().y - line2.getB().y)) - (mult2 * (line1.getA().y - line1.getB().y)))/denominator;
        }

        if (betweenTest(line1, common) && betweenTest(line2, common)) return new Vector2(common.x, common.y);
        return null;
    }
}
