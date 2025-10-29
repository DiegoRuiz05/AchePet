package AchePetWebSite.AchePet.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String hello() {
        return "Hello World, Spring Boot está rodando 🚀";
    }
} //controller principal
