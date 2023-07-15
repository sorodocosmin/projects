package org.example.gui;



import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ConfigPanel configPanel;
    private ControlPanel controlPanel;
    private DrawingPanel canvas;

    public ConfigPanel getConfigPanel() {
        return configPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public DrawingPanel getCanvas() {
        return canvas;
    }

    public MainFrame(){
        super("My Drawing Application");
        init();
    }

    private void init(){

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.configPanel = new ConfigPanel(this);
        add(configPanel, BorderLayout.NORTH);

        this.controlPanel = new ControlPanel(this);
        getContentPane().add(controlPanel,BorderLayout.SOUTH);


        canvas = new DrawingPanel(this);
        add(canvas,BorderLayout.CENTER);

        pack();

    }
}
