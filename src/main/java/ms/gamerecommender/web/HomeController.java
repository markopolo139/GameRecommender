package ms.gamerecommender.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping(path = "/home")
public class HomeController {
    @GetMapping
    public String homePage() {
        return "home";
    }
}
