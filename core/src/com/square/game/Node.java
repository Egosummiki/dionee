package com.square.game;

import java.util.Vector;

/**
 * Klasa reprezentująca blok z których składa się mapa gry.
 */
public class Node {

    /**
     * Enumeracja rodzajów struktur.
     */
    public enum Structure
    {
        blank, solid, custom
    }

    private int id;
    protected int texture;
    private String name;
    private int color;
    private boolean solid;
    private Structure structure;
    private boolean removable;


    /**
     * Konstruktor klasy Node.
     *
     * @param type_id       Id bloku.
     * @param t             Id tekstury.
     * @param node_name     Nazwa bloku.
     * @param _color        Kolor bloku odczytywany z bitmapy.
     * @param _solid        Czy blok jest stały?
     * @param _removable    Czy blok można usunąć?
     */
    public Node(int type_id, int t, String node_name, int _color, boolean _solid, boolean _removable)
    {
        id = type_id;
        texture = t;
        name = node_name;
        color = _color;
        solid = _solid;

        if(solid)
        {
            structure = Structure.solid;
        } else {
            structure = Structure.blank;
        }

        removable = _removable;
    }

    /**
     * Metoda jest wywoływana kiedy blok został postawiony.
     *
     * @param gameMap   Mapa gry.
     * @param x         Pozycja x.
     * @param y         Pozycja y.
     * @return          Brak zastosowania.
     */
    public boolean onSet(LevelMap gameMap, int x, int y)
    {
        return false;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Zwróć rodzaj struktury.
     *
     * @return  Rodzaj struktury.
     */
    Structure getStructure()
    {
        return structure;
    }

    /**
     * Funkcja do ponownego napisania.
     *
     * @param gameMap
     * @param hitMap
     * @param x
     * @param y
     */
    public void applyCustomHitMap(LevelMap gameMap, Vector<HitLine> hitMap, int x, int y)
    {
    }

    /**
     * Czy blok jest stały.
     *
     * @param x     Kiedyś x,y wskazywało na część bloku.
     * @param y     Aktualnie parametry x,y są niepotrzebne.
     * @return      Stałość bloku.
     */
    public boolean isSolid(float x, float y) { return solid; }

    /**
     * Czy blok jest usuwalny?
     *
     * @return  Usuwalność bloku.
     */
    boolean isRemovable() { return removable; }

    /**
     * Podaj id tekstury bloku.
     *
     * @return  Id tekstury bloku.
     */
    public int getTexture() {return texture;}

    /**
     * Podaj kolor bloku.
     *
     * @return Kolor.
     */
    int getColor()
    {
        return color;
    }

    /**
     * Funkcja wywoływana co cylk rysowania gry.
     *
     * @param ren   Obiekt klasy render.
     * @param x     Pozycja x bloku.
     * @param y     Pozycja y bloku.
     * @param data  Metainformacja bloku.
     */
    public void render(Render ren, float x, float y, byte data)
    {
        ren.drawScale(texture, (int)x, (int)y, Game.blockDimension, Game.blockDimension);
    }

    /**
     * Zdarzenie, kiedy blok jest dotknięty przez jednostkę.
     *
     * @param gameMap   Mapa gry.
     * @param e         Jednostka.
     * @param x         Pozycja X.
     * @param y         Pozycja Y.
     */
    public void onEntityTouch(LevelMap gameMap, Entity e, int x, int y)
    {
    }

    /**
     * Kiedy jednostka znajdzie się w środku bloku.
     *
     * @param gameMap   Mapa gry.
     * @param e         Jednostka.
     * @param x         Pozycja X.
     * @param y         Pozycja Y.
     */
    public void onEntityInside(LevelMap gameMap, Entity e, int x, int y)
    {
    }

    /**
     * Przy resecie mapy.
     *
     * @param gameMap   Mapa gry.
     * @param x         Pozycja X.
     * @param y         Pozycja Y.
     */
    public void onReset(LevelMap gameMap, int x, int y)
    {
    }

    /**
     * Po zakończeniu wyznaczonego oczekiwania.
     *
     * @param gameMap   Mapa gry.
     * @param x         Pozycja X.
     * @param y         Pozycja Y.
     */
    public void onTimer(LevelMap gameMap, int x, int y)
    {
    }

    /**
     * Kiedy jednostka wyjdzie poza blok.
     *
     * @param gameMap   Mapa gry.
     * @param e         Jednostka.
     * @param old_x     Poprzednia pozycja X.
     * @param old_y     Poprzednia pozycja Y.
     * @param new_x     Nowa pozycja X.
     * @param new_y     Nowa pozycja Y.
     */
    public void onLostInfluence(LevelMap gameMap, Entity e, int old_x, int old_y, int new_x, int new_y)
    {

    }

    /**
     * Czy blok może być postawiony na?
     *
     * @param gameMap   Mapa gry.
     * @param x         Pozycja X.
     * @param y         Pozycja Y.
     * @return          Pozwolenie na położenie bloku.
     */
    public boolean canBePlacedOn(LevelMap gameMap, int x, int y)
    {
        return (gameMap.getNode(x+1,y) != 0 || gameMap.getNode(x,y-1) != 0 || gameMap.getNode(x-1,y) != 0  || gameMap.getNode(x,y+1) != 0) || gameMap.getBackNode(x,y) != 0;
    }
}
