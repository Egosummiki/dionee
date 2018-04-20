package com.square.game;

import java.util.Vector;

/**
 * Created by Mikolaj on 14.10.2015.
 */
public class Level {

    Map gameMap;
    Render texturerRen;
    Control ctrl;

    int texture;
    int starting_block;

    protected Vector<InvItem> items;


    public Level(Map gm, Render tr, Control c, int tex, int sb)
    {
        items = new Vector<InvItem>();
        gameMap = gm;
        texturerRen = tr;
        texture = tex;
        starting_block = sb;
        ctrl = c;
    }

    public Level addStartingItem(int type, int amount)
    {
        items.add(new InvItem(type, amount));
        return this;
    }

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
        return gameMap.loadLevel(texturerRen.getLevelTexture(texture));
    }

}
