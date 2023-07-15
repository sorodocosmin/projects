package org.example.bonus;

import org.example.homework.Edge;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlgorithmDetColorNextEdge {
    private Map<Integer, List<Edge>> adjacencyList;//every for every node, it will correspond a list of edges because it will be easier to check if one edge is colored
    private Color colorPlayer;
    public AlgorithmDetColorNextEdge(Map<Integer,List<Edge>> adjacencyList){
        this.adjacencyList = adjacencyList;
    }

    public void setColorPlayer(Color colorPlayer) {
        this.colorPlayer = colorPlayer;
    }

    /**
     * take a random node and a random edge from the list of edges of that node and colors it
     * @return a random chosen edge
     */
    public Edge getEdgeInEasyMode(){

        int nrVertices = this.adjacencyList.size();
        boolean colored = false;
        int nr = 0;

        while(!colored){

            int randomNode = (int) (Math.random()*nrVertices);
            int nrNeighbours = this.adjacencyList.get(randomNode).size();

            int randomNeighbour = (int) (Math.random()*nrNeighbours);
            Edge edge = this.adjacencyList.get(randomNode).get(randomNeighbour);

            if( !edge.isColored() ){
                colored = true;
                return new Edge(edge.getNode1(), edge.getNode2());
            }

        }

        return null;//it will never return null;

    }

    /**
     * check if there is an edge that can be colored by the AI such that in forms a triangle with two other edges that are already colored
     * if not, check if there are two edges colored by the other player such that the AI can color the third edge and stops the other player from winning
     * @return an edge that can lead the AI to win
     */
    public Edge getEdgeInMediumMode(){

        boolean otherPlayerCanWin = false;
        Edge edgeThatCanLeadPlayerToWin = null;


        for(List<Edge> edges : this.adjacencyList.values()){
            for( Edge e : edges){
                if(!e.isColored()){
                    int edgeCanLeadToWin = this.checkIfEdgeCanLeadToWin(e);
                    if(edgeCanLeadToWin == 1){
                        return new Edge(e.getNode1(), e.getNode2());
                    }
                    else if ( !otherPlayerCanWin && edgeCanLeadToWin == -1) {
                        otherPlayerCanWin = true;
                        edgeThatCanLeadPlayerToWin = new Edge(e.getNode1(), e.getNode2());
                    }
                }
            }
        }

        //if the other player can win, then the AI will try to stop him
        if(otherPlayerCanWin){
            return edgeThatCanLeadPlayerToWin;
        }

        //if neither the player nor the AI can Win, the AI will color a random edge
        return this.getEdgeInEasyMode();

    }

    /**
     *
     * @param Edge e
     * @return -1 if this edge leads to a win for the other player, 1 if leads AI to a win, 0 otherwise
     */
    private int checkIfEdgeCanLeadToWin(Edge edge) {

        int node1 = edge.getNode1();
        int node2 = edge.getNode2();

        int k1 = 0, k2 = 0;

        int []neighboursWithTheSameColor = new int[ this.adjacencyList.get(edge.getNode1()).size()];
        int []neighboursWithDifferentColor = new int[ this.adjacencyList.get(edge.getNode1()).size()];

        for(Edge e : this.adjacencyList.get(edge.getNode1())){
            if(e.isColored()) {
                if( e.getColor().equals(this.colorPlayer)) {
                    neighboursWithTheSameColor[k1] = e.getNode2();
                    k1++;
                }
                else{
                    neighboursWithDifferentColor[k2] = e.getNode2();
                    k2++;
                }
            }
        }

        for( Edge e : this.adjacencyList.get(edge.getNode2())){
            if(e.isColored()){
                if(e.getColor().equals(this.colorPlayer)){//verify if those nodes don't have a common neighbour
                    for( int i=0; i< k1 ; ++i ){
                        if(neighboursWithTheSameColor[i] == e.getNode2()){
                            System.out.println("Edge for winning");
                            return 1;//found an edge for which the AI wins
                        }
                    }
                }
            }
        }

        for( Edge e : this.adjacencyList.get(edge.getNode2())){
            if(e.isColored()){
                if(!e.getColor().equals(this.colorPlayer)){//verify if those nodes don't have a common neighbour
                    for( int i=0; i< k2 ; ++i ){
                        if(neighboursWithDifferentColor[i] == e.getNode2()){
                            System.out.println("Edge for stopping");
                            return -1;//found an edge for which the player could win -> the AI should block it
                        }
                    }
                }
            }
        }
        System.out.println("Random edge");
        return 0;//neither the AI nor the player can win with this edge


    }


}
