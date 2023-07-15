package org.example.game;

import java.awt.*;

public class Game {
    private Board board;
    private Player playerWhoCreated;
    private Player playerWhoJoined;
    private boolean gameFinished = false;
    private boolean playerWhoCreatedTurn = false;
    private volatile boolean isFull = false;//a game is full when 2 players joined

    public Game(Player playerWhoCreated){

        this.board = new Board(15);
        this.playerWhoCreated = playerWhoCreated;

    }

    public void joinSecondPlayer(Player player2){

        this.playerWhoJoined = player2;
        this.playerWhoJoined.setColor(playerWhoCreated.getColor() == Color.BLACK ? Color.WHITE : Color.BLACK);
        this.isFull = true;

    }

    public Player getPlayerWhoCreated() {
        return playerWhoCreated;
    }

    public Player getPlayerWhoJoined() {
        return playerWhoJoined;
    }

    public boolean isPlayerWhoCreatedTurn(){
        return playerWhoCreatedTurn;
    }

    public boolean isFull() {
        return isFull;
    }

    public boolean isFinished(){
        return gameFinished;
    }

    public void prepareForNewGame(Player player1) {
        this.playerWhoCreated = player1;
        this.gameFinished = false;
        this.playerWhoCreatedTurn = true;
        this.isFull = false;

        this.board = new Board(15);

    }

    public void submitMove(Cell cell) {

        if(playerWhoCreatedTurn){
            this.board.submitMove(cell.getLine(),cell.getColumn(),1);
            playerWhoCreatedTurn = false;
        }
        else {
            this.board.submitMove(cell.getLine(), cell.getColumn(), 2);
            playerWhoCreatedTurn = true;
        }

        if(this.board.someoneWon()){
            this.gameFinished = true;
        }

    }

    public void setPlayerWhoCreatedTurn(boolean playerWhoCreatedTurn) {
        this.playerWhoCreatedTurn = playerWhoCreatedTurn;
    }
}
