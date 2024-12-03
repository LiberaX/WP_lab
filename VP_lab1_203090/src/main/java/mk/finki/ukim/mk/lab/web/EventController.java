package mk.finki.ukim.mk.lab.web;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.EventBooking;
import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.service.EventBookingService;
import mk.finki.ukim.mk.lab.service.EventService;
import mk.finki.ukim.mk.lab.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final LocationService locationService;
    private final EventBookingService eventBookingService;

    public EventController(EventService eventService, LocationService locationService, EventBookingService eventBookingService) {
        this.eventService = eventService;
        this.locationService = locationService;
        this.eventBookingService = eventBookingService;
    }

    @GetMapping //meneno
    public String getEventsPage(@RequestParam(required = false) String eventName, @RequestParam(required = false) Double rating, @RequestParam(required = false) Long locationId, Model model) {
        List<Event> events;

        if (locationId == null || locationId == 0) {
            events = eventService.findAll();
        } else {
            events = eventService.findAllByLocation_Id(locationId);
        }

        List<Event> filteredEvents = eventService.searchEvents(events, eventName, rating);

        List<Location> locations = locationService.findAll();
        model.addAttribute("currentLocation", locationId);
        model.addAttribute("events", filteredEvents);
        model.addAttribute("locations", locations);
        return "listEvents";
    }

    @GetMapping("/add") // Со get ќе ја прикажаме формата за додавање нов настан
    public String getEditEventForm(Model model) { //Model служи за да ги пренесеме податоците од Java апликацијата до Thymeleaf html-от
        Event event = new Event();
        List<Location> locations = locationService.findAll();
        model.addAttribute("event", event);
        model.addAttribute("locations", locations);

        return "add-event";
    }

    @PostMapping("/add") //post ќе ни служи за да ги обработиме податоците по клик на копчето Submit во html-от
    public String saveEvent(@RequestParam String name, @RequestParam String description, @RequestParam double popularityScore, @RequestParam Long locationId) {

        // Ако локацијата постои, создаваме нов настан
        eventService.saveEvent(name, description, popularityScore, locationId);
        return "redirect:/events";
    }

    // Метод за уредување на настан
    @GetMapping("/edit/{eventId}") //го прикажува веќе пополнетата форма за настанот кој сакаме да го уредиме
    public String editEvent(@PathVariable Long eventId, Model model) {
        Optional<Event> event = eventService.findById(eventId);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            model.addAttribute("locations", locationService.findAll());
        }
        return "add-event";
    }

    @PostMapping("/edit/{eventId}") //post ќе ни овозможи за да го уредуваме настанот со edit копчето
    public String getAddEventPage(@PathVariable Long eventId, @RequestParam String name, @RequestParam String description, @RequestParam double popularityScore, @RequestParam Long locationId) {

        eventService.updateEvent(eventId, name, description, popularityScore, locationId);
        return "redirect:/events";
    }

    // Метод за бришење на настан
    @GetMapping("/delete/{eventId}") //meneno
    public String deleteEvent(@PathVariable Long eventId) {
        Optional<Event> event = eventService.findById(eventId);

        if (event.isPresent()) {
            eventBookingService.deleteByEvent(event.get());
            eventService.deleteEvent(event.get());
        }

        return "redirect:/events";
    }

    @GetMapping("/details/{eventId}")
    public String eventDetails(@PathVariable Long eventId, Model model) {
        Optional<Event> event = eventService.findById(eventId);

        if (event.isEmpty()) return "redirect:/events";

        List<EventBooking> eventBookings = eventBookingService.findBookingByEventName(event.get().getName());
        if (!eventBookings.isEmpty()) model.addAttribute("eventBookings", eventBookings);

        model.addAttribute("event", event.get());

        return "details";
    }
}
