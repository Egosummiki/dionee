package com.square.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Klasa jest odpowiedzialna za wyświetlanie elementów na ekranie.
 */
public class Render {

    private AssetManager assetMan;
    private SpriteBatch batch;
    private Texture[] tex_registry;
    private Texture[] level_registry;
    private Texture[] level_tut_registry;
    private BitmapFont font;
    private BitmapFont font_small;

    private ShapeRenderer shape;

    private boolean batch_ready = false;

    public final static int LEVEL_NUMBER = 5;
    public final static int LEVEL_TUT_NUMBER = 2;

    public final static int TEXTURE_NULL = 0;
    public final static int TEXTURE_ENTITY_TEST = 1;

    public final static int TEXTURE_GRASS_01 = 2;
    public final static int TEXTURE_GRASS_02 = 3;
    public final static int TEXTURE_GRASS_03 = 4;
    public final static int TEXTURE_GRASS_04 = 5;

    public final static int TEXTURE_DIRT_01 = 6;
    public final static int TEXTURE_DIRT_02 = 7;
    public final static int TEXTURE_DIRT_03 = 8;
    public final static int TEXTURE_DIRT_04 = 9;

    public final static int TEXTURE_BUTTON_REMOVE = 10;

    public final static int TEXTURE_BACKGROUND = 11;

    public final static int TEXTURE_BUTTON_PLAY = 12;

    public final static int TEXTURE_CLOUD = 13;
    public final static int TEXTURE_CLOUD_2 = 14;

    public final static int TEXTURE_BUTTON_REFRESH = 15;

    public final static int TEXTURE_PLANK_01 = 16;
    public final static int TEXTURE_PLANK_02 = 17;
    public final static int TEXTURE_PLANK_03 = 18;
    public final static int TEXTURE_PLANK_04 = 19;

    public final static int TEXTURE_HIGHLIGHT = 20;

    public final static int TEXTURE_STAIRS = 21;

    public final static int TEXTURE_JUMP = 22;

    public final static int TEXTURE_TURN_LEFT = 23;
    public final static int TEXTURE_TURN_RIGHT = 24;

    public final static int TEXTURE_TELEPORT_A = 25;
    public final static int TEXTURE_TELEPORT_B = 26;

    public final static int TEXTURE_BACK_DIRT_01 = 27;
    public final static int TEXTURE_BACK_DIRT_02 = 28;
    public final static int TEXTURE_BACK_DIRT_03 = 29;
    public final static int TEXTURE_BACK_DIRT_04 = 30;

    public final static int TEXTURE_JUMP_USE = 31;

    public final static int TEXTURE_BUTTON_RUN = 32;
    public final static int TEXTURE_BUTTON_RUN_PRESSED = 33;

    public final static int TEXTURE_MENU_BACK = 34;

    public final static int TEXTURE_BUTTON_OPTIONS = 35;
    public final static int TEXTURE_BUTTON_OPTIONS_PRESSED = 36;

    public final static int TEXTURE_BUTTON_LEVELS = 37;
    public final static int TEXTURE_BUTTON_LEVELS_PRESSED = 38;

    public final static int TEXTURE_WATER_01 = 39;
    public final static int TEXTURE_WATER_02 = 40;
    public final static int TEXTURE_WATER_03 = 41;
    public final static int TEXTURE_WATER_04 = 42;

    public final static int TEXTURE_ALTERNATE_RIGHT = 43;
    public final static int TEXTURE_ALTERNATE_RIGHT_LIGHT = 44;
    public final static int TEXTURE_ALTERNATE_LEFT = 45;
    public final static int TEXTURE_ALTERNATE_LEFT_LIGHT = 46;

    public final static int TEXTURE_GUI_LEVELMENU_BACK = 47;
    public final static int TEXTURE_GUI_LEVELMENU_BUTTON = 48;
    public final static int TEXTURE_GUI_LEVELMENU_BUTTON_PRESS = 49;
    public final static int TEXTURE_GUI_LEVELMENU_LOCKED = 50;

    public final static int TEXTURE_TELEPORT_A_USE = 51;
    public final static int TEXTURE_TELEPORT_B_USE = 52;

    public final static int TEXTURE_WOOD = 53;
    public final static int TEXTURE_WOOD_END_R = 54;
    public final static int TEXTURE_WOOD_END_L = 55;

    public final static int TEXTURE_GOBACK = 56;

