package org.example.compulsory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConfigPanel extends JPanel {
    private final MainFrame frame;
    private JLabel dotsLabel, probabilityLabel;
    private JSpinner dotsSpinner;
    private JComboBox<Integer> probabilityCombo;

    public JSpinner getDotsSpinner() {
        return dotsSpinner;
    }

    public JComboBox<Integer> getProbabilityCombo() {
        return probabilityCombo;
    }

    private JButton createButton;
    public ConfigPanel(MainFrame frame){
        this.frame = frame;
        init();
    }

    private void init(){

        setLayout(new FlowLayout());

        //create the label and the spineer
        dotsLabel = new JLabel("Number of dots:");
        dotsSpinner = new JSpinner(new SpinnerNumberModel(6,3,100,1));

        probabilityLabel = new JLabel("Probability for existing an edge between 2 nodes (? %) : ");
        //probabilitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);


        probabilityCombo = new JComboBox<>(new Integer[] {25,50,75,100});
        probabilityCombo.setSelectedItem(100);


        this.createButton = new JButton("Create Graph");
        createButton.setBackground(new Color(65, 100, 74));
        createButton.addActionListener(this::onGenerateGraph);

        add(dotsLabel);
        add(dotsSpinner);

        add(probabilityLabel);
        add(probabilityCombo);

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
        //this.frame.getCanvas().setShouldRepaint(false);
    }

}
