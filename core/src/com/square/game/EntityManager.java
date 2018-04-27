package com.square.game;

import com.badlogic.gdx.Gdx;

import java.util.Vector;

/**
 * Created by Mikolaj on 09.10.2015.
 */
public class EntityManager {

    Vector<Entity> entity_list;

    int entity_finished;
    private int realsed_entities = 0;
    public final static int num_entites = 5;

    public EntityManager()
    {
        entity_list = new Vector();
    }

    public void spawn(Entity entity)
    {
        realsed_entities++;
        entity.setSpawned(entity_list.size(), this);
        entity_list.add(entity);
    }

    public int getRealsedEntities()
    {
        return realsed_entities;
    }

    public void resetRealsedEntites()
    {
        realsed_entities = 0;
    }

    public void killAll()
    {
        entity_list.clear();
    }

    public int getNumEntities()
    {
        return entity_list.size();
    }

    public void draw(Render ren)
    {
        for(int i = 0; i < entity_list.size(); i++)
        {
            entity_list.get(i).draw(ren);
        }
    }

    public boolean finished()
    {
        return entity_finished == num_entites;
    }


    public void update(Map gameMap)
    {
        entity_finished = 0;
        for(int i = 0; i < entity_list.size(); i++)
        {
            if(!entity_list.get(i).murder)
            {
                entity_list.get(i).update(gameMap);

                if(entity_list.get(i).getPositionX() > Gdx.graphics.getWidth() && entity_list.get(i).getPositionY() > 0)
                {
                    entity_finished++;
                }
            } else
            {
                entity_list.remove(i);
                i--;
            }
        }
    }
}
