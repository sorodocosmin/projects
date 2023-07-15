package org.example.compulsory;

import org.example.homework.TimeKeeper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Exploration {
    private final SharedMemory sharedMemory;
    private final ExplorationMap map;
    private final List<Robot> robots;
    private int nrRobots = 0;

    private Graphics2D graphics2D;

    private TimeKeeper timeKeeper;

    public Exploration(int n, Graphics2D graphics2D){
        this.sharedMemory = new SharedMemory(n);
        this.map = new ExplorationMap(n);
        this.robots = new ArrayList<>();
        this.timeKeeper = null;

        this.graphics2D = graphics2D;

    }

    public SharedMemory getSharedMemory(){
        return this.sharedMemory;
    }
    public ExplorationMap getMap(){
        return this.map;
    }

    public int getNrRobots() {
        return nrRobots;
    }

    public void addRobot(Robot robot){

        if(this.robots.indexOf(robot) != -1){//robot with the same name already exists
            System.out.println(" Robot with name " + robot.getName() + " already exists !");
            return;
        }

        robot.explore = this;
        this.robots.add(robot);
        this.nrRobots ++ ;
    }
    public void start(){
        if( this.robots.size() == 0){
            System.out.println("There is no robot added");
            return;
        }

        if(this.timeKeeper == null){//start the Daemon thread
            this.timeKeeper = new TimeKeeper(map,this.graphics2D);
            new Thread(this.timeKeeper).start();
        }

        for( Robot robot : this.robots){
            if(!robot.isRunning()) {//create a new thread for the robots which are not running
                robot.setRunning(true);
                new Thread(robot).start();
                System.out.println("Robot " + robot.getName() + " started");
            }
            else {//robot is running
                if( robot.isPause()){
                    robot.setPause(false);
                    System.out.println("Robot " + robot.getName() + " was paused and now resumed");
                }
            }
        }
    }
    public void start(String nameRobot){

        Robot robot = new Robot(nameRobot);
        int indexRobot = this.robots.indexOf(robot);

        if(indexRobot == -1) {//the robot doesn't exist
            System.out.println("Robot " + nameRobot + " doesn't exist");
            return;
        }

        if(this.robots.get(indexRobot).isRunning() && !this.robots.get(indexRobot).isPause()){//the robot is already running

            System.out.println("Robot " + nameRobot + " is already running and was not stopped");

        }
        else if (this.robots.get(indexRobot).isRunning() && this.robots.get(indexRobot).isPause()){//the robot is paused

            if(this.timeKeeper == null){//start the Daemon thread
                this.timeKeeper = new TimeKeeper(map,this.graphics2D);
                new Thread(this.timeKeeper).start();
            }

            this.robots.get(indexRobot).setPause(false);
            System.out.println("Robot " + nameRobot + " was paused and now resumed");

        }
        else{//the robot is not running

            if(this.timeKeeper == null){//start the Daemon thread
                this.timeKeeper = new TimeKeeper(map,this.graphics2D);
                new Thread(this.timeKeeper).start();
            }

            this.robots.get(indexRobot).setRunning(true);
            System.out.println("Robot " + nameRobot + " started");
            new Thread(this.robots.get(indexRobot)).start();



        }

    }

    public void stop(){
        for( Robot robot : this.robots){
            robot.setPause(true);
            robot.setPauseTime(-1);//stopped for undefined time
        }

        System.out.println("All robots stopped");

    }

    public void stop(String nameRobot){

        Robot robot = new Robot(nameRobot);
        int indexRobot = this.robots.indexOf(robot);

        if( this.robots.indexOf(robot) == -1) {//the robot doesn't exist
            System.out.println("Robot " + nameRobot + " doesn't exist");
            return;
        }

        this.robots.get(indexRobot).setPause(true);
        this.robots.get(indexRobot).setPauseTime(-1);//stopped for undefined time
        System.out.println("Robot " + nameRobot + " stopped");
    }

    public void stop(String nameRobot, int milliseconds){

        Robot robot = new Robot(nameRobot);
        int indexRobot = this.robots.indexOf(robot);

        if( indexRobot == -1) {//the robot doesn't exist
            System.out.println("Robot " + nameRobot + " doesn't exist");
            return;
        }

        this.robots.get(indexRobot).setPause(true);
        this.robots.get(indexRobot).setPauseTime(milliseconds);
        System.out.println("Robot " + nameRobot + " stopped for " + milliseconds + " milliseconds");

    }

}
