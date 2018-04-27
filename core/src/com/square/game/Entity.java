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

        aimPosition = new Vector2(0.0f, 0.0f);

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

    public void applyForce(float x, float y, float a)
    {
        velocity.x += x;
        velocity.y += y;
        angularVelocity += a;
    }

    public void stop()
    {
        direction = Direction.NONE;
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
        angularVelocity = -GameMath.pi*0.02f;
    }

    public void setLeftDirection()
    {
        direction = Direction.LEFT;
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

        switch (direction)
        {
            case RIGHT:
                if(velocity.x < speed) velocity.x += fightBack;
                break;
            case LEFT:
                if(velocity.x > -speed) velocity.x -= fightBack;
                break;
            default: break;
        }

        // Calculate entity new position

        aimPosition.x = position.x + velocity.x;
        aimPosition.y = position.y + velocity.y;
        aimAngle = angle + angularVelocity;

        while ( aimAngle < 0.0f           ) aimAngle += GameMath.tau;
        while ( aimAngle >= GameMath.tau  ) aimAngle -= GameMath.tau;

        // Recalculate the corners

        double topRightAngle = aimAngle - GameMath.pi/4.0;
        Vector2 topRightVector = new Vector2(
                (float)Math.cos(topRightAngle),
                (float)Math.sin(topRightAngle)
        );

        topRight.x      = aimPosition.x + diagonal * (float)Math.cos(aimAngle - GameMath.pi/4.0);
        topRight.y      = aimPosition.y + diagonal * (float)Math.sin(aimAngle - GameMath.pi/4.0);
        bottomRight.x   = aimPosition.x + diagonal * (float)Math.cos(aimAngle + GameMath.pi/4.0);
        bottomRight.y   = aimPosition.y + diagonal * (float)Math.sin(aimAngle + GameMath.pi/4.0);
        bottomLeft.x    = aimPosition.x + diagonal * (float)Math.cos(aimAngle + GameMath.pi*3.0/4.0);
        bottomLeft.y    = aimPosition.y + diagonal * (float)Math.sin(aimAngle + GameMath.pi*3.0/4.0);
        topLeft.x       = aimPosition.x + diagonal * (float)Math.cos(aimAngle - GameMath.pi*3.0/4.0);
        topLeft.y       = aimPosition.y + diagonal * (float)Math.sin(aimAngle - GameMath.pi*3.0/4.0);

        // Check the legality of the move

        for(int i = 0; i < 4; i++)
        {
            hitTests[i] = gameMap.hitTest(hitLines[i]);
        }

        boolean applyAim = true;

        for(Vector2 test : hitTests)
        {
            if(test != null)
            {
                applyForce(friction*(position.x - test.x), friction*(position.y - test.y), 0);
                gameMap.sendOnEntityTouch( (int)(test.x - 0.01f) / gameMap.getBlockSize(), (int)(test.y - 0.01f) / gameMap.getBlockSize(), this  );
                applyAim = false;
            }
        }

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

    public void draw(Render render)
    {
        render.drawCenterScale(texture, (int)position.x - offset, (int)position.y - offset, (int) dimension, (int) dimension, (float)Math.toDegrees(angle));
    }
}