    public final static int TEXTURE_PLAY_SMALL = 57;
    public final static int TEXTURE_PLAY_SMALL_PRESS = 58;

    public final static int TEXTURE_BUTTON_EXIT = 59;

    public final static int TEXTURE_BUTTON_REPLAY = 60;
    public final static int TEXTURE_BUTTON_REPLAY_PRESS = 61;
    public final static int TEXTURE_BACKGROUND_MENU = 62;

    public final static int TEXTURE_UPWARDS = 63;

    public final static int TEXTURE_METAL = 64;

    public final static int TEXTURE_METAL_BACK = 65;
    public final static int TEXTURE_LEVEL_DONE = 66;

    public final static int TEXTURE_BACK_LVL_CLEARED = 67;

    public final static int TEXTURE_BUTTON_SKIP = 68;
    public final static int TEXTURE_BUTTON_SKIP_PRESSED = 69;
    public final static int TEXTURE_BUTTON_CONT = 70;
    public final static int TEXTURE_BUTTON_CONT_PRESSED = 71;

    public final static int TEXTURE_TUTORIAL_DES_BACK = 72;
    public final static int TEXTURE_TUTORIAL_DES_ENTITIES = 73;
    public final static int TEXTURE_TUTORIAL_DES_JUMP = 74;
    public final static int TEXTURE_TUTORIAL_DES_MENU = 75;

    public final static int TEXTURE_MENU_SLIDEBAR = 76;
    public final static int TEXTURE_MENU_SLIDE_BUTTON = 77;

    public final static int TEXTURE_MUSIC_NOTE = 78;
    public final static int TEXTURE_MUSIC_NOTE_NO = 79;

    public final static int TEXTURE_LOGO = 80;

    public final static int TEXTURE_UPWARDS_LIGHT = 81;

    public final static int TEXTURE_LOGO_LIGHT = 82;
    public final static int TEXTURE_LOGO_MASK = 83;

    public final static int TEXTURE_PLUS = 84;

    /**
     * Klasa wewnętrzna, pomaga przy ładowaniu zasobów.
     */
    private class Load {

        Texture texture;
        String str;

        Load(Texture t)
        {
            texture = t;
            str = null;
        }

        Load(String s)
        {
            str = s;
            texture = null;
        }
    }


