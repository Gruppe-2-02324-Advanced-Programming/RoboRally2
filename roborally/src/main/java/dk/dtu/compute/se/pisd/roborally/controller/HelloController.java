package dk.dtu.compute.se.pisd.roborally.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    GameController gameController;
    String str;

    HelloController() {
        str = "Hello, World!";
    }

    @GetMapping("/hello")
    public String hello() {
        return str;
    }

    @PostMapping("/setString")
    public void setString(@RequestParam String newStr) {
        str = newStr;
    }

}