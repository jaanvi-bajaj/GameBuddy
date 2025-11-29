package com.gamebuddy.controller;

import com.gamebuddy.model.User;
import com.gamebuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String welcome(){
        return "welcome";
    }

    @GetMapping("/signup")
    public String signupForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("sportsList", new String[]{"Cricket","Football","Basketball","Badminton","Tennis"});
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignup(@ModelAttribute User user,
                           @RequestParam("idFile") MultipartFile idFile,
                           Model model){
        // combine sports if multiple are posted (when checkboxes are present)
        // If sportsInterested already arrives as comma-separated from form, keep it.
        userService.createUser(user, idFile);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value="registered", required=false) String reg, Model model){
        model.addAttribute("trophy", true);
        model.addAttribute("registered", reg != null);
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model){
        var op = userService.findByEmail(email);
        if (op.isEmpty()){
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
        var user = op.get();
        if (!userService.checkPassword(user, password)){
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFirstName());
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}