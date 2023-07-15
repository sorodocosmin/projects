package org.example.homework;

import org.example.bonus.AlgorithmDetColorNextEdge;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements Serializable {
    public static final Color colorPlayer1 = Color.RED, colorPlayer2 = Color.BLUE;
    private int[] coordinateX, coordinateY ; //the coordinates for each node
    private boolean isPlayer1Turn = true, gameIsFinished = false;
    private Map<Integer, List<Edge>> adjacencyList;//every for every node, it will correspond a list of edges because it will be easier to check if one edge is colored
    private int nrVertices;
    private int nrEdges;
    private int nrEdgesColored;
    private int playerWhoWon = 0;//by default it's equality
    private boolean playWithAI = false;
    private int mode = 1;//easy
    private int width, height;

    //@JSONignore
    private Edge lastEdgeColored;
    public Game(){

    }
    public Game(int nrVertices, int probability, int width, int height){
        this.nrEdges = 0;
        this.nrEdgesColored = 0;

        this.nrVertices = nrVertices;

        this.coordinateX = new int[nrVertices];
        this.coordinateY = new int[nrVertices];

        this.adjacencyList = new HashMap<>();
        for(int i=0;i<nrVertices;++i){
            this.adjacencyList.put(i, new ArrayList<>());
        }

        this.createVertices(width, height);
        this.createEdges(probability);


    }

    public int[] getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int[] coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int[] getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int[] coordinateY) {
        this.coordinateY = coordinateY;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        isPlayer1Turn = player1Turn;
    }

    public Map<Integer, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(Map<Integer, List<Edge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public int getNrVertices() {
        return nrVertices;
    }

    public void setNrVertices(int nrVertices) {
        this.nrVertices = nrVertices;
    }

    public boolean getGameIsFinished() {
        return gameIsFinished;
    }

    public void setGameIsFinished(boolean gameIsFinished) {
        this.gameIsFinished = gameIsFinished;
    }

    public void setNrEdges(int nrEdges) {
        this.nrEdges = nrEdges;
    }

    public void setNrEdgesColored(int nrEdgesColored) {
        this.nrEdgesColored = nrEdgesColored;
    }

    public void setPlayerWhoWon(int playerWhoWon) {
        this.playerWhoWon = playerWhoWon;
    }

    public void setPlayWithAI(boolean playWithAI) {
        this.playWithAI = playWithAI;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setLastEdgeColored(Edge lastEdgeColored) {
        this.lastEdgeColored = lastEdgeColored;
    }

    public int getPlayerWhoWon() {
        return playerWhoWon;
    }

    public int getNrEdges() {
        return nrEdges;
    }

    public int getNrEdgesColored() {
        return nrEdgesColored;
    }

    public boolean isPlayWithAI() {
        return playWithAI;
    }

    public int getWith() {
        return width;
    }

    public void setWith(int with) {
        this.width = with;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMode() {
        return mode;
    }

    public Edge getLastEdgeColored() {
        return lastEdgeColored;
    }

    public void setWidthAndHeight(int width, int height){
        this.height = height;
        this.width = width;
        this.createVertices(this.width, this.height);
    }

    private void createVertices(int width, int height){
        int x0 = width/2 ; int y0 = height/2;
        int radius = height/2 - 10;

        double alpha = 2 * Math.PI / nrVertices; //the angle

        for( int i = 0; i< this.nrVertices ; ++i) {
            this.coordinateX[i] = x0 + (int) (radius * Math.cos(alpha * i));
            this.coordinateY[i] = y0 + (int) (radius * Math.sin(alpha * i));
        }
    }

    private void createEdges(int probability ){

        for (int i = 0; i < this.nrVertices - 1; ++i) {
            for (int j = i + 1; j < this.nrVertices; j++) {
                int randomProb = (int) (Math.random() * 100);
                if (randomProb <= probability) {//create edge
                    Edge edge1 = new Edge(i,j);
                    Edge edge2 = new Edge(j,i);
                    this.adjacencyList.get(i).add(edge1);//for the arrayList of node i, we add the node j as well;
                    this.getAdjacencyList().get(j).add(edge2);
                    this.nrEdges++;
                }
            }
        }

    }


    public void playerClicked(int mouseX, int mouseY, Graphics2D graphics){
        //check if there was the click was made on an edge
        boolean ok = false;
        for( int i = 0 ; i < this.nrVertices && !ok; ++i){
            for(Edge edge : this.adjacencyList.get(i)){
                if(!edge.isColored()){
                    if(isPointOnLineSegment(this.coordinateX[i],this.coordinateY[i],this.coordinateX[edge.getNode2()],this.coordinateY[edge.getNode2()],mouseX,mouseY)){
                        ok = true;//if an edge was selected, we color it with the respective color
                        edge.setColored(true);//the edge ij and also edge ji
                        this.nrEdgesColored ++;

                        if(nrEdgesColored == this.nrEdges){
                            this.gameIsFinished = true;
                        }

                        this.setColoredReversedEdge(edge);

                        if(this.isPlayer1Turn){//set the specific color for edges
                            graphics.setColor(colorPlayer1);
                            edge.setColor(colorPlayer1);
                        }
                        else{
                            graphics.setColor(colorPlayer2);
                            edge.setColor(colorPlayer2);
                        }

                        this.switchPlayer();

                        this.lastEdgeColored = edge;
                        graphics.drawLine(this.coordinateX[i],this.coordinateY[i],this.coordinateX[edge.getNode2()],this.coordinateY[edge.getNode2()]);
                        graphics.setColor(Color.GRAY);
                        if(this.formedATriangle()) {
                            this.gameIsFinished = true;
                            this.playerWhoWon = this.isPlayer1Turn ? 2 : 1 ;
                        }

                        if(!this.gameIsFinished){
                            if(this.playWithAI){
                                this.colorTheNextEdge(graphics);

                                if(this.formedATriangle()) {
                                    this.gameIsFinished = true;
                                    this.playerWhoWon = this.isPlayer1Turn ? 2 : 1 ;
                                }
                            }
                        }

                        break;
                    }
                }
            }
        }

    }

    private void setColoredReversedEdge(Edge edge){

        for( Edge edgeReverse : this.adjacencyList.get(edge.getNode2())){
            if(edgeReverse.getNode2() == edge.getNode1()){
                edgeReverse.setColored(true);
                if(this.isPlayer1Turn){//set the specific color for edges
                    edgeReverse.setColor(colorPlayer1);
                }
                else{
                    edgeReverse.setColor(colorPlayer2);
                }
                break;
            }
        }

    }

    private void colorTheNextEdge(Graphics2D graphics){

        AlgorithmDetColorNextEdge alg = new AlgorithmDetColorNextEdge(this.adjacencyList);

        if(this.isPlayer1Turn){
            alg.setColorPlayer(colorPlayer1);
        }
        else{
            alg.setColorPlayer(colorPlayer2);
        }
        if(this.mode==1){
            Edge edge = alg.getEdgeInEasyMode();
            this.colorSpecificEdge(edge,graphics);
            this.setColoredReversedEdge(edge);
        }
        else if( this.mode == 2){
            Edge edge = alg.getEdgeInMediumMode();
            this.colorSpecificEdge(edge,graphics);
            this.setColoredReversedEdge(edge);
        }

        this.switchPlayer();

    }

    private void colorSpecificEdge(Edge edge, Graphics2D graphics) {

        boolean ok = false;

        for (int i = 0; i < this.nrVertices && !ok; ++i) {
            for (Edge e : this.adjacencyList.get(i)) {
                if (edge.getNode1() == e.getNode1() && edge.getNode2() == e.getNode2()) {
                    e.setColored(true);
                    if (this.isPlayer1Turn) {
                        e.setColor(colorPlayer1);
                        graphics.setColor(colorPlayer1);
                    }
                    else{
                        e.setColor(colorPlayer2);
                        graphics.setColor(colorPlayer2);
                    }

                    this.lastEdgeColored = e;

                    graphics.drawLine(this.coordinateX[i],this.coordinateY[i],this.coordinateX[e.getNode2()],this.coordinateY[e.getNode2()]);
                    graphics.setColor(Color.GRAY);

                    System.out.println("The AI colored the edge " + e.getNode1() + " " + e.getNode2());

                    break;
                }
            }
        }
    }

    private void switchPlayer(){

        if(this.isPlayer1Turn){
            this.isPlayer1Turn=false;
        }
        else{
            this.isPlayer1Turn=true;
        }
    }

    boolean isPointOnLineSegment(int x1, int y1, int x2, int y2, int a1, int a2) {
        //double slope = (double) (this.coordinateY[j] - this.coordinateY[i])/(this.coordinateX[j]-this.coordinateX[i]);// m = (y2-y1) / (x2-x1)
        double slope = (double) (y2 - y1) / (x2 - x1);


        double yOnOx = y1 - slope * x1;// y = mx +b;

        // Calculate the y-coordinate of the point A on the line passing through X and Y
        double expectedA2 = slope * a1 + yOnOx;

        // Check if the y-coordinate of point A is equal to a2
        if (Math.abs(expectedA2 - a2) > 10) {
            return false;
        }

        // Check if the x-coordinate of point A is between x1 and x2
        if ((a1 >= x1 && a1 <= x2) || (a1 <= x1 && a1 >= x2)) {
            return true;
        }

        return false;
    }

    boolean formedATriangle(){//the only possibility for a triangle to be formed needs to contain the last colored edge
        //we verify if there aren't a colored edge which goes into the same node (from lastColoredNode.node2() and lastColoredNode.node1())
        //lastEdgeColored will never be null
        int k = 0;

        int []neighbours1Colored = new int[ this.adjacencyList.get(this.lastEdgeColored.getNode1()).size()];
        for(Edge e : this.adjacencyList.get(this.lastEdgeColored.getNode1())){
            if(e.isColored()) {
                if( e.getColor().equals(lastEdgeColored.getColor())) {
                    neighbours1Colored[k] = e.getNode2();
                    k++;
                }
            }
        }
        for( Edge e : this.adjacencyList.get(this.lastEdgeColored.getNode2())){
            if(e.isColored()){
                if(e.getColor().equals(lastEdgeColored.getColor())){//verify if those nodes don't have a common neighbour
                    for( int i=0; i< k ; ++i ){
                        if(neighbours1Colored[i] == e.getNode2()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void resetGame(){
        this.nrEdgesColored = 0;

        this.gameIsFinished = false;
        for( int i=0 ; i< this.nrVertices ; ++i){
            for( Edge e : this.adjacencyList.get(i)){
                e.setColored(false);
            }
        }
    }

    //no usage!
    private boolean isOnLine(int x1,int y1, int x2,int y2,int x3,int y3){//it doesn't work, why ?
        //equation : (y-y1) / (y2-y1) == (x-x1) / (x2-x1) --> y(x2 - x1) - y1(x2 - x1) == x (y2-y1) - x1(y2-y1) --> y = ( x(y2-y1) - x1(y2-y1) + y1(x2-x1) ) / (x2 - x1)
        double expectedY1 = (double) (x1*(y3-y2) - x2*(y2-y1) + y2*(x3-x2) ) / (x3-x2);
        System.out.println("Check if y=" + y1 + " = expect = " + expectedY1);
        if(Math.abs(y1-expectedY1) < 1){
            return true;
        }

        return false;
    }

}
