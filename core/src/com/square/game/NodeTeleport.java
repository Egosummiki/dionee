package com.square.game;

/**
 * Created by Mikolaj on 14.10.2015.
 */

import com.badlogic.gdx.math.Vector2;

public class NodeTeleport extends Node {

    int teleport_to;

    int texture_in_use;

    public NodeTeleport(int type_id, int t, int tu, String node_name, int _color, int tel_node)
    {
        super(type_id, t, node_name, _color, false, false);
        teleport_to = tel_node;
        texture_in_use = tu;
    }

    @Override
    public void onEntityWalkOn(Map gameMap, Entity e, int x, int y)
    {
        if(!(e.teleport_lock))
        {
            e.teleport_lock = true;
            Vector2 des = gameMap.findBlockByID(teleport_to);

            if(des.x > 0)
            {
                boolean right = e.getVelocityX() > 0 ? true : false;
                e.apply_position(gameMap, des.x * gameMap.getBlockSize(), des.y * gameMap.getBlockSize() + 6.627417f*e.getAngle(), 0);
                if(right) e.setRightDirection(); else e.setLeftDirection();

                gameMap.setNodeData(x, y, (byte)1);
                gameMap.setTimer(x, y, 500);

                gameMap.setNodeData((int)des.x, (int)des.y, (byte)1);
                gameMap.setTimer((int)des.x, (int)des.y, 500);
            }
        }
    }

    @Override
    public void render(Render ren, float x, float y, byte data)
    {
        if(data == 0)
        {
            ren.drawCenterScale(texture, (int)x, (int)y, Game.blockDimension, Game.blockDimension, (System.currentTimeMillis() / 10) % 360 );
        } else
        {
            ren.drawCenterScale(texture_in_use, (int)x, (int)y, Game.blockDimension, Game.blockDimension, (System.currentTimeMillis() / 10) % 360 );
        }
    }

    @Override
    public void onTimer(Map gameMap, int x, int y)
    {
        gameMap.setNodeData(x, y, (byte)0);
    }


}
