package org.example.game;

public class Board {
    private int[][] matrixBoard;

    private Cell lastCellColored;

    public Board(int n) {

        matrixBoard = new int[n][n];
        this.lastCellColored = new Cell(-1, -1);

    }

    public void submitMove(int line, int column, int value) {

        matrixBoard[line][column] = value;

        this.lastCellColored.setLine(line);
        this.lastCellColored.setColumn(column);

    }

    public boolean someoneWon(){ //verify if there are 5 consecutive cells with the same number : vertical, horizontal, diagonal

        int line = this.lastCellColored.getLine();
        int column = this.lastCellColored.getColumn();
        int value = matrixBoard[line][column];


        int count = 0;
        //verify vertical
        for( int i= 0 ; i < this.matrixBoard.length && count <5 ; i++ ){
            if( this.matrixBoard[i][column] == value ){
                count++;
            } else{
                count = 0;
            }
        }

        if(count == 5){
            return true;
        }

        //verify horizontal
        count = 0;
        for( int i= 0 ; i < this.matrixBoard[0].length && count <5 ; i++ ){
            if( this.matrixBoard[line][i] == value ){
                count++;
            } else{
                count = 0;
            }
        }

        if(count == 5){
            return true;
        }

        //verify diagonal
        count = 0;
        int i = line;
        int j = column;

        while( i > 0 && j > 0 ){//maximum top-left
            i--;
            j--;
        }

        while( i < this.matrixBoard.length && j < this.matrixBoard[0].length && count < 5 ){
            if( this.matrixBoard[i][j] == value ){
                count++;
            } else{
                count = 0;
            }
            i++;
            j++;
        }

        if(count == 5){
            return true;
        }

        //verify anti-diagonal
        count = 0;
        i = line;
        j = column;

        while( i > 0 && j < this.matrixBoard[0].length - 1 ){//maximum top-right
            i--;
            j++;
        }

        while( i < this.matrixBoard.length && j >= 0 && count < 5 ){
            if( this.matrixBoard[i][j] == value ){
                count++;
            } else{
                count = 0;
            }
            i++;
            j--;
        }

        return count == 5;

    }

}
