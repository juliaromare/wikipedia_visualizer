package edu.macalester.comp124.hw6;

import acm.graphics.GArc;

/**
 * An arc between two instances of the LocalPageBox.
 * Created by juliaromare on 2016-04-11.
 */
public class Arcs extends GArc {

    public Arcs(LocalPageBox box1, LocalPageBox box2, int xDown, int yDown){
        super(box1.getX()+xDown,box1.getY()+yDown,Math.abs(box1.getX()-box2.getX()),75,0,180);


    }
}
