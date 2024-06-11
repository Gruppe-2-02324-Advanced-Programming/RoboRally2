package dk.dtu.compute.se.pisd.roborally.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dk.dtu.compute.se.pisd.server.postState;

@RestController
public class HelloController {

    AppController appController;
    String str;

    HelloController() {
        str = "Hello, World!";
        appController = new AppController(null);
        appController.newGame();
    }

    @GetMapping("/hello")
    public String hello() {
        return str;
    }

    @PostMapping("/setString")
    public void setString(@RequestParam String newStr) {
        str = newStr;
    }

    @GetMapping("/currentGamestate")
    public String currentGamestate() {
        return appController.saveCurrentGameAsString();
    }

}