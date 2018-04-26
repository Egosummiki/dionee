package com.square.game;

import java.util.Vector;

/**
 * Created by Mikolaj on 07.10.2015.
 */
public class Node {

    public enum Structure
    {
        blank, solid, custom
    }

    int id;
    protected int texture;
    private String name;
    private int color;
    private boolean solid;
    private Structure structure;
    private boolean removeable;


    public Node(int type_id, int t, String node_name, int _color, boolean _solid, boolean _removeable)
    {
        id = type_id;
        texture = t;
        name = node_name;
        color = _color;
        solid = _solid;
        removeable = _removeable;
    }

    public boolean onSet(Map gameMap, int x, int y)
    {
        return false;
    }

    public String getName()
    {
        return name;
    }

    public Structure getStructure()
    {
        return structure;
    }

    public void applyCustomHitMap(Vector<Map.Line> hitMap)
    {

    }

    public boolean isSolid(float x, float y) { return solid; }

    public boolean isRemoveable() { return removeable; }

    public int getTexture() {return texture;}

    public int getColor()
    {
        return color;
    }

    public void render(Render ren, float x, float y, byte data)
    {
        ren.drawScale(texture, (int)x, (int)y, Game.block_sz, Game.block_sz);
    }

    public void onEntityEnter(Map gameMap, Entity e, int x, int y)
    {
    }

    public void onEntityWalkOn(Map gameMap, Entity e, int x, int y)
    {
    }

    public void onReset(Map gameMap, int x, int y)
    {
    }

    public void onTimer(Map gameMap, int x, int y)
    {
    }

    public void onLostInfluence(Map gameMap, Entity e, int old_x, int old_y, int new_x, int new_y)
    {

    }

    public boolean canBePlacedOn(Map gameMap, int x, int y)
    {
        return (gameMap.getNode(x+1,y) != 0 || gameMap.getNode(x,y-1) != 0 || gameMap.getNode(x-1,y) != 0  || gameMap.getNode(x,y+1) != 0) || gameMap.getBackNode(x,y) != 0;
    }
}
