package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            ServerGame serverGame = new ServerGame(4444);
        } catch (IOException e) {
            System.out.println("Error at creating server");
        } catch (InterruptedException e) {
            System.out.println("Error at waiting threads to finish");
        }

    }
}