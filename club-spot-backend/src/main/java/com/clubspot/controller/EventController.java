package com.clubspot.controller;

import com.clubspot.entity.Event;
import com.clubspot.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

    @Autowired
    private EventService eventService;

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new event
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            Event savedEvent = eventService.createEvent(event);
            return ResponseEntity.ok(savedEvent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update event
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            if (updatedEvent != null) {
                return ResponseEntity.ok(updatedEvent);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Event deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Get upcoming events for a specific club
    @GetMapping("/club/{clubId}/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEventsByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(eventService.getUpcomingEventsByClub(clubId));
    }

    // Get events by club ID
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Event>> getEventsByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(eventService.getEventsByClubId(clubId));
    }

    // Get upcoming events (all clubs)
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    // Get past events
    @GetMapping("/past")
    public ResponseEntity<List<Event>> getPastEvents() {
        return ResponseEntity.ok(eventService.getPastEvents());
    }

    // Get ongoing events
    @GetMapping("/ongoing")
    public ResponseEntity<List<Event>> getOngoingEvents() {
        return ResponseEntity.ok(eventService.getOngoingEvents());
    }

    // Search/discover events by keyword
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String keyword) {
        return ResponseEntity.ok(eventService.searchEvents(keyword));
    }

    // Get events by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Event>> getEventsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(eventService.getEventsByStatus(status));
    }

    // Get events by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Event>> getEventsByType(@PathVariable String type) {
        return ResponseEntity.ok(eventService.getEventsByType(type));
    }

    // Get events by AICTE category
    @GetMapping("/aicte-category/{category}")
    public ResponseEntity<List<Event>> getEventsByAicteCategory(@PathVariable String category) {
        return ResponseEntity.ok(eventService.getEventsByAicteCategory(category));
    }

    // Get events by creator
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<Event>> getEventsByCreator(@PathVariable Long creatorId) {
        return ResponseEntity.ok(eventService.getEventsByCreator(creatorId));
    }

    // Get events for a specific month
    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<Event>> getEventsForMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(eventService.getEventsForMonth(year, month));
    }

    // Get events with high AICTE hours
    @GetMapping("/high-aicte-hours")
    public ResponseEntity<List<Event>> getHighAicteHoursEvents(@RequestParam(defaultValue = "5") Integer threshold) {
        return ResponseEntity.ok(eventService.getHighAicteHoursEvents(threshold));
    }

    // Get events by club name
    @GetMapping("/club-name/{clubName}")
    public ResponseEntity<List<Event>> getEventsByClubName(@PathVariable String clubName) {
        return ResponseEntity.ok(eventService.getEventsByClubName(clubName));
    }

    // Update event status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateEventStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        boolean updated = eventService.updateEventStatus(id, request.get("status"));
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "Event status updated successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Cancel event
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelEvent(@PathVariable Long id) {
        boolean cancelled = eventService.cancelEvent(id);
        if (cancelled) {
            return ResponseEntity.ok(Map.of("message", "Event cancelled successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Complete event
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeEvent(@PathVariable Long id) {
        boolean completed = eventService.completeEvent(id);
        if (completed) {
            return ResponseEntity.ok(Map.of("message", "Event completed successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Start event
    @PutMapping("/{id}/start")
    public ResponseEntity<?> startEvent(@PathVariable Long id) {
        boolean started = eventService.startEvent(id);
        if (started) {
            return ResponseEntity.ok(Map.of("message", "Event started successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Check if event is full
    @GetMapping("/{id}/is-full")
    public ResponseEntity<Map<String, Boolean>> isEventFull(@PathVariable Long id) {
        boolean isFull = eventService.isEventFull(id);
        return ResponseEntity.ok(Map.of("isFull", isFull));
    }

    // Get available spots
    @GetMapping("/{id}/available-spots")
    public ResponseEntity<Map<String, Integer>> getAvailableSpots(@PathVariable Long id) {
        int availableSpots = eventService.getAvailableSpots(id);
        return ResponseEntity.ok(Map.of("availableSpots", availableSpots));
    }

    // Get attendees count
    @GetMapping("/{id}/attendees-count")
    public ResponseEntity<Map<String, Integer>> getAttendeesCount(@PathVariable Long id) {
        int attendeesCount = eventService.getAttendeesCount(id);
        return ResponseEntity.ok(Map.of("attendeesCount", attendeesCount));
    }

    // Get event statistics for all events
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getEventStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEvents", eventService.getEventCount());
        stats.put("upcomingEvents", eventService.getEventCountByStatus("UPCOMING"));
        stats.put("ongoingEvents", eventService.getEventCountByStatus("ONGOING"));
        stats.put("completedEvents", eventService.getEventCountByStatus("COMPLETED"));
        stats.put("cancelledEvents", eventService.getEventCountByStatus("CANCELLED"));
        stats.put("totalAicteHours", eventService.getTotalAicteHours());
        stats.put("averageAicteHours", eventService.getAverageAicteHours());
        return ResponseEntity.ok(stats);
    }

    // Get events with images
    @GetMapping("/with-images")
    public ResponseEntity<List<Event>> getEventsWithImages() {
        return ResponseEntity.ok(eventService.getEventsWithImages());
    }

    // Get events without images
    @GetMapping("/without-images")
    public ResponseEntity<List<Event>> getEventsWithoutImages() {
        return ResponseEntity.ok(eventService.getEventsWithoutImages());
    }

    // Check if user can manage event
    @GetMapping("/{eventId}/can-manage/{userId}")
    public ResponseEntity<Map<String, Boolean>> canUserManageEvent(@PathVariable Long eventId,
            @PathVariable Long userId) {
        boolean canManage = eventService.canUserManageEvent(userId, eventId);
        return ResponseEntity.ok(Map.of("canManage", canManage));
    }

    // Check event status
    @GetMapping("/{id}/status-check")
    public ResponseEntity<Map<String, Object>> checkEventStatus(@PathVariable Long id) {
        Map<String, Object> statusInfo = new HashMap<>();
        statusInfo.put("isUpcoming", eventService.isEventUpcoming(id));
        statusInfo.put("isOngoing", eventService.isEventOngoing(id));
        statusInfo.put("isCompleted", eventService.isEventCompleted(id));
        statusInfo.put("isFull", eventService.isEventFull(id));
        return ResponseEntity.ok(statusInfo);
    }
}
