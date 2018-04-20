package com.square.game;

/**
 * Created by Mikolaj on 10.10.2015.
 */
public class InvItem {

    public int type;
    public int amount;

    public InvItem(int t, int a) {type = t; amount = a;}
    public InvItem(InvItem i) {type = i.type; amount = i.amount;}
}
