package com.square.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mikolaj on 09.10.2015.
 */
public class Entity {

    public enum direction {
        NONE, RIGHT, LEFT
    }

    private float position_x;
    private float position_y;
    private float angle;

    private float velocity_x;
    private float velocity_y;
    private float angular_velocity;

    private Vector2 topLeft;
    private Vector2 topRight;
    private Vector2 bottomLeft;
    private Vector2 bottomRight;

    private final float box_length;
    private final float box_length_dst;

    public boolean apply_death = false;

    private direction e_direction = direction.NONE;

    private int texture;

    public boolean teleport_lock = false;

    private int entity_id = -1;
    private EntityManager entityMan;

    public Entity(int tex, float pos_x, float pos_y, float box_len) {
        texture = tex;

        position_x = pos_x;
        position_y = pos_y;

        topLeft = new Vector2(0,0);
        topRight = new Vector2(0,0);
        bottomLeft = new Vector2(0,0);
        bottomRight = new Vector2(0,0);

        box_length = box_len;
        box_length_dst = box_len / (float)Math.sqrt(2);

        angle = 0.0f;

        angular_velocity = 0.0f;
        velocity_x = 0.0f;
        velocity_y = 0.0f;
    }

    public void setSpawned(int id, EntityManager ent)
    {
        entity_id = id;
        entityMan = ent;
    }

    public void accelerate(float x, float y, float a)
    {

        velocity_x += x;
        velocity_y += y;
        angular_velocity += a;
    }

    public void stopY()
    {
        velocity_y = 0;
    }

    public void stop()
    {
        velocity_x  = 0;
        velocity_y  = 0;
        angular_velocity = 0;
    }

    public void setRightDirection()
    {
        e_direction = direction.RIGHT;
        velocity_x = 1.6f;
        angular_velocity = -GameMath.pi*0.02f;
    }

    public void setLeftDirection()
    {
        e_direction = direction.LEFT;
        velocity_x = -1.6f;
        angular_velocity = GameMath.pi*0.02f;
    }

    public direction getDirection()
    {
        return e_direction;
    }

    public void continueDirection()
    {
        if(e_direction == direction.RIGHT) setRightDirection(); else
            if(e_direction == direction.LEFT) setLeftDirection();
    }

    public void setPosition(float x, float y)
    {
        position_x = x;
        position_y = y;
        angle = 0.0f;

        velocity_x = 0;
        velocity_y = 0;
        angular_velocity = 0;
    }

    public boolean point_check(Map gameMap, float x, float y)
    {
        return !gameMap.isSolid((int)(x/gameMap.getBlockSize()), (int)(y/gameMap.getBlockSize()), x % gameMap.getBlockSize(), y % gameMap.getBlockSize());
    }

    public boolean linear_equation(float x, float y, float xa, float ya, float xb, float yb)
    {
        return (y - ya)*(xb - xa) - (yb - ya)*(x - xa) == 0;
    }

    public boolean linear_equation_gth(float x, float y, float xa, float ya, float xb, float yb)
    {
        return (y - ya)*(xb - xa) - (yb - ya)*(x - xa) > 0;
    }

    public boolean linear_equation_lss(float x, float y, float xa, float ya, float xb, float yb)
    {
        return (y - ya)*(xb - xa) - (yb - ya)*(x - xa) < 0;
    }

