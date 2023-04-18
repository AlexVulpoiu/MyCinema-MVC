package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.domain.Actor;
import com.unibuc.fmi.mycinemamvc.services.ActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    @GetMapping
    public ModelAndView getActors() {
        ModelAndView modelAndView = new ModelAndView("actorsList");
        List<Actor> actors = actorService.getActors();
        modelAndView.addObject("actors", actors);
        return modelAndView;
    }

    @GetMapping("/add")
    public String addActorForm(Model model) {
        model.addAttribute("actor", new Actor());
        return "actorForm";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute Actor actor, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "actorForm";
        }
        actorService.save(actor);
        return "redirect:/actors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("actor", actorService.findById(id));
        return "actorForm";
    }

    @GetMapping("/{id}")
    public String delete(@PathVariable Long id) {
        actorService.deleteById(id);
        return "redirect:/actors";
    }
}
