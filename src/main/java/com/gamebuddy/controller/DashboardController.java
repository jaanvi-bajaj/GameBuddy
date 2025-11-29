package com.gamebuddy.controller;

import com.gamebuddy.model.User;
import com.gamebuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        Optional<User> u = userService.findById(userId);
        if (u.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }
        model.addAttribute("user", u.get());
        return "dashboard";
    }
}