    public boolean inside_check(float x, float y)
    {
        float[] p = new float[4];
        float[] d = new float[4];

        float a = 0.5f * box_length;
        float b = (float)Math.sqrt(2) * a;
        float beta = angle + 0.25f * GameMath.pi;

        float cen_x = position_x + a;
        float cen_y = position_y + a;

        p[0] = cen_x - b*(float)Math.sin(beta);
        p[1] = cen_x + b*(float)Math.cos(beta);
        p[2] = cen_x + b*(float)Math.sin(beta);
        p[3] = cen_x - b*(float)Math.cos(beta);

        d[0] = cen_y + b*(float)Math.cos(beta);
        d[1] = cen_y + b*(float)Math.sin(beta);
        d[2] = cen_y - b*(float)Math.cos(beta);
        d[3] = cen_y - b*(float)Math.sin(beta);

        /*boolean t1 = linear_equation_lss(cen_x, cen_y, p[0], d[0], p[1], d[1]);
        boolean t2 = linear_equation_lss(cen_x, cen_y, p[1], d[1], p[2], d[2]);
        boolean t3 = linear_equation_lss(cen_x, cen_y, p[2], d[2], p[3], d[3]);
        boolean t4 = linear_equation_lss(cen_x, cen_y, p[3], d[3], p[0], d[0]);*/


        if(linear_equation_gth(x, y, p[0], d[0], p[1], d[1]) &&
                linear_equation_gth(x, y, p[1], d[1], p[2], d[2]) &&
                linear_equation_gth(x, y, p[2], d[2], p[3], d[3]) &&
                linear_equation_gth(x, y, p[3], d[3], p[0], d[0]))
        {
            return false;
        }

        return true;
    }

    public boolean collision_check(Map gameMap, float pos_x, float pos_y, float alpha)
    {
        float a = 0.5f * box_length;
        float b = (float)Math.sqrt(2) * a;
        float beta = alpha + 0.25f * GameMath.pi;

        return point_check(gameMap, pos_x + a - b*(float)Math.sin(beta), pos_y + a + b*(float)Math.cos(beta)) &&
                point_check(gameMap, pos_x + a + b*(float)Math.cos(beta), pos_y + a + b*(float)Math.sin(beta)) &&
                point_check(gameMap, pos_x + a + b*(float)Math.sin(beta), pos_y + a - b*(float)Math.cos(beta)) &&
                point_check(gameMap, pos_x + a - b*(float)Math.cos(beta), pos_y + a - b*(float)Math.sin(beta)) &&

                point_check(gameMap, pos_x + a - a*(float)Math.sin(alpha), pos_y + a + a*(float)Math.cos(alpha)) &&
                point_check(gameMap, pos_x + a + a*(float)Math.cos(alpha), pos_y + a + a*(float)Math.sin(alpha)) &&
                point_check(gameMap, pos_x + a + a*(float)Math.sin(alpha), pos_y + a - a*(float)Math.cos(alpha)) &&
                point_check(gameMap, pos_x + a - a*(float)Math.cos(alpha), pos_y + a - a*(float)Math.sin(alpha));

    }

    public boolean apply_position(Map gameMap, float pos_x, float pos_y, float alpha)
    {
        if(collision_check(gameMap, pos_x, pos_y, alpha))
        {
            int ox = (int)Math.floor((position_x + 0.5f*box_length)/box_length);
            int oy = (int)Math.floor((position_y + 0.5f*box_length)/box_length);

            int nx = (int)Math.floor((pos_x + 0.5f*box_length)/box_length);
            int ny = (int)Math.floor((pos_y + 0.5f*box_length)/box_length);

            if(ox != nx || oy != ny)
            {
                gameMap.sendOnLostInfluence(this, ox, oy, nx, ny);
            }

            position_x = pos_x;
            position_y = pos_y;
            angle = alpha;

            return true;
        }

        return false;
    }


