package com.square.game;

import java.util.Vector;

/**
 * Created by Mikolaj on 04.11.2015.
 */
public class LevelTutorial extends Level {

    public LevelTutorial(Map gm, Render tr, Control c, int tex, int sb)
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

        ctrl.setStartingItems(newitmset);
        ctrl.setMode(ControlMode.NONE);
        ctrl.loadInvGui();
        ctrl.starting_bl = starting_block;
        return gameMap.loadLevel(texturerRen.getLevelTutorialTexture(texture));
    }
}
