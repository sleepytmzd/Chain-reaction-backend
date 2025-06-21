package com.ChainReaction.game;

public class Move {
    public int row;
    public int col;
    int player;

    public Move(){
        row = 0;
        col = 0;
        player = 0;
    }

    public Move(int row, int col, int player){
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public String toString(){
        return "Row: " + row + ", Col: " + col + ", Player: " + player;
    }
}
