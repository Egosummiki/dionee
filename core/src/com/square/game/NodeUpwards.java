package com.square.game;

/**
 * Klasa odpowiedzialna za blok unoszący jednostki do góry.
 */
public class NodeUpwards extends Node {

    NodeUpwards(int type_id, int t, String node_name, int _color, boolean _removable)
    {
        super(type_id, t, node_name, _color,  false, _removable);
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

        if(py%4 == k) ren.drawScale(Render.TEXTURE_UPWARDS_LIGHT, (int) x, (int) y, Game.blockDimension, Game.blockDimension);
            else ren.drawScale(texture, (int) x, (int) y, Game.blockDimension, Game.blockDimension);
    }

    @Override
    public void onEntityInside(LevelMap gameMap, Entity e, int x, int y)
    {
        e.stop();
        e.applyForce(e.getRememberedDirection() == Entity.Direction.RIGHT ? 1.0f : -1.0f, 2.0f, 0.0f);
    }
}
