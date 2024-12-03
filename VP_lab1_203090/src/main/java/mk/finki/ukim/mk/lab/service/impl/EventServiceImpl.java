package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.repository.EventRepository;
import mk.finki.ukim.mk.lab.repository.LocationRepository;
import mk.finki.ukim.mk.lab.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventServiceImpl(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public void saveEvent(String name, String description, double popularityScore, Long locationId) {
        Optional<Location> location = locationRepository.findById(locationId);
        if (location.isPresent()) {
            Event event = new Event(name, description, popularityScore, location.get());
            eventRepository.save(event);
        }
    }

    @Override
    public void updateEvent(Long eventId, String name, String description, double popularityScore, Long locationId) {
        Optional<Location> location = locationRepository.findById(locationId);
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isPresent() && location.isPresent()) {
            event.get().setName(name);
            event.get().setDescription(description);
            event.get().setPopularityScore(popularityScore);
            event.get().setLocation(location.get());
            eventRepository.save(event.get());
        }
    }

    @Override
    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    @Override
    public List<Event> findAllByLocation_Id(Long locationId) {
        return eventRepository.findAllByLocation_Id(locationId);
    }

    public List<Event> searchEvents(List<Event> events, String eventName, Double rating) {
        var filteredEvents = events;

        if (eventName != null) //ako ima poveke space koristam trim
            filteredEvents = events.stream().filter(event -> event.getName().toLowerCase().contains(eventName.toLowerCase().trim())).toList();

        if (rating != null)
            filteredEvents = events.stream().filter(event -> event.getPopularityScore() >= rating).toList();

        return filteredEvents;
    }
}
