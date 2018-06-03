package com.square.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Klasa klasa gry.
 */
public class Game extends ApplicationAdapter {

    public static Background background;
	static final String gameName = "Dionees";
	static final String gameNameSingular = "Dionee";

    LevelMap gameMap;
	private LevelManager levelMan;
    private Render textureRender;
	private EntityManager entityMan;

	static int blockDimension = 0;

    /**
     * Metoda wywoływana podczas tworzenia gry.
     */
	@Override
	public void create () {

        blockDimension = Gdx.graphics.getWidth() / 30;

		if(Gdx.graphics.getWidth() % 30 > 0) blockDimension++;

        entityMan = new EntityManager();
		textureRender = new Render(blockDimension);
        NodeManager nodeMan = new NodeManager();
		background = new BackgroundMenu();
        Control ctrl = new Control(textureRender, nodeMan);

		gameMap = new LevelMap(30, 17, nodeMan, blockDimension); // 30x17 blocks

		levelMan = new LevelManager(gameMap, textureRender, entityMan, ctrl);

		Gui.menu = new GuiMenu(textureRender, levelMan);
		Gui.game = new GuiGame(ctrl, gameMap, entityMan, blockDimension);
		Gui.levels = new GuiLevels(textureRender, levelMan);
		Gui.level_cleared = new GuiLevelFinish(textureRender, levelMan);
		Gui.tutorial = new GuiTutorial(levelMan);
		Gui.settings = new GuiSettings();

		TutorialQueue.prepare(levelMan);

		Gui.setGui(Gui.GUI_MENU);
	}


    /**
     * Metoda wywoływana co cylk logiki gry.
     *
     * @param time Atrybut nie jest, już więcej potrzebny.
     */
	public void update(float time)
	{
		entityMan.update(gameMap);
		Gui.current_gui.update(time);
		background.update(time);

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

	private int doneFrames = 0;
	private long startTime = 0;

    /**
     * Metoda wywoływana co cylk rysowania gry.
     */
	@Override
	public void render () {

		// Realizacja FIXED TIME STEP.

		if(startTime == 0)
		{
			startTime = System.currentTimeMillis();
		}

		int shouldFrames = (int)Math.floor((System.currentTimeMillis() - startTime) * 0.06f); // Liczba cylki do zrealizowania.

		while(shouldFrames > doneFrames) // Tak długo jak, nie została wykonania właściwa liczba cylki.
		{
			doneFrames++;
			update(1); // Time jest 1, bo już nie jest używany.
		}


		// Rysowanie sceny.

		Gdx.gl.glColorMask(true, true, true, true);
		Gdx.gl.glClearColor(.0f, .0f, .0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		textureRender.begin();
		background.draw(textureRender);

		Gui.current_gui.draw(textureRender);

		textureRender.end();
	}

    /**
     * Po skończeniu gry.
     */
	@Override
	public void dispose() {
		super.dispose();
		textureRender.dispose();
	}
}
