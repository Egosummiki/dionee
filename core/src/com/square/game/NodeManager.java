package com.square.game;

import java.util.Vector;

/**
 * Created by Mikolaj on 07.10.2015.
 */
public class NodeManager {

    private Vector<Node> node_registry;
    private Vector<Node> background_registry;

    public final static int NODE_AIR = 0;
    public final static int NODE_GRASS = 1;
    public final static int NODE_DIRT = 2;
    public final static int NODE_PLANK = 3;
    public final static int NODE_STAIRS = 4;
    public final static int NODE_JUMP = 5;
    public final static int NODE_TURN_LEFT = 6;
    public final static int NODE_TURN_RIGHT = 7;
    public final static int NODE_TELEPORT_A = 8;
    public final static int NODE_TELEPORT_B = 9;
    public final static int NODE_BACKGROUND_DIRT = 10;
    public final static int NODE_WATER = 11;
    public final static int NODE_ALTERNATE_MOVELESS = 12;

    public final static int NODE_TURN_LEFT_MOVELESS = 13;
    public final static int NODE_TURN_RIGHT_MOVELESS = 14;
    public final static int NODE_ALTERNATE = 15;

    public final static int NODE_WOOD = 16;
    public final static int NODE_WOOD_R = 17;
    public final static int NODE_WOOD_L = 18;

    public final static int NODE_UPWARDS = 19;

    public final static int NODE_METAL = 20;

    public NodeManager()
    {
        node_registry = new Vector<Node>();
        background_registry = new Vector<Node>();

        register(new Node(0, Render.TEXTURE_NULL, "Air", 0x000000FF, false, false));
        register(new NodeMultiColor(1, Render.TEXTURE_GRASS_01, "Grass", 0x008000FF, true, 4, false));
        register(new NodeMultiColor(2, Render.TEXTURE_DIRT_01, "Dirt", 0x7F0000FF, true, 4, false));
        register(new NodeMultiColor(3, Render.TEXTURE_PLANK_01, "Planks", 0xA06400FF, true, 4, true));
        register(new NodeStairs(4, Render.TEXTURE_STAIRS, "Stairs", 0x808080FF));
        register(new NodeJump(5, Render.TEXTURE_JUMP, "Jump Block", 0x666666FF));
        register(new NodeTurn(6, Render.TEXTURE_TURN_LEFT, "Turn Left Block", 0x800001FF, true, false));
        register(new NodeTurn(7, Render.TEXTURE_TURN_RIGHT, "Turn Right Block", 0x490001FF, true, true));

        register(new NodeTeleport(8, Render.TEXTURE_TELEPORT_A, Render.TEXTURE_TELEPORT_A_USE, "Teleport Block", 0xFFD800FF, 9));
        register(new NodeTeleport(9, Render.TEXTURE_TELEPORT_B, Render.TEXTURE_TELEPORT_B_USE, "Teleport Block", 0x267F00FF, 8));

        register(new NodeMultiColor(10, Render.TEXTURE_BACK_DIRT_01, "Dirt Background", 0x420001FF, false, 4, false));

        register(new NodeWater(11, Render.TEXTURE_WATER_01, 4, "Water", 0x004577FF));

        register(new NodeAlternate(12, Render.TEXTURE_ALTERNATE_RIGHT, "Alternate", 0xC0C0C0FF, false, true));

        register(new NodeTurn(13, Render.TEXTURE_TURN_LEFT, "Turn Left Block", 0x800000FF, false, false));
        register(new NodeTurn(14, Render.TEXTURE_TURN_RIGHT, "Turn Right Block", 0x490000FF, false, true));

        register(new NodeAlternate(15, Render.TEXTURE_ALTERNATE_RIGHT, "Alternate", 0xC0C0C1FF, true, true));

        register(new Node(16, Render.TEXTURE_WOOD, "Wood", 0x945500FF, true, false));
        register(new Node(17, Render.TEXTURE_WOOD_END_R, "Wood", 0x945501FF, true, false));
        register(new Node(18, Render.TEXTURE_WOOD_END_L, "Wood", 0x945502FF, true, false));

        register(new NodeUpwards(19, Render.TEXTURE_UPWARDS, "Upward Node", 0xFFD810FF, true));

        register(new Node(20, Render.TEXTURE_METAL, "Metal", 0x8890A1FF, true, false));

        registerBack(new Node(0, Render.TEXTURE_NULL, "Air", 0x000000FF, false, false));
        registerBack(new NodeMultiColor(1, Render.TEXTURE_BACK_DIRT_01, "Dirt Background", 0x420000FF, false, 4, false));
        registerBack(new Node(2, Render.TEXTURE_METAL_BACK, "Metal Background", 0x606267FF, false, false));
    }

    public void register(Node nd)
    {
        node_registry.add(nd);
    }

    public void registerBack(Node nd)
    {
        background_registry.add(nd);
    }

    public int getSize()
    {
        return node_registry.size();
    }
    public int getBackSize()
    {
        return background_registry.size();
    }

    public Node getNode(int i)
    {
        return node_registry.get(i);
    }
    public Node getBackNode(int i)
    {
        return background_registry.get(i);
    }

}
