package com.square.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Vector;

/**
 * Klasa reprezentująca mapę danego poziomu gry.
 */
public class LevelMap {

    /**
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

    /**
    * Rodzaj zderzenia.
    * */
    public enum HitType
    {
        Vertical, Horizonal
    }

    /**
    * Wynik testu zderzenia.
    * */
    public class HitTestResult
    {
        HitType type;
        Vector2 hitPoint;


        boolean side;


        float start, end;

        public HitTestResult(HitType type, Vector2 hitPoint, float start, float end, boolean side) {
            this.type = type;
            this.hitPoint = hitPoint;
            this.start = start;
            this.end = end;
            this.side = side;
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

        public boolean getSide() {
            return side;
        }



    }

    /**
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

    private NodeManager nodeManager;

    private int width;
    private int height;

    private int blockDimension;

    /**
     * Konstruktor klasy LevelMap
     *
     * @param mapWidth          Szerokość mapy podana w blokach.
     * @param mapHeight         Wysokość mapy podana w blokach.
     * @param nodeManager       Manager bloków.
     * @param blockDimension    Wymiar pojedynczego bloku.
     */
    public LevelMap(int mapWidth, int mapHeight, NodeManager nodeManager, int blockDimension)
    {
        width = mapWidth;
        height = mapHeight;
        this.nodeManager = nodeManager;
        this.blockDimension = blockDimension;

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
                nodeManager.getNode(getNode(timers.get(i).x, timers.get(i).y)).onTimer(this, timers.get(i).x, timers.get(i).y);
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
                    nodeManager.getBackNode(getBackNode(x, y)).render(textureRender, x* blockDimension, y* blockDimension, getNodeData(x, y));
                }

