package com.square.game;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Gdx;

/**
 * Created by Mikolaj on 14.10.2015.
 */
public class SaveData {

    private static Preferences prefs;
    private static final String prefs_name = "square_prefs";

    public static void loadPreferences()
    {
        if(prefs == null)
        {
            prefs = Gdx.app.getPreferences(prefs_name);
        }
    }

    public static void savePreferences()
    {
        if(prefs != null)
        {
            prefs.flush();
            prefs = null;
        }
    }

    public static void setMusicVol(float s)
    {
        loadPreferences();
        prefs.putFloat("music_vol", s);
        savePreferences();
    }

    public static float getMusicVol()
    {
        loadPreferences();
        return prefs.getFloat("music_vol", .5f);
    }

    public static void unlockNextLevel()
    {
        loadPreferences();
        prefs.putInteger("max_level", getMaxLevel() + 1);
        savePreferences();
    }

    public static void unlockLevelUpTo(int l)
    {
        loadPreferences();
        prefs.putInteger("max_level", l);
        savePreferences();
    }

    public static void setTutorialPassed()
    {
        loadPreferences();
        prefs.putBoolean("tutorial_passed", true);
        savePreferences();
    }

    public static Boolean tutorialPassed()
    {
        loadPreferences();
        return prefs.getBoolean("tutorial_passed", false);
    }

    public static int getMaxLevel()
    {
        loadPreferences();
        return prefs.getInteger("max_level");
    }
}
