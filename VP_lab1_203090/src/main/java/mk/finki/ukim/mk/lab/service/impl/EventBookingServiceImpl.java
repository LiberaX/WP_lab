package mk.finki.ukim.mk.lab.service.impl;

import jakarta.transaction.Transactional;
import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.EventBooking;
import mk.finki.ukim.mk.lab.repository.EventBookingRepository;
import mk.finki.ukim.mk.lab.service.EventBookingService;
import mk.finki.ukim.mk.lab.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventBookingServiceImpl implements EventBookingService {

    private final EventBookingRepository eventBookingRepository;
    private final EventService eventService;

    public EventBookingServiceImpl(EventBookingRepository eventBookingRepository, EventService eventService) {
        this.eventBookingRepository = eventBookingRepository;
        this.eventService = eventService;
    }

    @Override
    public List<EventBooking> findBookingByEventName(String name) {
        return eventBookingRepository.findByEvent_Name(name);
    }

    @Override
    public EventBooking placeBooking(Long eventId, String attendeeName, String attendeeAddress, Long numberOfTickets) {
        Optional<Event> event = eventService.findById(eventId);

        if (event.isPresent()) {
            EventBooking eventBooking = new EventBooking(event.get(), attendeeName, attendeeAddress, numberOfTickets);
            return eventBookingRepository.save(eventBooking);
        }

        return null;
    }

    @Override
    @Transactional //koga se pravi update u baza se pravi transakcii
    public void deleteByEvent(Event event) {
        this.eventBookingRepository.deleteByEvent(event);

    }

}
