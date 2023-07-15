package org.example.tournament;

public class MainTournament {

    public static void main(String[] args){
        TournamentGenerator tournamentGenerator = new TournamentGenerator(5,14,20);
        tournamentGenerator.printTournament();

        DigraphTournament digraphTournament = new DigraphTournament(tournamentGenerator.getProgramTournament());
        digraphTournament.printSequence();

    }


}
