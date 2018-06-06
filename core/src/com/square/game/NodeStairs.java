package com.square.game;

/**
 * Blok schodów aktualnie nie występujący na żadnym z poziomów.
 */
public class NodeStairs extends Node {

    public NodeStairs(int type_id, int t, String node_name, int _color)
    {
        super(type_id, t, node_name, _color, true, true);
    }

    @Override
    public boolean isSolid(float x, float y)
    {
        if(x > y) return true;

        return false;
    }
}
