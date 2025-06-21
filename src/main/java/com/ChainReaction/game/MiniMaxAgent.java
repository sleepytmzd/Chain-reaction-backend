package com.ChainReaction.game;

import java.util.List;

public class MiniMaxAgent {
    public static long count = 0;
    public static boolean ab_pruning = true;
    public MiniMaxAgent(){}

    public static int bestScore(GameState game, int depth, int alpha, int beta){
        if(depth <= 0 || game.goalReached()){
//            System.out.println("Static evaluation");
//            System.out.println("Player: " + game.player);
//            System.out.println(game);
            int score = game.staticEvaluation();
//            System.out.println("Score: " + score);
            return score;
        }
        if(game.player == 1){
            count++;
            int maxScore = Integer.MIN_VALUE;
            for(Move move : game.getCandidateMoves()){
                GameState newGame = game.makeMove(move);
                newGame.player = 2;
//                System.out.println(newGame);
                int score = bestScore(newGame, depth-1, alpha, beta);
//                System.out.println("Depth: " + depth);
//                System.out.println("Move: " + move);
//                System.out.println(game);
//                System.out.println("Score: " + score);
                maxScore = Math.max(score, maxScore);
                if(ab_pruning){
                    alpha = Math.max(score, alpha);
                    if(beta <= alpha) break;
                }
            }
            return maxScore;
        } else if(game.player == 2) {
            count++;
            int minScore = Integer.MAX_VALUE;
            for(Move move : game.getCandidateMoves()){
                GameState newGame = game.makeMove(move);
                newGame.player = 1;
//                System.out.println(newGame);
                int score = bestScore(newGame, depth-1, alpha, beta);
//                System.out.println("Depth: " + depth);
//                System.out.println("Move: " + move);
//                System.out.println(game);
//                System.out.println("Score: " + score);
                minScore = Math.min(score, minScore);
                if(ab_pruning){
                    beta = Math.min(score, beta);
                    if(beta <= alpha) break;
                }
            }
            return minScore;
        } else {
            return 0;
        }
    }

    public static Move findBestMove(GameState game, int depth){
        int bestScore = 0;
        Move bestMove = null;
        List<Move> candidateMoves = game.getCandidateMoves();

        if(game.player == 1){
            bestScore = Integer.MIN_VALUE;
            for(Move move : candidateMoves){
//                System.out.println("Considering move: " + move);
                GameState newGame = game.makeMove(move);
//                System.out.println(newGame);
                newGame.player = 2;
                int score = bestScore(newGame, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE);
//                System.out.println("Score: " + score);
                if(score > bestScore){
                    bestScore = score;
                    bestMove = move;
                }
            }
        } else if(game.player == 2) {
            bestScore = Integer.MAX_VALUE;
            for(Move move : candidateMoves){
//                System.out.println("Considering move: " + move);
                GameState newGame = game.makeMove(move);
//                System.out.println(newGame);
                newGame.player = 1;
                int score = bestScore(newGame, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE);
//                System.out.println("Score: " + score);
                if(score < bestScore){
                    bestScore = score;
                    bestMove = move;
                }
            }
        }
//        System.out.println("Best score: " + bestScore);
        return bestMove;
    }

    public static Move findRandomMove(GameState game){
        List<Move> candidateMoves = game.getCandidateMoves();
        int rand_idx = (int) (Math.random() * candidateMoves.size()) % candidateMoves.size();
        return candidateMoves.get(rand_idx);
    }
}

