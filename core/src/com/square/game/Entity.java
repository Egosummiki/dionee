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

    private Vector2[] hitTests;
    private HitLine[] hitLines;
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

        topLeft = new Vector2(0,0);
        topRight = new Vector2(0,0);
        bottomLeft = new Vector2(0,0);
        bottomRight = new Vector2(0,0);

        this.dimension = dimension;
        diagonal = dimension / (float)Math.sqrt(2);
        offset = (int)dimension/2;

        angle = 0.0f;
        angularVelocity = 0.0f;

        aimPosition = new Vector2(0.0f, 0.0f);
        cornerVector = new Vector2(0.0f, 0.0f);

        hitTests = new Vector2[4];
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

    private static final float friction = 0.02f;
    private static final float fightBack = 0.25f;
    private static final float angularFightBack = GameMath.pi*0.005f;


    public void update(Map gameMap)
    {
        // Obsługa gravitacji i siły ciągnącej istotę w określonym kierunku.

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

        // Sprawdź czy ruch jest legalny. (Czy postać nie będzie zawadzać o mapę uderzeń.)

        for(int i = 0; i < 4; i++)
        {
            hitTests[i] = gameMap.hitTest(hitLines[i]); // hitLines[0-3] contain the corners!
        }

        boolean applyAim = true;

        for(Vector2 test : hitTests)
        {
            if(test != null)
            {
                //applyForce(friction*(aimPosition.x - test.x), friction*(aimPosition.y - test.y), 0);
                applyForce(0.0f, 0.5f, 0.0f);
                gameMap.sendOnEntityTouch( (int)(test.x - 0.01f) / gameMap.getBlockSize(), (int)(test.y - 0.01f) / gameMap.getBlockSize(), this  );
                applyAim = false;
            }
        }

        // Jeśli ruch był legalny zastosuj go.

        if(applyAim)
        {
            position.x = aimPosition.x;
            position.y = aimPosition.y;
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
