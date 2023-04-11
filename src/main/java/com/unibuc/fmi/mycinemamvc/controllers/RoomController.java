package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.domain.Room;
import com.unibuc.fmi.mycinemamvc.services.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ModelAndView getRooms() {
        ModelAndView modelAndView = new ModelAndView("roomsList");
        List<Room> rooms = roomService.getRooms();
        modelAndView.addObject("rooms", rooms);
        return modelAndView;
    }

    @GetMapping("/add")
    public String addRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "roomForm";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute Room room, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "roomForm";
        }
        roomService.save(room);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.findById(id));
        return "roomForm";
    }
}
