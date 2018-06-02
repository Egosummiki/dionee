package com.square.game;

/**
 * Created by Mikolaj on 04.11.2015.
 */

import java.util.Vector;

public class TutorialQueue {

    public static class queue_elm{

        public queue_elm()
        {
        }

        public void apply()
        {
        }
    }

    public static class queue_elm_gui extends queue_elm{

        int texture;
        String info;

        public queue_elm_gui(int tex, String text)
        {
            super();
            texture = tex;
            info = text;
        }

        @Override
        public void apply()
        {
            ((GuiTutorial) Gui.tutorial).setText(info);
            ((GuiTutorial) Gui.tutorial).setPicture(texture);
            Gui.setGui(Gui.GUI_TUTORIAL);
        }

    }

    public static class queue_elm_game extends queue_elm {

        int level_num;
        LevelManager lvlMan;

        public queue_elm_game(LevelManager lvl, int level)
        {
            lvlMan = lvl;
            level_num = level;
        }

        @Override
        public void apply()
        {
            lvlMan.loadTutorialLevel(level_num);
            Gui.setGui(Gui.GUI_GAME);
        }

    }

    public static class queue_elm_finish extends queue_elm {
        LevelManager lvlMan;

        public queue_elm_finish(LevelManager lvl)
        {
            lvlMan = lvl;
        }

        @Override
        public void apply()
        {
            TutorialQueue.stop();
            SaveData.setTutorialPassed();
            lvlMan.loadLevel(0);
            Gui.setGui(Gui.GUI_GAME);
        }
    }

    public static int current = -1;
    public static Vector<queue_elm> queue;

    public static void prepare(LevelManager lvlMan)
    {
        current = -1;
        queue = new Vector<queue_elm>();

        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_BACK, "Welcome to the "+ Game.game_name+"!"));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_ENTITIES, Game.game_name+" are those square little creatures."));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_ENTITIES, "Although they're on a tough quest."));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_ENTITIES, "And your job is to help them!"));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_MENU, "In the up left corner you have menu, use it..."));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_MENU, "to choose a tile and place it on the LevelMap."));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_MENU, "Then press a play Button to let the "+ Game.game_name+"..."));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_MENU, "pass the LevelMap."));
        queue.add(new queue_elm_game(lvlMan, 0));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_JUMP, "There are also some special blocks..."));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_JUMP, "Jump block for example..."));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_JUMP, Game.game_name_singular+" jump when it steps on the block."));
        queue.add(new queue_elm_game(lvlMan, 1));
        queue.add(new queue_elm_gui(Render.TEXTURE_TUTORIAL_DES_BACK, "Have a nice play!"));
        queue.add(new queue_elm_finish(lvlMan));
    }

    public static void start()
    {
        current = 0;
        apply_element(0);
    }

    public static void apply_element(int elm)
    {
        queue.get(elm).apply();
    }

    public static void stop()
    {
        current = -1;
    }

    public static boolean isRunning()
    {
        return current >= 0;
    }

    public static void apply_next()
    {
        current++;
        if(current < queue.size()) apply_element(current); else stop();
    }
}
