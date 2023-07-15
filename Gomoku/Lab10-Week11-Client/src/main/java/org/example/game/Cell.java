package org.example.game;

public class Cell {
    private int xCoordinateTopLeft;
    private int yCoordinateTopLeft;

    private int xCoordinateBottomRight;
    private int yCoordinateBottomRight;
    private boolean isColored = false;

    public Cell(int xCoordinateTopLeft, int yCoordinateTopLeft, int xCoordinateBottomRight, int yCoordinateBottomRight){
        this.xCoordinateTopLeft = xCoordinateTopLeft;
        this.yCoordinateTopLeft = yCoordinateTopLeft;

        this.xCoordinateBottomRight = xCoordinateBottomRight;
        this.yCoordinateBottomRight = yCoordinateBottomRight;
    }

    public void setColored(boolean isColored){
        this.isColored = isColored;
    }
    public boolean isColored(){
        return this.isColored;
    }

    public boolean isInside(int x, int y){
        return x >= this.xCoordinateTopLeft && x <= this.xCoordinateBottomRight && y >= this.yCoordinateTopLeft && y <= this.yCoordinateBottomRight;
    }

    public int getXCoordinateTopLeft() {
        return xCoordinateTopLeft;
    }

    public int getYCoordinateTopLeft() {
        return yCoordinateTopLeft;
    }

    public int getXCoordinateBottomRight() {
        return xCoordinateBottomRight;
    }

    public int getYCoordinateBottomRight() {
        return yCoordinateBottomRight;
    }
}
