package org.example.compulsory;

import org.example.homework.Edge;
import org.example.homework.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {

    private final MainFrame frame;
    int W = 800, H = 500, RADIUS_VERTEX = 10;

    private Game game;

    private BufferedImage image;
    private Graphics2D graphics2D;

    public DrawingPanel(MainFrame frame){
        this.frame = frame;
        this.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                W = getWidth();
                H = getHeight();
                if(H > W){
                    H = W;
                }
                createOffScreenImage();
                game.setWidthAndHeight(W,H);
                createBoard(game);
                initPanel();
            }
        });
        createOffScreenImage();
        createBoard();
        initPanel();
        setBackground(Color.gray);
    }

    public Graphics2D getGraphics2D() {
        return graphics2D;
    }

    public void setGraphics2D(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.createBoard(this.game);
    }

    private void initPanel(){
        setPreferredSize(new Dimension(W,H));
        setBorder(BorderFactory.createEtchedBorder());

        // Remove any existing mouse listeners -> if the window is resized, it will be added multiple times
        for (MouseListener listener : getMouseListeners()) {
            removeMouseListener(listener);
        }

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(game.getGameIsFinished()) {
                        Font font = new Font("Arial", Font.PLAIN, 24);
                        graphics2D.setFont(font);

                        if (game.getPlayerWhoWon() == 0) {
                            graphics2D.setColor(Color.ORANGE);
                            graphics2D.drawString("EQUALITY", 20, 20);
                            graphics2D.setColor(Color.GRAY);
                        } else if (game.getPlayerWhoWon() == 2) {
                            graphics2D.setColor(Game.colorPlayer2);
                            graphics2D.drawString("Player 2 WON ", 20, 20);
                            graphics2D.setColor(Color.GRAY);
                        } else {
                            graphics2D.setColor(Game.colorPlayer1);
                            graphics2D.drawString("Player 1 WON", 20, 20);
                            graphics2D.setColor(Color.GRAY);
                        }
                        repaint();
                    }
                    else {
                        game.playerClicked(e.getX(), e.getY(), graphics2D);
                    }
                    repaint();
                }
            });
    }

    private void createOffScreenImage(){

        this.image = new BufferedImage(W,H,BufferedImage.TYPE_INT_ARGB);
        this.graphics2D = image.createGraphics();


        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.GRAY);

        graphics2D.fillRect(0,0,W,H);
    }

    final void createBoard(){

        int nrVertices = (Integer) this.frame.getConfigPanel().getDotsSpinner().getValue();
        int probability = (Integer) this.frame.getConfigPanel().getProbabilityCombo().getSelectedItem();

        this.createOffScreenImage();

        this.createGame(nrVertices,probability);

        this.drawLines();
        this.drawVertices();

        repaint();

    }

    final void createBoard(Game game){

        this.createOffScreenImage();

        this.drawLines();
        this.drawVertices();

        repaint();
    }

    private void createGame(int nrVertices, int probability){

        this.game = new Game(nrVertices,probability,W,H);
    }

     void drawVertices(){
        this.graphics2D.setColor(Color.BLACK);

        for(int i = 0, n = this.game.getNrVertices(); i < n ; ++i){
            this.graphics2D.fillOval(this.game.getCoordinateX()[i] - RADIUS_VERTEX, this.game.getCoordinateY()[i] - RADIUS_VERTEX, 2 * RADIUS_VERTEX, 2 * RADIUS_VERTEX);
            this.graphics2D.setColor(Color.MAGENTA);
            this.graphics2D.drawString(String.valueOf(i),this.game.getCoordinateX()[i] - RADIUS_VERTEX/2,this.game.getCoordinateY()[i] + RADIUS_VERTEX/2);
            this.graphics2D.setColor(Color.BLACK);

        }
        this.graphics2D.setColor(Color.GRAY);
        repaint();

    }

     void drawLines(){
        this.graphics2D.setColor(Color.BLACK);

        boolean [] visited = new boolean[this.game.getNrVertices()];

        for(int i=0 , n = this.game.getNrVertices() ; i< n ; ++i){
            for( Edge edge : this.game.getAdjacencyList().get(i)){
                if (!visited[edge.getNode2()]){
                    if(edge.isColored()){
                        this.graphics2D.setColor(edge.getColor());
                        this.graphics2D.drawLine(  this.game.getCoordinateX()[i],this.game.getCoordinateY()[i],
                                this.game.getCoordinateX()[edge.getNode2()],this.game.getCoordinateY()[edge.getNode2()]);
                        this.graphics2D.setColor(Color.BLACK);
                    }
                    else {
                        this.graphics2D.drawLine(this.game.getCoordinateX()[i], this.game.getCoordinateY()[i],
                                this.game.getCoordinateX()[edge.getNode2()], this.game.getCoordinateY()[edge.getNode2()]);
                    }
                }
            }
            visited[i] = true;
        }

        this.graphics2D.setColor(Color.GRAY);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(this.image,0,0,this);
    }

    //    private MainFrame frame;
//    final static int W = 900 , H = 500;
//
//    private boolean shouldRepaint = false;
//
//    public void setFrame(MainFrame frame) {
//        this.frame = frame;
//    }
//
//    public void setShouldRepaint( boolean shouldRepaint){
//        this.shouldRepaint = shouldRepaint;
//    }
//
//    public DrawingPanel(MainFrame frame){
//        this.frame = frame;
//        initPanel();
//    }
//    private void initPanel(){
//        setPreferredSize(new Dimension(W,H));
//        setBorder(BorderFactory.createEtchedBorder());
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        if ( this.shouldRepaint) {
//            Graphics2D g2d = (Graphics2D) g;
//
//
//            // Set up the coordinate system
//            Color color = new Color(170, 0, 0, 0);
//
//            int radius = 200;
//            int centerX = getWidth() / 2;
//            int centerY = getHeight() / 2;
//
//            //g2d.fillOval(centerX, centerY, 15, 15);
//
//
//            // Draw the big circle
//            //g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
//
//            // Set the number of small circles
//            int n = (int) this.frame.getConfigPanel().getDotsSpinner().getValue();
//            System.out.println(" n este : " + n);
//
//            int probability = (int) this.frame.getConfigPanel().getProbabilityCombo().getSelectedItem();
//
//            System.out.println(" probability : " + probability);
//            // Draw the small circles
//            int[] coordX = new int[n];
//            int[] coordY = new int[n];
//            for (int i = 0; i < n; i++) {
//                double angle = 2 * Math.PI * i / n;
//                int x = (int) (centerX + radius * Math.cos(angle));
//                coordX[i] = x;
//                int y = (int) (centerY + radius * Math.sin(angle));
//                coordY[i] = y;
//                int r = 10;
//                g.fillOval(x - r, y - r, 2 * r, 2 * r);
//            }
//
//            for (int i = 0; i < n - 1; ++i) {
//                for (int j = i + 1; j < n; j++) {
//                    int randomProb = (int) (Math.random() * 100);
//                    if (randomProb <= probability) {//create edge
//                        g.drawLine(coordX[i], coordY[i], coordX[j], coordY[j]);
//                    }
//                }
//            }
//        }
//
//
//    }
}