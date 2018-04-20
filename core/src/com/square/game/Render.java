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
 * Created by Mikolaj on 07.10.2015.
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

    private class load{

        Texture texture;
        String str;

        public load(Texture t)
        {
            texture = t;
            str = null;
        }

        public load(String s)
        {
            str = s;
            texture = null;
        }
    }


    public Render(int block_sz)
    {
        level_registry = new Texture[LEVEL_NUMBER];
        level_tut_registry = new Texture[LEVEL_TUT_NUMBER];
        batch = new SpriteBatch();
        assetMan = new AssetManager();

        load[] load_queue = {
                new load(Generate.squareBorder(block_sz, .7f, .7f, 1.0f, .3f, .3f, .3f)),
                new load(Generate.square(block_sz, .0f, .5f, .0f)),
                new load(Generate.square(block_sz, .0f, .48f, .0f)),
                new load(Generate.square(block_sz, .0f, .46f, .0f)),
                new load(Generate.square(block_sz, .0f, .52f, .0f)),
                new load(Generate.square(block_sz, .48f, .0f, .0f)),
                new load(Generate.square(block_sz, .47f, .0f, .0f)),
                new load(Generate.square(block_sz, .46f, .0f, .0f)),
                new load(Generate.square(block_sz, .42f, .0f, .0f)),
                new load("buttons/destroy.png"),
                new load(Generate.gradientBackground(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 120.0f / 255.0f, 147.0f / 255.0f, 255.0f / 255.0f, 49.0f / 255.0f, 92.0f / 255.0f, 162.0f / 255.0f)),
                new load("buttons/start.png"),

                new load("background/cloud.png"),
                new load("background/cloud2.png"),

                new load("buttons/refresh.png"),

                new load(Generate.square(block_sz, .63f, .4f, .0f)),
                new load(Generate.square(block_sz, .64f, .4f, .0f)),
                new load(Generate.square(block_sz, .64f, .42f, .0f)),
                new load(Generate.square(block_sz, .6f, .392f, .0f)),

                new load("buttons/highlight.png"), // 20

                new load("stairs.png"),

                new load("nodes/jump.png"),

                new load("nodes/turnleft.png"),
                new load("nodes/turnright.png"),

                new load("nodes/teleporta.png"),
                new load("nodes/teleportb.png"),

                new load(Generate.square(block_sz, .26f, .0f, .0f)),
                new load(Generate.square(block_sz, .27f, .0f, .0f)),
                new load(Generate.square(block_sz, .24f, .0f, .0f)),
                new load(Generate.square(block_sz, .25f, .0f, .0f)),

                new load("nodes/jumpuse.png"),

                new load("menu/play.png"),
                new load("menu/playpress.png"),

                new load("menu/background.png"),

                new load("menu/options.png"),
                new load("menu/optionspress.png"),

                new load("menu/level.png"),
                new load("menu/levelpress.png"),

                new load(Generate.downfill(block_sz, (block_sz / 4) * 3, .106f, .227f, .51f)),
                new load(Generate.downfill(block_sz, (block_sz / 4) * 3, .192f, .314f, .592f)),
                new load(Generate.downfill(block_sz, (block_sz / 4) * 3, .055f, .165f, .42f)),
                new load(Generate.downfill(block_sz, (block_sz / 4) * 3, .035f, .161f, .447f)),

                new load("nodes/changeright.png"),
                new load("nodes/changerightlight.png"),
                new load("nodes/changeleft.png"),
                new load("nodes/changeleftlight.png"),

                new load("menu/levelback.png"),
                new load("menu/levelbutton.png"),
                new load("menu/levelbuttonpress.png"),
                new load("menu/levellocked.png"),

                new load("nodes/teleporta_u.png"),
                new load("nodes/teleportb_u.png"),

                new load("nodes/wood.png"),
                new load("nodes/wood_end_r.png"),
                new load("nodes/wood_end_l.png"),
                new load("menu/back.png"),

                new load("menu/playsmall.png"),
                new load("menu/playsmallpress.png"),

                new load("buttons/exit.png"),

                new load("menu/replay.png"),
                new load("menu/replaypress.png"),
                new load(Generate.gradientBackground(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 173.0f / 255.0f, 180.0f / 255.0f, 1.0f, 191.0f / 255.0f, 153.0f / 255.0f, 196.0f / 255.0f)),

                new load("nodes/upwards.png"),
                new load("nodes/metal.png"),
                new load("nodes/metalback.png"),

                new load("menu/welldone.png"),
                new load("menu/levelclearedback.png"),

                new load("menu/skip.png"),
                new load("menu/skippress.png"),
                new load("menu/continue.png"),
                new load("menu/continuepress.png"),

                new load("levels/tutorial/description_back.png"),
                new load("levels/tutorial/description_entities.png"),
                new load("levels/tutorial/description_jump.png"),
                new load("levels/tutorial/description_menu.png"),

                new load("menu/slidebar.png"),
                new load("menu/slidebutton.png"),
                new load("menu/note.png"),
                new load("menu/notenot.png"),

                new load("menu/logo.png"),
                new load("nodes/upwardslight.png"),

                new load("menu/logolight.png"),
                new load("menu/mask.png"),
                new load("menu/plus.png")

        };

        for(int i = 1; i <= LEVEL_NUMBER; i++)
        {
            assetMan.load("levels/level"+i+".png", Texture.class);
        }

        for(int i = 1; i <= LEVEL_TUT_NUMBER; i++)
        {
            assetMan.load("levels/tutorial/level"+i+".png", Texture.class);
        }

        for(int i = 0 ; i < load_queue.length; i++)
        {
            if(load_queue[i].str != null)
            {
                assetMan.load(load_queue[i].str, Texture.class);
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

    public void begin()
    {
        if(!batch_ready)
        {
            batch.begin();
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            batch_ready = true;
        }
    }

    public void end()
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

    public SpriteBatch getBatch()
    {
        return batch;
    }

    public void draw(int i, int x, int y)
    {
        if(i>0)
        {
            begin();
            batch.draw(getTexture(i), x, y);
        }
    }

    public void drawScale(int i, int x, int y, int w, int h)
    {
        if(i>0)
        {
            begin();
            batch.draw(new TextureRegion(getTexture(i)), x, y, 0, 0, w, h, 1.0f, 1.0f, 0.0f);
        }
    }

    public void drawCenter(int i, int x, int y, float r)
    {
        if(i>0) {
            begin();
            float d_w = getTexture(i).getWidth();
            float d_h = getTexture(i).getHeight();
            batch.draw(new TextureRegion(getTexture(i)), x, y, d_w/2, d_h/2, d_w, d_h, 1.0f, 1.0f, r);
        }
    }

    public void drawCenterScale(int i, int x, int y, int w, int h, float r)
    {
        if(i>0) {
            begin();
            batch.draw(new TextureRegion(getTexture(i)), x, y, w/2, h/2, w, h, 1.0f, 1.0f, r);
        }
    }

    public void drawText(String s, int x, int y)
    {
        begin();
        font.draw(batch, s, x, y);
    }

    public void drawTextSmall(String s, int x, int y)
    {
        font_small.draw(batch, s, x, y);
    }

    public Vector2 getTextSize(String s)
    {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, s);

        return new Vector2(layout.width, layout.height);
    }

    public void drawSquare(int color, int x, int y, int w, int h)
    {
        end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color(color));
        shape.rect(x, y, w, h);
        shape.end();
    }

    public Vector2 getTextSmallSize(String s)
    {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font_small, s);

        return new Vector2(layout.width, layout.height);
    }

    public Texture getTexture(int i)
    {
        return tex_registry[i];
    }
    public Texture getLevelTexture(int i)
    {
        return level_registry[i];
    }

    public Texture getLevelTutorialTexture(int i)
    {
        return level_tut_registry[i];
    }

    public void dispose()
    {
        batch.dispose();
        assetMan.dispose();
        font.dispose();
    }


}
