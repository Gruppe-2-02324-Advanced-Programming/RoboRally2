package dk.dtu.compute.se.pisd.server.controller;
import dk.dtu.compute.se.pisd.server.model.Board;
import dk.dtu.compute.se.pisd.server.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {


    private BoardService boardService;



    @Autowired
    public Object BoardController(BoardService boardService) {
        this.boardService = boardService;
        return null;
    }
}

/*
@GetMapping
    public Board getBoard(@RequestParam Integer id) {

        return boardService.getBoard(id);
    }


}
*/