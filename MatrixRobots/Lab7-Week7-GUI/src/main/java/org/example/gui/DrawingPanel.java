package org.example.gui;


import org.example.compulsory.Exploration;
import org.example.compulsory.Robot;
import org.example.homework.PointCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class DrawingPanel extends JPanel {

    private final MainFrame frame;
    int W = 800, H = 500;

    private Exploration exploration;

    private BufferedImage image;
    private Graphics2D graphics2D;

    private List<PointCell> pointsVisited;

    public DrawingPanel(MainFrame frame) {
        this.pointsVisited = new CopyOnWriteArrayList<>();

        this.frame = frame;

        this.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(pointsVisited.size() == 0) {
                    W = getWidth();
                    H = getHeight();
                    if (H > W) {
                        H = W;
                    }
                    createOffScreenImage();
                    drawOutlineSquares(exploration.getMap().getSizeMatrix());
                    initPanel();
                }
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

    public Exploration getExploration() {
        return exploration;
    }

    public void setExploration(Exploration exploration) {
        this.exploration = exploration;
    }

    private void initPanel() {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());

    }

    private void createOffScreenImage() {

        this.image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        this.graphics2D = image.createGraphics();


        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.GRAY);

        graphics2D.fillRect(0, 0, W, H);
    }

    final void createBoard() {

        int lengthMatrix = (Integer) this.frame.getConfigPanel().getDotsSpinner().getValue();
        int nrRobots = (Integer) this.frame.getConfigPanel().getNrRobotsSpinner().getValue();

        this.createOffScreenImage();

        this.createExploration(lengthMatrix, nrRobots, this.graphics2D);

        this.drawOutlineSquares(lengthMatrix);


        repaint();

    }


    private void createExploration(int sizeMatrix, int nrRobots, Graphics2D graphics2D) {

        this.exploration = new Exploration(sizeMatrix, graphics2D);
        for (int i = 1; i <= nrRobots; ++i) {
            Random random = new Random();
            this.exploration.addRobot(new Robot("" + i, new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)),this));
        }

    }


    public void drawOutlineSquares(int lengthMatrix) {

        this.graphics2D.setColor(Color.BLACK);

        int squareSize = Math.min(W, H) / lengthMatrix; // calculate the size of each square
        int startX = (W - squareSize * lengthMatrix) / 2; // calculate the starting X coordinate for drawing
        int startY = (H - squareSize * lengthMatrix) / 2; // calculate the starting Y coordinate for drawing

        for (int i = 0; i < lengthMatrix; i++) { // loop through the rows
            for (int j = 0; j < lengthMatrix; j++) { // loop through the columns
                int x = startX + j * squareSize; // calculate the X coordinate for drawing
                int y = startY + i * squareSize; // calculate the Y coordinate for drawing
                this.graphics2D.drawRect(x, y, squareSize, squareSize); // draw the square
            }
        }

        this.graphics2D.setColor(Color.GRAY);

        repaint();

    }

    public void drawSquare(int row, int col, Color color, String nameRobot, boolean isEntryPoint) {


        int squareSize = Math.min(W, H) / this.exploration.getMap().getSizeMatrix(); // calculate the size of each square
        int startX = (W - squareSize * this.exploration.getMap().getSizeMatrix()) / 2; // calculate the starting X coordinate for drawing
        int startY = (H - squareSize * this.exploration.getMap().getSizeMatrix()) / 2; // calculate the starting Y coordinate for drawing

        int x = startX + col * squareSize; // calculate the X coordinate for drawing
        int y = startY + row * squareSize; // calculate the Y coordinate for drawing

        int centerX = x + squareSize / 2; // calculate center X coordinate
        int centerY = y + squareSize / 2; // calculate center Y coordinate

        synchronized (this.graphics2D) {
            this.graphics2D.setColor(color);
            this.graphics2D.fillRect(x, y, squareSize, squareSize); // draw the square

            this.graphics2D.setColor(Color.BLACK);

            PointCell point = new PointCell(row, col, color, nameRobot);

            if(isEntryPoint){
                this.graphics2D.setColor(Color.RED);
                point.setEntryPoint(true);
            }

            this.pointsVisited.add(point);

            graphics2D.drawString(nameRobot, centerX, centerY);//draw the name of the robot

            if(isEntryPoint){
                this.graphics2D.setColor(Color.BLACK);
            }

            this.graphics2D.drawRect(x, y, squareSize, squareSize);

            this.graphics2D.setColor(Color.GRAY);
            repaint();
        }

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