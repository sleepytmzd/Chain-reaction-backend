package com.ChainReaction.game.service;

import com.ChainReaction.game.GameState;
import com.ChainReaction.game.MiniMaxAgent;
import com.ChainReaction.game.Move;
import com.ChainReaction.game.dto.MoveRequestDto;
import com.ChainReaction.game.dto.MoveResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    public MoveResponseDto processMove(MoveRequestDto request) {
        // Validate input
        if (request.getGrid() == null || request.getGrid().isEmpty()) {
            throw new IllegalArgumentException("Grid cannot be empty");
        }
        if (!List.of("Human", "AI").contains(request.getCurrentPlayer())) {
            throw new IllegalArgumentException("Invalid current player: " + request.getCurrentPlayer());
        }
        if (request.getDepth() <= 0) {
            throw new IllegalArgumentException("Depth must be positive");
        }
        if (request.getHeuristic() == null || request.getHeuristic().isEmpty()) {
            throw new IllegalArgumentException("Heuristic cannot be empty");
        }

        // Create GameState from request
        GameState gameState = new GameState(new ArrayList<>(request.getGrid()));
        gameState.player = request.getCurrentPlayer().equals("Human") ? 2 : 1;

        // Check if game has ended
        if (gameState.goalReached()) {
            MoveResponseDto response = new MoveResponseDto();
            response.setCurrentPlayer(request.getCurrentPlayer());
            response.setGrid(request.getGrid());
            response.setBestMoveRow(request.getMoveRow());
            response.setBestMoveCol(request.getMoveCol());
            response.setGameEnded(true);
            return response;
        }

        // Process AI move
        gameState.setHeuristic(request.getHeuristic());
        Move bestMove = request.getHeuristic().equalsIgnoreCase("random")
                ? MiniMaxAgent.findRandomMove(gameState)
                : MiniMaxAgent.findBestMove(gameState, request.getDepth());
        GameState updatedGameState = gameState.makeMove(bestMove);

        // Prepare response
        MoveResponseDto response = new MoveResponseDto();
        response.setCurrentPlayer(updatedGameState.player == 1 ? "Human" : "AI");
        List<String> grid = new ArrayList<>();
        String[] gameStr = updatedGameState.toString().split("\n");
        for (String row : gameStr) {
            grid.add(row.trim());
        }
        response.setGrid(grid);
        response.setBestMoveRow(bestMove.row);
        response.setBestMoveCol(bestMove.col);
        response.setGameEnded(updatedGameState.goalReached());

        return response;
    }
}
