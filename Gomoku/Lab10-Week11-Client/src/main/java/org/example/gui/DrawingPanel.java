package org.example.gui;

import org.example.commands.SendCommandClient;
import org.example.game.Cell;
import org.example.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private final MainFrame frame;

    private Game game;

    Timer countdownTimer;
    int minutes=0, seconds=0;

    int W = 800, H = 500;

    private BufferedImage image;
    private Graphics2D graphics2D;

    final private Color backgroundColor = new Color(211, 211, 211);


    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        this.game = new Game();

        //createBoard();
        welcomeMessage();
        initPanel();

        setBackground(this.backgroundColor);

        repaint();
    }

    private void welcomeMessage() {

        createOffScreenImage();
        // Set font and color for the welcome message
        Font font = new Font("Arial", Font.BOLD, 36);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.BLACK);

        String welcomeMessage = "Welcome! - Enjoy the game!";
        graphics2D.drawString(welcomeMessage, W/5, H/2);

        graphics2D.setColor(this.backgroundColor);

        repaint();

    }

    public void noGamesAvailable() {

        createOffScreenImage();
        // Set font and color for the welcome message
        Font font = new Font("Arial", Font.BOLD, 28);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.BLACK);

        String welcomeMessage = "There are no games available at the moment, try create one!";
        graphics2D.drawString(welcomeMessage, 0, H/2);

        graphics2D.setColor(this.backgroundColor);

        repaint();

    }


    public void drawNamePlayer() {

            graphics2D.setColor(this.game.getColor());

            Font font = new Font("Arial", Font.BOLD, 25);
            graphics2D.setFont(font);

            String welcomeMessage = "Player" + this.game.getNrPlayer();
            graphics2D.drawString(welcomeMessage, 5, 20);

            graphics2D.setColor(this.backgroundColor);

            repaint();

    }

    public void drawFinishGame(int nrPlayer) {

        graphics2D.setColor(Color.RED);
        String finishMessage;
        if(this.game.getNrPlayer() == nrPlayer){
            finishMessage = "You won!";
        }else{
            finishMessage = "You lost!";
        }

        Font font = new Font("Arial", Font.BOLD, 30);
        graphics2D.setFont(font);
        graphics2D.drawString(finishMessage, W/3, H/2);

        graphics2D.setColor(this.backgroundColor);
        repaint();

    }

    public void startCountdown() {

        countdownTimer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the countdown
                if (seconds > 0) {
                    seconds--;
                } else {
                    if (minutes > 0) {
                        minutes--;
                        seconds = 59;
                    } else {
                        // Player lost
                        sendRequestFinishTime();
                    }
                }

                drawTime();
                // Redraw the panel

            }
        });

        // Start the countdown timer
        countdownTimer.start();
    }



    private void drawTime() {

        graphics2D.fillRect(10,30, 80, 40);

        String time = String.format("%02d:%02d", minutes, seconds);

        graphics2D.setColor(Color.BLACK);

        Font font = new Font("Arial", Font.BOLD, 25);
        graphics2D.setFont(font);

        graphics2D.drawString(time, 10, 50);

        graphics2D.setColor(this.backgroundColor);
        repaint();

    }

    public void stopCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }

    private void initPanel() {

        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (game.isStarted() && !game.isEnded() && game.isMyTurn()) {
                    int x = e.getX();
                    int y = e.getY();
                    if(checkIfCellIsClickedAndSendReq(x, y)){
                        game.setMyTurn(false);
                        stopCountdown();
                    }
                }

            }
        });
    }

    private void createOffScreenImage() {

        this.image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        this.graphics2D = image.createGraphics();

        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(this.backgroundColor);
        graphics2D.fillRect(0, 0, W, H);
    }


     public void createBoard() {

        this.createOffScreenImage();
        this.createGame(15);

        this.drawBoard();

        repaint();

    }
    private void createGame(int lengthMatrix) {


        int squareSize = Math.min(W, H) / lengthMatrix; // calculate the size of each square

        this.game = new Game(lengthMatrix,squareSize);

        int startX = (W - squareSize * lengthMatrix) / 2; // calculate the starting X coordinate for drawing
        int startY = (H - squareSize * lengthMatrix) / 2; // calculate the starting Y coordinate for drawing


        for(int i=0; i < lengthMatrix ; ++i){
            for(int j=0 ; j< lengthMatrix ; ++j){
                int x = startX + j * squareSize; // calculate the X coordinateTopLeft
                int y = startY + i * squareSize; // calculate the Y coordinateTopLeft
                this.game.createCell(i,j,x,y,x+squareSize,y+squareSize);
            }
        }

    }

    public void drawBoard() {

        this.graphics2D.setColor(Color.BLACK);
        int lengthMatrix = this.game.getLengthMatrix();

        for (int i = 0; i < lengthMatrix; i++) {
            for (int j = 0; j < lengthMatrix; j++) {

                Cell cell = this.game.getCell(i,j);

                this.graphics2D.drawRect(cell.getXCoordinateTopLeft(), cell.getYCoordinateTopLeft(),
                        this.game.getSquareSize(),this.game.getSquareSize()); // draw the square
            }
        }

        this.graphics2D.setColor(this.backgroundColor);

        repaint();

    }

    private boolean checkIfCellIsClickedAndSendReq(int x, int y) {

        int lengthMatrix = this.game.getLengthMatrix();
        for(int i = 0 ; i < lengthMatrix ; ++i ){
            for ( int j = 0 ; j < lengthMatrix ; ++j ){
                Cell cell = this.game.getCell(i,j);
                if(!cell.isColored() && cell.isInside(x,y)){

                    this.drawPointInCell(cell);
                    cell.setColored(true);
                    this.sendRequestToServer(i,j);
                    return true;

                }
            }
        }

        return false;

    }

    private void sendRequestToServer(int i, int j) {

        this.frame.getClientGame().sendRequest(SendCommandClient.getSubmitMove(i,j));

    }

    private void sendRequestFinishTime() {

        this.stopCountdown();

        this.frame.getClientGame().sendRequest(SendCommandClient.getFinishTime(this.game.getColor()==Color.BLACK ? 1 : 2));

    }

    private void drawPointInCell(Cell cell) {

        //draw the circle in the middle of the cell
        int x = cell.getXCoordinateTopLeft() + game.getSquareSize()/2;
        int y = cell.getYCoordinateTopLeft() + game.getSquareSize()/2;

        int radius = game.getSquareSize()/2;

        graphics2D.setColor(this.game.getColor());

        graphics2D.fillOval(x-radius, y-radius, radius*2, radius*2);

        graphics2D.setColor(this.backgroundColor);

        repaint();

    }

    public void drawPointByOtherPlayer(int line, int column){

        Cell cell = this.game.getCell(line,column);

        cell.setColored(true);

        int x = cell.getXCoordinateTopLeft() + game.getSquareSize()/2;
        int y = cell.getYCoordinateTopLeft() + game.getSquareSize()/2;

        int radius = game.getSquareSize()/2;

        System.out.println("drawPointByOtherPlayer: " + line + " " + column);
        graphics2D.setColor(this.game.getColor() == Color.BLACK ? Color.WHITE : Color.BLACK);

        graphics2D.fillOval(x-radius, y-radius, radius*2, radius*2);

        graphics2D.setColor(this.backgroundColor);

        repaint();

    }

    public void setTime(){
        this.minutes = this.game.getTime();
        this.seconds = 0;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(this.image, 0, 0, this);
    }

}
