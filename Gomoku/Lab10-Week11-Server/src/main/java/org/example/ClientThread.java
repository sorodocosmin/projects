package org.example;

import org.example.commands.ReceiveCommandServer;
import org.example.commands.SendCommandServer;
import org.example.game.Cell;
import org.example.game.Game;
import org.example.game.GameManager;
import org.example.game.Player;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientThread implements Runnable {

    private final Socket socket;
    private boolean clientNotExited = true;

    private Game game;
    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Client connected");

        try {
            BufferedReader readFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writeToClient = new PrintWriter(socket.getOutputStream(), true);

            while (ServerGame.notStopped && clientNotExited) {

                handleRequestFromClient(readFromClient, writeToClient);
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Socket timed out!");
        } catch (IOException e) {
            System.err.println("Communication error: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private void handleRequestFromClient(BufferedReader readFromClient, PrintWriter writeToClient) throws IOException {

        String request = readFromClient.readLine();

        if (ReceiveCommandServer.isExitServer(request)) {

            ServerGame.stop();
            writeToClient.println(SendCommandServer.getExitServer());

        } else if (ReceiveCommandServer.isCreateGame(request)) {

            this.game = GameManager.createGame(new Player("PlayerCreate", 5, writeToClient));
            writeToClient.println(SendCommandServer.getCreateGame());

        } else if (ReceiveCommandServer.isJoinGame(request)) {

            this.game = GameManager.joinAvailableGame(new Player("Player", 5, writeToClient));

            if (this.game != null) {

                //if the first player plays with black -> the first player will start
                if(this.game.getPlayerWhoCreated().getColor() == Color.BLACK){
                    this.game.setPlayerWhoCreatedTurn(true);
                    this.game.getPlayerWhoCreated().getWriteToClient().println(SendCommandServer.getStartGame(1,1));
                    this.game.getPlayerWhoJoined().getWriteToClient().println(SendCommandServer.getStartGame(1,2));
                }else{
                    this.game.setPlayerWhoCreatedTurn(false);
                    this.game.getPlayerWhoCreated().getWriteToClient().println(SendCommandServer.getStartGame(1,2));
                    this.game.getPlayerWhoJoined().getWriteToClient().println(SendCommandServer.getStartGame(1,1));
                }


            } else {

                writeToClient.println(SendCommandServer.getNoAvailableGames());

            }

        } else if (ReceiveCommandServer.isExitApp(request)) {

            this.clientNotExited = false;
            writeToClient.println(SendCommandServer.getExitApp());

        } else if (ReceiveCommandServer.isSubmitMove(request)) {

            Cell cell = ReceiveCommandServer.getCellFromSubmitMove(request);

            this.game.submitMove(cell);//and change player turn (If was player1 turn -> now is player2 turn)

            if(!this.game.isPlayerWhoCreatedTurn()){//if it was player1 turn -> we are writing to player2 that he can make a move

                this.game.getPlayerWhoJoined().getWriteToClient().println(SendCommandServer.getSubmitMove(cell.getLine(), cell.getColumn()));

            }else{

                this.game.getPlayerWhoCreated().getWriteToClient().println(SendCommandServer.getSubmitMove(cell.getLine(), cell.getColumn()));

            }

            if(this.game.isFinished()){//after we send to the clients the move -> we check if the game is finished
                //if the game is finished -> we are writing to both players that the game is finished
                int nrPlayerWhoWon = this.game.getPlayerWhoCreated().getColor() == Color.BLACK ? 1 : 2;
                this.game.getPlayerWhoCreated().getWriteToClient().println(SendCommandServer.getGameFinished(nrPlayerWhoWon));
                this.game.getPlayerWhoJoined().getWriteToClient().println(SendCommandServer.getGameFinished(nrPlayerWhoWon));

            }


        } else if (ReceiveCommandServer.isFinishTime(request)) {

            int nrPlayer = ReceiveCommandServer.getNrPlayerWhoFinishedTime(request);

            //the player who finished the time -> lost the game
            this.game.getPlayerWhoCreated().getWriteToClient().println(SendCommandServer.getGameFinished(nrPlayer==1 ? 2 : 1));
            this.game.getPlayerWhoJoined().getWriteToClient().println(SendCommandServer.getGameFinished(nrPlayer==1 ? 2 : 1));

        } else {
            System.out.printf("Unknown request: %s\n", request);
        }
    }

}
