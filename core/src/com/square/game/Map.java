package com.square.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Mikolaj on 07.10.2015.
 */
public class Map {

    private class timer_data{
        int x;
        int y;
        long time;

        public timer_data(int _x, int _y, long _t)
        {
            x = _x;
            y = _y;
            time = _t;
        }
    }

    public short[] nodes;
    public byte[] nodes_data;
    public byte[] nodes_back;

    public Vector<timer_data> timers;

    NodeManager nodeMan;

    int width;
    int height;

    int block_sz;

    public Map(int map_width, int map_height, NodeManager nd, int b_s)
    {
        width = map_width;
        height = map_height;
        nodeMan = nd;
        block_sz = b_s;

        timers = new Vector<timer_data>();

        nodes = new short[width * height];
        nodes_data = new byte[width * height];
        nodes_back = new byte[width * height];
        Arrays.fill(nodes, (short)0);
        Arrays.fill(nodes_data, (byte)0);
        Arrays.fill(nodes_back, (byte)0);
    }

    public void render(Render textureRender)
    {
        for(int i = 0; i < timers.size(); i++)
        {
            if(timers.get(i).time <= System.currentTimeMillis())
            {
                nodeMan.getNode(getNode(timers.get(i).x, timers.get(i).y)).onTimer(this, timers.get(i).x, timers.get(i).y);
                timers.remove(i);
                i--;
            }
        }

        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                short node_id = getNode(x, y);

                if(getBackNode(x, y) > 0)
                {
                    nodeMan.getBackNode(getBackNode(x, y)).render(textureRender, x*block_sz, y*block_sz, getNodeData(x, y));
                }

                nodeMan.getNode(getNode(x, y)).render(textureRender, x*block_sz, y*block_sz, getNodeData(x, y));
            }
        }
    }

    public void sendReset()
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < height; x++)
            {
                nodeMan.getNode(getNode(x, y)).onReset(this, x, y);
            }
        }
    }

    public void setTimer(int x, int y, int milisecs)
    {
        timers.add(new timer_data(x, y, System.currentTimeMillis() + milisecs));
    }

    public short getNode(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodes[x + y * width];

        return 0;
    }

    public void setNode(int x, int y, short n)
    {
        nodeMan.getNode(n).onSet(this, x, y);
        if(x > -1 && x < 30 && y > -1 && y < 17) nodes[x + y * width] = n;
    }

    public void setBackNode(int x, int y, byte n)
    {
        //nodeMan.getBackNode(n).onSet(this, x, y);
        if(x > -1 && x < 30 && y > -1 && y < 17) nodes_back[x + y * width] = n;
    }

    public byte getBackNode(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodes_back[x + y * width];

        return 0;
    }

    public byte getNodeData(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodes_data[x + y * width];

        return 0;
    }

    public void setNodeData(int x, int y, byte n)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) nodes_data[x + y * width] = n;
    }

    public boolean isBlockSolid(int x, int y, float i_x, float i_y)
    {
        return nodeMan.getNode(getNode(x, y)).isSolid(i_x, i_y);
    }

    public boolean isBlockRemoveable(int x, int y)
    {
        return nodeMan.getNode(getNode(x, y)).isRemoveable();
    }

    public float getMaxDistanceToBlockX(float x_bot, float x_up, float mov) //Works only if mov < block_sz
    {
        if(mov > 0)
        {
            return (float)Math.floor((x_up+mov)/block_sz)*block_sz - x_up + 1;
        } else
        {
            return ((float)Math.floor((x_bot+mov)/block_sz)+1)*block_sz - x_bot - 1;
        }
    }

    public Vector2 findBlockByID(int id)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                if(getNode(x, y) == id)
                {
                    return new Vector2(x, y);
                }
            }
        }

        return new Vector2(-1, -1);
    }

    public float getMaxDistanceToBlockY(float y_bot, float y_up, float mov) //Works only if mov < block_sz
    {
        if(mov > 0)
        {
            return (float)Math.floor((y_up+mov)/block_sz)*block_sz - y_up - 1;
        } else
        {
            return ((float)Math.floor((y_bot+mov)/block_sz)+1)*block_sz - y_bot + 1;
        }
    }

    public void sendOnEntityEnter(int x, int y, Entity e)
    {
        nodeMan.getNode(getNode(x, y)).onEntityEnter(this, e, x, y);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void sendOnEntityWalkOn(int x, int y, Entity e)
    {
        nodeMan.getNode(getNode(x, y)).onEntityWalkOn(this, e, x, y);
    }

    public void senOnLostInfluence(Entity e, int old_x, int old_y, int new_x, int new_y)
    {
        nodeMan.getNode(getNode(old_x, old_y)).onLostInfluence(this, e, old_x, old_y, new_x, new_y);
    }

    public int getBlockSize()
    {
        return block_sz;
    }

    public Boolean loadLevel(Texture tex)
    {
        if(tex.getWidth() != 30 && tex.getHeight() != 17) return false;

        if (!tex.getTextureData().isPrepared()) {
            tex.getTextureData().prepare();
        }
        Pixmap pixmap = tex.getTextureData().consumePixmap();

        for(int y = 0; y < 17; y++)
        {
            for(int x = 0; x < 30; x++)
            {
                int color = pixmap.getPixel(x, 16-y);

                setNodeData(x, y, (byte)0);

                setNode(x, y, (short)0);
                for(int i = 0; i < nodeMan.getSize(); i++)
                {
                    if(color == nodeMan.getNode(i).getColor())
                    {
                        setNode(x, y, (short)i);
                    }
                }

                setBackNode(x, y, (byte)0);
                for(int i = 0; i < nodeMan.getBackSize(); i++)
                {
                    if(color == nodeMan.getBackNode(i).getColor())
                    {
                        setBackNode(x, y, (byte)i);
                    }
                }

            }
        }

        return true;
    }


}
