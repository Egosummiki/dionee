package com.square.game;

/**
 * Created by Mikolaj on 14.10.2015.
 */
public class GuiGame extends Gui {

    private Control control;
    private LevelMap gameMap;
    private EntityManager entityMan;
    private int blockDimension;

    public GuiGame(Control control, LevelMap gameMap, EntityManager entityMan, int blockDimension)
    {
        super();

        this.control = control;
        this.control.setGui(this);
        this.gameMap = gameMap;
        this.entityMan = entityMan;
        this.blockDimension = blockDimension;
    }

    @Override
    public void onSet()
    {
        Game.background = new BackgroundClouds();
    }

    @Override
    public void onUnset()
    {
        Game.background = new BackgroundMenu();
    }

    @Override
    public void draw(Render ren)
    {
        gameMap.render(ren);
        entityMan.draw(ren);

        super.draw(ren);
        control.draw(ren);
    }

    @Override
    public void update(float time)
    {
        super.update(time);
        control.update(time, entityMan, gameMap , blockDimension);
    }
}
