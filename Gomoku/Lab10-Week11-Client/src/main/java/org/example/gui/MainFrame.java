package org.example.gui;

import org.example.ClientGame;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ControlPanel controlPanel;
    private DrawingPanel canvas;
    private ClientGame clientGame;

    public MainFrame(ClientGame clientGame){
        super("My Game Application");
        this.clientGame = clientGame;
        init();
    }

    private void init(){

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.controlPanel = new ControlPanel(this);
        add(controlPanel,BorderLayout.SOUTH);

        canvas = new DrawingPanel(this);
        add(canvas,BorderLayout.CENTER);

        pack();
    }


    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public DrawingPanel getCanvas() {
        return canvas;
    }

    public void setCanvas(DrawingPanel canvas) {
        this.canvas = canvas;
    }

    public ClientGame getClientGame() {
        return clientGame;
    }

    public void setClientGame(ClientGame clientGame) {
        this.clientGame = clientGame;
    }
}
