package com.clubspot.service;

import com.clubspot.entity.Event;
import com.clubspot.entity.Club;
import com.clubspot.entity.User;
import com.clubspot.repository.EventRepository;
import com.clubspot.repository.ClubRepository;
import com.clubspot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new event
    @Transactional
    public Event createEvent(Event event) {
        // Validate that the club exists
        if (event.getClub() != null && event.getClub().getId() != null) {
            Optional<Club> clubOpt = clubRepository.findById(event.getClub().getId());
            if (clubOpt.isEmpty()) {
                throw new RuntimeException("Club not found with ID: " + event.getClub().getId());
            }
            event.setClub(clubOpt.get());
        }

        // Validate that the creator exists
        if (event.getCreatedBy() != null) {
            Optional<User> creatorOpt = userRepository.findById(event.getCreatedBy());
            if (creatorOpt.isEmpty()) {
                throw new RuntimeException("Creator not found with ID: " + event.getCreatedBy());
            }
        }

        // Set default values if not provided
        if (event.getAicteHours() == null) {
            event.setAicteHours(0);
        }
        if (event.getMaxAttendees() == null) {
            event.setMaxAttendees(50);
        }
        if (event.getStatus() == null) {
            event.setStatus("UPCOMING");
        }

        return eventRepository.save(event);
    }

    // Get event by ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get events by club ID (Fixed: Calls new repo method with startDateTime)
    public List<Event> getEventsByClubId(Long clubId) {
        return eventRepository.findByClubIdAndStartDateTimeAfter(clubId, LocalDateTime.now());
    }

    // Get upcoming events by club
    public List<Event> getUpcomingEventsByClub(Long clubId) {
        return eventRepository.findUpcomingEventsByClub(clubId);
    }

    // Get upcoming events (Optimized: Use status filter + entity method)
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByStatus("UPCOMING").stream()
                .filter(Event::isUpcoming)
                .collect(Collectors.toList());
    }

    // Get past events (Optimized)
    public List<Event> getPastEvents() {
        return eventRepository.findByStatus("COMPLETED").stream()
                .filter(Event::isCompleted)
                .collect(Collectors.toList());
    }

    // Get ongoing events (Use repo method; pass null for all clubs or add clubId
    // param if needed)
    public List<Event> getOngoingEvents() {
        // For all clubs; adjust if club-specific
        return eventRepository.findAll().stream()
                .filter(Event::isOngoing)
                .collect(Collectors.toList());
    }

    // Get events by date range (Optimized)
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByStartDateTimeBetween(startDate, endDate);
    }

    // Get events by status (Optimized)
    public List<Event> getEventsByStatus(String status) {
        return eventRepository.findByStatus(status);
    }

    // Get events by created by (Optimized)
    public List<Event> getEventsByCreator(Long creatorId) {
        return eventRepository.findByCreatedBy(creatorId);
    }

    // Update event
    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();

            if (eventDetails.getTitle() != null)
                event.setTitle(eventDetails.getTitle());
            if (eventDetails.getDescription() != null)
                event.setDescription(eventDetails.getDescription());
            if (eventDetails.getStartDateTime() != null)
                event.setStartDateTime(eventDetails.getStartDateTime());
            if (eventDetails.getEndDateTime() != null)
                event.setEndDateTime(eventDetails.getEndDateTime());
            if (eventDetails.getLocation() != null)
                event.setLocation(eventDetails.getLocation());
            if (eventDetails.getMaxAttendees() != null)
                event.setMaxAttendees(eventDetails.getMaxAttendees());
            if (eventDetails.getAicteHours() != null)
                event.setAicteHours(eventDetails.getAicteHours());
            if (eventDetails.getAicteCategory() != null)
                event.setAicteCategory(eventDetails.getAicteCategory());
            if (eventDetails.getEventType() != null)
                event.setEventType(eventDetails.getEventType());
            if (eventDetails.getImageUrl() != null)
                event.setImageUrl(eventDetails.getImageUrl());
            if (eventDetails.getStatus() != null)
                event.setStatus(eventDetails.getStatus());

            // Update club
            if (eventDetails.getClub() != null && eventDetails.getClub().getId() != null) {
                Optional<Club> clubOpt = clubRepository.findById(eventDetails.getClub().getId());
                if (clubOpt.isPresent()) {
                    event.setClub(clubOpt.get());
                }
            }

            // Update creator
            if (eventDetails.getCreatedBy() != null) {
                Optional<User> creatorOpt = userRepository.findById(eventDetails.getCreatedBy());
                if (creatorOpt.isPresent()) {
                    event.setCreatedBy(eventDetails.getCreatedBy());
                }
            }

            return eventRepository.save(event);
        }
        throw new RuntimeException("Event not found with ID: " + id);
    }

    // Delete event
    @Transactional
    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Check if event is full
    public boolean isEventFull(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        return eventOpt.map(Event::isFull).orElse(false);
    }

    // Get available spots
    public int getAvailableSpots(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            return event.getMaxAttendees() != null ? event.getMaxAttendees() - event.getAttendeesCount() : 0;
        }
        return 0;
    }

    // Get high AICTE hours events (Optimized)
    public List<Event> getHighAicteHoursEvents(Integer threshold) {
        return eventRepository.findByAicteHoursGreaterThanEqual(threshold);
    }

    // Get events by club name (Fallback: Stream filter until advanced query added)
    public List<Event> getEventsByClubName(String clubName) {
        if (clubName == null || clubName.trim().isEmpty()) {
            return getAllEvents();
        }
        return eventRepository.findAll().stream()
                .filter(event -> event.getClub() != null && event.getClub().getName() != null &&
                        event.getClub().getName().toLowerCase().contains(clubName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Update event status
    @Transactional
    public boolean updateEventStatus(Long eventId, String status) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            eventOpt.get().setStatus(status);
            eventRepository.save(eventOpt.get());
            return true;
        }
        return false;
    }

    // Cancel event
    public boolean cancelEvent(Long eventId) {
        return updateEventStatus(eventId, "CANCELLED");
    }

    // Complete event
    public boolean completeEvent(Long eventId) {
        return updateEventStatus(eventId, "COMPLETED");
    }

    // Start event
    public boolean startEvent(Long eventId) {
        return updateEventStatus(eventId, "ONGOING");
    }

    // Get event count
    public long getEventCount() {
        return eventRepository.count();
    }

    // Get event count by status (Optimized)
    public long getEventCountByStatus(String status) {
        return eventRepository.findByStatus(status).size();
    }

    // Get total AICTE hours
    public Integer getTotalAicteHours() {
        return eventRepository.findAll().stream()
                .mapToInt(e -> e.getAicteHours() != null ? e.getAicteHours() : 0)
                .sum();
    }

    // Get average AICTE hours
    public Double getAverageAicteHours() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty())
            return 0.0;
        return (double) getTotalAicteHours() / events.size();
    }

    // Search events (Fallback: Stream filter until advanced query added)
    public List<Event> searchEvents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllEvents();
        }
        return eventRepository.findAll().stream()
                .filter(event -> (event.getTitle() != null
                        && event.getTitle().toLowerCase().contains(keyword.toLowerCase())) ||
                        (event.getDescription() != null
                                && event.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Get events for month
    public List<Event> getEventsForMonth(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        return getEventsByDateRange(startOfMonth, endOfMonth);
    }

    // Get events by type (Optimized)
    public List<Event> getEventsByType(String eventType) {
        return eventRepository.findByEventType(eventType);
    }

    // Get events by AICTE category (Optimized)
    public List<Event> getEventsByAicteCategory(String aicteCategory) {
        return eventRepository.findByAicteCategory(aicteCategory);
    }

    // Check if user can manage event (Fixed: No space in method name)
    public boolean canUserManageEvent(Long userId, Long eventId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (userOpt.isPresent() && eventOpt.isPresent()) {
            User user = userOpt.get();
            Event event = eventOpt.get();
            if (user.getRole() == User.Role.ADMIN || userId.equals(event.getCreatedBy())) {
                return true;
            }
        }
        return false;
    }

    // Get attendees count
    public int getAttendeesCount(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        return eventOpt.map(Event::getAttendeesCount).orElse(0);
    }

    // Is event upcoming
    public boolean isEventUpcoming(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        return eventOpt.map(Event::isUpcoming).orElse(false);
    }

    // Is event ongoing
    public boolean isEventOngoing(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        return eventOpt.map(Event::isOngoing).orElse(false);
    }

    // Is event completed
    public boolean isEventCompleted(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        return eventOpt.map(Event::isCompleted).orElse(false);
    }

    // Get events with images (Optimized)
    public List<Event> getEventsWithImages() {
        return eventRepository.findByImageUrlNotNull();
    }

    // Get events without images
    public List<Event> getEventsWithoutImages() {
        List<Event> all = eventRepository.findAll();
        List<Event> withImages = getEventsWithImages();
        return all.stream().filter(e -> !withImages.contains(e)).collect(Collectors.toList());
    }
}
