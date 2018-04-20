package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by Mikolaj on 11.10.2015.
 */
public class Button extends GuiElement {

    int texture;
    int texture_normal;
    int texture_press;

    ButtonAction action;

    boolean lock = false;

    public Button(int _texture, int _texture_press, int _x, int _y, int _width, int _height)
    {
        super(_x, _y, _width, _height);
        texture = _texture;
        texture_normal = _texture;
        texture_press = _texture_press;
    }


    public Button(int _texture, int _x, int _y, int _width, int _height)
    {
        super(_x, _y, _width, _height);
        texture = _texture;
        texture_normal = _texture;
        texture_press = _texture;
    }

    public Button setAction(ButtonAction a)
    {
        action = a;
        return this;
    }

    public void setTextureNormal(int tex)
    {
        texture_normal = tex;
    }

    public void setTexturePressed(int tex)
    {
        texture_press = tex;
    }

    public void setTexture(int tex)
    {
        texture = tex;
    }

    @Override
    public void update(float time)
    {
        super.update(time);
        if(Gdx.input.isTouched())
        {
            int input_x = Gdx.input.getX();
            int input_y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if(input_x > x && input_x < x + width && input_y > y && input_y < y + height)
            {
                action.onPress(this, time);

                if(!lock)
                {
                    lock = true;
                    action.onTap(this, time);
                    texture = texture_press;
                }
            }
        } else
        {
            if(lock)
            {
                lock = false;
                action.onTapRelease(this, time);
                texture = texture_normal;
            }

        }
    }

    @Override
    public void draw(Render ren)
    {
        ren.drawScale(texture, x, y, width, height);
    }
}
