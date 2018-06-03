package com.square.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Klasa zawierająca jedynie zmienne i metody statycznie, które wspomagają grę matematycznie.
 */
public class GameMath {

    final static float pi = 3.14159265359f;
    final static float pi_over_4 = 0.7853982f;
    final static float tau = 6.28318530718f;

    final static float gravitationalConstant = 0.2f;

    /**
     * Metoda sprawdza czy punkt znajduje się na linii.
     *
     * @param line  Linia
     * @param point Punkt
     * @return Wynik testu.
     */
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

    /**
     * Test liniowy. Czy dwie linie się przecinają, a jak tak, to gdzie?
     *
     * @param line1     Pierwsza linia.
     * @param line2     Druga linia.
     * @return          Punkt przecięcia.
     */
    static Vector2 linearTest(HitLine line1, HitLine line2)
    {
        Vector2 common = new Vector2();

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
