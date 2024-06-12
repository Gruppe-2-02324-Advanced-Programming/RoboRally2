package dk.dtu.compute.se.pisd.server.controller;

import dk.dtu.compute.se.pisd.server.model.Board;
import dk.dtu.compute.se.pisd.server.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Integer id) {
        Optional<Board> board = boardService.getBoardById(id);
        return board.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardService.saveBoard(board);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable Integer id, @RequestBody Board boardDetails) {
        Optional<Board> board = boardService.getBoardById(id);
        if (board.isPresent()) {
            Board existingBoard = board.get();
            existingBoard.setWidth(boardDetails.getWidth());
            existingBoard.setHeight(boardDetails.getHeight());
            existingBoard.setBoardName(boardDetails.getBoardName());
            existingBoard.setPhase(boardDetails.getPhase());
            existingBoard.setStep(boardDetails.getStep());
            existingBoard.setStepMode(boardDetails.isStepMode());
            existingBoard.setTotalCheckpoints(boardDetails.getTotalCheckpoints());
            existingBoard.setCounter(boardDetails.getCounter());
            existingBoard.setWon(boardDetails.isWon());
            final Board updatedBoard = boardService.saveBoard(existingBoard);
            return ResponseEntity.ok(updatedBoard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Integer id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
