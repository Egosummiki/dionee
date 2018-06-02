package com.square.game;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Mikolaj on 18.10.2015.
 */
public class NodeWater extends Node {

    private Random rand;
    private int variation;

    NodeWater(int type_id, int t, int v, String node_name, int _color)
    {
        super(type_id, t, node_name, _color, true, false);

        rand = new Random();
        variation = v;
    }

    @Override
    public void render(Render ren, float x, float y, byte data)
    {
        ren.draw(texture + (int) data, (int) x, (int) y);
    }

    @Override
    public void onTimer(LevelMap gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte)rand.nextInt(variation));
        gameMap.setTimer(x, y, 1000 + rand.nextInt(1000));
    }

    @Override
    public boolean onSet(LevelMap gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte)rand.nextInt(variation));
        gameMap.setTimer(x, y, 500 + rand.nextInt(500));
        return true;
    }

    @Override
    public void applyCustomHitMap(LevelMap gameMap, Vector<HitLine> hitMap, int x, int y) {
    }

    @Override
    public boolean isSolid(float x, float y) {
        return !(y > 24.0f);
    }

    @Override
    public void onEntityTouch(LevelMap gameMap, Entity e, int x, int y)
    {
        e.kill();
    }
}
