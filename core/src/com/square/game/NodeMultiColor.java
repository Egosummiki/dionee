package com.square.game;

import java.util.Random;

/**
 * Created by Mikolaj on 10.10.2015.
 */
public class NodeMultiColor extends Node {

    Random rand;
    int versions;

    public NodeMultiColor(int type_id, int t, String node_name, int _color, boolean _solid, int _ver, boolean _removeable)
    {
        super(type_id, t, node_name, _color, _solid, _removeable);
        rand = new Random();
        versions = _ver;
    }

    @Override
    public boolean onSet(Map gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte)rand.nextInt(versions));
        return true;
    }

    @Override
    public void render(Render ren, float x, float y, byte data)
    {
        ren.draw(super.texture + data, (int)x, (int)y);
    }
}
