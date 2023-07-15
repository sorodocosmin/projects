package org.example.tournament;

import gurobi.*;

public class TournamentGenerator {
    private final int nrPlayers;
    private final int nrDays;
    private final int maxNrGamesPerDay;
    boolean[][][] programTournament;

    public TournamentGenerator(int nrPlayers, int nrDays, int maxNrGamesPerDay){
        this.nrPlayers = nrPlayers;
        this.nrDays = nrDays;
        this.maxNrGamesPerDay = maxNrGamesPerDay;
        this.generateTournament();
    }

    private void generateTournament(){

        try {
            // Create Gurobi environment
            GRBEnv env = new GRBEnv();
            env.set(GRB.IntParam.OutputFlag, 0); // Disable Gurobi output

            // Create Gurobi model
            GRBModel model = new GRBModel(env);

            // Set parameters
            int n = this.nrPlayers; // Number of players
            int d = this.nrDays;  // Maximum number of days

            // Create variables
            GRBVar[][][] x = new GRBVar[n][n][d];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < d; k++) {
                        x[i][j][k] = model.addVar(0, 1, 0, GRB.BINARY, "x[" + i + "][" + j + "][" + k + "]" );
                    }
                }
            }

            // Symmetry constraint
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    for (int k = 0; k < d; k++) {
                        model.addConstr(x[i][j][k], GRB.EQUAL, x[j][i][k], "symmetry[" + i + "][" + j + "][" + k + "]");
                    }
                }
            }

            // One-game constraint
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    GRBLinExpr sumFrom1ToD = new GRBLinExpr();
                    for (int k = 0; k < d; k++) {
                        sumFrom1ToD.addTerm(1, x[i][j][k]);
                    }
                    model.addConstr(sumFrom1ToD, GRB.EQUAL, 1, "playOnce[" + i + "][" + j + "]");
                }
            }

            // Daily limit constraint
            for (int i = 0; i < n; i++) {
                for (int k = 0; k < d; k++) {
                    GRBLinExpr sumGamesPerDay = new GRBLinExpr();
                    for (int j = 0; j < n; j++) {
                        sumGamesPerDay.addTerm(1, x[i][j][k]);
                    }
                    model.addConstr(sumGamesPerDay, GRB.LESS_EQUAL, this.maxNrGamesPerDay, "gamesPerDay[" + i + "][" + k + "]");
                }
            }

            model.optimize();

            this.programTournament = new boolean[n][n][d];

            for (int k = 0; k < d; k++) {
                for (int i = 0; i < n - 1 ; i++) {
                    for (int j = i+1; j < n; j++) {
                        if (x[i][j][k].get(GRB.DoubleAttr.X) == 1) {
                            this.programTournament[i][j][k] = true;
                            this.programTournament[j][i][k] = true;
                        }
                    }
                }
            }

            model.dispose();
            env.dispose();
        } catch (GRBException e) {
            System.out.println("Error : doesn't exist solution");
        }

    }
    public void printTournament(){
        System.out.println("Tournament with " + this.nrPlayers + " players, " + this.nrDays + " days and " + this.maxNrGamesPerDay + " games per day.");
        System.out.println("Tournament Schedule:");
        for (int k = 0; k < this.nrDays; k++) {
            System.out.println("Day " + k + ":");
            for (int i = 0; i < this.nrPlayers - 1 ; i++) {
                for (int j = i+1; j < this.nrPlayers; j++) {
                    if (this.programTournament[i][j][k]) {
                        System.out.println("Player " + i + " vs Player " + j);
                    }
                }
            }
        }
    }

    public boolean[][][] getProgramTournament() {
        return programTournament;
    }
}
