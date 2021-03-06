package com.square.game;

import java.util.Vector;

/**
 * Klasa jest odpowiedzialna za sprawowanie kontroli nad poziomami gry.
 */
public class LevelManager {

    private Vector<Level> Levels;
    private Vector<LevelTutorial> tutorial_levels;
    private LevelMap gameMap;
    private Render textureRender;
    private Control ctrl;
    private int current_lvl = -1;
    private boolean tutorial = false;
    private EntityManager entityMan;

    /**
     * Konstruktor klasy LevelManager.
     *
     * @param gm    Mapa gry.
     * @param ren   Obiekt klasy Render.
     * @param em    Menadżer jednostek.
     * @param cl    Obiekt klasy Control.
     */
    LevelManager(LevelMap gm, Render ren, EntityManager em, Control cl)
    {
        Levels = new Vector<Level>();
        tutorial_levels = new Vector<LevelTutorial>();
        gameMap = gm;
        textureRender = ren;
        ctrl = cl;
        entityMan = em;

       /*-- Rejestrowanie kolejnych poziomów --*/

        // Poziom zero
        createLevel(0, 11)
                .addStartingItem(NodeManager.NODE_JUMP, 1)
                .addStartingItem(NodeManager.NODE_TURN_RIGHT, 1);

        // Poziom jeden
        createLevel(1, 13)
                .addStartingItem(NodeManager.NODE_TURN_LEFT, 1)
                .addStartingItem(NodeManager.NODE_JUMP, 1);

        // Poziom dwa
        createLevel(2, 14)
                .addStartingItem(NodeManager.NODE_TURN_LEFT, 1)
                .addStartingItem(NodeManager.NODE_ALTERNATE, 2);

        // Poziom trzy
        createLevel(3, 11)
                .addStartingItem(NodeManager.NODE_UPWARDS, 3);

        // Poziom cztery
        createLevel(4, 13);


        /*-- Poziomy samouczkowe --*/

        // Poziom zero
        createTutorialLevel(0, 8)
                .addStartingItem(NodeManager.NODE_PLANK, 3);

        // Poziom jeden
        createTutorialLevel(1, 7)
                .addStartingItem(NodeManager.NODE_JUMP, 1);
    }

    /**
    * Metoda zwraca aktualny poziom
    * */
    public int getCurrentLevel()
    {
        return current_lvl;
    }

     /**
     * Metoda zwraca liczbę dostępnych poziomów
     * */
    public int getNumLevels()
    {
        return Levels.size();
    }

    /**
    * Metoda dodaje poziom.
    * */
    public Level createLevel(int lvl_tex, int starting_block)
    {
        Level lvl = new Level(gameMap, textureRender, ctrl, lvl_tex, starting_block);
        Levels.add(lvl);
        return lvl;
    }

    /**
    * Metoda ustawia tryb edytowania.
    * */
    public void setEditMode(boolean e)
    {
        ctrl.editMode = e;
    }

    /**
    * Metoda dodaje poziom samouczkowy.
    * */
    public Level createTutorialLevel(int lvl_tex, int starting_block)
    {
        LevelTutorial lvl = new LevelTutorial(gameMap, textureRender, ctrl, lvl_tex, starting_block);
        tutorial_levels.add(lvl);
        return lvl;
    }

    /**
    * Metoda ładuje wybrany poziom
    * */
    public boolean loadLevel(int i)
    {
		if(i <= SaveData.getMaxLevel())
		{
            tutorial = false;
			current_lvl = i;
            entityMan.resetReleasedEntities();
			return Levels.get(i).load();
		}

		return false;
    }

    /**
    * Metoda ładuje poziom samouczkowy.
    * */
    public boolean loadTutorialLevel(int i)
    {
        if(i <= SaveData.getMaxLevel())
        {
            tutorial = true;
            current_lvl = i;
            entityMan.resetReleasedEntities();
            return tutorial_levels.get(i).load();
        }

        return false;
    }

    /**
    * Metoda ładuje następny poziom.
    * */
    public boolean loadNextLevel()
    {
        if(current_lvl+1 <= SaveData.getMaxLevel())
        {
            current_lvl++;
            entityMan.resetReleasedEntities();
            return Levels.get(current_lvl).load();
        }

        return false;
    }

    /* Czy jest to poziom samouczkowy? */
    public boolean isTutorial()
    {
        return tutorial;
    }

    /**
    * Załaduj poziom ponownie.
    * */
    public boolean reload()
    {
        entityMan.resetReleasedEntities();
        return Levels.get(current_lvl).load();
    }
}
