package com.square.game;

import com.badlogic.gdx.Gdx;

import java.util.Vector;

/**
 * Klasa odpowiada za kontrolowanie jednostek.
 * */
public class EntityManager {

    private Vector<Entity> entityList;

    private int entitiesFinished;
    private int releasedEntities = 0;
    private final static int numberOfEntities = 5;

    /**
     * Konstruktor klasy EntityManager.
     */
    EntityManager()
    {
        entityList = new Vector<Entity>();
    }

    /**
    * Powołuje jednostkę do życia
    *
    * @param entity Jednostka, która ma być powołana.
    * */
    void spawn(Entity entity)
    {
        releasedEntities++;
        entityList.add(entity);
    }

    /**
    * Metoda podaje liczbę wypuszczonych istot.
    *
    * @return Liczba wypuszcznoych jednostek.
    * */
    int getReleasedEntities()
    {
        return releasedEntities;
    }

    /**
    * Ustaw liczbę wypuszczonych jednostek na zero.
    * */
    void resetReleasedEntities()
    {
        releasedEntities = 0;
    }

    /**
    * Zabij wszystkie jednostki.
    * */
    void killAll()
    {
        entityList.clear();
    }

    /**
    * Narysuj jednostki
    * @param ren Klasa render.
    * */
    public void draw(Render ren)
    {
        for(int i = 0; i < entityList.size(); i++)
        {
            entityList.get(i).draw(ren);
        }
    }

    /**
    * @return Czy wszystkie jednostki przeszły.
    * */
    boolean finished()
    {
        return entitiesFinished == numberOfEntities;
    }


    /**
    * Metoda update wywołuje update u wszystkich jednostek.
    * @param gameMap Mapa poziomu.
    * */
    public void update(LevelMap gameMap)
    {
        entitiesFinished = 0;
        for(int i = 0; i < entityList.size(); i++)
        {
            if(!entityList.get(i).murder)
            {
                entityList.get(i).update(gameMap);

                if(entityList.get(i).getPositionX() > Gdx.graphics.getWidth() && entityList.get(i).getPositionY() > 0)
                {
                    entitiesFinished++;
                }
            } else
            {
                entityList.remove(i);
                i--;
            }
        }
    }
}
