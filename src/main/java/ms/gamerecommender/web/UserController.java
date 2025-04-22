package ms.gamerecommender.web;

import jakarta.servlet.http.HttpServletRequest;
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

        try {
            userService.createUser(username, password);
        }
        catch (Exception e) {
            return "<div class='text-danger'>%s</div>".formatted(e.getMessage());
        }

        return "<div class='alert alert-success'>Registered successfully! You can now log in.</div>";
    }

    @GetMapping("/check-password-match")
    @ResponseBody
    public String checkPasswordMatch(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return "<div class='text-success'>Passwords match ✅</div>";
        } else {
            return "<div class='text-danger'>Passwords do not match ❌</div>";
        }
    }

    @Controller
    @RequestMapping(path = "/user/settings")
    public class UserSettings {

        @GetMapping
        public String settings() {
            return "user/settings";
        }

        @PostMapping("/change/password")
        @ResponseBody
        public String updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
            userService.updatePassword(oldPassword, newPassword);
            return "<div class='text-success'>Passwords updated</div>";
        }

        @PostMapping("/change/username")
        @ResponseBody
        public String updateUsername(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password) {
            userService.updateUsername(username, password, request);
            return "<div class='text-success'>Username updated</div>";
        }
    }
}
