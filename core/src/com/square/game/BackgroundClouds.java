package com.square.game;

import com.badlogic.gdx.Gdx;

import java.util.Random;
import java.util.Vector;

/**
 * Klasa jest odpowiedzialna za wyświetlanie tła chmur.
 */
public class BackgroundClouds implements Background {

    /**
     * Klasa wewnętrzna odpowiadająca pojedynczej chmurze.
     */
    private class Cloud {
        float x;
        float y;
        int type;

        /**
         * Konstruktor klasy Cloud
         *
         * @param x     Położenie x chmury.
         * @param y     Położenie y chmury.
         * @param type  Wariant chmury.
         */
        Cloud(float x, float y, int type)
        {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    private Vector<Cloud> clouds;
    private Random rand;

    BackgroundClouds()
    {
        clouds = new Vector<Cloud>();
        rand = new Random();

        for(int i = 0; i < rand.nextInt(24); i++)
        {
            clouds.add(new Cloud(rand.nextInt(Gdx.graphics.getWidth())-256.0f, rand.nextInt(Gdx.graphics.getHeight()/2 - 127) + (Gdx.graphics.getHeight()/2), rand.nextInt(2)));
        }
    }

    /**
     * Metoda wykonywana co cykl logiki gry.
     *
     * @param time Do usunięcia.
     */
    @Override
    public void update(float time)
    {
        for (Cloud cloud : clouds) {
            cloud.x += 0.3f;
        }

        if(rand.nextInt(512) == 0)
        {
           clouds.add(new Cloud(-512.0f, rand.nextInt(Gdx.graphics.getHeight()/2 - 127) + (Gdx.graphics.getHeight()/2), rand.nextInt(2)));
        }
    }

    /**
     * Metoda wywoływana co cylk rysowania gry.
     *
     * @param ren Obiekt klasy render.
     */
    @Override
    public void draw(Render ren)
    {
        ren.draw(Render.TEXTURE_BACKGROUND, 0, 0);
        for (Cloud Cloud : clouds) {
            ren.draw(Render.TEXTURE_CLOUD + Cloud.type, (int) Cloud.x, (int) Cloud.y);
        }
    }
}
