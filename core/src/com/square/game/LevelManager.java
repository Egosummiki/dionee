package com.square.game;

import java.util.Vector;

/**
 * Created by Mikolaj on 18.10.2015.
 */

public class LevelManager {

    private Vector<Level> Levels;
    private Vector<LevelTutorial> tutorial_levels;
    private Map gameMap;
    private Render textureRender;
    private Control ctrl;
    private int current_lvl = -1;
    private boolean tutorial = false;
    private EntityManager entityMan;

    public LevelManager(Map gm, Render ren, EntityManager em, Control cl)
    {
        Levels = new Vector<Level>();
        tutorial_levels = new Vector<LevelTutorial>();
        gameMap = gm;
        textureRender = ren;
        ctrl = cl;
        entityMan = em;

        createLevel(0, 9).addStartingItem(NodeManager.NODE_JUMP, 1).addStartingItem(NodeManager.NODE_TURN_LEFT, 1).addStartingItem(NodeManager.NODE_TURN_RIGHT, 1);
        createLevel(1, 13).addStartingItem(NodeManager.NODE_TURN_LEFT, 1).addStartingItem(NodeManager.NODE_JUMP, 1);
        createLevel(2, 14).addStartingItem(NodeManager.NODE_TURN_LEFT, 1).addStartingItem(NodeManager.NODE_ALTERNATE, 2);
        createLevel(3, 11).addStartingItem(NodeManager.NODE_UPWARDS, 3);
        createLevel(4, 13);

        createTutorialLevel(0, 8).addStartingItem(NodeManager.NODE_PLANK, 3);
        createTutorialLevel(1, 7).addStartingItem(NodeManager.NODE_JUMP, 1);
    }

    public int getCurrentLevel()
    {
        return current_lvl;
    }

    public int getNumLevels()
    {
        return Levels.size();
    }

    public Level createLevel(int lvl_tex, int starting_block)
    {
        Level lvl = new Level(gameMap, textureRender, ctrl, lvl_tex, starting_block);
        Levels.add(lvl);
        return lvl;
    }

    public void setEditMode(boolean e)
    {
        ctrl.edit_mode = e;
    }

    public Level createTutorialLevel(int lvl_tex, int starting_block)
    {
        LevelTutorial lvl = new LevelTutorial(gameMap, textureRender, ctrl, lvl_tex, starting_block);
        tutorial_levels.add(lvl);
        return lvl;
    }

    public boolean loadLevel(int i)
    {
		if(i <= SaveData.getMaxLevel())
		{
            tutorial = false;
			current_lvl = i;
            entityMan.resetRealsedEntites();
			return Levels.get(i).load();
		}

		return false;
    }

    public boolean loadTutorialLevel(int i)
    {
        if(i <= SaveData.getMaxLevel())
        {
            tutorial = true;
            current_lvl = i;
            entityMan.resetRealsedEntites();
            return tutorial_levels.get(i).load();
        }

        return false;
    }

    public boolean loadNextLevel()
    {
        if(current_lvl+1 <= SaveData.getMaxLevel())
        {
            current_lvl++;
            entityMan.resetRealsedEntites();
            return Levels.get(current_lvl).load();
        }

        return false;
    }

    public boolean isTutorial()
    {
        return tutorial;
    }

    public boolean reload()
    {
        entityMan.resetRealsedEntites();
        return Levels.get(current_lvl).load();
    }
}
