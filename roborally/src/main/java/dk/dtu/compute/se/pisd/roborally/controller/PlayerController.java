package dk.dtu.compute.se.pisd.roborally.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
/*
 * @RestController
 * 
 * @RequestMapping("/players")
 * public class PlayerController {
 * 
 * private final List<String> players = new ArrayList<>();
 * 
 * @PostMapping("/join")
 * public ResponseEntity<String> joinGame(@RequestBody Player player) {
 * players.add(player.getName());
 * return ResponseEntity.ok("Player " + player.getName() + " joined the game.");
 * }
 * 
 * @GetMapping
 * public ResponseEntity<List<String>> getPlayers() {
 * return ResponseEntity.ok(players);
 * }
 * }
 */