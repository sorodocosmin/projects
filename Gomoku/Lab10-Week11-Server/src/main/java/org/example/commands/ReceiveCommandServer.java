package org.example.commands;

import org.example.game.Cell;

public class ReceiveCommandServer {
    private final static String createGame = "create";
    private final static String exitApp = "exitApp";
    private final static String exitServer = "exit";
    private final static String joinGame = "join";
    private final static String submitMove = "move";

    private final static String finishTime = "finishTime";

    private final static String delimiter = "\\|";


    public static boolean isCreateGame(String command) {
        return command.equals(createGame);
    }

    public static boolean isExitApp(String command) {
        return command.equals(exitApp);
    }

    public static boolean isExitServer(String command) {
        return command.equals(exitServer);
    }

    public static boolean isJoinGame(String command) {
        return command.equals(joinGame);
    }

    public static boolean isSubmitMove(String command) {
        return command.startsWith(submitMove);
    }

    public static Cell getCellFromSubmitMove(String command) {//it will be called only if first it's checked that the command is a submitMove

        //the received command will be like this : move|nrLine|nrColumn|

        String [] commandReceived = command.split(delimiter); // cell[0] = move, cell[1] = nrLine, cell[2] = nrColumn, cell[3] = ""

        int line = Integer.parseInt(commandReceived[1]);
        int column = Integer.parseInt(commandReceived[2]);

        return new Cell(line, column);

    }

    public static boolean isFinishTime(String command) {
        return command.startsWith(finishTime);
    }

    public static int getNrPlayerWhoFinishedTime(String command) {//it will be called only if first it's checked that the command is a finishTime

        String [] commandReceived = command.split(delimiter);

        return Integer.parseInt(commandReceived[1]);
    }

}
