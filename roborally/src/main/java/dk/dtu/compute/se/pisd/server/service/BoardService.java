package dk.dtu.compute.se.pisd.server.service;

import dk.dtu.compute.se.pisd.server.model.Board;
import dk.dtu.compute.se.pisd.server.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Optional<Board> getBoardById(Integer id) {
        return boardRepository.findById(id);
    }

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public void deleteBoard(Integer id) {
        boardRepository.deleteById(id);
    }
}