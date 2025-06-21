package com.ChainReaction.game.controller;

import com.ChainReaction.game.dto.MoveRequestDto;
import com.ChainReaction.game.dto.MoveResponseDto;
import com.ChainReaction.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/move")
    public ResponseEntity<MoveResponseDto> processMove(@RequestBody MoveRequestDto request) {
        try {
            MoveResponseDto response = gameService.processMove(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
