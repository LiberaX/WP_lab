package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> findAll();

    Optional<Event> findById(Long eventId);

    void saveEvent(String name, String description, double popularityScore, Long locationId);

    void updateEvent(Long eventId, String name, String description, double popularityScore, Long locationId);

    void deleteEvent(Event event);

    List<Event> findAllByLocation_Id(Long locationId);

    List<Event> searchEvents(List<Event> events, String eventName, Double rating);
}
