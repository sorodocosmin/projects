package org.example.game;

import java.awt.*;

public class Game {

    private boolean started = false;
    private boolean ended = false;
    private boolean myTurn = false;
    private int time = 0;
    private Color color;
    private int squareSize;
    private Cell[][] matrix;
    int lengthMatrix;


    public Game(int n, int squareSize) {

        this.lengthMatrix = n;
        this.matrix = new Cell[n][n];
        this.squareSize = squareSize;

    }
    public Game(){

    }

    public void createCell(int line, int column, int xCoordinateTopLeft, int yCoordinateTopLeft, int xCoordinateBottomRight, int yCoordinateBottomRight){
        this.matrix[line][column] = new Cell(xCoordinateTopLeft, yCoordinateTopLeft, xCoordinateBottomRight, yCoordinateBottomRight);
    }

    public Cell getCell(int line, int column){
        return this.matrix[line][column];
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public int getLengthMatrix(){
        return this.lengthMatrix;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Color getColor() {
        return color;
    }

    public void setPlayer(int nrPlayer) {

        if(nrPlayer == 1){
            this.myTurn = true;
            this.color = Color.BLACK;
        }else{
            this.myTurn = false;
            this.color = Color.WHITE;
        }
    }

    public int getNrPlayer(){
        return this.color == Color.BLACK ? 1 : 2;
    }

}
