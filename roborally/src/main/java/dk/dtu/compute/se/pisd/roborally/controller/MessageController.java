package dk.dtu.compute.se.pisd.roborally.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @PostMapping
    public String receiveMessage(@RequestBody String message) {
        System.out.println("Received message: " + message);
        return "Message received";
    }
}