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

    /*
    * Klasa jest podtrzebna blokom, które wyliczają czas.
    * */
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

    /*
    * Rodzaj zderzenia.
    * */
    public enum HitType
    {
        Vertical, Horizonal
    }

    /*
    * Wynik testu zderzenia.
    * */
    public class HitTestResult
    {
        HitType type;
        Vector2 hitPoint;


        float start, end;

        public HitTestResult(HitType type, Vector2 hitPoint, float start, float end) {
            this.type = type;
            this.hitPoint = hitPoint;
            this.start = start;
            this.end = end;
        }


        public HitType getType() {
            return type;
        }

        public Vector2 getHitPoint() {
            return hitPoint;
        }

        public float getStart() {
            return start;
        }

        public float getEnd() {
            return end;
        }



    }

    /*
    * Klasa potrzebna, w celu stworzenia tablicy wektorów.
    * */
    private class HitLineVector extends Vector<HitLine> {
    }

    private short[] nodes;
    private byte[] nodesData;
    private byte[] nodesBack;

    private Vector<HitLine>[] hitMapVertical;
    private Vector<HitLine>[] hitMapHorizontal;

    private Vector<TimerData> timers;

    private NodeManager nodeMan;

    private int width;
    private int height;

    private int blockDimension;

    public Map(int map_width, int map_height, NodeManager nd, int b_s)
    {
        width = map_width;
        height = map_height;
        nodeMan = nd;
        blockDimension = b_s;

        timers = new Vector<TimerData>();
        hitMapVertical = new HitLineVector[width];
        hitMapHorizontal = new HitLineVector[height];

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

    /*
    * Zadaniem funkcji jest wyliczenie mapy zderzeń poziomu.
    * */
    void generateHitMap()
    {
        Arrays.fill(hitMapVertical, null);
        Arrays.fill(hitMapHorizontal, null);

        for(int x = 0; x < width; x++)
        {
            if(nodeMan.getNode(getNode(x,0)).getStructure() == Node.Structure.custom)
            {
                nodeMan.getNode(getNode(x,0)).applyCustomHitMap(this, null, x, 0);
            }
        }

        for(int y = 1; y < height; y++)
        {
            int xStart = -1;
            for(int x = 0; x < width; x++)
            {
                Node.Structure up       = nodeMan.getNode(getNode(x,y)).getStructure();
                Node.Structure bottom   = nodeMan.getNode(getNode(x,y-1)).getStructure();

                if(up == Node.Structure.custom)
                {
                    nodeMan.getNode(getNode(x,y)).applyCustomHitMap(this, null, x, y);
                    up = Node.Structure.blank;
                }

                if(bottom == Node.Structure.custom) bottom = Node.Structure.blank;

                if(up != bottom)
                {
                    if(xStart == -1) xStart = (x == 0 ? -10 : x);
                } else if(xStart != -1)
                {
                    if(hitMapHorizontal[y] == null) hitMapHorizontal[y] = new HitLineVector();

                    hitMapHorizontal[y].add(new HitLine( new Vector2(blockDimension*xStart, blockDimension*y),
                                            new Vector2(blockDimension*x,blockDimension*y)));
                    xStart = -1;
                }
            }

            if(xStart != -1)
            {
                if(hitMapHorizontal[y] == null) hitMapHorizontal[y] = new HitLineVector();
                hitMapHorizontal[y].add(new HitLine( new Vector2(blockDimension*xStart, blockDimension*y),
                        new Vector2(blockDimension*(width+10),blockDimension*y)));
            }
        }

        for(int x = 1; x < width; x++)
        {
            int yStart = -1;
            for(int y = 0; y < height; y++)
            {
                Node.Structure right  = nodeMan.getNode(getNode(x,y)).getStructure();
                Node.Structure left   = nodeMan.getNode(getNode(x-1,y)).getStructure();

                if(right == Node.Structure.custom) right = Node.Structure.blank;
                if(left == Node.Structure.custom) left = Node.Structure.blank;

                if(right != left)
                {
                    if(yStart == -1) yStart = y;
                } else if(yStart != -1)
                {
                    if(hitMapVertical[x] == null) hitMapVertical[x] = new HitLineVector();

                    hitMapVertical[x].add(new HitLine(new Vector2(blockDimension*x, blockDimension*yStart),
                                            new Vector2(blockDimension*x,blockDimension*y)));
                    yStart = -1;
                }
            }

            if(yStart != -1)
            {
                if(hitMapVertical[x] == null) hitMapVertical[x] = new HitLineVector();
                hitMapVertical[x].add(new HitLine(new Vector2(blockDimension*x, blockDimension*yStart),
                        new Vector2(blockDimension*x,blockDimension*height)));
            }
        }

    }

    /*
    * Funkcja testuje czy konkretna linia przechodzi przez mapę zderzeń.
    * */
    HitTestResult hitTest(HitLine line)
    {
        // Horizontal test

        float min_y = Math.min(line.getA().y, line.getB().y) / blockDimension;
        float max_y = Math.max(line.getA().y, line.getB().y) / blockDimension;

        if(min_y < 0) min_y = 0;
        if(min_y > height-1) min_y = height-1;
        if(max_y < 0) max_y = 0;
        if(max_y > height-1) max_y = height-1;

        for(float whole = (float)Math.ceil(min_y); whole < max_y; whole++)
        {
            Vector<HitLine> currentHitMap = hitMapHorizontal[(int)whole];
            if(currentHitMap == null) continue;

            for(HitLine compare : currentHitMap)
            {
                Vector2 result = GameMath.linearTest(compare, line);
                if(result != null)
                {
                    return new HitTestResult(HitType.Horizonal, result, compare.getA().x, compare.getB().x);
                }
            }
        }

        // Vertical test

        float min_x = Math.min(line.getA().x, line.getB().x) / blockDimension;
        float max_x = Math.max(line.getA().x, line.getB().x) / blockDimension;

        if(min_x < 0) min_x = 0;
        if(min_x > width-1) min_x = width-1;
        if(max_x < 0) max_x = 0;
        if(max_x > width-1) max_x = width-1;

        for(float whole = (float)Math.ceil(min_x); whole < max_x; whole++)
        {
            Vector<HitLine> currentHitMap = hitMapVertical[(int)whole];
            if(currentHitMap == null) continue;

            for(HitLine compare : currentHitMap)
            {
                Vector2 result = GameMath.linearTest(compare, line);
                if(result != null)
                {
                    return new HitTestResult(HitType.Vertical, result, compare.getA().y, compare.getB().y);
                }
            }
        }

        return null;
    }

    /*
    * Funkcja informuje bloki o tym, że mapa jest resetowana.
    * */
    void sendReset()
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < height; x++)
            {
                nodeMan.getNode(getNode(x, y)).onReset(this, x, y);
            }
        }

    }

    /*
    * Ustaw timer.
    * */
    void setTimer(int x, int y, int milisecs)
    {
        timers.add(new TimerData(x, y, System.currentTimeMillis() + milisecs));
    }

    /*
    * Podaj wartość bloku.
    * */
    short getNode(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodes[x + y * width];

        return 0;
    }

    /*
    * Ustaw wartość bloku na konkretnej pozycji.
    * */
    void setNode(int x, int y, short n)
    {
        nodeMan.getNode(n).onSet(this, x, y);
        if(x > -1 && x < 30 && y > -1 && y < 17) nodes[x + y * width] = n;
    }

    /*
    * Ustaw block tła.
    * */
    public void setBackNode(int x, int y, byte n)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) nodesBack[x + y * width] = n;
    }

    /*
    * Podaj wartość bloku tła.
    * */
    public byte getBackNode(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodesBack[x + y * width];

        return 0;
    }

    /*
    * Podaj informację o bloku.
    * */
    public byte getNodeData(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodesData[x + y * width];

        return 0;
    }

    /*
    * Ustaw informację o bloku.
    * */
    public void setNodeData(int x, int y, byte n)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) nodesData[x + y * width] = n;
    }

    /*
    * Czy blok jest blokiem stałym?
    * */
    public boolean isSolid(int x, int y, float i_x, float i_y)
    {
        return nodeMan.getNode(getNode(x, y)).isSolid(i_x, i_y);
    }

    /*
    * Czy blok można usunąć?
    * */
    public boolean isRemovable(int x, int y)
    {
        return nodeMan.getNode(getNode(x, y)).isRemovable();
    }

    /*
    * Znajdź blok o id.
    * */
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

    /*
    * Informuj o zdarzeniu, kiedy istota doktnie bloku.
    * */
    public void sendOnEntityTouch(int x, int y, Entity e)
    {
        nodeMan.getNode(getNode(x, y)).onEntityTouch(this, e, x, y);
    }

    /*
    * Informuj o zdarzeniu, kiedy istota jest wewnątrz bloku.
    * */
    public void sendOnEntityInside(int x, int y, Entity e)
    {
        nodeMan.getNode(getNode(x, y)).onEntityInside(this, e, x, y);
    }

    /*
    * Informuj o zdarzeniu, kiedy istota opuści blok.
    * */
    public void sendOnLostInfluence(Entity e, int old_x, int old_y, int new_x, int new_y)
    {
        nodeMan.getNode(getNode(old_x, old_y)).onLostInfluence(this, e, old_x, old_y, new_x, new_y);
    }

    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }
    public int getBlockSize()
    {
        return blockDimension;
    }

    /*
    * Załaduj poziom!
    * */
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
