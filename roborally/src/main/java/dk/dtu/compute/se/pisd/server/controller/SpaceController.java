package dk.dtu.compute.se.pisd.server.controller;

import dk.dtu.compute.se.pisd.server.model.Space;
import dk.dtu.compute.se.pisd.server.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @GetMapping
    public List<Space> getAllSpaces() {
        return spaceService.getAllSpaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Space> getSpaceById(@PathVariable Integer id) {
        Optional<Space> space = spaceService.getSpaceById(id);
        return space.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Space createSpace(@RequestBody Space space) {
        return spaceService.saveSpace(space);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Space> updateSpace(@PathVariable Integer id, @RequestBody Space spaceDetails) {
        Optional<Space> space = spaceService.getSpaceById(id);
        if (space.isPresent()) {
            Space existingSpace = space.get();
            existingSpace.setX(spaceDetails.getX());
            existingSpace.setY(spaceDetails.getY());
            existingSpace.setPlayer(spaceDetails.getPlayer());
            existingSpace.setWalls(spaceDetails.getWalls());
            existingSpace.setActions(spaceDetails.getActions());
            final Space updatedSpace = spaceService.saveSpace(existingSpace);
            return ResponseEntity.ok(updatedSpace);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Integer id) {
        spaceService.deleteSpace(id);
        return ResponseEntity.noContent().build();
    }
}
