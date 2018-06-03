package com.square.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Klasa istota - reprezentuje konkretną istotę.
 */
public class Entity {

    /**
    * Enumeracja kierunków.
    */
    public enum Direction {
        NONE, RIGHT, LEFT
    }

    private Vector2 position; // Pozycja istoty
    private Vector2 velocity; // Pęd istoty.
    private Vector2 aimPosition; // Docelowa pozycja.

    private float angle; // Kąt istoty.
    private float angularVelocity; // Moment pędu istoty.
    private float aimAngle; // Docelowy kąt.

    /**
    * Poprzedni blok odwiedzony przez istotę.
    * */
    private int previousBlockX = -1;
    private int previousBlockY = -1;

    private HitLine[] hitLines; // Linie zderzeń.
    private Vector2[] corners; // Kąty istoty.
    private Vector2 topLeft;
    private Vector2 topRight;
    private Vector2 bottomLeft;
    private Vector2 bottomRight;

    private final float dimension; // Rozmiar istoty.
    private final float diagonal; // Połowa przekątnej.
    private final int offset; // Różnica na ekranie.

    boolean murder = false; // Czy istota ma być usunięta przez EntityManager.

    private Direction direction = Direction.NONE; // Kierunek istoty.

    private int texture; // Tekstura istoty.

    private Vector2 cornerVector; // Wektor wyliczania kątów.

    boolean moveLock = false; // Blokada przemieszczania (DEPRECATED)

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

