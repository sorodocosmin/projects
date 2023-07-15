package org.example;

import org.example.commands.ReceiveCommandClient;
import org.example.gui.MainFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGame {

    private MainFrame mainFrame;
    final private String serverAddress ; // The server's IP address
    final private int PORT ; // The server's port
    final private Socket socket;

    final private PrintWriter writeToServer;
    final private BufferedReader readFromServer;

    private boolean clientExit = false;

    public ClientGame(int nrPort, String serverAddress) throws IOException {
        this.serverAddress = serverAddress;
        this.PORT = nrPort;

        this.socket = new Socket(this.serverAddress, this.PORT);

        this.writeToServer = new PrintWriter(this.socket.getOutputStream(), true);

        this.readFromServer = new BufferedReader (new InputStreamReader(this.socket.getInputStream()));

        this.connect();
    }

    private void connect(){

        this.mainFrame = new MainFrame(this);
        this.mainFrame.setVisible(true);

        while(!clientExit) {

            this.handleResponses();

        }
        this.mainFrame.dispose();

    }

    private void handleResponses(){

        String response = null;

        try {
             response = this.readFromServer.readLine();
        }
        catch (IOException e) {
            System.out.println("Error at reading from server");
        }

        if(response == null){
            this.clientExit = true;
            System.out.println("Server is down");
            return;
        }

        System.out.println("The response is: " + response);
        if(ReceiveCommandClient.isExitServer(response)){

            this.clientExit = true;
            this.mainFrame.dispose();

            System.out.println("Someone closed the server");

        } else if(ReceiveCommandClient.isExitApp(response)){

            this.clientExit = true;
            this.mainFrame.dispose();

        } else if (ReceiveCommandClient.isCreateGame(response)) {

            this.mainFrame.getCanvas().createBoard();
            this.handleResponses();// we are waiting for someone to join the game

        } else if (ReceiveCommandClient.isStartGame(response)) {

            this.mainFrame.getCanvas().createBoard();

            int nrMinutes = ReceiveCommandClient.getNrMinutes(response);
            int nrPlayer = ReceiveCommandClient.getNrPlayer(response);


            this.mainFrame.getCanvas().getGame().setPlayer(nrPlayer);
            this.mainFrame.getCanvas().getGame().setTime(nrMinutes);
            this.mainFrame.getCanvas().setTime();

            if(nrPlayer == 1){
                this.mainFrame.getCanvas().startCountdown();
            }

            this.mainFrame.getCanvas().drawNamePlayer();

            //System.out.println("I am player" + nrPlayer + " and I have " + nrMinutes + " minutes to play");

            this.mainFrame.getCanvas().getGame().setStarted(true);

        } else if (ReceiveCommandClient.isSubmitMove(response)) {

            //ystem.out.println("The other player made a move");
            int line = ReceiveCommandClient.getNrLine(response);
            int column = ReceiveCommandClient.getNrColumn(response);

            this.mainFrame.getCanvas().drawPointByOtherPlayer(line, column);
            this.mainFrame.getCanvas().getGame().setMyTurn(true);
            this.mainFrame.getCanvas().startCountdown();

        } else if (ReceiveCommandClient.isNoAvailableGames(response)) {

            this.mainFrame.getCanvas().noGamesAvailable();

        } else if(ReceiveCommandClient.isSomeoneWon(response)){

            int nrPlayer = ReceiveCommandClient.getWinner(response);
            this.mainFrame.getCanvas().getGame().setEnded(true);
            this.mainFrame.getCanvas().drawFinishGame(nrPlayer);
            this.mainFrame.getCanvas().stopCountdown();

        }
        else {
            System.out.println("UNKNOWN RESPONSE");
        }

    }

    public void sendRequest(String request){

        this.writeToServer.println(request);

    }

}
