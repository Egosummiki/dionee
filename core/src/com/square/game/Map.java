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

    private class TimerData {
        int x;
        int y;
        long time;

        public TimerData(int _x, int _y, long _t)
        {
            x = _x;
            y = _y;
            time = _t;
        }
    }

    private short[] nodes;
    private byte[] nodesData;
    private byte[] nodesBack;

    private Vector<HitLine> hitMap;

    private Vector<TimerData> timers;

    NodeManager nodeMan;

    int width;
    int height;

    int blockDimension;

    public Map(int map_width, int map_height, NodeManager nd, int b_s)
    {
        width = map_width;
        height = map_height;
        nodeMan = nd;
        blockDimension = b_s;

        timers = new Vector<TimerData>();
        hitMap = new Vector<HitLine>();

        nodes = new short[width * height];
        nodesData = new byte[width * height];
        nodesBack = new byte[width * height];
        Arrays.fill(nodes, (short)0);
        Arrays.fill(nodesData, (byte)0);
        Arrays.fill(nodesBack, (byte)0);
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
                if(getBackNode(x, y) > 0)
                {
                    nodeMan.getBackNode(getBackNode(x, y)).render(textureRender, x* blockDimension, y* blockDimension, getNodeData(x, y));
                }

                nodeMan.getNode(getNode(x, y)).render(textureRender, x* blockDimension, y* blockDimension, getNodeData(x, y));
            }
        }
    }

    public void generateHitMap()
    {
        hitMap.clear();

        for(int x = 0; x < width; x++)
        {
            if(nodeMan.getNode(getNode(x,0)).getStructure() == Node.Structure.custom)
            {
                nodeMan.getNode(getNode(x,0)).applyCustomHitMap(hitMap);
            }
        }

        for(int y = 1; y < height; y++)
        {
            int startx = -1;
            for(int x = 0; x < width; x++)
            {
                Node.Structure up       = nodeMan.getNode(getNode(x,y)).getStructure();
                Node.Structure bottom   = nodeMan.getNode(getNode(x,y-1)).getStructure();

                if(up == Node.Structure.custom)
                {
                    nodeMan.getNode(getNode(x,y)).applyCustomHitMap(hitMap);
                    up = Node.Structure.blank;
                }

                if(bottom == Node.Structure.custom) bottom = Node.Structure.blank;

                if(up != bottom)
                {
                    if(startx == -1)
                    {
                        startx = (x == 0 ? -10 : x);
                    }
                } else {
                    if(startx != -1)
                    {
                        if(x == width-1) x += 10;
                        hitMap.add(new HitLine( new Vector2(blockDimension*startx, blockDimension*y),
                                                new Vector2(blockDimension*x,blockDimension*y)));
                    }

                    startx = -1;
                }
            }
        }

        for(int x = 1; x < height; x++)
        {
            int starty = -1;
            for(int y = 0; y < width; y++)
            {
                Node.Structure right  = nodeMan.getNode(getNode(x,y)).getStructure();
                Node.Structure left   = nodeMan.getNode(getNode(x-1,y)).getStructure();

                if( (right == Node.Structure.solid || left == Node.Structure.solid) && right != left)
                {
                    if(starty == -1)
                    {
                        starty = y;
                    }
                } else {
                    if(starty != -1)
                    {
                        hitMap.add(new HitLine(new Vector2(blockDimension*x, blockDimension*starty),
                                                new Vector2(blockDimension*x,blockDimension*y)));
                    }
                    starty = -1;
                }
            }
        }

    }

    public Vector2 hitTest(HitLine line)
    {
        for(HitLine compare : hitMap)
        {
            Vector2 result = GameMath.linearTest(compare, line);
            if(result != null)
            {
               return result;
            }
        }

        return null;
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
        timers.add(new TimerData(x, y, System.currentTimeMillis() + milisecs));
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
        //generateHitMap();
    }

    public void setBackNode(int x, int y, byte n)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) nodesBack[x + y * width] = n;
    }

    public byte getBackNode(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodesBack[x + y * width];

        return 0;
    }

    public byte getNodeData(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodesData[x + y * width];

        return 0;
    }

    public void setNodeData(int x, int y, byte n)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) nodesData[x + y * width] = n;
    }

    public boolean isSolid(int x, int y, float i_x, float i_y)
    {
        return nodeMan.getNode(getNode(x, y)).isSolid(i_x, i_y);
    }

    public boolean isRemoveable(int x, int y)
    {
        return nodeMan.getNode(getNode(x, y)).isRemoveable();
    }

    public float getMaxDistanceToBlockX(float x_bot, float x_up, float mov) //Works only if mov < blockDimension
    {
        if(mov > 0)
        {
            return (float)Math.floor((x_up+mov)/ blockDimension)* blockDimension - x_up + 1;
        } else
        {
            return ((float)Math.floor((x_bot+mov)/ blockDimension)+1)* blockDimension - x_bot - 1;
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

    public float getMaxDistanceToBlockY(float y_bot, float y_up, float mov) //Works only if mov < blockDimension
    {
        if(mov > 0)
        {
            return (float)Math.floor((y_up+mov)/ blockDimension)* blockDimension - y_up - 1;
        } else
        {
            return ((float)Math.floor((y_bot+mov)/ blockDimension)+1)* blockDimension - y_bot + 1;
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

    public void sendOnLostInfluence(Entity e, int old_x, int old_y, int new_x, int new_y)
    {
        nodeMan.getNode(getNode(old_x, old_y)).onLostInfluence(this, e, old_x, old_y, new_x, new_y);
    }

    public int getBlockSize()
    {
        return blockDimension;
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

        generateHitMap();

        return true;
    }


}
