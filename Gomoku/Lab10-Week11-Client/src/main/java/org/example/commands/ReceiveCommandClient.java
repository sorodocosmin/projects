package org.example.commands;

public class ReceiveCommandClient {
    private final static String startGame = "start";
    private final static String exitApp = "exitApp";
    private final static String someoneWon = "gameFinished";
    private final static String noAvailableGames = "noAvailableGames";
    private final static String submitMove = "move";
    private final static String createGame = "create";
    private final static String myTurn = "yourTurn";
    private final static String notMyTurn = "notYourTurn";
    private final static String exitServer = "exit";
    private final static String delimiter = "\\|";

    public static boolean isNoAvailableGames(String command) {
        return command.equals(noAvailableGames);
    }

    public static boolean isCreateGame(String command) {
        return command.equals(createGame);
    }

    public static boolean isMyTurn(String command) {
        return command.equals(myTurn);
    }

    public static boolean isNotMyTurn(String command) {
        return command.equals(notMyTurn);
    }

    public static boolean isExitApp(String command) {
        return command.equals(exitApp);
    }

    public static boolean isExitServer(String command) {
        return command.equals(exitServer);
    }

    public static boolean isStartGame(String command) {
        return command.startsWith(startGame);
    }
    public static int getNrMinutes(String response) {//it will be called only if first it's checked that the command is a startGame

        //the received command will be like this : start|nrMinutes|nrPlayer|

        String [] commandReceived = response.split(delimiter); // cell[0] = start, cell[1] = nrMinutes, cell[2] = "nrPlayer
        System.out.println("The number of minutes is: " + commandReceived[1]);

        return Integer.parseInt(commandReceived[1]);
    }

    public static int getNrPlayer(String response) {//it will be called only if first it's checked that the command is a startGame

        //the received command will be like this : start|nrMinutes|

        String [] commandReceived = response.split(delimiter); // cell[0] = start, cell[1] = nrMinutes, cell[2] = "nrPlayer"
        System.out.println("The player number is: " + commandReceived[2]);
        return Integer.parseInt(commandReceived[2]);
    }

    public static boolean isSubmitMove(String command) {
        return command.startsWith(submitMove);
    }

    public static int getNrLine(String command) {//it will be called only if first it's checked that the command is a submitMove

        //the received command will be like this : move|nrLine|nrColumn|

        String [] commandReceived = command.split(delimiter);
        System.out.println("The line number is: " + commandReceived[1]);
        return Integer.parseInt(commandReceived[1]);

    }

    public static int getNrColumn(String command) {//it will be called only if first it's checked that the command is a submitMove

        //the received command will be like this : move|nrLine|nrColumn|

        String [] commandReceived = command.split(delimiter);
        System.out.println("The column number is: " + commandReceived[2]);
        return Integer.parseInt(commandReceived[2]);

    }

    public static boolean isSomeoneWon(String command) {
        return command.startsWith(someoneWon);
    }

    public static int getWinner(String command) {//it will be called only if first it's checked that the command is a someoneWon

        //the received command will be like this : gameFinished|winner|

        String [] commandReceived = command.split(delimiter);
        System.out.println("The winner is: " + commandReceived[1]);
        return Integer.parseInt(commandReceived[1]);

    }

}
