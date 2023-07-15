package org.example.homework;

import org.example.compulsory.ExplorationMap;

import java.awt.*;

public class TimeKeeper extends Thread {
    private long startTime;
    private boolean timeExpired;
    private long timeLimit;
    private boolean threadStarted = false;

    private ExplorationMap explorationMap;

    private Graphics2D graphics2D;

    public TimeKeeper(ExplorationMap explorationMap, Graphics2D graphics2D){

        this.setDaemon(true);

        this.explorationMap = explorationMap;

        this.timeExpired = false;
        this.timeLimit = 60_000;//1 minute

        this.graphics2D = graphics2D;

    }

    public boolean isThreadStarted() {
        return this.threadStarted;
    }

    @Override
    public void run(){
        this.threadStarted = true;
        this.startTime = System.currentTimeMillis();

        while(!this.timeExpired){
            double timeSinceStarted = System.currentTimeMillis() - this.startTime;
            try {
                String timeRunning = "Time since started: ";
                String actualTime  = (timeSinceStarted/1000) + " s\n";

                sleep(400);

                if(timeSinceStarted >= this.timeLimit){
                    this.timeExpired = true;
                    timeRunning = "Time expired";
                }
                else if(this.explorationMap.finishedVisited()){
                    this.timeExpired = true;
                    timeRunning = "Exploration finished";
                }

                synchronized (this.graphics2D){
                    this.graphics2D.setColor(Color.GRAY);
                    this.graphics2D.fillRect(5,10,100,30);//clear the previous text

                    this.graphics2D.setColor(Color.BLACK);
                    this.graphics2D.drawString(timeRunning, 5, 20);
                    if(!timeExpired){
                        this.graphics2D.drawString(actualTime, 5, 40);
                    }
                    this.graphics2D.setColor(Color.GRAY);

                }

            }
            catch (InterruptedException e) {
                System.out.println("Error at sleeping");
            }

        }
    }

}
