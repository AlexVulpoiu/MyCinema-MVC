package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.dto.RegisterDto;
import com.unibuc.fmi.mycinemamvc.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public String save(@Valid @ModelAttribute RegisterDto registerDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register";
        }
        userService.register(registerDto);
        return "redirect:/login";
    }
}
