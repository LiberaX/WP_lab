package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.EventBooking;

import java.util.List;
import java.util.Optional;

public interface EventBookingService {

    List<EventBooking> findBookingByEventName(String name);

    EventBooking placeBooking(Long eventId, String attendeeName, String attendeeAddress, Long numberOfTickets);

    void deleteByEvent(Event event);
}
