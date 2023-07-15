package org.example.game;

import java.awt.*;
import java.io.PrintWriter;

public class Player {
    private String name;
    private Color color;
    private int time;

    private PrintWriter writeToClient;


    public Player(String name, int time, PrintWriter writeToClient) {
        this.name = name;
        int nr = (int) (Math.random() * 2);

        if (nr == 0) {
            this.color = Color.BLACK;
        } else {
            this.color = Color.WHITE;
        }

        this.time = time;
        this.writeToClient = writeToClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public PrintWriter getWriteToClient() {
        return writeToClient;
    }
}
