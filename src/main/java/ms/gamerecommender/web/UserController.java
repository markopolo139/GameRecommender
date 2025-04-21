package ms.gamerecommender.web;

import ms.gamerecommender.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public String registerPage(@RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "<div class='text-danger'>Passwords do not match, can't register with invalid passwords</div>";
        }

        userService.createUser(username, password);
        return "<div class='alert alert-success'>Registered successfully! You can now log in.</div>";
    }

    @GetMapping("/check-password-match")
    @ResponseBody
    public String checkPasswordMatch(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword) {
        System.out.println(password);
        System.out.println(confirmPassword);
        if (password.equals(confirmPassword)) {
            return "<div class='text-success'>Passwords match ✅</div>";
        } else {
            return "<div class='text-danger'>Passwords do not match ❌</div>";
        }
    }
}
