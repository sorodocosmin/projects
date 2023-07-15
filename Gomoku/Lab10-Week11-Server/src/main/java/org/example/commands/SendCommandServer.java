package org.example.commands;

public class SendCommandServer {
    private final static String startGame = "start";
    private final static String noAvailableGames = "noAvailableGames";
    private final static String someoneWon = "gameFinished";
    private final static String createGame = "create";
    private final static String yourTurn = "yourTurn";
    private final static String notYourTurn = "notYourTurn";
    private final static String exitServer = "exit";
    private final static String exitApp = "exitApp";
    private final static String submitMove = "move";

    private final static String delimiter = "|";

    public static String getStartGame(int nrMinutes, int nrPlayer) {
        return startGame + delimiter + nrMinutes + delimiter + nrPlayer + delimiter;
    }

    public static String getCreateGame() {
        return createGame;
    }


    public static String getSubmitMove(int nrLine, int nrColumn) {
        return submitMove + delimiter + nrLine + delimiter + nrColumn + delimiter;
    }

    public static String getNoAvailableGames() {
        return noAvailableGames;
    }

    public static String getExitServer() {
        return exitServer;
    }

    public static String getExitApp() {
        return exitApp;
    }

    public static String getGameFinished(int nrPlayerWhoWon) {
        return someoneWon + delimiter + nrPlayerWhoWon + delimiter;
    }
}
