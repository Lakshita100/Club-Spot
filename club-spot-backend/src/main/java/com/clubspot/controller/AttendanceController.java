package com.clubspot.controller;

import com.clubspot.entity.Attendance;
import com.clubspot.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // Get all attendance records
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    // Get attendance by ID
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);
        return attendance.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Register for event
    @PostMapping("/register")
    public ResponseEntity<?> registerForEvent(@RequestBody Map<String, Long> request) {
        Long eventId = request.get("eventId");
        Long userId = request.get("userId");

        if (eventId == null || userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "eventId and userId are required"));
        }

        try {
            Attendance attendance = attendanceService.registerForEvent(eventId, userId);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Cancel registration
    @DeleteMapping("/cancel-registration")
    public ResponseEntity<?> cancelRegistration(@RequestBody Map<String, Long> request) {
        Long eventId = request.get("eventId");
        Long userId = request.get("userId");

        if (eventId == null || userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "eventId and userId are required"));
        }

        boolean cancelled = attendanceService.cancelRegistration(eventId, userId);
        if (cancelled) {
            return ResponseEntity.ok(Map.of("message", "Registration cancelled successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Failed to cancel registration"));
    }

    // Mark attendance by attendance ID
    @PutMapping("/{id}/mark-present")
    public ResponseEntity<?> markAttendance(@PathVariable Long id) {
        boolean marked = attendanceService.markAttendance(id);
        if (marked) {
            return ResponseEntity.ok(Map.of("message", "Attendance marked as present"));
        }
        return ResponseEntity.notFound().build();
    }

    // Mark attendance by event and user
    @PostMapping("/mark-by-event-user")
    public ResponseEntity<?> markAttendanceByEventAndUser(@RequestBody Map<String, Long> request) {
        Long eventId = request.get("eventId");
        Long userId = request.get("userId");

        if (eventId == null || userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "eventId and userId are required"));
        }

        boolean marked = attendanceService.markAttendanceByEventAndUser(eventId, userId);
        if (marked) {
            return ResponseEntity.ok(Map.of("message", "Attendance marked successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Failed to mark attendance"));
    }

    // Mark absent
    @PutMapping("/{id}/mark-absent")
    public ResponseEntity<?> markAbsent(@PathVariable Long id) {
        boolean marked = attendanceService.markAbsent(id);
        if (marked) {
            return ResponseEntity.ok(Map.of("message", "Attendance marked as absent"));
        }
        return ResponseEntity.notFound().build();
    }

    // Submit feedback
    @PutMapping("/{id}/feedback")
    public ResponseEntity<?> submitFeedback(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        String feedback = (String) request.get("feedback");
        Integer rating = (Integer) request.get("rating");

        boolean submitted = attendanceService.submitFeedback(id, feedback, rating);
        if (submitted) {
            return ResponseEntity.ok(Map.of("message", "Feedback submitted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Get attendances by event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Attendance>> getAttendancesByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByEvent(eventId));
    }

    // Get attendances by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getAttendancesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByUser(userId));
    }

    // Get user attendance history
    @GetMapping("/user/{userId}/history")
    public ResponseEntity<List<Attendance>> getUserAttendanceHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getUserAttendanceHistory(userId));
    }

    // Check if user is registered for event
    @GetMapping("/check-registration/user/{userId}/event/{eventId}")
    public ResponseEntity<Map<String, Boolean>> isUserRegisteredForEvent(@PathVariable Long userId,
            @PathVariable Long eventId) {
        boolean registered = attendanceService.isUserRegisteredForEvent(eventId, userId);
        return ResponseEntity.ok(Map.of("registered", registered));
    }

    // Get event attendance statistics
    @GetMapping("/event/{eventId}/stats")
    public ResponseEntity<Map<String, Long>> getEventAttendanceStats(@PathVariable Long eventId) {
        return ResponseEntity.ok(attendanceService.getEventAttendanceStats(eventId));
    }

    // Get event attendance rate
    @GetMapping("/event/{eventId}/attendance-rate")
    public ResponseEntity<Map<String, Double>> getEventAttendanceRate(@PathVariable Long eventId) {
        Double rate = attendanceService.getEventAttendanceRate(eventId);
        return ResponseEntity.ok(Map.of("attendanceRate", rate));
    }

    // Get user total AICTE hours
    @GetMapping("/user/{userId}/aicte-hours")
    public ResponseEntity<Map<String, Integer>> getUserTotalAicteHours(@PathVariable Long userId) {
        Integer aicteHours = attendanceService.getUserTotalAicteHours(userId);
        return ResponseEntity.ok(Map.of("totalAicteHours", aicteHours));
    }

    // Get user AICTE hours by category
    @GetMapping("/user/{userId}/aicte-hours/by-category")
    public ResponseEntity<Map<String, Integer>> getUserAicteHoursByCategory(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getUserAicteHoursByCategory(userId));
    }

    // Get top rated events
    @GetMapping("/top-rated-events")
    public ResponseEntity<List<Object[]>> getTopRatedEvents(@RequestParam(defaultValue = "4.0") Double minRating) {
        return ResponseEntity.ok(attendanceService.getTopRatedEvents(minRating));
    }

    // Get attendances with feedback
    @GetMapping("/with-feedback")
    public ResponseEntity<List<Attendance>> getAttendancesWithFeedback() {
        return ResponseEntity.ok(attendanceService.getAttendancesWithFeedback());
    }

    // Generate certificate
    @PutMapping("/{id}/generate-certificate")
    public ResponseEntity<?> generateCertificate(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String certificateUrl = request.get("certificateUrl");

        if (certificateUrl == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "certificateUrl is required"));
        }

        boolean generated = attendanceService.generateCertificate(id, certificateUrl);
        if (generated) {
            return ResponseEntity.ok(Map.of("message", "Certificate generated successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Get user certificates
    @GetMapping("/user/{userId}/certificates")
    public ResponseEntity<List<Attendance>> getUserCertificates(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getUserCertificates(userId));
    }

    // Get user attended events in date range
    @GetMapping("/user/{userId}/events-in-range")
    public ResponseEntity<List<Attendance>> getUserAttendedEventsInDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);

            return ResponseEntity.ok(attendanceService.getUserAttendedEventsInDateRange(userId, start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Get attendance summary
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getAttendanceSummary() {
        Map<String, Object> summary = new HashMap<>();
        // This would need to be implemented in the service
        summary.put("message", "Attendance summary endpoint - implementation needed in service");
        return ResponseEntity.ok(summary);
    }
}