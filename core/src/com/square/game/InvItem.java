package com.square.game;

/**
 * Klasa stanowiąca przedmiot znajdujący się w ekwipunku gracza.
 */
public class InvItem {

    public int type;
    public int amount;

    public InvItem(int t, int a) {type = t; amount = a;}
    public InvItem(InvItem i) {type = i.type; amount = i.amount;}
}
