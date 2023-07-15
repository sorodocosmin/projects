package org.example.commands;

public class SendCommandClient {
    private static String createGame = "create";
    private static String exitApp = "exitApp";
    private static String exitServer = "exit";
    private static String joinGame = "join";
    private static String submitMove = "move";
    private static String finishTime = "finishTime";
    private static String delimiter = "|";

    public static String getCreateGame() {
        return createGame;
    }

    public static String getExitApp() {
        return exitApp;
    }

    public static String getExitServer() {
        return exitServer;
    }

    public static String getJoinGame() {
        return joinGame;
    }

    public static String getSubmitMove(int nrLine, int nrColumn) {
        return submitMove + delimiter + nrLine + delimiter + nrColumn + delimiter;
    }


    public static String getFinishTime(int nrPlayer) {
        return finishTime + delimiter + nrPlayer + delimiter;
    }
}
