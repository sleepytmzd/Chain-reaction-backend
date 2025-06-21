package com.ChainReaction.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/*
Assumptions:
    Maximizing player: Player 1 (R)
    Minimizing player: Player 2 (B)
*/
class Cell {
    int row, col;
    int orbs;
    int player;

    public Cell(){
        orbs = 0;
        player = 0;
        row = col = 0;
    }

    public Cell(int row, int col, int orbs, int player){
        this.orbs = orbs;
        this.player = player;
        this.row = row;
        this.col = col;
    }
    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        this.orbs = 0;
        this.player = 0;
    }
    boolean isEmpty(){
        return orbs == 0;
    }

    public String toString(){
        if(player == 1) return orbs + "R";
        if(player == 2) return orbs + "B";
        return "" + orbs;
    }
}



public class GameState {
    int row, col;
    Cell[][] state;
    public int player;
    String heuristic = "";

    public GameState(int row, int col){
        state = new Cell[row][col];
        for(int i = 0; i < row; i++){
            Cell[] t = new Cell[col];
            for(int j = 0; j < col; j++){
                t[j] = new Cell(i, j);
            }
            state[i] = t;
        }
    }

    public GameState(List<String> lines){
        row = lines.size();
        String[] words = lines.get(0).split(" ");
        col = words.length;

        state = new Cell[row][col];
        for(int i = 0; i < row; i++){
            Cell[] t = new Cell[col];
            for(int j = 0; j < col; j++){
                t[j] = new Cell(i, j);
            }
            state[i] = t;
        }

        for(int i = 0; i < row; i++){
            String[] elem = lines.get(i).split(" ");
            for(int j = 0; j < col; j++){
                if(elem[j].length() == 1){
                    state[i][j] = new Cell(i, j);
                } else if (elem[j].length() == 2) {
                    int orbs = Integer.parseInt(String.valueOf(elem[j].charAt(0)));
                    int player = elem[j].charAt(1) == 'R' ? 1 : 2;
                    state[i][j] = new Cell(i, j, orbs, player);
                }
            }
        }
    }

    public GameState(Cell[][] state){
        this.row = state.length;
        this.col = state[0].length;
        this.state = new Cell[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                Cell c = state[i][j];
                this.state[i][j] = new Cell(c.row, c.col, c.orbs, c.player);
            }
        }
    }

    public void setHeuristic(String h){
        heuristic = h;
    }

    public List<Move> getCandidateMoves(){
        List<Move> moves = new ArrayList<>();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(state[i][j].isEmpty() || state[i][j].player == player){
                    moves.add(new Move(i, j, player));
                }
            }
        }
        return moves;
    }

    public List<Cell> getAdjacentCells(int row, int col){
        List<Cell> adjacentCells = new ArrayList<>();
        if(row-1 >= 0 && row-1 < this.row && col >= 0 && col < this.col){
            adjacentCells.add(state[row-1][col]);
        }
        if(row+1 >= 0 && row+1 < this.row && col >= 0 && col < this.col){
            adjacentCells.add(state[row+1][col]);
        }
        if(row >= 0 && row < this.row && col-1 >= 0 && col-1 < this.col){
            adjacentCells.add(state[row][col-1]);
        }
        if(row >= 0 && row < this.row && col+1 >= 0 && col+1 < this.col){
            adjacentCells.add(state[row][col+1]);
        }
        return adjacentCells;
    }

    public GameState makeMove(Move move){
        GameState newState = new GameState(state);
        newState.player = move.player;
        newState.heuristic = heuristic;

        Cell oldCell = newState.state[move.row][move.col];
        oldCell.orbs++;
        oldCell.player = move.player;
        newState.state[move.row][move.col] = oldCell;

        if(newState.goalReached()){
            return newState;
        }

        List<Cell> adjacentCells = getAdjacentCells(move.row, move.col);
        if(oldCell.orbs >= adjacentCells.size()){
            Cell emptyCell = new Cell(move.row, move.col);
            newState.state[move.row][move.col] = emptyCell;
            for(Cell cell : adjacentCells){
                newState = newState.makeMove(new Move(cell.row, cell.col, move.player));
            }
        }

        return newState;
    }

    public boolean goalReached(){
        boolean not_started = true;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(state[i][j].player != 0){
                    not_started = false;
                    break;
                }
            }
            if(!not_started){
                break;
            }
        }
        if(not_started) return false;

        boolean player1 = false;
        boolean player2 = false;
        int player1_orbs = 0;
        int player2_orbs = 0;
        for(int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (state[i][j].player == 1) {
                    player1 = true;
                    player1_orbs++;
                }
                if (state[i][j].player == 2) {
                    player2 = true;
                    player2_orbs++;
                }
            }
        }
        if(player1_orbs + player2_orbs < 2) return false;
        if(player1 && !player2) return true;
        if(!player1 && player2) return true;
        return false;
    }

    boolean isCorner(int i, int j) {
        return (i == 0 || i == row - 1) && (j == 0 || j == col - 1);
    }

    boolean isEdge(int i, int j) {
        return (i == 0 || i == row - 1 || j == 0 || j == col - 1);
    }
