package com.square.game;

/**
 * Created by Mikolaj on 22.10.2015.
 */
public class NodeAlternate extends Node {

    boolean turnRight;

    public NodeAlternate(int type_id, int t, String node_name, int _color, boolean removable, boolean right)
    {
        super(type_id, t, node_name, _color, false, removable);
        turnRight = right;
    }

    @Override
    public boolean onSet(LevelMap gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, turnRight ? (byte) 0 : (byte) 2);
        return true;
    }

    @Override
    public void onTimer(LevelMap gameMap, int x, int y)
    {
        if(gameMap.getNodeData(x, y) == 3)
        {
            gameMap.setNodeData(x, y, (byte)2);
        } else if(gameMap.getNodeData(x, y) == 1)
        {
            gameMap.setNodeData(x, y, (byte)0);
        }
    }

    @Override
    public void onEntityInside(LevelMap gameMap, Entity e, int x, int y)
    {
        if(gameMap.getNodeData(x,y) == 0)
        {
            gameMap.setNodeData(x, y,(byte)3);
            gameMap.setTimer(x, y, 512);
            e.setRightDirection();
        } else if(gameMap.getNodeData(x,y) == 2)
        {
            gameMap.setNodeData(x, y,(byte)1);
            gameMap.setTimer(x, y, 512);
            e.setLeftDirection();
        }
    }

    @Override
    public void render(Render ren, float x, float y, byte data)
    {
        ren.drawScale(texture + data, (int) x, (int) y, Game.blockDimension, Game.blockDimension);
    }

    @Override
    public boolean canBePlacedOn(LevelMap gameMap, int x, int y)
    {
        return super.canBePlacedOn(gameMap, x, y) && gameMap.getNode(x, y-1) != NodeManager.NODE_WATER && gameMap.isSolid(x, y-1, 0, 0);
    }

    @Override
    public void onReset(LevelMap gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, turnRight ? (byte) 0 : (byte) 2);
    }
}
