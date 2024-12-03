package mk.finki.ukim.mk.lab.web;

import jakarta.servlet.http.HttpServletRequest;
import mk.finki.ukim.mk.lab.model.EventBooking;
import mk.finki.ukim.mk.lab.service.EventBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/eventBooking")
public class EventBookingController {

    private final EventBookingService eventBookingService;

    public EventBookingController(EventBookingService eventBookingService) {
        this.eventBookingService = eventBookingService;
    }

    @PostMapping
    public String buyTickets(@RequestParam Long eventId, @RequestParam Long numberOfTickets, @RequestParam String attendeeName, HttpServletRequest request, Model model) {
        EventBooking eventBooking = eventBookingService.placeBooking(eventId, attendeeName, request.getRemoteAddr(), numberOfTickets);

        if (eventBooking != null) {
            model.addAttribute("event", eventBooking);
            return "bookingConfirmation";
        }

        return "redirect:events";
    }
}
