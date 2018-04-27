package com.square.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mikolaj on 09.10.2015.
 */
public class Entity {

    public enum Direction {
        NONE, RIGHT, LEFT
    }

    private Vector2 position;
    private Vector2 velocity;

    private float angle;
    private float angularVelocity;

    private Vector2[] hitTests;
    private HitLine[] hitLines;
    private Vector2 topLeft;
    private Vector2 topRight;
    private Vector2 bottomLeft;
    private Vector2 bottomRight;

    private final float dimension;
    private final float diagonal;
    private final int offset;

    public boolean murder = false;

    private Direction direction = Direction.NONE;

    private int texture;

    public boolean moveLock = false;

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

        hitTests = new Vector2[4];
        hitLines = new HitLine[4];
        hitLines[0] = new HitLine(topRight, bottomRight);
        hitLines[1] = new HitLine(bottomRight, bottomLeft);
        hitLines[2] = new HitLine(bottomLeft, topLeft);
        hitLines[3] = new HitLine(topLeft, topRight);
    }

    public void setSpawned(int id, EntityManager entityMan)
    {
        this.id = id;
        this.entityMan = entityMan;
    }

    public void accelerate(float x, float y, float a)
    {

        velocity.x += x;
        velocity.y += y;
        angularVelocity += a;
    }

    public void stop()
    {
        velocity.x  = 0;
        velocity.y  = 0;
        angularVelocity = 0;
    }

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

    public void setRightDirection()
    {
        direction = Direction.RIGHT;
        velocity.x = speed;
        angularVelocity = -GameMath.pi*0.02f;
    }

    public void setLeftDirection()
    {
        direction = Direction.LEFT;
        velocity.x = speed;
        angularVelocity = GameMath.pi*0.02f;
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

    private static final float friction = 0.01f;
    private static final float fightBack = 0.25f;

    public void update(Map gameMap)
    {
        // Gravitation and fight back

        velocity.y -= GameMath.gravitationalConstant;
        if(direction == Direction.RIGHT)
        {
            if(velocity.x < speed)
            {
                velocity.x += fightBack;
            }
            if(velocity.x > speed) velocity.x = speed;
        } else if(direction == Direction.LEFT)
        {
            if(velocity.x > -speed)
            {
                velocity.x -= fightBack;
            }
            if(velocity.x < -speed) velocity.x = -speed;
        }

        // Calculate entity new position

        float nextPositionX = position.x + velocity.x;
        float nextPositionY = position.y + velocity.y;

        float nextAngle = angle + angularVelocity;

        while ( nextAngle < 0.0f           ) nextAngle += GameMath.tau;
        while ( nextAngle >= GameMath.tau  ) nextAngle -= GameMath.tau;

        // Recalculate the corners

        topRight.x      = nextPositionX + diagonal * (float)Math.cos(nextAngle - GameMath.pi/4.0);
        topRight.y      = nextPositionY + diagonal * (float)Math.sin(nextAngle - GameMath.pi/4.0);
        bottomRight.x   = nextPositionX + diagonal * (float)Math.cos(nextAngle + GameMath.pi/4.0);
        bottomRight.y   = nextPositionY + diagonal * (float)Math.sin(nextAngle + GameMath.pi/4.0);
        bottomLeft.x    = nextPositionX + diagonal * (float)Math.cos(nextAngle + GameMath.pi*3.0/4.0);
        bottomLeft.y    = nextPositionY + diagonal * (float)Math.sin(nextAngle + GameMath.pi*3.0/4.0);
        topLeft.x       = nextPositionX + diagonal * (float)Math.cos(nextAngle - GameMath.pi*3.0/4.0);
        topLeft.y       = nextPositionY + diagonal * (float)Math.sin(nextAngle - GameMath.pi*3.0/4.0);

        // Check the legality of the move

        for(int i = 0; i < 4; i++)
        {
            hitTests[i] = gameMap.hitTest(hitLines[i]);
        }

        boolean allNull = true;

        for(Vector2 test : hitTests)
        {
            if(test != null)
            {
                allNull = false;

                velocity.x -= friction*(test.x - position.x);
                velocity.y -= friction*(test.y - position.y);
            }
        }

        if(allNull)
        {
            position.x = nextPositionX;
            position.y = nextPositionY;
            angle = nextAngle;
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

    public void draw(Render render)
    {
        render.drawCenterScale(texture, (int)position.x - offset, (int)position.y - offset, (int) dimension, (int) dimension, (float)Math.toDegrees(angle));
    }
}
