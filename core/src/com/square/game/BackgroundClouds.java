package com.square.game;

import com.badlogic.gdx.Gdx;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Mikolaj on 27.10.2015.
 */
public class BackgroundClouds extends Background {

    private class cloud {
        float x;
        float y;
        int type;

        public cloud(float _x, float _y, int t)
        {
            x = _x;
            y = _y;
            type = t;
        }
    }

    private Vector<cloud> clouds;
    private Random rand;

    public BackgroundClouds()
    {
        clouds = new Vector<cloud>();
        rand = new Random();

        for(int i = 0; i < rand.nextInt(24); i++)
        {
            clouds.add(new cloud(rand.nextInt(Gdx.graphics.getWidth())-256.0f, rand.nextInt(Gdx.graphics.getHeight()/2 - 127) + (Gdx.graphics.getHeight()/2), rand.nextInt(2)));
        }
    }


    @Override
    public void update(float time)
    {
        for(int i = 0; i < clouds.size(); i++)
        {
            clouds.get(i).x += 0.3f*time;
        }

        if(rand.nextInt(512) == 0)
        {
           clouds.add(new cloud(-256.0f, rand.nextInt(Gdx.graphics.getHeight()/2 - 127) + (Gdx.graphics.getHeight()/2), rand.nextInt(2)));
        }
    }

    @Override
    public void draw(Render ren)
    {
        ren.draw(Render.TEXTURE_BACKGROUND, 0, 0);
        for(int i = 0; i < clouds.size(); i++)
        {
            ren.draw(Render.TEXTURE_CLOUD + clouds.get(i).type, (int)clouds.get(i).x, (int)clouds.get(i).y);
        }
    }
}
