package com.square.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mikolaj on 09.10.2015.
 *
 * Klasa istota - reprezentuje konkretną istotę.
 */
public class Entity {

    public enum Direction {
        NONE, RIGHT, LEFT
    }

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 aimPosition;

    private float angle;
    private float angularVelocity;
    private float aimAngle;

    private int previousBlockX = -1;
    private int previousBlockY = -1;

    private Map.HitTestResult[] hitTests;
    private HitLine[] hitLines;
    private Vector2[] corners;
    private Vector2 topLeft;
    private Vector2 topRight;
    private Vector2 bottomLeft;
    private Vector2 bottomRight;

    private final float dimension;
    private final float diagonal;
    private final int offset;

    boolean murder = false;

    private Direction direction = Direction.NONE;

    private int texture;

    private Vector2 cornerVector;

    boolean moveLock = false;

    private int id;
    private EntityManager entityMan;

    public Entity(int texture, float position_x, float position_y, float dimension) {
        this.texture = texture;

        position = new Vector2(position_x, position_y);
        velocity = new Vector2(0.0f, 0.0f);

        corners = new Vector2[4];
        corners[0] = topLeft = new Vector2(0,0);
        corners[1] = topRight = new Vector2(0,0);
        corners[2] = bottomLeft = new Vector2(0,0);
        corners[3] = bottomRight = new Vector2(0,0);

        this.dimension = dimension;
        diagonal = dimension / (float)Math.sqrt(2);
        offset = (int)dimension/2;

        angle = 0.0f;
        angularVelocity = 0.0f;

        aimPosition = new Vector2(0.0f, 0.0f);
        cornerVector = new Vector2(0.0f, 0.0f);

        hitTests = new Map.HitTestResult[4];
        hitLines = new HitLine[4];
        hitLines[0] = new HitLine(topRight, bottomRight);
        hitLines[1] = new HitLine(bottomRight, bottomLeft);
        hitLines[2] = new HitLine(bottomLeft, topLeft);
        hitLines[3] = new HitLine(topLeft, topRight);
    }

    /*
    * Funkcja ustawia paramentry przywołanej istoty.
    * */
    void setSpawned(int id, EntityManager entityMan)
    {
        this.id = id;
        this.entityMan = entityMan;
    }

    /*
    * Funkcja podaje siłę.
    * */
    void applyForce(float x, float y, float a)
    {
        velocity.x += x;
        velocity.y += y;
        angularVelocity += a;
    }

    /*
    * Funkcja całkowicie zatrzumuje istotę.
    * */
    void stop()
    {
        direction = Direction.NONE;
        velocity.x  = 0;
        velocity.y  = 0;
        angularVelocity = 0;
    }

    /*
    * Funkcja bezkonfliktowo zmienia pozycję istoty.
    * */
    public void applyPosition(Map gameMap, float pos_x, float pos_y, float alpha)
    {
        int ox = (int)Math.floor((position.x + 0.5f* dimension)/ dimension);
        int oy = (int)Math.floor((position.y + 0.5f* dimension)/ dimension);

        int nx = (int)Math.floor((pos_x + 0.5f* dimension)/ dimension);
        int ny = (int)Math.floor((pos_y + 0.5f* dimension)/ dimension);

        if(ox != nx || oy != ny)
        {
            gameMap.sendOnLostInfluence(this, ox, oy, nx, ny);
        }

        position.x = pos_x;
        position.y = pos_y;
        angle = alpha;
    }

    private static final float speed = 2.6f;
    private static final float angularSpeed = GameMath.pi*0.015f;

    public void setRightDirection()
    {
        direction = Direction.RIGHT;
    }

