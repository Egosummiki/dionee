package com.square.game;

import java.util.Vector;

/**
 * Klasa reprezentująca poziom tutorialowy gry.
 */
public class LevelTutorial extends Level {

    public LevelTutorial(LevelMap gm, Render tr, Control c, int tex, int sb)
    {
        super(gm, tr, c, tex, sb);
    }

    @Override
    public boolean load()
    {
        Vector<InvItem> newitmset = new Vector<InvItem>();

        for(int i = 0; i < items.size(); i++)
        {
            newitmset.add(new InvItem(items.get(i)));
        }

        control.setStartingItems(newitmset);
        control.setMode(ControlMode.NONE);
        control.loadInventoryGui();
        control.startingBlock = startingBlock;
        return gameMap.loadLevel(gameRender.getLevelTutorialTexture(texture));
    }
}
