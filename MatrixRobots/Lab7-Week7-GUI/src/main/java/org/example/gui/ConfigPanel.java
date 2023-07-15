package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConfigPanel extends JPanel {
    private final MainFrame frame;
    private JLabel dotsLabel, nrRobotsLabel;
    private JSpinner dotsSpinner;
    private JSpinner nrRobotsSpinner;

    public JSpinner getDotsSpinner() {
        return dotsSpinner;
    }

    public JSpinner getNrRobotsSpinner() {
        return nrRobotsSpinner;
    }

    private JButton createButton;
    public ConfigPanel(MainFrame frame){
        this.frame = frame;
        init();
    }

    private void init(){

        setLayout(new FlowLayout());

        //create the label and the spineer
        dotsLabel = new JLabel("Size of the matrix:");
        dotsSpinner = new JSpinner(new SpinnerNumberModel(10,1,100,1));

        //probabilitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);


        nrRobotsLabel = new JLabel("Number of robots");
        nrRobotsSpinner = new JSpinner(new SpinnerNumberModel(2,1,100,1));


        this.createButton = new JButton("Start Exploration");
        createButton.setBackground(new Color(65, 100, 74));
        createButton.addActionListener(this::onGenerateGraph);

        add(dotsLabel);
        add(dotsSpinner);

        add(nrRobotsLabel);
        add(nrRobotsSpinner);

        add(createButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(createButton, gbc);

    }
    private void onGenerateGraph(ActionEvent e){
        //this.frame.getCanvas().setShouldRepaint(true);
        this.frame.getCanvas().createBoard();
        this.frame.getCanvas().getExploration().start();
        //this.frame.getCanvas().setShouldRepaint(false);
    }

}
