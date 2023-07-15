package org.example.compulsory;

import org.example.homework.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class ControlPanel extends JPanel {
    private final MainFrame frame;
    private JButton exitButton = new JButton("Exit ❌ ");
    private JButton saveButton = new JButton("Save Game");
    private JButton savePngButton = new JButton("Save Game As Image");
    private JButton loadButton = new JButton("Load Previous Saved Game");
    private JButton resetButton = new JButton("Reset \uD83D\uDD04");
    private JComboBox<String> dificultyLevelComboBox = new JComboBox<String>(new String[] {"Easy", "Medium"});

    private JLabel playWithAiLabel = new JLabel("Play with AI? ( \uD83E\uDD16 )");

    public ControlPanel(MainFrame frame){
        this.frame = frame;
        init();
    }

    private void init(){
        setLayout(new GridLayout(3,2));
        //setLayout(new FlowLayout());

        this.exitButton.setBackground(new Color(255, 109, 96));

        JButton playAI = new JButton();
        playAI.setLayout(new GridLayout(1,2));
        playAI.add(playWithAiLabel);
        playAI.add(dificultyLevelComboBox);

        this.dificultyLevelComboBox.setSelectedItem("Easy");

        add(this.savePngButton);
        add(this.saveButton);
        add(this.loadButton);
        add(this.resetButton);
        add(playAI);
        add(this.exitButton);


        savePngButton.addActionListener(this::saveAsPngGame);

        saveButton.addActionListener(this::serializeGame);

        loadButton.addActionListener(this::deserializeGame);

        resetButton.addActionListener(this::resetGame);

        playAI.addActionListener(this::playWithAI);

        exitButton.addActionListener(this::exitGame);
    }

    private void playWithAI( ActionEvent e){
        // Create a new combo box for selecting difficulty level
        this.frame.getCanvas().getGame().setPlayWithAI(true);
        if(this.dificultyLevelComboBox.getSelectedItem().equals("Easy")){
            System.out.println("Selected difficulty : " + this.dificultyLevelComboBox.getSelectedItem());
            this.frame.getCanvas().getGame().setMode(1);
        }

        else if (this.dificultyLevelComboBox.getSelectedItem().equals("Medium")) {
            System.out.println("Selected difficulty : " + this.dificultyLevelComboBox.getSelectedItem());
            this.frame.getCanvas().getGame().setMode(2);//medium
        }

    }

    private void exitGame(ActionEvent e){
        frame.dispose();
    }

    private void saveAsPngGame (ActionEvent e){
        try {
            ImageIO.write(this.frame.getCanvas().getImage(), "png", new File("graph.png"));
            System.out.println("The image saved successfully");
        }
        catch (IOException ex) {
            System.out.println("The image couldn't save );");
        }
    }
    private void resetGame(ActionEvent e){
        this.frame.getCanvas().getGame().resetGame();
        this.frame.getCanvas().setGame(this.frame.getCanvas().getGame());
    }

    private void serializeGame(ActionEvent e) {
        //we'll use for serialization XML format
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("game.xml")));
            encoder.writeObject(this.frame.getCanvas().getGame());
            encoder.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("Couldn't serialize object");
        }

    }
    private void deserializeGame(ActionEvent e){
        try{
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("game.xml")));
            Game game = (Game) decoder.readObject();
            this.frame.getCanvas().setGame(game);
        }
        catch (FileNotFoundException ex){
            System.out.println("Couldn't deserialize object");
        }
    }
}
