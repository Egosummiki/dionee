package com.square.game;

/**
 * Created by Mikolaj on 14.10.2015.
 */
public class NodeTurn extends Node {

    boolean turnRight;

    public NodeTurn(int type_id, int t, String node_name, int _color, boolean removable, boolean right)
    {
        super(type_id, t, node_name, _color, false, removable);
        turnRight = right;
    }

    @Override
    public void onEntityInside(LevelMap gameMap, Entity e, int x, int y)
    {
        if(turnRight)
        {
            e.setRightDirection();
        } else
        {
            e.setLeftDirection();
        }
    }

    @Override
    public boolean canBePlacedOn(LevelMap gameMap, int x, int y)
    {
        return super.canBePlacedOn(gameMap, x, y) && gameMap.getNode(x, y-1) != NodeManager.NODE_WATER && gameMap.isSolid(x, y-1, 0, 0);
    }
}