    /**
     * Konstruktor klasy Render.
     *
     * @param blockDimension    Rozmiar pojedynczego bloku.
     */
    public Render(int blockDimension)
    {
        level_registry = new Texture[LEVEL_NUMBER];
        level_tut_registry = new Texture[LEVEL_TUT_NUMBER];
        batch = new SpriteBatch();
        assetMan = new AssetManager();

        Load[] load_queue = {
                new Load(Generate.squareBorder(blockDimension, 1f, 1f, 1f, 0.42f, 0f, 0f)),
                new Load(Generate.square(blockDimension, .0f, .5f, .0f)),
                new Load(Generate.square(blockDimension, .0f, .48f, .0f)),
                new Load(Generate.square(blockDimension, .0f, .46f, .0f)),
                new Load(Generate.square(blockDimension, .0f, .52f, .0f)),
                new Load(Generate.square(blockDimension, .48f, .0f, .0f)),
                new Load(Generate.square(blockDimension, .47f, .0f, .0f)),
                new Load(Generate.square(blockDimension, .46f, .0f, .0f)),
                new Load(Generate.square(blockDimension, .42f, .0f, .0f)),
                new Load("buttons/destroy.png"),
                new Load(Generate.gradientBackground(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 120.0f / 255.0f, 147.0f / 255.0f, 255.0f / 255.0f, 49.0f / 255.0f, 92.0f / 255.0f, 162.0f / 255.0f)),
                new Load("buttons/start.png"),

                new Load("background/cloud.png"),
                new Load("background/cloud2.png"),

                new Load("buttons/refresh.png"),

                new Load(Generate.square(blockDimension, .63f, .4f, .0f)),
                new Load(Generate.square(blockDimension, .64f, .4f, .0f)),
                new Load(Generate.square(blockDimension, .64f, .42f, .0f)),
                new Load(Generate.square(blockDimension, .6f, .392f, .0f)),

                new Load("buttons/highlight.png"), // 20

                new Load("stairs.png"),

                new Load("nodes/jump.png"),

                new Load("nodes/turnleft.png"),
                new Load("nodes/turnright.png"),

                new Load("nodes/teleporta.png"),
                new Load("nodes/teleportb.png"),

                new Load(Generate.square(blockDimension, .26f, .0f, .0f)),
                new Load(Generate.square(blockDimension, .27f, .0f, .0f)),
                new Load(Generate.square(blockDimension, .24f, .0f, .0f)),
                new Load(Generate.square(blockDimension, .25f, .0f, .0f)),

                new Load("nodes/jumpuse.png"),

                new Load("menu/play.png"),
                new Load("menu/playpress.png"),

                new Load("menu/background.png"),

                new Load("menu/options.png"),
                new Load("menu/optionspress.png"),

                new Load("menu/level.png"),
                new Load("menu/levelpress.png"),

                new Load(Generate.downfill(blockDimension, (blockDimension / 4) * 3, .106f, .227f, .51f)),
                new Load(Generate.downfill(blockDimension, (blockDimension / 4) * 3, .192f, .314f, .592f)),
                new Load(Generate.downfill(blockDimension, (blockDimension / 4) * 3, .055f, .165f, .42f)),
                new Load(Generate.downfill(blockDimension, (blockDimension / 4) * 3, .035f, .161f, .447f)),

                new Load("nodes/changeright.png"),
                new Load("nodes/changerightlight.png"),
                new Load("nodes/changeleft.png"),
                new Load("nodes/changeleftlight.png"),

                new Load("menu/levelback.png"),
                new Load("menu/levelbutton.png"),
                new Load("menu/levelbuttonpress.png"),
                new Load("menu/levellocked.png"),

                new Load("nodes/teleporta_u.png"),
                new Load("nodes/teleportb_u.png"),

                new Load("nodes/wood.png"),
                new Load("nodes/wood_end_r.png"),
                new Load("nodes/wood_end_l.png"),
                new Load("menu/back.png"),

                new Load("menu/playsmall.png"),
                new Load("menu/playsmallpress.png"),

                new Load("buttons/exit.png"),

                new Load("menu/replay.png"),
                new Load("menu/replaypress.png"),
                new Load(Generate.gradientBackground(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 173.0f / 255.0f, 180.0f / 255.0f, 1.0f, 191.0f / 255.0f, 153.0f / 255.0f, 196.0f / 255.0f)),

                new Load("nodes/upwards.png"),
                new Load("nodes/metal.png"),
                new Load("nodes/metalback.png"),

                new Load("menu/welldone.png"),
                new Load("menu/levelclearedback.png"),

                new Load("menu/skip.png"),
                new Load("menu/skippress.png"),
                new Load("menu/continue.png"),
                new Load("menu/continuepress.png"),

                new Load("levels/tutorial/description_back.png"),
                new Load("levels/tutorial/description_entities.png"),
                new Load("levels/tutorial/description_jump.png"),
                new Load("levels/tutorial/description_menu.png"),

                new Load("menu/slidebar.png"),
                new Load("menu/slidebutton.png"),
                new Load("menu/note.png"),
                new Load("menu/notenot.png"),

                new Load("menu/logo.png"),
                new Load("nodes/upwardslight.png"),

                new Load("menu/logolight.png"),
                new Load("menu/mask.png"),
                new Load("menu/plus.png")

        };

        for(int i = 1; i <= LEVEL_NUMBER; i++)
        {
            assetMan.load("levels/level"+i+".png", Texture.class);
        }

        for(int i = 1; i <= LEVEL_TUT_NUMBER; i++)
        {
            assetMan.load("levels/tutorial/level"+i+".png", Texture.class);
        }

        for (Load aLoad_queue : load_queue) {
            if (aLoad_queue.str != null) {
                assetMan.load(aLoad_queue.str, Texture.class);
            }
        }

        assetMan.load("font.fnt", BitmapFont.class);
        assetMan.load("font_small.fnt", BitmapFont.class);

        GameMusic.load(assetMan);

        while(!assetMan.update()) {
        }

        assetMan.finishLoading();

        font = assetMan.get("font.fnt", BitmapFont.class);
        font.setColor(Color.WHITE);

        font_small = assetMan.get("font_small.fnt", BitmapFont.class);
        font_small.setColor(Color.WHITE);

        tex_registry = new Texture[1 + load_queue.length];

        tex_registry[0] = null;

        for(int i = 0; i < load_queue.length; i++)
        {
            if(load_queue[i].texture != null)
            {
                tex_registry[i+1] = load_queue[i].texture;
                tex_registry[i+1].setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            } else if(load_queue[i].str != null)
            {
                tex_registry[i+1] = assetMan.get(load_queue[i].str, Texture.class);
                tex_registry[i+1].setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }

        }

        for(int i = 1; i <= LEVEL_NUMBER; i++)
        {
            level_registry[i-1] = assetMan.get("levels/level"+i+".png", Texture.class);
        }

        for(int i = 1; i <= LEVEL_TUT_NUMBER; i++)
        {
            level_tut_registry[i-1] = assetMan.get("levels/tutorial/level"+i+".png", Texture.class);
        }

        GameMusic.onLoaded(assetMan);

        shape = new ShapeRenderer();

        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }

