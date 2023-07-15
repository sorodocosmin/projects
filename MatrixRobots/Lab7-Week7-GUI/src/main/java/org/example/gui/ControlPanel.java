package org.example.gui;

import org.example.compulsory.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class ControlPanel extends JPanel {
    private final MainFrame frame;
    private final JButton exitButton = new JButton("Exit ❌ ");
    private final JButton stopAllRobotsButton = new JButton("Stop All Robots");

    private final JLabel stopSpecificRobotLabel = new JLabel("Stop a specific Robot: ");
    private final JSpinner stopSpecificRobotSpinner = new JSpinner(new SpinnerNumberModel(1,1,100,1));

    private final JButton newRobotButton = new JButton("Add a new Robot");

    private final JLabel startSpecificRobotLabel = new JLabel("Start specific robot ( \uD83E\uDD16 )");
    private final JSpinner startSpecificRobotSpinner = new JSpinner(new SpinnerNumberModel(1,1,100,1));


    public ControlPanel(MainFrame frame){
        this.frame = frame;
        init();
    }

    private void init(){
        setLayout(new GridLayout(3,2));
        //setLayout(new FlowLayout());

        this.exitButton.setBackground(new Color(255, 109, 96));

        JButton startSpecificRobot = new JButton();
        startSpecificRobot.setLayout(new GridLayout(1,2));
        startSpecificRobot.add(startSpecificRobotLabel);
        startSpecificRobot.add(startSpecificRobotSpinner);


        JButton stopSpecificRobot = new JButton();
        stopSpecificRobot.setLayout(new GridLayout(1,2));
        stopSpecificRobot.add(stopSpecificRobotLabel);
        stopSpecificRobot.add(stopSpecificRobotSpinner);


        add(this.stopAllRobotsButton);
        add(stopSpecificRobot);

        add(this.newRobotButton);

        add(startSpecificRobot);

        add(this.exitButton);


        stopAllRobotsButton.addActionListener(this::stopAllRobots);

        stopSpecificRobot.addActionListener(this::stopSpecificRobot);

        newRobotButton.addActionListener(this::addNewRobot);

        startSpecificRobot.addActionListener(this::startSpecificRobot);

//        playAI.addActionListener(this::playWithAI);

        exitButton.addActionListener(this::exitGame);
    }

    private void startSpecificRobot(ActionEvent actionEvent) {
        this.frame.getCanvas().getExploration().start(""+this.startSpecificRobotSpinner.getValue());
    }

    private void addNewRobot(ActionEvent actionEvent) {
        int nrRobots = this.frame.getCanvas().getExploration().getNrRobots() + 1;
        Random random = new Random();

        Robot newRobot = new Robot(""+nrRobots,
                new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)),
                this.frame.getCanvas());

        this.frame.getCanvas().getExploration().addRobot(newRobot);
        this.frame.getCanvas().getExploration().start(newRobot.getName());
    }

    private void stopSpecificRobot(ActionEvent actionEvent) {
        this.frame.getCanvas().getExploration().stop(""+this.stopSpecificRobotSpinner.getValue());
    }

    private void stopAllRobots(ActionEvent actionEvent) {
        this.frame.getCanvas().getExploration().stop();
    }


    private void exitGame(ActionEvent e){
        frame.dispose();
    }



}
