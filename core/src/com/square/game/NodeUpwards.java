package com.square.game;

/**
 * Created by Mikolaj on 29.10.2015.
 */
public class NodeUpwards extends Node {

    public NodeUpwards(int type_id, int t, String node_name, int _color, boolean _removeable)
    {
        super(type_id, t, node_name, _color,  false, _removeable);
    }

    @Override
    public boolean isSolid(float x, float y)
    {
        return false;
    }

    @Override
    public void onLostInfluence(LevelMap gameMap, Entity e, int old_x, int old_y, int new_x, int new_y)
    {
        e.continueDirection();
        e.applyForce(0, 2.0f, 0);
    }

    @Override
    public void render(Render ren, float x, float y, byte data)
    {
        int py = (int)(y / Game.blockDimension);

        int s = (int)(System.currentTimeMillis() % (4*200));
        int k = s / 200;

        if(py%4 == k) ren.draw(Render.TEXTURE_UPWARDS_LIGHT, (int) x, (int) y);  else ren.draw(texture, (int) x, (int) y);
    }

    @Override
    public void onEntityInside(LevelMap gameMap, Entity e, int x, int y)
    {
        if(e.getDirection() == Entity.Direction.RIGHT)
        {
            if(e.getPositionX() + 0.5f*gameMap.getBlockSize() - (x*gameMap.getBlockSize()) < 0.5f*gameMap.getBlockSize()) return;
        } else
        {
            if(e.getPositionX() + 0.5f*gameMap.getBlockSize() - (x*gameMap.getBlockSize()) > 0.5f*gameMap.getBlockSize()) return;
        }

        e.stop();
        if(e.getDirection() == Entity.Direction.RIGHT) e.applyForce(1.0f, 2.0f, 0); else e.applyForce(-1.0f, 2.0f, 0);


        /*if(y < gameMap.getHeight()-1)
        {
            if(gameMap.getNode(x,y+1) == id)
            {
                e.stop();
                e.applyForce(0, 2.0f, 0);
            } else
            {
                e.continueDirection();
                e.applyForce(0, 0.2f, 0);
            }
        } else
        {
            e.stop();
            e.applyForce(0, 2.0f, 0);
        }*/

    }
}
