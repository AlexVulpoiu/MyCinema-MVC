package com.unibuc.fmi.mycinemamvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage() {
        return "accessDenied";
    }
}
