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

        if(solid)
        {
            structure = Structure.solid;
        } else {
            structure = Structure.blank;
        }

        removeable = _removeable;
    }

    public boolean onSet(LevelMap gameMap, int x, int y)
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

    public void applyCustomHitMap(LevelMap gameMap, Vector<HitLine> hitMap, int x, int y)
    {

    }

    public boolean isSolid(float x, float y) { return solid; }

    public boolean isRemovable() { return removeable; }

    public int getTexture() {return texture;}

    public int getColor()
    {
        return color;
    }

    public void render(Render ren, float x, float y, byte data)
    {
        ren.drawScale(texture, (int)x, (int)y, Game.blockDimension, Game.blockDimension);
    }

    public void onEntityTouch(LevelMap gameMap, Entity e, int x, int y)
    {
    }

    public void onEntityInside(LevelMap gameMap, Entity e, int x, int y)
    {
    }

    public void onReset(LevelMap gameMap, int x, int y)
    {
    }

    public void onTimer(LevelMap gameMap, int x, int y)
    {
    }

    public void onLostInfluence(LevelMap gameMap, Entity e, int old_x, int old_y, int new_x, int new_y)
    {

    }

    public boolean canBePlacedOn(LevelMap gameMap, int x, int y)
    {
        return (gameMap.getNode(x+1,y) != 0 || gameMap.getNode(x,y-1) != 0 || gameMap.getNode(x-1,y) != 0  || gameMap.getNode(x,y+1) != 0) || gameMap.getBackNode(x,y) != 0;
    }
}
