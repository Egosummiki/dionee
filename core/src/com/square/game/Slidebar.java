package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Klasa odpowiedzialna za element interfejsu użytkownika - suwak.
 */
public class Slidebar extends GuiElement {

    private int process;
    private int last_process;
    private boolean last_touched = false;
    private boolean touched = false;

    public Slidebar(int _x, int _y, int start_proc)
    {
        super(_x, _y, 256, 32);
        process = start_proc;
        last_process = start_proc;
    }

    public void setProcess(int i)
    {
        process = i;
    }

    public void setProcessFloat(float f)
    {
        process = (int)(f*244.0f);
    }

    public int getProcess()
    {
        return process;
    }

    public float getProcessFloat()
    {
        return (float)process / 244.0f;
    }

    public boolean processChanged()
    {
        return process != last_process;
    }

    public boolean released()
    {
        return last_touched && !touched;
    }

    public void update(float time)
    {
        super.update(time);

        last_process = process;

        last_touched = touched;
        if(Gdx.input.isTouched())
        {
            touched = true;
            int rev_y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if(Gdx.input.getX() > x && rev_y > y && Gdx.input.getX() < x+width-12 && rev_y < y+height)
            {
                process = Gdx.input.getX() - x;
            }
        } else
        {
            touched = false;
        }
    }

    public void draw(Render ren)
    {
        ren.draw(Render.TEXTURE_MENU_SLIDEBAR, x, y);
        ren.draw(Render.TEXTURE_MENU_SLIDE_BUTTON, x+process, y);
    }
}