    public float getExtremumX(Map gameMap, float pos_x, float pos_y, float alpha, boolean right)
    {
        float a = 0.5f * box_length;
        float b = (float)Math.sqrt(2) * a;
        float beta = alpha + 0.25f * GameMath.pi;

        float[] p = new float[8];

        p[0] = pos_x + a - b*(float)Math.sin(beta);
        p[1] = pos_x + a + b*(float)Math.cos(beta);
        p[2] = pos_x + a + b*(float)Math.sin(beta);
        p[3] = pos_x + a - b*(float)Math.cos(beta);
        p[4] = pos_x + a - a*(float)Math.sin(alpha);
        p[5] = pos_x + a + a*(float)Math.cos(alpha);
        p[6] = pos_x + a + a*(float)Math.sin(alpha);
        p[7] = pos_x + a - a*(float)Math.cos(alpha);

        float[] d = new float[8];

        d[0] = pos_y + a + b*(float)Math.cos(beta);
        d[1] = pos_y + a + b*(float)Math.sin(beta);
        d[2] = pos_y + a - b*(float)Math.cos(beta);
        d[3] = pos_y + a - b*(float)Math.sin(beta);
        d[4] = pos_y + a + a*(float)Math.cos(alpha);
        d[5] = pos_y + a + a*(float)Math.sin(alpha);
        d[6] = pos_y + a - a*(float)Math.cos(alpha);
        d[7] = pos_y + a - a*(float)Math.sin(alpha);

        int top = 0;

        if(right)
        {
            for(int i = 0; i < 8; i++)
            {
                if(p[i] > p[top] && !point_check(gameMap, p[i], d[i]))
                {
                    top = i;
                }
            }
        } else
        {
            for(int i = 0; i < 8; i++)
            {
                if(p[i] < p[top] && !point_check(gameMap, p[i], d[i]))
                {
                    top = i;
                }
            }
        }

        return p[top];
    }

    public float getExtremumY(Map gameMap, float pos_x, float pos_y, float alpha, boolean right)
    {
        float a = 0.5f * box_length;
        float b = (float)Math.sqrt(2) * a;
        float beta = alpha + 0.25f * GameMath.pi;

        float[] p = new float[8];

        p[0] = pos_x + a - b*(float)Math.sin(beta);
        p[1] = pos_x + a + b*(float)Math.cos(beta);
        p[2] = pos_x + a + b*(float)Math.sin(beta);
        p[3] = pos_x + a - b*(float)Math.cos(beta);
        p[4] = pos_x + a - a*(float)Math.sin(alpha);
        p[5] = pos_x + a + a*(float)Math.cos(alpha);
        p[6] = pos_x + a + a*(float)Math.sin(alpha);
        p[7] = pos_x + a - a*(float)Math.cos(alpha);

        float[] d = new float[8];

        d[0] = pos_y + a + b*(float)Math.cos(beta);
        d[1] = pos_y + a + b*(float)Math.sin(beta);
        d[2] = pos_y + a - b*(float)Math.cos(beta);
        d[3] = pos_y + a - b*(float)Math.sin(beta);
        d[4] = pos_y + a + a*(float)Math.cos(alpha);
        d[5] = pos_y + a + a*(float)Math.sin(alpha);
        d[6] = pos_y + a - a*(float)Math.cos(alpha);
        d[7] = pos_y + a - a*(float)Math.sin(alpha);

        int top = 0;

        if(right)
        {
            for(int i = 0; i < 8; i++)
            {
                if(d[i] > d[top] && !point_check(gameMap, p[i], d[i]))
                {
                    top = i;
                }
            }
        } else
        {
            for(int i = 0; i < 8; i++)
            {
                if(d[i] < d[top] && !point_check(gameMap, p[i], d[i]))
                {
                    top = i;
                }
            }
        }

        return d[top];
    }


    public void kill()
    {
        apply_death = true;
    }

    public float getNumberNP(float n)
    {
        return n >= 0 ? 1 : -1;
    }

    private static final float bounce = -0.1f;

