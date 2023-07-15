package org.example.gui;

import org.example.commands.SendCommandClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ControlPanel extends JPanel {

    private final MainFrame frame;
    private final JButton exitButton = new JButton("Exit ❌ ");
    private final JButton stopServerButton = new JButton("Stop Server and Exit");
    private final JButton createNewGameButton = new JButton("Create a Game");
    private final JButton joinExistingGameButton = new JButton("Join a Game");

    public ControlPanel(MainFrame frame){
        this.frame = frame;
        init();
    }

    private void init(){
        setLayout(new GridLayout(3,2));
        //setLayout(new FlowLayout());

        this.exitButton.setBackground(new Color(255, 109, 96));

        add(this.createNewGameButton);
        add(this.joinExistingGameButton);
        add(this.stopServerButton);
        add(this.exitButton);


        createNewGameButton.addActionListener(this::createNewGameButton);

        joinExistingGameButton.addActionListener(this::joinExistingGameButton);

        stopServerButton.addActionListener(this::stopServerButton);

        exitButton.addActionListener(this::exitGame);
    }

    private void stopServerButton(ActionEvent actionEvent) {

        this.frame.getClientGame().sendRequest(SendCommandClient.getExitServer());

    }

    private void joinExistingGameButton(ActionEvent actionEvent) {
        this.frame.getClientGame().sendRequest(SendCommandClient.getJoinGame());
    }

    private void createNewGameButton(ActionEvent actionEvent) {
        this.frame.getClientGame().sendRequest(SendCommandClient.getCreateGame());
    }

    private void exitGame(ActionEvent e){
        this.frame.getClientGame().sendRequest(SendCommandClient.getExitApp());
    }


}
