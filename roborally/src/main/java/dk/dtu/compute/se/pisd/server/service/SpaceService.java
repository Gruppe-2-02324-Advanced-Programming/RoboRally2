package dk.dtu.compute.se.pisd.server.service;

import dk.dtu.compute.se.pisd.server.model.Space;
import dk.dtu.compute.se.pisd.server.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    public Optional<Space> getSpaceById(Integer id) {
        return spaceRepository.findById(id);
    }

    public Space saveSpace(Space space) {
        return spaceRepository.save(space);
    }

    public void deleteSpace(Integer id) {
        spaceRepository.deleteById(id);
    }
}
