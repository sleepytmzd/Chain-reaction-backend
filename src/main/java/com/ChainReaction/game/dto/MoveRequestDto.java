package com.ChainReaction.game.dto;

import java.util.List;

public class MoveRequestDto {
    private String currentPlayer; // "Human" or "AI"
    private List<String> grid; // Grid rows as strings (e.g., "0 1R 0")
    private int depth;
    private String heuristic;
    private int moveRow; // Last move row
    private int moveCol; // Last move column

    // Getters and Setters
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<String> getGrid() {
        return grid;
    }

    public void setGrid(List<String> grid) {
        this.grid = grid;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(String heuristic) {
        this.heuristic = heuristic;
    }

    public int getMoveRow() {
        return moveRow;
    }

    public void setMoveRow(int moveRow) {
        this.moveRow = moveRow;
    }

    public int getMoveCol() {
        return moveCol;
    }

    public void setMoveCol(int moveCol) {
        this.moveCol = moveCol;
    }
}