    public void setLeftDirection()
    {
        direction = Direction.LEFT;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void continueDirection()
    {
        if(direction == Direction.RIGHT) setRightDirection(); else
            if(direction == Direction.LEFT) setLeftDirection();
    }

    public void kill()
    {
        murder = true;
    }

    private static final float bounce = 0.1f;
    private static final float fightBack = 0.25f;
    private static final float angularFightBack = GameMath.pi*0.005f;

    Vector2 getCorner(boolean lowest, boolean axis_x)
    {
        Vector2 result = corners[0];

        for(int i = 1; i < 4; i++)
        {
            if(lowest && axis_x) {
                if (corners[i].x < result.x)
                    result = corners[i];
            } else if(lowest) {
                if(corners[i].y < result.y)
                    result = corners[i];
            } else if(axis_x) {
                if (corners[i].x > result.x)
                    result = corners[i];
            } else {
                if (corners[i].y > result.y)
                    result = corners[i];
            }
        }

        return result;
    }



    public void update(Map gameMap)
    {
        // Obsługa grawitacji i siły ciągnącej istotę w określonym kierunku.

        velocity.y -= GameMath.gravitationalConstant;

        switch (direction)
        {
            case RIGHT:
                if(velocity.x < speed) velocity.x += fightBack;
                if(angularVelocity > -angularSpeed) angularVelocity -= angularFightBack;
                break;
            case LEFT:
                if(velocity.x > -speed) velocity.x -= fightBack;
                if(angularVelocity < angularSpeed) angularVelocity += angularFightBack;
                break;
            default: break;
        }

        // Wylicz docelową pozycje istoty.

        aimPosition.x = position.x + velocity.x;
        aimPosition.y = position.y + velocity.y;
        aimAngle = angle + angularVelocity;

        while ( aimAngle < 0.0f           ) aimAngle += GameMath.tau;
        while ( aimAngle >= GameMath.tau  ) aimAngle -= GameMath.tau;

        // Wylicz wszystkie krawędzie istoty.

        double cornerAngle = aimAngle - GameMath.pi_over_4;
        cornerVector.x = diagonal*(float)Math.cos(cornerAngle);
        cornerVector.y = diagonal*(float)Math.sin(cornerAngle);

        topRight.x      = aimPosition.x + cornerVector.x;
        topRight.y      = aimPosition.y + cornerVector.y;
        bottomRight.x   = aimPosition.x + cornerVector.y;
        bottomRight.y   = aimPosition.y - cornerVector.x;
        bottomLeft.x    = aimPosition.x - cornerVector.x;
        bottomLeft.y    = aimPosition.y - cornerVector.y;
        topLeft.x       = aimPosition.x - cornerVector.y;
        topLeft.y       = aimPosition.y + cornerVector.x;

        /*
        * Sprawdź czy docelowa pozycja nie powoduje kolizji, jak tak to podaj siłę, która nie pozwoli
        * istocie przejść.
        * */

        boolean horizontalLock = false;
        boolean verticalLock = false;

        for(int i = 0; i < 4; i++)
        {
            Map.HitTestResult test = gameMap.hitTest(hitLines[i]);

            if(test != null)
            {
                Vector2 hitPoint = test.getHitPoint();

                if(test.getType() == Map.HitType.Vertical && !verticalLock)
                {
                    verticalLock = true;
                    if(position.x > hitPoint.x && velocity.x < 0.0f)
                    {
                        Vector2 extremal = getCorner(true, true);
                        if(extremal.y > test.getStart() && extremal.y < test.getEnd())
                            applyForce(hitPoint.x - extremal.x + bounce, 0.0f, 0.0f);
                    } else if(position.x < hitPoint.x && velocity.x > 0.0f)
                    {
                        Vector2 extremal = getCorner(false, true);
                        if(extremal.y > test.getStart() && extremal.y < test.getEnd())
                            applyForce(hitPoint.x - extremal.x - bounce, 0.0f, 0.0f);
                    }
                } else if(test.getType() == Map.HitType.Horizonal && !horizontalLock)
                {
                    horizontalLock = true;
                    if(position.y > hitPoint.y && velocity.y < 0.0f)
                    {
                        Vector2 extremalCorner = getCorner(true, false);
                        Vector2 displacement = extremalCorner;

                        if(displacement.x < test.getStart())
                        {
                            float start = test.getStart();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(start, hitPoint.y),
                                            new Vector2(start, hitPoint.y - gameMap.getBlockSize())));
                        } else if (displacement.x > test.getEnd())
                        {
                            float end = test.getEnd();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(end, hitPoint.y),
                                            new Vector2(end, hitPoint.y - gameMap.getBlockSize())));
                        }

                        if(displacement == null)
                            displacement = extremalCorner;

                        applyForce(0.0f, hitPoint.y - displacement.y + bounce, 0.0f);

                    } else if(position.y < hitPoint.y && velocity.y > 0.0f)
                    {
                        Vector2 extremal = getCorner(false, false);
                        if(extremal.x > test.getStart() && extremal.x < test.getEnd() || velocity.x < 0.0f)
                            applyForce(0.0f, hitPoint.y - extremal.y - bounce, 0.0f);
                    }

                    gameMap.sendOnEntityTouch(
                            (int)(hitPoint.x - 0.01f) / gameMap.getBlockSize(),
                            (int)(hitPoint.y - 0.01f) / gameMap.getBlockSize(),
                            this  );
                }

            }
        }

        position.x += velocity.x;
        position.y += velocity.y;
        angle = aimAngle;

        int blockX = (int)position.x / gameMap.getBlockSize();
        int blockY = (int)position.y / gameMap.getBlockSize();

        if(blockX != previousBlockX || blockY != previousBlockY)
        {
            gameMap.sendOnEntityInside(blockX, blockY, this);
            previousBlockX = blockX;
            previousBlockY = blockY;
        }

    }

    public float getVelocityX()
    {
        return velocity.x;
    }
    public float getPositionX()
    {
        return position.x;
    }
    public float getPositionY()
    {
        return position.y;
    }
    public float getAngle()
    {
        return angle;
    }

    /*
    * Funkcja wykonuje rysowanie istoty.
    * */
    public void draw(Render render)
    {
        render.drawCenterScale(texture, (int)position.x - offset, (int)position.y - offset, (int) dimension, (int) dimension, (float)Math.toDegrees(angle));
    }
}
