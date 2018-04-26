package com.square.game;

/**
 * Created by Mikolaj on 14.10.2015.
 */
public class NodeJump extends Node {

    public NodeJump(int type_id, int t, String node_name, int _color)
    {
        super(type_id, t, node_name, _color, true, true);
    }

    @Override
    public void onEntityEnter(Map gameMap, Entity e, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte)1);
        gameMap.setTimer(x, y, 600);
        e.accelerate(0, 6.0f, 0);
    }

    @Override
    public void onTimer(Map gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte) 0);
    }

    @Override
    public void render(Render ren, float x, float y, byte data)
    {
        if(data == 0)
        {
            ren.drawScale(texture, (int) x, (int) y, Game.blockDimension, Game.blockDimension);
        } else
        {
            ren.drawScale(Render.TEXTURE_JUMP_USE, (int) x, (int) y, Game.blockDimension, Game.blockDimension);
        }
    }

}