    public void alternativeUpdate(Map gameMap)
    {
        // Gravitation

        velocity_y -= GameMath.gravitational_constant;

        // Calculate entity new position

        float nextPositionX = position_x + velocity_x;
        float nextPositionY = position_y + velocity_y;

        float nextAngle = angle + angular_velocity;

        while ( nextAngle < 0.0f           ) nextAngle += GameMath.tau;
        while ( nextAngle >= GameMath.tau  ) nextAngle -= GameMath.tau;

        // Recalculate the corners

        topRight.x      = nextPositionX + box_length_dst * (float)Math.cos(nextAngle - GameMath.pi/4.0);
        topRight.y      = nextPositionY + box_length_dst * (float)Math.sin(nextAngle - GameMath.pi/4.0);
        bottomRight.x   = nextPositionX + box_length_dst * (float)Math.cos(nextAngle + GameMath.pi/4.0);
        bottomRight.y   = nextPositionY + box_length_dst * (float)Math.sin(nextAngle + GameMath.pi/4.0);
        bottomLeft.x    = nextPositionX + box_length_dst * (float)Math.cos(nextAngle + GameMath.pi*3.0/4.0);
        bottomLeft.y    = nextPositionY + box_length_dst * (float)Math.sin(nextAngle + GameMath.pi*3.0/4.0);
        topLeft.x       = nextPositionX + box_length_dst * (float)Math.cos(nextAngle - GameMath.pi*3.0/4.0);
        topLeft.y       = nextPositionY + box_length_dst * (float)Math.sin(nextAngle - GameMath.pi*3.0/4.0);

        // Check the legality of the move

        Vector2[] tests = new Vector2[4];
        tests[0] = gameMap.hitTest(new HitLine(topRight, bottomRight));
        tests[1] = gameMap.hitTest(new HitLine(bottomRight, bottomLeft));
        tests[2] = gameMap.hitTest(new HitLine(bottomLeft, topLeft));
        tests[3] = gameMap.hitTest(new HitLine(topLeft, topRight));

        boolean allNull = true;

        for(Vector2 test : tests)
        {
            if(test != null)
            {
                allNull = false;

                velocity_x -= 0.01f*(test.x - position_x);
                velocity_y -= 0.01f*(test.y - position_y);
            }
        }

        if(allNull)
        {
            position_x = nextPositionX;
            position_y = nextPositionY;
            angle = nextAngle;
        }

    }

