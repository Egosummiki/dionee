package com.square.game;

import com.badlogic.gdx.Gdx;

/**
 * Klasa odpowiada przyciskowi jako elementowi interfejsu użytkownika.
 */
public class Button extends GuiElement {

    int texture;
    private int textureNormal;
    private int texturePress;

    private ButtonAction action;

    private boolean lock = false;

    /**
     * Konstruktor klasy Button.
     *
     * @param texture       Tekstura wyświetlana bez akcji.
     * @param texturePress  Tekstura wyświetlana przy naciśnięciu.
     * @param x             Pozycja x przycisku.
     * @param y             Pozycja y przycisku.
     * @param width         Szerokość przycisku.
     * @param height        Wysokość przycisku.
     */
    public Button(int texture, int texturePress, int x, int y, int width, int height)
    {
        super(x, y, width, height);
        this.texture = texture;
        textureNormal = texture;
        this.texturePress = texturePress;
    }


    /**
     * Konstruktor klasy Button.
     *
     * @param texture       Tekstura wyświetlana bez akcji.
     * @param x             Pozycja x przycisku.
     * @param y             Pozycja y przycisku.
     * @param width         Szerokość przycisku.
     * @param height        Wysokość przycisku.
     */
    public Button(int texture, int x, int y, int width, int height)
    {
        super(x, y, width, height);
        this.texture = texture;
        textureNormal = texture;
        texturePress = texture;
    }

    /**
     * Ustaw reagowanie na zdarzenie przycisku.
     *
     * @param a Akcja przycisku.
     * @return Zwracanie tego obiektu.
     */
    Button setAction(ButtonAction a)
    {
        action = a;
        return this;
    }

    /**
     * Ustaw zwykłą teksturę
     *
     * @param tex Tekstura.
     */
    void setTextureNormal(int tex)
    {
        textureNormal = tex;
    }

    /**
     * Ustaw teksturę przy przyciśnięciu.
     *
     * @param tex Tekstura.
     */
    void setTexturePressed(int tex)
    {
        texturePress = tex;
    }

    /**
     * Ustaw teksturę.
     *
     * @param tex tekstura.
     */
    public void setTexture(int tex)
    {
        texture = tex;
    }

    /**
     * Metoda wykonywana co cylk logiki gry.
     *
     * @param time Do usunięcia.
     */
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
                    texture = texturePress;
                }
            }
        } else
        {
            if(lock)
            {
                lock = false;
                action.onTapRelease(this, time);
                texture = textureNormal;
            }

        }
    }

    /**
     * Metoda wykonywana co cylk rysowania gry.
     *
     * @param ren Obiekt klasy Render.
     */
    @Override
    public void draw(Render ren)
    {
        ren.drawScale(texture, x, y, width, height);
    }
}
