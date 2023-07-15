package org.example.homework;

import java.awt.*;

public class PointCell {
    private int row, col;
    private Color color ;
    private boolean entryPoint = false;

    private String name;

    public PointCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public PointCell(int row, int col, Color color, String name) {
        this.row = row;
        this.col = col;

        this.color = color;
        this.name = name;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setEntryPoint(boolean entryPoint) {
        this.entryPoint = entryPoint;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEntryPoint() {
        return entryPoint;
    }

    public String getName() {
        return name;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
