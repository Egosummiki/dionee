package com.square.game;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ScreenUtils;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Klasa zawiera statyczne metody służące do generowania tekstur.
 */
public class Generate {

    /**
     * Metoda służy do wygenerowania kwadratu o jednolitym kolorze.
     *
     * @param size
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static Texture square(int size, float r, float g, float b)
    {
        Pixmap px_map = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        px_map.setColor(r, g, b, 1.0f);
        px_map.fill();

        return new Texture(px_map);
    }

    /**
     * Wypełń kolorem od.
     * */
    static Texture downfill(int size, int fill, float r, float g, float b)
    {
        Pixmap px_map = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        px_map.setColor(0.0f, 0.0f, 0.0f, 0.0f);
        px_map.fill();

        px_map.setColor(r, g, b, 1.0f);

        px_map.fillRectangle(0, size - fill, size, size);

        return new Texture(px_map);
    }

    /**
     * Wygeneruj kwadrat i obwódką.
     *
     * @param size  Wielkość.
     * @param r     Kolor obwódki.
     * @param g
     * @param b
     * @param r2    Kolor wewnętrzny.
     * @param g2
     * @param b2
     * @return      Tekstura.
     */
    static Texture squareBorder(int size, float r, float g, float b, float r2, float g2, float b2)
    {
        Pixmap px_map = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        px_map.setColor(r, g, b, 1.0f);
        px_map.fill();
        px_map.setColor(r2, g2, b2, 1.0f);
        px_map.drawRectangle(0, 0, size, size);
        px_map.drawRectangle(1, 1, size-1, size-1);

        return new Texture(px_map);
    }

    static Texture gradientBackground(int width, int height, float r, float g, float b, float r2, float g2, float b2)
    {
        Pixmap px_map = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        for(int y = 0; y < height; y++)
        {
            float rc = (float)y*(r - r2)/(float)height + r2;
            float gc = (float)y*(g - g2)/(float)height + g2;
            float bc = (float)y*(b - b2)/(float)height + b2;
            px_map.setColor(rc, gc, bc, 1.0f);
            px_map.drawLine(0, y, width, y);
        }

        return new Texture(px_map);
    }

    static Texture darkPixmap(Pixmap px_map, float am)
    {
        px_map.setColor(0.0f, 0.0f, 0.0f, am);
        px_map.fillRectangle(0, 0, px_map.getWidth(), px_map.getHeight());

        /*for(int y = 0; y < px_map.getHeight(); y++)
        {
            for(int x = 0; x < px_map.getWidth(); x++)
            {
                int color = px_map.getPixel(x, y);

                ByteBuffer color_b = ByteBuffer.allocate(4);
                color_b.putInt(color);

                float r = toUnsignedByte(color_b.get(0)) / 255.0f;
                float g = toUnsignedByte(color_b.get(1)) / 255.0f;
                float b = toUnsignedByte(color_b.get(2)) / 255.0f;
                float a = toUnsignedByte(color_b.get(3)) / 255.0f;

                px_map.setColor(r * am, g * am, b * am, a);
                px_map.drawPixel(x, y);
            }
        }*/

        return new Texture(px_map);
    }

    private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

        if (yDown) {
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
            pixels.clear();
        }

        return pixmap;
    }

    static Pixmap getScreenshot(){
        return getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }
}
