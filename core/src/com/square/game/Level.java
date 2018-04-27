package com.square.game;

import java.util.Vector;

/**
 * Created by Mikolaj on 14.10.2015.
 */
public class Level {

    Map gameMap;
    Render gameRender;
    Control control;

    int texture;
    int startingBlock;

    protected Vector<InvItem> items;


    public Level(Map gm, Render tr, Control c, int tex, int sb)
    {
        items = new Vector<InvItem>();
        gameMap = gm;
        gameRender = tr;
        texture = tex;
        startingBlock = sb;
        control = c;
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

        control.setStartingItems(newitmset);
        control.setMode(ControlMode.NONE);
        control.loadInvGui();
        control.startingBlock = startingBlock;
        return gameMap.loadLevel(gameRender.getLevelTexture(texture));
    }

}
