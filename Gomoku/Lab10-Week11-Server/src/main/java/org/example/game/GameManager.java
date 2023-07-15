package org.example.game;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager {

    private static CopyOnWriteArrayList<Game> games =  new CopyOnWriteArrayList<>();;

    private GameManager(){
    }

    public static Game joinAvailableGame( Player player2){

        for(Game game : games){
            if(!game.isFull()){//we found a game which was created by someone else and is not full
                game.joinSecondPlayer(player2);
                return game;
            }
        }

        return null;//there is no available game to join
    }

    public static Game createGame(Player player1){

        for(Game game : games){
            if(game.isFinished()){//if there is already an instance of game created and players finished the game, we can reuse the instance
                game.prepareForNewGame(player1);
                return game;
            }
        }

        Game game = new Game(player1);
        games.add(game);
        return game;

    }




}
