package banners.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SomeController {
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/home-redir")
    public String homePost(){
        return "redirect:/home";
    }
}