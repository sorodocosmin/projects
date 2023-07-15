package org.example.compulsory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class ExplorationMap {
    private final Cell[][] matrix;
    private final int sizeMatrix;
    private int cellsVisited = 0;

    public ExplorationMap(int n){
        this.sizeMatrix = n;
        this.matrix = new Cell[n][n];

        for( int i=0; i<n;++i){
            for(int j=0; j<n;++j){
                this.matrix[i][j] = new Cell();
            }
        }

    }

    public int getSizeMatrix(){
        return this.sizeMatrix;
    }
    public boolean finishedVisited(){
            return this.cellsVisited == this.sizeMatrix*this.sizeMatrix;
    }
    public boolean isVisited(int row, int col){
        return this.matrix[row][col].isVisited();
    }

    public boolean visit (int row, int col, Robot robot){
        synchronized (this.matrix[row][col]){
            if(!this.matrix[row][col].isVisited()){
                int nrTokensWhichWillBeInserted = this.sizeMatrix;
                this.matrix[row][col].setTokens(robot.getExplore().getSharedMemory().extractTokens(nrTokensWhichWillBeInserted));
                this.matrix[row][col].setVisited(true);
                robot.setTokensInserted(robot.getTokensInserted() + nrTokensWhichWillBeInserted);

                this.cellsVisited ++;

                robot.addCellsWhichWillBeVisited(row,col);//it will be added all the neighbours of that node/point

                try {
                    sleep(1000);
                }

                catch (InterruptedException e) {
                    System.err.print(e);
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Matrix : \n");
        for(int i=0; i<this.sizeMatrix ; ++i){
            for( int j=0 ; j< this.sizeMatrix ; ++j){
                res.append("Cell["+i+"]["+j+"] visited(" + this.matrix[i][j].isVisited() + ") : " );
                res.append(this.matrix[i][j].getTokens());
                res.append("\n");
            }
        }

        return res.toString();
    }
}
