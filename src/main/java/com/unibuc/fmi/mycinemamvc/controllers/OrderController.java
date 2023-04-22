package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinemamvc.domain.MovieSchedule;
import com.unibuc.fmi.mycinemamvc.dto.OrderDto;
import com.unibuc.fmi.mycinemamvc.services.MovieScheduleService;
import com.unibuc.fmi.mycinemamvc.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final MovieScheduleService movieScheduleService;

    @GetMapping
    public String orderForm(Model model, @RequestParam Long movieId, @RequestParam Long roomId,
                            @RequestParam LocalDate date, @RequestParam LocalTime hour) {
        MovieScheduleId movieScheduleId = MovieScheduleId.builder()
                .movieId(movieId)
                .roomId(roomId)
                .date(date)
                .hour(hour)
                .build();
        MovieSchedule movieSchedule = movieScheduleService.findById(movieScheduleId);
        model.addAttribute("movieSchedule", movieSchedule);
        OrderDto orderDto = OrderDto.builder()
                .movieId(movieId)
                .roomId(roomId)
                .date(date)
                .hour(hour)
                .price(movieSchedule.getPrice())
                .build();
        model.addAttribute("orderDto", orderDto);
        return "orderForm";
    }

    @PostMapping
    public String save(Authentication authentication, @Valid @ModelAttribute OrderDto orderDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "orderForm";
        }
        orderDto.setUsername(((UserDetails) authentication.getPrincipal()).getUsername());
        orderService.save(orderDto);
        return "redirect:/movies";
    }

    @GetMapping("/myOrders")
    public String myOrders(Authentication authentication, Model model) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        model.addAttribute("orders", orderService.findOrdersForUser(username));
        return "myOrders";
    }
}
