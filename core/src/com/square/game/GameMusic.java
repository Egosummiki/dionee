package com.square.game;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/**
 * Klasa odpowiada za tło muzyczne gry. Klasa zawiera zmienne i metody statyczne.
 */
public class GameMusic
{

    /**
     * Enumeracja dostępnych teł muzycznych gry.
     */
    public enum Song {
        main_theme, easter_egg
    }

    private static Music gameTheme;
    private static Music easterEggSong;

    private static Song current = Song.main_theme;

    /**
     * Zwróć aktualne tło muzyczne.
     *
     * @return Tło muzyczne.
     */
    private static Music getCurrentSong()
    {
        switch (current)
        {
            case main_theme: return gameTheme;
            case easter_egg: return easterEggSong;
        }

        return null;
    }

    /**
     * Ustaw tło muzyczne.
     *
     * @param s     Tło muzyczne.
     */
    static void setCurrentSong(Song s)
    {
        assert getCurrentSong() != null;
        getCurrentSong().stop();
        getCurrentSong().setPosition(0);
        current = s;
        playCurrentSong();
    }

    /**
     *  Puść aktualne tło muzyczne.
     */
    private static void playCurrentSong()
    {
        assert getCurrentSong() != null;
        getCurrentSong().play();
        setMusicLoudness(SaveData.getMusicVol());
    }

    /**
     * Metoda ładująca zasoby tła.
     *
     * @param assetMan  Menadżer zasobów.
     */
    static void load(AssetManager assetMan)
    {
        assetMan.load("music/theme.mp3", Music.class);
        assetMan.load("music/easter_egg.mp3", Music.class);
    }

    /**
     * Kiedy zasoby zostaną załadowane.
     *
     * @param assetMan Menadżer zasobów.
     */
    static void onLoaded(AssetManager assetMan)
    {
        gameTheme = assetMan.get("music/theme.mp3", Music.class);
        gameTheme.setLooping(true);
        easterEggSong = assetMan.get("music/easter_egg.mp3", Music.class);
        easterEggSong.setLooping(false);

    }

    /**
     * Kiedy zostanie załadowane menu gry.
     */
    static void onMenu()
    {
        playCurrentSong();
    }

    /**
     * Metoda wywoływana co cylk logiki gry.
     */
    public static void update()
    {
        assert getCurrentSong() != null;
        if(!getCurrentSong().isPlaying())
        {
            if(current == Song.easter_egg)
            {
                setCurrentSong(Song.main_theme);
            }
        }
    }

    /**
     * Ustaw poziom głośności muzyki.
     *
     * @param vol   Głośność.
     */
    static void setMusicLoudness(float vol)
    {
        getCurrentSong().setVolume(vol);
    }

}
