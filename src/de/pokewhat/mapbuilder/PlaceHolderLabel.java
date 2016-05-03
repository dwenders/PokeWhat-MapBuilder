package de.pokewhat.mapbuilder;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dwenders on 03.05.2016.
 */
public class PlaceHolderLabel extends JLabel{

    private int size = 100;
    private Graphics g;

    public PlaceHolderLabel(String text, int size){
        super(text);
        this.size = size;
        setSize(size,size);
    }

    public void setSize(int size){
        this.size = size;
        setSize(size,size);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0,0,size,size);
    }
}
