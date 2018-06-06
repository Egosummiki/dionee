package com.square.game;

import java.util.Vector;

/**
 * Klasa obsługuje konkretny poziom gry.
 */
public class Level {

    LevelMap gameMap;
    Render gameRender;
    Control control;

    int texture;
    int startingBlock;

    Vector<InvItem> items;


    /**
     * Konstruktor klasy level.
     *
     * @param gameMap           Mapa poziomu gry.
     * @param render            Obiekt klasy render.
     * @param control           Obiekt klasy control.
     * @param texture           Tesktura poziomu.
     * @param startingBlock     Wysokośc bloku startowego.
     */
    Level(LevelMap gameMap, Render render, Control control, int texture, int startingBlock)
    {
        items = new Vector<InvItem>();
        this.gameMap = gameMap;
        gameRender = render;
        this.texture = texture;
        this.startingBlock = startingBlock;
        this.control = control;
    }

   /*
   * Funkcja addStartingItem pozwala na dodanie przedmiotu startowego podczas definiowania poziomu.
   * */
    public Level addStartingItem(int type, int amount)
    {
        items.add(new InvItem(type, amount));
        return this;
    }

    /*
    * Funkcja Load jest odpowiedzialna za ładowanie poziomu
    * */
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
        return gameMap.loadLevel(gameRender.getLevelTexture(texture));
    }

}
