package com.square.game;


import java.util.Vector;

/**
 * Klasa stanowiąca "kolejkę" informacji samouczkowej dla gracza.
 */
public class TutorialQueue {

    /**
     * Interfejs stanowi pojedynczy element kolejki samouczkowej.
     */
    public interface QueueElement {
        void apply();
    }

    /**
     * Klasa wewnętrzna stanowiąca element kolejki będący graficznym interfejsem użytkownika.
     */
    public static class QueueElementGui implements QueueElement {

        int texture;
        String info;

        QueueElementGui(int tex, String text)
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

    /**
     * Klasa wewnętrzna stanowiąca rozgrywkę samouczkową.
     */
    public static class QueueElementGame implements QueueElement {

        int level_num;
        LevelManager lvlMan;

        QueueElementGame(LevelManager lvl, int level)
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

    /**
     * Klasa wewnętrzna stanowiąca zakończenie samouczka.
     */
    public static class QueueElementFinish implements QueueElement {
        LevelManager lvlMan;

        QueueElementFinish(LevelManager lvl)
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
    public static Vector<QueueElement> queue;

    public static void prepare(LevelManager lvlMan)
    {
        current = -1;
        queue = new Vector<QueueElement>();

        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_BACK, "Welcome to the "+ Game.gameName +"!"));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_ENTITIES, Game.gameName +" are those square little creatures."));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_ENTITIES, "Although they're on a tough quest."));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_ENTITIES, "And your job is to help them!"));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_MENU, "In the up left corner you have menu, use it..."));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_MENU, "to choose a tile and place it on the LevelMap."));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_MENU, "Then press a play Button to let the "+ Game.gameName +"..."));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_MENU, "pass the LevelMap."));
        queue.add(new QueueElementGame(lvlMan, 0));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_JUMP, "There are also some special blocks..."));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_JUMP, "Jump block for example..."));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_JUMP, Game.gameNameSingular +" jump when it steps on the block."));
        queue.add(new QueueElementGame(lvlMan, 1));
        queue.add(new QueueElementGui(Render.TEXTURE_TUTORIAL_DES_BACK, "Have a nice play!"));
        queue.add(new QueueElementFinish(lvlMan));
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