    public void update(float time, Map gameMap)
    {
        alternativeUpdate(gameMap);
/*
        if(position_x >= 0 && position_x < Gdx.graphics.getWidth() && position_y >= 0 && position_y < Gdx.graphics.getHeight())
        {
            gameMap.sendOnEntityWalkOn((int)((position_x + 0.5f*box_length)/box_length), (int)((position_y + 0.5f*box_length)/box_length), this);
        }

        float new_position_x = position_x + (velocity_x*time);
        float new_position_y = position_y + (velocity_y*time);
        float new_alpha = (float)Math.IEEEremainder(angle + (angular_velocity * time), 0.5f* GameMath.pi);

        if(new_alpha < 0)
        {
            new_alpha = 0.5f* GameMath.pi + new_alpha;
        }

        if(position_x < 0 || position_x > Gdx.graphics.getWidth())
        {
            position_x = new_position_x;
            angle = new_alpha;
        } else if(apply_position(gameMap, new_position_x, new_position_y, new_alpha))
        {
            velocity_y -= GameMath.gravitational_constant;
        } else if(apply_position(gameMap, position_x, new_position_y, new_alpha))
        {
            if(velocity_x > 0)
            {
                float top_x = getExtremumX(gameMap, new_position_x, new_position_y, new_alpha, true);
                float distance = top_x - new_position_x;
                float dat = top_x - (top_x%gameMap.getBlockSize());

                apply_position(gameMap, dat - distance - 1, new_position_y, new_alpha);
            } else
            {
                float top_x = getExtremumX(gameMap, new_position_x, new_position_y, new_alpha, false);
                float distance = top_x - new_position_x;
                float dat = top_x + gameMap.getBlockSize() - top_x%gameMap.getBlockSize();

                apply_position(gameMap, dat - distance + 1, new_position_y, new_alpha);
            }

            velocity_y -= GameMath.gravitational_constant;
        } else if(apply_position(gameMap, new_position_x, position_y, new_alpha))
        {
            if(velocity_y > 0)
            {
                float top_y = getExtremumY(gameMap, new_position_x, new_position_y, new_alpha, true);
                float distance = top_y - new_position_y;
                float dat = top_y - (top_y%gameMap.getBlockSize());

                apply_position(gameMap, new_position_x, dat - distance - 1, new_alpha);
            } else
            {
                float top_y = getExtremumY(gameMap, new_position_x, new_position_y, new_alpha, false);
                float distance = top_y - new_position_y;
                float dat = top_y + gameMap.getBlockSize() - top_y%gameMap.getBlockSize();

                apply_position(gameMap, new_position_x, dat - distance + 1, new_alpha);
            }

            velocity_y = 0;
            gameMap.sendOnEntityEnter((int)((position_x / box_length) + 0.5f), (int)(position_y / box_length) - 1, this);
        } else if(apply_position(gameMap, new_position_x, new_position_y, angle))
        {
            //Never happens?
        } else if(apply_position(gameMap, position_x, new_position_y, angle))
        {
            if(velocity_x > 0)
            {
                float top_x = getExtremumX(gameMap, new_position_x, new_position_y, new_alpha, true);
                float distance = top_x - new_position_x;
                float dat = top_x - (top_x%gameMap.getBlockSize());

                apply_position(gameMap, dat - distance - 1, new_position_y, new_alpha);
            } else
            {
                float top_x = getExtremumX(gameMap, new_position_x, new_position_y, new_alpha, false);
                float distance = top_x - new_position_x;
                float dat = top_x + gameMap.getBlockSize() - top_x%gameMap.getBlockSize();

                apply_position(gameMap, dat - distance + 1, new_position_y, new_alpha);
            }

            velocity_y -= GameMath.gravitational_constant;

        } else if(apply_position(gameMap, new_position_x, position_y, angle))
        {
            if(velocity_y > 0)
            {
                float top_y = getExtremumY(gameMap, new_position_x, new_position_y, new_alpha, true);
                float distance = top_y - new_position_y;
                float dat = top_y - (top_y%gameMap.getBlockSize());

                apply_position(gameMap, new_position_x, dat - distance - 1, new_alpha);

            } else
            {
                float top_y = getExtremumY(gameMap, new_position_x, new_position_y, new_alpha, false);
                float distance = top_y - new_position_y;
                float dat = top_y + gameMap.getBlockSize() - top_y%gameMap.getBlockSize();

                apply_position(gameMap, new_position_x, dat - distance + 1, new_alpha);
            }

            velocity_y = 0;
            gameMap.sendOnEntityEnter((int)((position_x / box_length) + 0.5f), (int)(position_y / box_length) - 1, this);
        } else
        {
            float res_x;
            float res_y;

            if(velocity_x > 0)
            {
                float top_x = getExtremumX(gameMap, new_position_x, new_position_y, new_alpha, true);
                float distance = top_x - new_position_x;
                float dat = top_x - (top_x%gameMap.getBlockSize());

                res_x = dat - distance - 1;
            } else
            {
                float top_x = getExtremumX(gameMap, new_position_x, new_position_y, new_alpha, false);
                float distance = top_x - new_position_x;
                float dat = top_x + gameMap.getBlockSize() - top_x%gameMap.getBlockSize();

                res_x = dat - distance + 1;
            }

            if(velocity_y > 0)
            {
                float top_y = getExtremumY(gameMap, new_position_x, new_position_y, new_alpha, true);
                float distance = top_y - new_position_y;
                float dat = top_y - (top_y%gameMap.getBlockSize());

                res_y = dat - distance - 1;
            } else
            {
                float top_y = getExtremumY(gameMap, new_position_x, new_position_y, new_alpha, false);
                float distance = top_y - new_position_y;
                float dat = top_y + gameMap.getBlockSize() - top_y%gameMap.getBlockSize();

                res_y = dat - distance + 1;
            }

            apply_position(gameMap, res_x, res_y, new_alpha);

            velocity_y = 0;
            gameMap.sendOnEntityEnter((int)((position_x / box_length) + 0.5f), (int)(position_y / box_length) - 1, this);
        }

        */

    }

    public float getVelocityX()
    {
        return velocity_x;
    }
    public float getPositionX()
    {
        return position_x;
    }
    public float getPositionY()
    {
        return position_y;
    }
    public float getAngle()
    {
        return angle;
    }

    public void draw(Render ren)
    {
        ren.drawCenterScale(texture, (int)position_x, (int)position_y, (int)box_length, (int)box_length, (float)Math.toDegrees(angle));
    }
}