//    int staticEvaluation(){
//        int score = 0;
//        for(int i = 0; i < row; i++){
//            for(int j = 0; j < col; j++){
//                Cell cell = state[i][j];
//                int maxOrbs = getAdjacentCells(i, j).size();
//
//                if(cell.player == 1){
//                    score += cell.orbs;
//                    if(heuristic.equalsIgnoreCase("critical_cell_bias") || heuristic.equalsIgnoreCase("combined")){
//                        if(cell.orbs == maxOrbs-1){
//                            score += maxOrbs;
//                        }
//                    }
//                    if (heuristic.equalsIgnoreCase("edge_control_bias") || heuristic.equalsIgnoreCase("combined")) {
//                        if(isCorner(i, j)) score += 3;
//                        else if(isEdge(i, j)) score += 2;
//                    }
//                } else if(cell.player == 2){
//                    score -= cell.orbs;
//                    if(heuristic.equalsIgnoreCase("critical_cell_bias") || heuristic.equalsIgnoreCase("combined")){
//                        if(cell.orbs == maxOrbs-1){
//                            score -= maxOrbs;
//                        }
//                    }
//                    if (heuristic.equalsIgnoreCase("edge_control_bias") || heuristic.equalsIgnoreCase("combined")) {
//                        if(isCorner(i, j)) score -= 3;
//                        else if(isEdge(i, j)) score -= 2;
//                    }
//                }
//            }
//        }
//        return score;
//    }

    int staticEvaluation(){
        switch(heuristic){
            case "orb_count":
                return orbCount();
            case "critical_cell_bias":
                return criticalCellBias();
            case "edge_bias":
                return edgeBias();
            case "cell_control":
                return cellControl();
            case "chain_potential":
                return chainPotential();
            default:
                return orbCount();
        }
    }

    int orbCount(){
        int score = 0;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                Cell cell = state[i][j];
                if(cell.player == 1){
                    score += cell.orbs;
                } else if(cell.player == 2){
                    score -= cell.orbs;
                }
            }
        }
        return score;
    }

    int criticalCellBias(){
        int score = 0;
        final int CRITICAL_BONUS = 100;
        final int THREAT_PENALTY = 100;

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                Cell cell = state[i][j];
                if(cell.player == 0) continue;

                int capacity = getAdjacentCells(i, j).size();

                // Basic orb count
                if(cell.player == 1){
                    score += cell.orbs * 10;
                } else {
                    score -= cell.orbs * 10;
                }

                // Critical mass bonus/penalty
                if(cell.orbs == capacity - 1){ // About to explode
                    if(cell.player == 1){
                        score += CRITICAL_BONUS;
                    } else {
                        score -= THREAT_PENALTY;
                    }
                }
            }
        }
        return score;
    }

    int edgeBias(){
        int score = 0;
        final int CORNER_VALUE = 50;
        final int EDGE_VALUE = 20;
        final int CENTER_VALUE = 10;

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                Cell cell = state[i][j];
                if(cell.player == 0) continue;

                int position_value;
                if(isCorner(i, j)) position_value = CORNER_VALUE;
                else if(isEdge(i, j)) position_value = EDGE_VALUE;
                else position_value = CENTER_VALUE;

                if(cell.player == 1){
                    score += cell.orbs * 8 + position_value;
                } else {
                    score -= cell.orbs * 8 + position_value;
                }
            }
        }
        return score;
    }

    int cellControl(){
        int player1_cells = 0, player2_cells = 0;
        int player1_orbs = 0, player2_orbs = 0;

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                Cell cell = state[i][j];
                if(cell.player == 1){
                    player1_cells++;
                    player1_orbs += cell.orbs;
                } else if(cell.player == 2){
                    player2_cells++;
                    player2_orbs += cell.orbs;
                }
            }
        }

        // Combine territory and orb count
        int territory_score = (player1_cells - player2_cells) * 20;
        int orb_score = (player1_orbs - player2_orbs) * 5;

        return territory_score + orb_score;
    }

    int chainPotential(){
        int score = 0;
        final int CHAIN_BONUS = 50;

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                Cell cell = state[i][j];
                if(cell.player == 0) continue;

                int capacity = getAdjacentCells(i, j).size();

                // Basic scoring
                if(cell.player == 1){
                    score += cell.orbs * 8;
                } else {
                    score -= cell.orbs * 8;
                }

                // Chain potential for critical cells
                if(cell.orbs == capacity - 1){
                    List<Cell> adjacent = getAdjacentCells(i, j);
                    int chain_value = 0;

                    for(Cell adj : adjacent){
                        int ai = adj.row, aj = adj.col;
                        Cell adj_cell = state[ai][aj];

                        if(cell.player == 1){
                            if(adj_cell.player == 2){ // Can capture enemy cell
                                chain_value += CHAIN_BONUS;
                            } else if(adj_cell.player == 0){ // Can expand to empty
                                chain_value += CHAIN_BONUS / 3;
                            }
                        } else { // cell.player == 2
                            if(adj_cell.player == 1){ // Enemy can capture our cell
                                chain_value -= CHAIN_BONUS;
                            } else if(adj_cell.player == 0){
                                chain_value -= CHAIN_BONUS / 3;
                            }
                        }
                    }
                    score += chain_value;
                }
            }
        }
        return score;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                str.append(state[i][j]).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}

