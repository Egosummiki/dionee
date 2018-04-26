package com.square.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import java.util.Random;
import com.badlogic.gdx.utils.Logger;

public class Game extends ApplicationAdapter {

	NodeManager nodeMan;
	Map gameMap;
	Random rand;
	Control ctrl;
	public static Background bgr;
	public static final String game_name = "Dionees";
	public static final String game_name_singular = "Dionee";

	LevelManager levelMan;

	//Gui current_gui;

	Logger logger;

	Render textureRender;

	EntityManager entityMan;

	long timer = 0;
	float n_fps = 0.06f;
	float l_fps = 0.06f;

	public static int block_sz = 0;


	
	@Override
	public void create () {

		logger = new Logger("GAME LOG", Logger.DEBUG);

		block_sz = Gdx.graphics.getWidth() / 30;

		if(Gdx.graphics.getWidth() % 30 > 0) block_sz++;

		rand = new Random();
		entityMan = new EntityManager();
		textureRender = new Render(block_sz);
		nodeMan = new NodeManager();
		bgr = new BackgroundMenu();
		ctrl = new Control(textureRender, nodeMan);

		gameMap = new Map(30, 17, nodeMan, block_sz); // 30x17 blocks

		levelMan = new LevelManager(gameMap, textureRender, entityMan, ctrl);

		//current_gui = new GuiGame(ctrl, gameMap, entityMan, blockDimension);
		Gui.menu = new GuiMenu(textureRender, levelMan);
		Gui.game = new GuiGame(ctrl, gameMap, entityMan, block_sz);
		Gui.levels = new GuiLevels(textureRender, levelMan);
		Gui.level_cleared = new GuiLevelFinish(textureRender, levelMan);
		Gui.tutorial = new GuiTutorial(levelMan);
		Gui.settings = new GuiSettings();

		TutorialQueue.prepare(levelMan);

		Gui.setGui(Gui.GUI_MENU);
	}


	public void update(float time)
	{
		entityMan.update(time, gameMap);
		Gui.current_gui.update(time);
		bgr.update(time);

		GameMusic.update();

		if(entityMan.finished())
		{
			entityMan.killAll();
			if(levelMan.getCurrentLevel()+1 < levelMan.getNumLevels())
			{
				if(levelMan.getCurrentLevel()+1 > SaveData.getMaxLevel())
				{
					SaveData.unlockLevelUpTo(levelMan.getCurrentLevel()+1);
				}

				if(TutorialQueue.isRunning())
				{
					TutorialQueue.apply_next();
				} else
				{
					Gui.setGui(Gui.GUI_LEVEL_CLEARED);
				}
				//levelMan.loadLevel(levelMan.getCurrentLevel()+1);
			}

		}
	}

	int done_frames = 0;
	long start_time = 0;

	@Override
	public void render () {

		//Simple time counter

		if(start_time == 0)
		{
			start_time = System.currentTimeMillis();
		}

		int should_frames = (int)Math.floor((System.currentTimeMillis() - start_time) * 0.06f);

		while(should_frames > done_frames)
		{
			done_frames++;
			update(1);
		}


		//Rendering the scene

		Gdx.gl.glColorMask(true, true, true, true);
		Gdx.gl.glClearColor(.0f, .0f, .0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		textureRender.begin();
		bgr.draw(textureRender);

		Gui.current_gui.draw(textureRender);

		textureRender.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		textureRender.dispose();
	}
}
