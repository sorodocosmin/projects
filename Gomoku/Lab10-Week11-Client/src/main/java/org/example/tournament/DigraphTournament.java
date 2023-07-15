package org.example.tournament;

import org.graph4j.Digraph;
import org.graph4j.GraphBuilder;
import org.graph4j.alg.Tournament;

import java.util.Random;

public class DigraphTournament {

    Digraph digraph;

    public DigraphTournament(boolean[][][] programTournament){
        this.digraph = GraphBuilder
                .numVertices(programTournament.length)
                .buildDigraph();

        this.populateDigraph(programTournament);
    }

    private void populateDigraph(boolean[][][] programTournament){

        Random random = new Random();
        System.out.println("\nThe result of the tournament is : ");
        for (int i = 0; i < programTournament.length - 1 ; i++) {
            for (int j = i + 1 ; j < programTournament.length; j++) {
                for (int k = 0; k < programTournament[0][0].length; k++) {
                    if(programTournament[i][j][k]){
                        int nr = random.nextInt(0,2);
                        if(nr == 0){
                            System.out.println("Player " + i + " wins against player " + j + " on day " + k);
                            this.digraph.addEdge(i, j);
                        }
                        else if(nr == 1){
                            System.out.println("Player " + j + " wins against player " + i + " on day " + k);
                            this.digraph.addEdge(j, i);
                        }
                    }
                }
            }
        }
    }

    public void printSequence(){
        Tournament tournament = new Tournament(this.digraph);
        System.out.println("\nThe sequence where Player_i beats Player_j ... etc : ");

        for( int player : tournament.getHamiltonianPath()){
            System.out.print(player + " -> ");
        }

    }
}