        hitLines = new HitLine[4];
        hitLines[0] = new HitLine(topRight, bottomRight, false);
        hitLines[1] = new HitLine(bottomRight, bottomLeft, false);
        hitLines[2] = new HitLine(bottomLeft, topLeft, false);
        hitLines[3] = new HitLine(topLeft, topRight, false);
    }

    /**
    * Metoda podaje siłę.
    *
    * @param x Współczynnik x
    * @param y Współczynnik y
    * @param a Kąt
    * */
    void applyForce(float x, float y, float a)
    {
        velocity.x += x;
        velocity.y += y;
        angularVelocity += a;
    }

    /**
    * Metoda całkowicie zatrzumuje istotę.
    * */
    void stop()
    {
        direction = Direction.NONE;
        velocity.x  = 0;
        velocity.y  = 0;
        angularVelocity = 0;
    }

    /**
    * Metoda bezkonfliktowo zmienia pozycję istoty.
    *
    * @param gameMap    Mapa poziomu gry.
    * @param positionX  Docelowa pozycja X
    * @param positionY  Docelowa pozycja Y
    * @param angle      Docelowy kąt
    * */
    void applyPosition(LevelMap gameMap, float positionX, float positionY, float angle)
    {
        int ox = (int)Math.floor((position.x + 0.5f* dimension)/ dimension);
        int oy = (int)Math.floor((position.y + 0.5f* dimension)/ dimension);

        int nx = (int)Math.floor((positionX + 0.5f* dimension)/ dimension);
        int ny = (int)Math.floor((positionY + 0.5f* dimension)/ dimension);

        if(ox != nx || oy != ny)
        {
            gameMap.sendOnLostInfluence(this, ox, oy, nx, ny);
        }

        position.x = positionX;
        position.y = positionY;
        this.angle = angle;
    }

    private static final float speed = 2.6f; // Prędkość istoty
    private static final float angularSpeed = GameMath.pi*0.015f; // Prędkość obrotowa istoty

    /**
    * Metoda zmusza istotę do podążania w prawo!
    * */
    void setRightDirection()
    {
        direction = Direction.RIGHT;
    }

    /**
    * Metoda zmusza istotę do podążania w lewo!
    * */
    void setLeftDirection()
    {
        direction = Direction.LEFT;
    }

    /**
    * @return Metoda zwraca kierunek podążania istoty.
    * */
    Direction getDirection()
    {
        return direction;
    }

    void continueDirection()
    {
        if(direction == Direction.RIGHT) setRightDirection(); else
            if(direction == Direction.LEFT) setLeftDirection();
    }

    /**
    * Metoda zabija jednostkę.
    * */
    void kill()
    {
        murder = true;
    }

    private static final float bounce = 0.1f;
    private static final float fightBack = 0.25f;
    private static final float maxPush = 2.5f;
    private static final float angularFightBack = GameMath.pi*0.005f;

    /**
    * Metoda oblicza ekstremum kątów.
    *
    * @param lowest Jeśli true wylicza najniższy kąt, Jak false najwyższy
    * @param xAxis  Jak true po osi x, jak false po osi y.
    * */
    private Vector2 getCorner(boolean lowest, boolean xAxis)
    {
        Vector2 result = corners[0];

        for(int i = 1; i < 4; i++)
        {
            if(lowest && xAxis) {
                if (corners[i].x < result.x)
                    result = corners[i];
            } else if(lowest) {
                if(corners[i].y < result.y)
                    result = corners[i];
            } else if(xAxis) {
                if (corners[i].x > result.x)
                    result = corners[i];
            } else {
                if (corners[i].y > result.y)
                    result = corners[i];
            }
        }

        return result;
    }

    /**
    * Metoda update. Jest odpowiedzialna za ruch postaci i kolizje z obiektami mapy.
    *
    * @param gameMap Mapa poziomy gry.
    * */
    public void update(LevelMap gameMap)
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

        // Wylicz docelową pozycję istoty.

        aimPosition.x = position.x + velocity.x;
        aimPosition.y = position.y + velocity.y;
        aimAngle = angle + angularVelocity;

        while ( aimAngle < 0.0f           ) aimAngle += GameMath.tau;
        while ( aimAngle >= GameMath.tau  ) aimAngle -= GameMath.tau;

        // Wylicz wszystkie krawędzie istoty.

        calculateCorners();

        /*
        * Sprawdź czy docelowa pozycja nie powoduje kolizji, jak tak to podaj siłę, która nie pozwoli
        * istocie przejść.
        * */

        boolean horizontalLock = false;
        boolean verticalLock = false;

        for(int i = 0; i < 4; i++)
        {
            LevelMap.HitTestResult test = gameMap.hitTest(hitLines[i]);

            if(test != null)
            {
                Vector2 hitPoint = test.getHitPoint();

                if(test.getType() == LevelMap.HitType.Vertical && !verticalLock)
                {
                    verticalLock = true;
                    if(velocity.x < 0.0f && test.getSide())
                    {
                        Vector2 extremalCorner = getCorner(true, true);
                        Vector2 displacement = extremalCorner;

                        if(displacement.y < test.getStart())
                        {
                            float start = test.getStart();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(hitPoint.x, start),
                                            new Vector2(hitPoint.x - gameMap.getBlockSize(), start), false));
                        } else if (displacement.y > test.getEnd())
                        {
                            float end = test.getEnd();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(hitPoint.x, end),
                                            new Vector2(hitPoint.x - gameMap.getBlockSize(), end), false));
                        }

                        if(displacement == null)
                            displacement = extremalCorner;

                        applyForce(hitPoint.x - displacement.x + bounce, 0.0f, 0.0f);
                        if(velocity.x > maxPush) velocity.x = maxPush;


                    } else if(velocity.x > 0.0f && !test.getSide())
                    {
                        Vector2 extremalCorner = getCorner(false, true);
                        Vector2 displacement = extremalCorner;

                        if(displacement.y < test.getStart())
                        {
                            float start = test.getStart();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(hitPoint.x, start),
                                            new Vector2(hitPoint.x + gameMap.getBlockSize(), start), false));
                        } else if (displacement.y > test.getEnd())
                        {
                            float end = test.getEnd();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(hitPoint.x, end),
                                            new Vector2(hitPoint.x + gameMap.getBlockSize(), end), false));
                        }

                        if(displacement == null)
                            displacement = extremalCorner;

                        applyForce(hitPoint.x - displacement.x - bounce, 0.0f, 0.0f);
                        if(velocity.x < -maxPush) velocity.x = -maxPush;

                    }
                } else if(test.getType() == LevelMap.HitType.Horizonal && !horizontalLock)
                {
                    horizontalLock = true;
                    if(velocity.y < 0.0f && test.getSide())
                    {
                        Vector2 extremalCorner = getCorner(true, false);
                        Vector2 displacement = extremalCorner;

                        if(displacement.x < test.getStart())
                        {
                            float start = test.getStart();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(start, hitPoint.y),
                                            new Vector2(start, hitPoint.y - gameMap.getBlockSize()), false));
                        } else if (displacement.x > test.getEnd())
                        {
                            float end = test.getEnd();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(end, hitPoint.y),
                                            new Vector2(end, hitPoint.y - gameMap.getBlockSize()), false));
                        }

                        if(displacement == null)
                            displacement = extremalCorner;


                        applyForce(0.0f, hitPoint.y - displacement.y + bounce, 0.0f);
                        if(velocity.y > maxPush) velocity.y = maxPush;

                    } else if(velocity.y > 0.0f && !test.getSide())
                    {
                        Vector2 extremalCorner = getCorner(false, false);
                        Vector2 displacement = extremalCorner;

                        if(displacement.x < test.getStart())
                        {
                            float start = test.getStart();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(start, hitPoint.y),
                                            new Vector2(start, hitPoint.y + gameMap.getBlockSize()), false));
                        } else if (displacement.x > test.getEnd())
                        {
                            float end = test.getEnd();
                            displacement = GameMath.linearTest(hitLines[i],
                                    new HitLine(
                                            new Vector2(end, hitPoint.y),
                                            new Vector2(end, hitPoint.y + gameMap.getBlockSize()), false));
                        }

                        if(displacement == null)
                            displacement = extremalCorner;

                        applyForce(0.0f, hitPoint.y - displacement.y - bounce, 0.0f);
                        if(velocity.y < -maxPush) velocity.y = -maxPush;
                    }

                    gameMap.sendOnEntityTouch(
                            (int)(hitPoint.x - 0.01f) / gameMap.getBlockSize(),
                            (int)(hitPoint.y - 0.01f) / gameMap.getBlockSize(),
                            this  );
                }

            }
        }

        // Ustaw wyliczoną pozycję.

        position.x += velocity.x;
        position.y += velocity.y;
        angle = aimAngle;

        int blockX = (int)position.x / gameMap.getBlockSize();
        int blockY = (int)position.y / gameMap.getBlockSize();

        if(blockX != previousBlockX || blockY != previousBlockY)
        {
            gameMap.sendOnEntityInside(blockX, blockY, this);
            gameMap.sendOnLostInfluence(this, previousBlockX, previousBlockY, blockX, blockY);
            previousBlockX = blockX;
            previousBlockY = blockY;
        }

    }

    /**
    * Metoda jest używana przez update, służy do wyliczenia pozycji kątów istoty.
    * */
    private void calculateCorners() {
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
    }

    /**
    * @return Metoda zwraca pozycję x istoty.
    * */
    float getPositionX()
    {
        return position.x;
    }

    /**
    * @return Metoda zwraca pozycję y istoty.
    * */
    float getPositionY()
    {
        return position.y;
    }

    /**
    * Metoda wykonuje rysowanie istoty.
    *
    * @param render Klasa render.
    * */
    public void draw(Render render)
    {
        render.drawCenterScale(texture, (int)position.x - offset, (int)position.y - offset, (int) dimension, (int) dimension, (float)Math.toDegrees(angle));
    }
}
