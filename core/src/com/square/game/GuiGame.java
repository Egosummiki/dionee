package com.square.game;

/**
 * Created by Mikolaj on 14.10.2015.
 */
public class GuiGame extends Gui {

    Control game_operations;

    Map gameMap;
    EntityManager entityMan;
    int block_sz;

    public GuiGame(Control ctrl, Map gm, EntityManager em, int bs)
    {
        super();

        game_operations = ctrl;
        game_operations.setGui(this);
        gameMap = gm;
        entityMan = em;
        block_sz = bs;
    }

    @Override
    public void onSet()
    {
        Game.bgr = new BackgroundClouds();
    }

    @Override
    public void onUnset()
    {
        Game.bgr = new BackgroundMenu();
    }

    @Override
    public void draw(Render ren)
    {
        gameMap.render(ren);
        entityMan.draw(ren);

        super.draw(ren);
        game_operations.draw(ren);
    }

    @Override
    public void update(float time)
    {
        super.update(time);
        game_operations.update(time, entityMan, gameMap , block_sz);
    }
}
