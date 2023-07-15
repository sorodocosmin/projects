package org.example;

public class Main {
    public static void main(String[] args) {
        try{
            ClientGame clientGame = new ClientGame(4444,"127.0.0.1");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}