package com.square.game;

/**
 * Created by Mikolaj on 08.11.2015.
 */

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class GameMusic
{

    public enum song {
        main_theme, satan
    }

    public static Music gameTheme;
    public static Music egg_satan;

    private static song current = song.main_theme;

    public static Music getCurrentSong()
    {
        switch (current)
        {
            case main_theme: return gameTheme;
            case satan: return egg_satan;
        }

        return null;
    }

    public static void setCurrentSong(song s)
    {
        getCurrentSong().stop();
        getCurrentSong().setPosition(0);
        current = s;
        playCurrentSong();
    }

    public static void playCurrentSong()
    {
        getCurrentSong().play();
        setMusicLoudness(SaveData.getMusicVol());
    }

    public static void load(AssetManager assetMan)
    {
        assetMan.load("music/theme.mp3", Music.class);
        assetMan.load("music/satan.mp3", Music.class);
    }

    public static void onLoaded(AssetManager assetMan)
    {
        gameTheme = assetMan.get("music/theme.mp3", Music.class);
        gameTheme.setLooping(true);
        egg_satan = assetMan.get("music/satan.mp3", Music.class);
        egg_satan.setLooping(false);

    }

    public static void onMenu()
    {
        playCurrentSong();
    }

    public static void update()
    {
        if(!getCurrentSong().isPlaying())
        {
            if(current == song.satan)
            {
                setCurrentSong(song.main_theme);
            }
        }
    }

    public static void setMusicLoudness(float vol)
    {
        getCurrentSong().setVolume(vol);
    }

    public static void onGame()
    {

    }
}