                nodeManager.getNode(getNode(x, y)).render(textureRender, x* blockDimension, y* blockDimension, getNodeData(x, y));
            }
        }
    }

    /**
    * Zadaniem metody jest wyliczenie mapy zderzeń poziomu.
    * */
    void generateHitMap()
    {
        Arrays.fill(hitMapVertical, null);
        Arrays.fill(hitMapHorizontal, null);

        for(int x = 0; x < width; x++)
        {
            if(nodeManager.getNode(getNode(x,0)).getStructure() == Node.Structure.custom)
            {
                nodeManager.getNode(getNode(x,0)).applyCustomHitMap(this, null, x, 0);
            }
        }

        for(int y = 1; y < height; y++)
        {
            int xStart = -1;
            boolean side = false;
            for(int x = 0; x < width; x++)
            {
                Node.Structure up       = nodeManager.getNode(getNode(x,y)).getStructure();
                Node.Structure bottom   = nodeManager.getNode(getNode(x,y-1)).getStructure();

                if(up == Node.Structure.custom)
                {
                    nodeManager.getNode(getNode(x,y)).applyCustomHitMap(this, null, x, y);
                    up = Node.Structure.blank;
                }

                if(bottom == Node.Structure.custom) bottom = Node.Structure.blank;

                if(up != bottom)
                {
                    side = up != Node.Structure.solid || bottom != Node.Structure.blank;
                    if(xStart == -1) xStart = (x == 0 ? -10 : x);
                } else if(xStart != -1)
                {
                    if(hitMapHorizontal[y] == null) hitMapHorizontal[y] = new HitLineVector();

                    hitMapHorizontal[y].add(new HitLine( new Vector2(blockDimension*xStart, blockDimension*y),
                                            new Vector2(blockDimension*x,blockDimension*y), side));
                    xStart = -1;
                }
            }

            if(xStart != -1)
            {
                if(hitMapHorizontal[y] == null) hitMapHorizontal[y] = new HitLineVector();
                hitMapHorizontal[y].add(new HitLine( new Vector2(blockDimension*xStart, blockDimension*y),
                        new Vector2(blockDimension*(width+10),blockDimension*y), side));
            }
        }

        for(int x = 1; x < width; x++)
        {
            int yStart = -1;
            boolean side = false;
            for(int y = 0; y < height; y++)
            {
                Node.Structure right  = nodeManager.getNode(getNode(x,y)).getStructure();
                Node.Structure left   = nodeManager.getNode(getNode(x-1,y)).getStructure();

                if(right == Node.Structure.custom) right = Node.Structure.blank;
                if(left == Node.Structure.custom) left = Node.Structure.blank;

                if(right != left)
                {
                    side = right != Node.Structure.solid || left != Node.Structure.blank;
                    if(yStart == -1) yStart = y;
                } else if(yStart != -1)
                {
                    if(hitMapVertical[x] == null) hitMapVertical[x] = new HitLineVector();

                    hitMapVertical[x].add(new HitLine(new Vector2(blockDimension*x, blockDimension*yStart),
                                            new Vector2(blockDimension*x,blockDimension*y), side));
                    yStart = -1;
                }
            }

            if(yStart != -1)
            {
                if(hitMapVertical[x] == null) hitMapVertical[x] = new HitLineVector();
                hitMapVertical[x].add(new HitLine(new Vector2(blockDimension*x, blockDimension*yStart),
                        new Vector2(blockDimension*x,blockDimension*height), side));
            }
        }

    }

    /**
    * Metoda testuje czy konkretna linia przechodzi przez mapę zderzeń.
    *
    * @param line   Linia, która będzie podlegać testom.
    * @return       Wynik testu.
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
                    return new HitTestResult(HitType.Horizonal, result, compare.getA().x, compare.getB().x, compare.getSide());
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
                    return new HitTestResult(HitType.Vertical, result, compare.getA().y, compare.getB().y, compare.getSide());
                }
            }
        }

        return null;
    }

    /**
    * Metoda informuje bloki o tym, że mapa jest resetowana.
    * */
    void sendReset()
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < height; x++)
            {
                nodeManager.getNode(getNode(x, y)).onReset(this, x, y);
            }
        }

    }

    /**
    * Ustaw timer.
    * */
    void setTimer(int x, int y, int milisecs)
    {
        timers.add(new TimerData(x, y, System.currentTimeMillis() + milisecs));
    }

    /**
     * Metoda podaje typ bloku w konkretym miejscu.
     *
     * @param x     koordynat x mapy.
     * @param y     koordynat y mapy.
     * @return      id rodzaju bloku.
     */
    short getNode(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodes[x + y * width];

        return 0;
    }

    /**
    * Ustaw wartość bloku na konkretnej pozycji.
    * */
    void setNode(int x, int y, short n)
    {
        nodeManager.getNode(n).onSet(this, x, y);
        if(x > -1 && x < 30 && y > -1 && y < 17) nodes[x + y * width] = n;
    }

    /**
    * Ustaw block tła.
    * */
    public void setBackNode(int x, int y, byte n)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) nodesBack[x + y * width] = n;
    }

    /**
    * Podaj wartość bloku tła.
    * */
    public byte getBackNode(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodesBack[x + y * width];

        return 0;
    }

    /**
    * Podaj informację o bloku.
    * */
    public byte getNodeData(int x, int y)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) return nodesData[x + y * width];

        return 0;
    }

    /**
    * Ustaw informację o bloku.
    * */
    public void setNodeData(int x, int y, byte n)
    {
        if(x > -1 && x < 30 && y > -1 && y < 17) nodesData[x + y * width] = n;
    }

    /**
    * Czy blok jest blokiem stałym?
    * */
    public boolean isSolid(int x, int y, float i_x, float i_y)
    {
        return nodeManager.getNode(getNode(x, y)).isSolid(i_x, i_y);
    }

    /**
    * Czy blok można usunąć?
    * */
    public boolean isRemovable(int x, int y)
    {
        return nodeManager.getNode(getNode(x, y)).isRemovable();
    }

    /**
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

    /**
    * Informuj o zdarzeniu, kiedy istota doktnie bloku.
    * */
    public void sendOnEntityTouch(int x, int y, Entity e)
    {
        nodeManager.getNode(getNode(x, y)).onEntityTouch(this, e, x, y);
    }

    /**
    * Informuj o zdarzeniu, kiedy istota jest wewnątrz bloku.
    * */
    public void sendOnEntityInside(int x, int y, Entity e)
    {
        nodeManager.getNode(getNode(x, y)).onEntityInside(this, e, x, y);
    }

    /**
    * Informuj o zdarzeniu, kiedy istota opuści blok.
    * */
    void sendOnLostInfluence(Entity e, int old_x, int old_y, int new_x, int new_y)
    {
        nodeManager.getNode(getNode(old_x, old_y)).onLostInfluence(this, e, old_x, old_y, new_x, new_y);
    }

    /**
     * Informuj o zdarzeniu, że istota opuściła blok.
     * Od OnLostInfluence różni się tym, że jest wykonywana po poinformowaniu bloku,
     * w którego zasięg jednostka weszła.
     */
    void sendOnEntityLeavesBlock(Entity e, int oldX, int oldY, int newX, int newY)
    {
        nodeManager.getNode(getNode(oldX, oldY)).onEntityLeavesBlock(this, e, oldX, oldY, newX, newY);
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

    /**
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
                for(int i = 0; i < nodeManager.getSize(); i++)
                {
                    if(color == nodeManager.getNode(i).getColor())
                    {
                        setNode(x, y, (short)i);
                    }
                }

                setBackNode(x, y, (byte)0);
                for(int i = 0; i < nodeManager.getBackSize(); i++)
                {
                    if(color == nodeManager.getBackNode(i).getColor())
                    {
                        setBackNode(x, y, (byte)i);
                    }
                }

            }
        }

        return true;
    }


}
