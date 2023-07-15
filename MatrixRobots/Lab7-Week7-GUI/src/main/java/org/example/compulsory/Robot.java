package org.example.compulsory;

import org.example.gui.DrawingPanel;
import org.example.homework.PointCell;

import java.awt.*;
import java.util.*;

import static java.lang.Thread.sleep;

public class Robot implements Runnable{
    private final String name;
    private boolean running = false;
    Exploration explore;

    private PointCell entryCell;
    private Deque<PointCell> cellsWhichWillBeVisited;

    private boolean pause = false;
    private int pauseTime = 0;

    private long tokensInserted = 0;

    private Color color;

    private DrawingPanel drawingPanel;


    public Robot(String name, Color color, DrawingPanel drawingPanel){
        this.name = name;
        this.color = color;
        this.drawingPanel = drawingPanel;
    }

    public Robot(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    public Exploration getExplore(){
        return this.explore;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        if(!pause){
            synchronized (this) {
                this.notify();
            }
        }
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public long getTokensInserted() {
        return tokensInserted;
    }

    public void setTokensInserted(long tokensInserted) {
        this.tokensInserted = tokensInserted;
    }

    @Override
    public void run(){

        this.cellsWhichWillBeVisited = new ArrayDeque<>();//because we will often insert and remove elements from both ends
        boolean newEntryPoint = false;
        if (this.setRandomEntryPoint()) {//there is at least one cell which is not visited

            int row = this.entryCell.getRow();
            int col = this.entryCell.getCol();
            newEntryPoint = true;

            System.out.println("Robot " + this.name + " started at point : (" + row + ", " + col + ");");

            while (running) {

                if (this.cellsWhichWillBeVisited.size() == 0) {//all the neighbours are visited, but the matrix is not done visited
                    if(!this.setRandomEntryPoint()){//all the cells are visited
                        running = false;
                        break;
                    }
                    else {
                        newEntryPoint = true;
                    }
                }

                row = this.cellsWhichWillBeVisited.getFirst().getRow();
                col = this.cellsWhichWillBeVisited.getFirst().getCol();

                if(this.explore.getMap().visit(row, col, this)){//if the cell is visited by this robot -> we draw the corresponding square
                    this.drawingPanel.drawSquare(row, col, this.color, this.name, newEntryPoint);
                    newEntryPoint = false;
                }

                this.cellsWhichWillBeVisited.removeFirst();
                if (this.pause) {
                    if ((this).pauseTime == -1) {//the robot is paused for an undefined time limit
                            try {
                                while (this.pause){
                                    synchronized(this) {
                                        this.wait();
                                    }
                                }
                            } catch (InterruptedException e) {
                                System.out.println(e);
                            }
                    } else {//a robot is paused for a specific time
                        this.pause = false;
                        try {
                            sleep(this.pauseTime);
                        } catch (InterruptedException e) {
                            System.out.println(e);
                        }
                    }
                }

                if (this.explore.getMap().finishedVisited()) {
                    running = false;
                }

            }

            System.out.println("Robot " + this.name + " done (inserted " + this.tokensInserted + " tokens).");
        }
        else{
            System.out.println("Robot " + this.name + " found all cells visited when he started.");
            this.running = false;
        }
    }

    public void addCellsWhichWillBeVisited(int row, int col){
        // g h a
        // f X b
        // e d c
        int n = this.explore.getMap().getSizeMatrix();

        //condition for a
        if( row-1 >=0 && col+1 < n ){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row-1,col+1));

        }
        //condition for b
        if ( col+1 < n){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row,col+1));
        }
        //condition for c
        if ( row+1 < n && col+1 < n ){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row+1,col+1));
        }
        //condition for d
        if( row+1 < n){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row+1,col));
        }
        //condition for e
        if( row+1 < n && col-1 >=0 ){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row+1,col-1));
        }
        //condition for f
        if( col-1 >= 0){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row,col-1));
        }
        //condition for g
        if(row-1 >=0 && col-1 >=0){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row-1,col-1));
        }
        //condition for h
        if(row-1 >= 0){
            this.cellsWhichWillBeVisited.addLast(new PointCell(row-1,col));
        }
    }

    private boolean setRandomEntryPoint(){
        //we need to generate a random point for the robot to start such that point is not visited

        int row = (int) (Math.random() * this.explore.getMap().getSizeMatrix());
        int col = (int) (Math.random() * this.explore.getMap().getSizeMatrix());

        while (this.explore.getMap().isVisited(row,col) && !this.explore.getMap().finishedVisited()){
            row = (int) (Math.random() * this.explore.getMap().getSizeMatrix());
            col = (int) (Math.random() * this.explore.getMap().getSizeMatrix());
        }

        if(this.explore.getMap().finishedVisited()){
            return false;
        }

        this.entryCell = new PointCell(row,col);
        this.cellsWhichWillBeVisited.addFirst(this.entryCell);
        return true;

    }


    @Override
    public String toString() {
        return "Robot{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return name.equals(robot.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
