package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by Mikolaj on 11.10.2015.
 */
public class GuiElement {

    protected int x;
    protected int y;

    private int original_x;
    private int original_y;

    protected int width;
    protected int height;

    public enum ani_type {
        NONE, SLIDE_FROM_LEFT, SLIDE_FROM_BOTTOM, SLIDE_TO_LEFT, SLIDE_TO_BOTTOM, SLIDE_FROM_RIGHT, SLIDE_TO_RIGHT
    }

    public interface ani_callback {
        void onAnimationEnd();
    }

    private boolean animation = false;
    private ani_type animation_type;
    private int animation_beg;
    private int animation_end;
    private long animation_start_time;
    private int animation_duration;
    private int animation_length;
    private ani_callback animation_callback;

    public GuiElement(int _x, int _y, int _w, int _h)
    {
        x = _x;
        y = _y;
        original_x = _x;
        original_y = _y;
        width = _w;
        height = _h;
    }

    public void resetPosition()
    {
        x = original_x;
        y = original_y;
    }

    public void setAnimation(ani_type t, int time, int ani_beg, ani_callback ani_call)
    {
        if(time <= 0) return;
        animation = true;
        animation_type = t;
        animation_start_time = System.currentTimeMillis();
        animation_duration = time;
        animation_callback = ani_call;

        if(animation_type == ani_type.SLIDE_FROM_LEFT)
        {
            animation_beg = ani_beg;
            animation_end = x;
            x = ani_beg;
            animation_length = animation_end - animation_beg;
        } else if(animation_type == ani_type.SLIDE_FROM_BOTTOM)
        {
            animation_beg = ani_beg;
            animation_end = y;
            y = ani_beg;
            animation_length = animation_end - animation_beg;
        } else if(animation_type == ani_type.SLIDE_TO_LEFT)
        {
            animation_beg = x;
            animation_end = ani_beg;
            x = ani_beg;
            animation_length = animation_end - animation_beg;
        }  else if(animation_type == ani_type.SLIDE_TO_BOTTOM)
        {
            animation_beg = y;
            animation_end = ani_beg;
            y = ani_beg;
            animation_length = animation_end - animation_beg;
        }  else if(animation_type == ani_type.SLIDE_FROM_RIGHT)
        {
            animation_beg = ani_beg;
            animation_end = x;
            x = ani_beg;
            animation_length = animation_beg - animation_end;
        }  else if(animation_type == ani_type.SLIDE_TO_RIGHT)
        {
            animation_beg = x;
            animation_end = ani_beg;
            x = ani_beg;
            animation_length = animation_beg - animation_end;
        }


    }

    public void setAnimation(ani_type t, int time, ani_callback ani_call)
    {
        if(t == ani_type.SLIDE_FROM_LEFT || t == ani_type.SLIDE_TO_LEFT)
        {
            setAnimation(t, time, -width, ani_call);
        } else if(t == ani_type.SLIDE_FROM_RIGHT || t == ani_type.SLIDE_TO_RIGHT)
        {
            setAnimation(t, time, Gdx.graphics.getWidth(), ani_call);
        } else if(t == ani_type.SLIDE_FROM_BOTTOM || t == ani_type.SLIDE_TO_BOTTOM)
        {
            setAnimation(t, time, -height, ani_call);
        }
    }

    public void setAnimation(ani_type t, int time)
    {
        setAnimation(t, time, null);
    }

    public void setAnimation(ani_type t, int ani_beg, int time)
    {
        setAnimation(t, time, ani_beg, null);
    }

    public void stopAnimation()
    {
        animation = false;
        animation_type = ani_type.NONE;
        animation_duration = 0;
        animation_start_time = 0;
        animation_beg = 0;
        animation_end = 0;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setPosition(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    public void setPositionX(int _x)
    {
        x = _x;
    }

    public void update(float time)
    {
        if(animation)
        {
            float process = (float)(System.currentTimeMillis() - animation_start_time)/(float)animation_duration;

            if(animation_type == ani_type.SLIDE_FROM_LEFT || animation_type == ani_type.SLIDE_TO_LEFT)
            {
                if(process >= 1.0f)
                {
                    x = animation_end;
                    if(animation_callback != null) animation_callback.onAnimationEnd();
                    stopAnimation();
                } else
                {
                    x = (int)(animation_beg + (animation_length*process));
                }
            } else if(animation_type == ani_type.SLIDE_FROM_RIGHT || animation_type == ani_type.SLIDE_TO_RIGHT)
            {
                if(process >= 1.0f)
                {
                    x = animation_end;
                    if(animation_callback != null) animation_callback.onAnimationEnd();
                    stopAnimation();
                } else
                {
                    x = (int)(animation_beg - (animation_length*process));
                }
            } else if(animation_type == ani_type.SLIDE_FROM_BOTTOM || animation_type == ani_type.SLIDE_TO_BOTTOM)
            {
                if(process >= 1.0f)
                {
                    y = animation_end;
                    if(animation_callback != null) animation_callback.onAnimationEnd();
                    stopAnimation();
                } else
                {
                    y = (int)(animation_beg + (animation_length*process));
                }
            }

        }
    }

    public void draw(Render ren)
    {
    }

}
