package com.square.game;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Gdx;

/**
 * Klasa zawiera statyczne metody do zapisu i odczytu danych.
 */
public class SaveData {

    private static Preferences prefs;
    private static final String prefs_name = "square_prefs";

    private static void loadPreferences()
    {
        if(prefs == null)
        {
            prefs = Gdx.app.getPreferences(prefs_name);
        }
    }

    private static void savePreferences()
    {
        if(prefs != null)
        {
            prefs.flush();
            prefs = null;
        }
    }

    static void setMusicVol(float s)
    {
        loadPreferences();
        prefs.putFloat("music_vol", s);
        savePreferences();
    }

    static float getMusicVol()
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

    static void unlockLevelUpTo(int l)
    {
        loadPreferences();
        prefs.putInteger("max_level", l);
        savePreferences();
    }

    static void setTutorialPassed()
    {
        loadPreferences();
        prefs.putBoolean("tutorial_passed", true);
        savePreferences();
    }

    static Boolean tutorialPassed()
    {
        loadPreferences();
        return prefs.getBoolean("tutorial_passed", false);
    }

    static int getMaxLevel()
    {
        loadPreferences();
        return prefs.getInteger("max_level");
    }
}
