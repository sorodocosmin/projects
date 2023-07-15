package org.example.homework;

import java.awt.*;
import java.io.Serializable;

public class Edge implements Serializable {
    private int node1, node2;
    private boolean isColored = false;
    private Color color;

    private double slope; //panta

    public Edge(){

    }

    public Edge(int node1, int node2){
        this.node1 = node1;
        this.node2 = node2;
    }

    public int getNode1() {
        return node1;
    }

    public void setNode1(int node1) {
        this.node1 = node1;
    }

    public int getNode2() {
        return node2;
    }

    public void setNode2(int node2) {
        this.node2 = node2;
    }

    public boolean isColored() {
        return isColored;
    }

    public void setColored(boolean colored) {
        isColored = colored;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getSlope() {
        return slope;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "node1=" + node1 +
                ", node2=" + node2 +
                ", isColored=" + isColored +
                ", color=" + color +
                '}';
    }
}
