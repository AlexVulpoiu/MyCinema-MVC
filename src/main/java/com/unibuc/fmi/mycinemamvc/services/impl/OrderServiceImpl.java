package com.unibuc.fmi.mycinemamvc.services.impl;

import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinemamvc.domain.*;
import com.unibuc.fmi.mycinemamvc.dto.OrderDto;
import com.unibuc.fmi.mycinemamvc.exceptions.BadRequestException;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.repositories.*;
import com.unibuc.fmi.mycinemamvc.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MovieRepository movieRepository;
    private final MovieScheduleRepository movieScheduleRepository;
    private final OrderRepository orderRepository;
    private final RoomRepository roomRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void save(OrderDto orderDto) {
        Optional<User> optionalUser = userRepository.findByUsername(orderDto.getUsername());
        if(optionalUser.isEmpty()) {
            log.warn("User " + orderDto.getUsername() + " not found");
            throw new ResourceNotFoundException("User with username " + orderDto.getUsername() + " not found!");
        }

        Optional<Movie> optionalMovie = movieRepository.findById(orderDto.getMovieId());
        if(optionalMovie.isEmpty()) {
            log.warn("Movie with id " + orderDto.getMovieId() + " not found");
            throw new ResourceNotFoundException("Movie with id " + orderDto.getMovieId() + " not found!");
        }

        Optional<Room> optionalRoom = roomRepository.findById(orderDto.getRoomId());
        if(optionalRoom.isEmpty()) {
            log.warn("Room with id " + orderDto.getRoomId() + " not found");
            throw new ResourceNotFoundException("Room with id " + orderDto.getRoomId() + " not found!");
        }

        Movie movie = optionalMovie.get();
        Room room = optionalRoom.get();
        MovieScheduleId movieScheduleId = MovieScheduleId.builder()
                .movieId(movie.getId())
                .roomId(room.getId())
                .date(orderDto.getDate())
                .hour(orderDto.getHour())
                .build();
        Optional<MovieSchedule> optionalMovieSchedule = movieScheduleRepository.findById(movieScheduleId);
        if(optionalMovieSchedule.isEmpty()) {
            log.warn("Movie schedule not found on " + orderDto.getDate() + " at " + orderDto.getHour());
            throw new BadRequestException("The selected movie isn't scheduled at the requested time!");
        }

        if(LocalDateTime.of(orderDto.getDate(), orderDto.getHour()).isBefore(LocalDateTime.now())) {
            log.warn("Order request for a past scheduled movie");
            throw new BadRequestException("You can't buy tickets for a movie scheduled in the past!");
        }

        MovieSchedule movieSchedule = optionalMovieSchedule.get();
        int roomCapacity = room.getNumberOfRows() * room.getSeatsPerRow();
        int soldTickets = movieSchedule.getTickets().size();
        if(roomCapacity - soldTickets < orderDto.getNumberOfTickets()) {
            log.warn("Not enough tickets left");
            throw new BadRequestException("There are not enough tickets left for this movie!");
        }

        Optional<Ticket> optionalLastTicket = ticketRepository.findLastTicketForMovie(movieSchedule);
        int row = 1;
        int seat = 0;
        if(optionalLastTicket.isPresent()) {
            row = optionalLastTicket.get().getNumberOfRow();
            seat = optionalLastTicket.get().getSeat();
        }

        List<Ticket> tickets = new ArrayList<>();
        for(int i = 0; i < orderDto.getNumberOfTickets(); i++) {
            seat++;
            if(seat > room.getSeatsPerRow()) {
                row++;
                seat = 1;
            }
            Ticket ticket = Ticket.builder()
                    .numberOfRow(row)
                    .seat(seat)
                    .movieSchedule(movieSchedule)
                    .build();
            tickets.add(ticket);
            log.info("Generated ticket for seat " + seat + ", row " + row + ", room " + room.getName());
        }

        User user = optionalUser.get();
        Order order = Order.builder()
                .totalPrice(movieSchedule.getPrice() * tickets.size())
                .date(LocalDate.now())
                .hour(LocalTime.now())
                .tickets(tickets)
                .user(user)
                .build();
        order = orderRepository.save(order);
        log.info("Order saved");

        for(Ticket ticket : tickets) {
            ticket.setOrder(order);
            ticketRepository.save(ticket);
        }
    }

    @Override
    public List<Order> findOrdersForUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) {
            log.warn("User with username " + username + " not found");
            throw new ResourceNotFoundException("User with username " + username + " not found!");
        }
        log.info("Get orders for user " + username);
        return optionalUser.get().getOrders().stream().sorted(
                (o1, o2) -> {
                    MovieSchedule movieSchedule1 = o1.getTickets().get(0).getMovieSchedule();
                    MovieSchedule movieSchedule2 = o2.getTickets().get(0).getMovieSchedule();
                    if(movieSchedule1.getId().getDate().equals(movieSchedule2.getId().getDate())) {
                        return movieSchedule1.getId().getHour().compareTo(movieSchedule2.getId().getHour());
                    }
                    return movieSchedule1.getId().getDate().compareTo(movieSchedule2.getId().getDate());
                }
        ).collect(Collectors.toList());
    }
}
