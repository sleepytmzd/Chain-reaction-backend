package com.ChainReaction.game.dto;

import java.util.List;

public class MoveResponseDto {
    private String currentPlayer; // "Human" or "AI"
    private List<String> grid; // Updated grid
    private int bestMoveRow; // Best move row
    private int bestMoveCol; // Best move column
    private boolean gameEnded; // True if game has ended

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

    public int getBestMoveRow() {
        return bestMoveRow;
    }

    public void setBestMoveRow(int bestMoveRow) {
        this.bestMoveRow = bestMoveRow;
    }

    public int getBestMoveCol() {
        return bestMoveCol;
    }

    public void setBestMoveCol(int bestMoveCol) {
        this.bestMoveCol = bestMoveCol;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }
}
