package com.square.game;

import java.util.Random;

/**
 * Created by Mikolaj on 18.10.2015.
 */
public class NodeWater extends Node {

    Random rand;
    int varriation;

    public NodeWater(int type_id, int t, int v, String node_name, int _color)
    {
        super(type_id, t, node_name, _color, false, false);

        rand = new Random();
        varriation = v;
    }

    @Override
    public void render(Render ren, float x, float y, byte data)
    {
        ren.draw(texture + (int) data, (int) x, (int) y);
    }

    @Override
    public void onTimer(Map gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte)rand.nextInt(varriation));
        gameMap.setTimer(x, y, 1000 + rand.nextInt(1000));
    }

    @Override
    public boolean onSet(Map gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte)rand.nextInt(varriation));
        gameMap.setTimer(x, y, 500 + rand.nextInt(500));
        return true;
    }

    @Override
    public boolean isSolid(float x, float y) { if(y > 24.0f) return false; return true; }

    @Override
    public void onEntityEnter(Map gameMap, Entity e, int x, int y)
    {
        e.kill();
    }
}