    /**
     * Rozpocznij rysowanie sceny.
     */
    void begin()
    {
        if(!batch_ready)
        {
            batch.begin();
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            batch_ready = true;
        }
    }

    /**
     * Zakończ rysowanie sceny.
     */
    void end()
    {
        if(batch_ready)
        {
            batch.end();
            batch_ready = false;

            Gdx.gl.glColorMask(false, false, false, true);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

    /**
     * Pobierz SpriteBatch.
     *
     * @return batch.
     */
    SpriteBatch getBatch()
    {
        return batch;
    }

    /**
     * Narysuj teksturę na ekranie.
     *
     * @param i     Id tesktury.
     * @param x     Pozycja X.
     * @param y     Pozycja Y.
     */
    public void draw(int i, int x, int y)
    {
        if(i>0)
        {
            begin();
            batch.draw(getTexture(i), x, y);
        }
    }

    /**
     * Narysuj z zeskalowaniem.
     *
     * @param i     Id tekstury.
     * @param x     Pozycja X.
     * @param y     Pozycja Y.
     * @param w     Szerokość.
     * @param h     Wysokość.
     */
    void drawScale(int i, int x, int y, int w, int h)
    {
        if(i>0)
        {
            begin();
            batch.draw(new TextureRegion(getTexture(i)), x, y, 0, 0, w, h, 1.0f, 1.0f, 0.0f);
        }
    }

    /**
     * Narysuj pod kątem ze skalą.
     *
     * @param i     Id tekstury.
     * @param x     Pozycja X.
     * @param y     Pozycja Y.
     * @param w     Szerokość.
     * @param h     Wysokość.
     * @param r     Kąt.
     */
    void drawCenterScale(int i, int x, int y, int w, int h, float r)
    {
        if(i>0) {
            begin();
            batch.draw(new TextureRegion(getTexture(i)), x, y, w/2, h/2, w, h, 1.0f, 1.0f, r);
        }
    }

    /**
     * Wyświet tekst.
     *
     * @param s     Tekst do wyświetlenia.
     * @param x     Pozycja X.
     * @param y     Pozycja Y.
     */
    void drawText(String s, int x, int y)
    {
        begin();
        font.draw(batch, s, x, y);
    }

    /**
     * Wyświetl tekst z mniejszą czcionką.
     *
     * @param s     Tekst do wyświetlenia.
     * @param x     Pozycja X.
     * @param y     Pozycja Y.
     */
    void drawTextSmall(String s, int x, int y)
    {
        font_small.draw(batch, s, x, y);
    }

    /**
     * Oblicz rozmiar tekstu.
     *
     * @param s     Tekst.
     * @return  Rozmiar.
     */
    Vector2 getTextSize(String s)
    {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, s);

        return new Vector2(layout.width, layout.height);
    }

    /**
     * Oblicz rozmiar tekstu o mnieszej czcionce.
     *
     * @param s     Tekst.
     * @return  Rozmiar.
     */
    Vector2 getTextSmallSize(String s)
    {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font_small, s);

        return new Vector2(layout.width, layout.height);
    }

    /**
     * Podaj teksturę o id.
     *
     * @param i     Id.
     * @return      Tekstura.
     */
    Texture getTexture(int i)
    {
        return tex_registry[i];
    }

    /**
     * Podaj teksturę określającą poziom.
     *
     * @param i     Id.
     * @return  Tekstura.
     */
    Texture getLevelTexture(int i)
    {
        return level_registry[i];
    }

    /**
     * Podaj teksturę poziomu tutorialowego.
     *
     * @param i     Id tekstury.
     * @return      Tekstura.
     */
    Texture getLevelTutorialTexture(int i)
    {
        return level_tut_registry[i];
    }

    /**
     * Oddaj procesy renderowania.
     */
    void dispose()
    {
        batch.dispose();
        assetMan.dispose();
        font.dispose();
    }


}
