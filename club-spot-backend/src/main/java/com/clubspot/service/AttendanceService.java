package com.clubspot.service;

import com.clubspot.entity.Attendance;
import com.clubspot.entity.Event;
import com.clubspot.entity.User;
import com.clubspot.repository.AttendanceRepository;
import com.clubspot.repository.EventRepository;
import com.clubspot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new attendance record (register for event)
    public Attendance registerForEvent(Long eventId, Long userId) {
        // Check if event exists
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (!eventOpt.isPresent()) {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }

        // Check if user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        Event event = eventOpt.get();
        User user = userOpt.get();

        // Check if user is already registered
        Optional<Attendance> existingAttendance = attendanceRepository.findByEventIdAndUserId(eventId, userId);
        if (existingAttendance.isPresent()) {
            throw new IllegalStateException("User is already registered for this event");
        }

        // Check if event is full
        if (event.isFull()) {
            throw new IllegalStateException("Event has reached maximum capacity");
        }

        // Check if event is still accepting registrations
        if (!event.isUpcoming()) {
            throw new IllegalStateException("Registration is closed for this event");
        }

        // Create attendance record
        Attendance attendance = new Attendance(event, user);
        return attendanceRepository.save(attendance);
    }

    // Cancel registration
    public boolean cancelRegistration(Long eventId, Long userId) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findByEventIdAndUserId(eventId, userId);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            if ("REGISTERED".equals(attendance.getStatus())) {
                attendance.cancel();
                attendanceRepository.save(attendance);
                return true;
            }
        }
        return false;
    }

    // Mark attendance (for event organizers)
    public boolean markAttendance(Long attendanceId) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(attendanceId);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            attendance.markAttended();
            attendanceRepository.save(attendance);
            return true;
        }
        return false;
    }

    // Mark attendance by event and user ID
    public boolean markAttendanceByEventAndUser(Long eventId, Long userId) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findByEventIdAndUserId(eventId, userId);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            attendance.markAttended();
            attendanceRepository.save(attendance);
            return true;
        }
        return false;
    }

    // Mark as absent
    public boolean markAbsent(Long attendanceId) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(attendanceId);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            attendance.markAbsent();
            attendanceRepository.save(attendance);
            return true;
        }
        return false;
    }

    // Submit feedback and rating
    public boolean submitFeedback(Long attendanceId, String feedback, Integer rating) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(attendanceId);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            if (attendance.isAttended()) {
                attendance.setFeedback(feedback);
                if (rating != null && rating >= 1 && rating <= 5) {
                    attendance.setRating(rating);
                }
                attendanceRepository.save(attendance);
                return true;
            }
        }
        return false;
    }

    // Get all attendance records
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    // Get attendance by ID
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    // Get attendance records by event
    public List<Attendance> getAttendancesByEvent(Long eventId) {
        return attendanceRepository.findByEventId(eventId);
    }

    // Get attendance records by user
    public List<Attendance> getAttendancesByUser(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    // Get user's attendance history
    public List<Attendance> getUserAttendanceHistory(Long userId) {
        return attendanceRepository.getUserAttendanceHistory(userId);
    }

    // Check if user is registered for event
    public boolean isUserRegisteredForEvent(Long eventId, Long userId) {
        return attendanceRepository.isUserRegisteredForEvent(eventId, userId);
    }

    // Get attendance statistics for an event
    public Map<String, Long> getEventAttendanceStats(Long eventId) {
        List<Object[]> stats = attendanceRepository.getAttendanceStatsByEvent(eventId);
        Map<String, Long> result = new HashMap<>();

        for (Object[] stat : stats) {
            String status = (String) stat[0];
            Long count = (Long) stat[1];
            result.put(status, count);
        }

        return result;
    }

    // Get attendance rate for event
    public Double getEventAttendanceRate(Long eventId) {
        return attendanceRepository.getAttendanceRateForEvent(eventId);
    }

    // Get total AICTE hours for user
    public Integer getUserTotalAicteHours(Long userId) {
        return attendanceRepository.getTotalAicteHoursByUser(userId);
    }

    // Get AICTE hours by category for user
    public Map<String, Integer> getUserAicteHoursByCategory(Long userId) {
        List<Object[]> categoryHours = attendanceRepository.getAicteHoursByCategoryForUser(userId);
        Map<String, Integer> result = new HashMap<>();

        for (Object[] categoryHour : categoryHours) {
            String category = (String) categoryHour[0];
            Integer hours = ((Number) categoryHour[1]).intValue();
            result.put(category, hours);
        }

        return result;
    }

    // Get top rated events
    public List<Object[]> getTopRatedEvents(Double minRating) {
        return attendanceRepository.getTopRatedEvents(minRating);
    }

    // Get attendances with feedback
    public List<Attendance> getAttendancesWithFeedback() {
        return attendanceRepository.getAttendanceWithFeedback();
    }

    // Generate certificate for attendance
    public boolean generateCertificate(Long attendanceId, String certificateUrl) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(attendanceId);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            if (attendance.isAttended()) {
                attendance.setCertificateGenerated(true);
                attendance.setCertificateUrl(certificateUrl);
                attendanceRepository.save(attendance);
                return true;
            }
        }
        return false;
    }

    // Get user's certificates
    public List<Attendance> getUserCertificates(Long userId) {
        return attendanceRepository.findByUserIdAndCertificateGeneratedTrue(userId);
    }

    // Get events attended by user in date range
    public List<Attendance> getUserAttendedEventsInDateRange(Long userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return attendanceRepository.getUserAttendedEventsInDateRange(userId, startDate, endDate);
    }

    // Delete attendance record
    public boolean deleteAttendance(Long id) {
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update attendance record
    public Attendance updateAttendance(Long id, Attendance attendanceDetails) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(id);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();

            if (attendanceDetails.getStatus() != null) {
                attendance.setStatus(attendanceDetails.getStatus());
            }
            if (attendanceDetails.getFeedback() != null) {
                attendance.setFeedback(attendanceDetails.getFeedback());
            }
            if (attendanceDetails.getRating() != null) {
                attendance.setRating(attendanceDetails.getRating());
            }
            if (attendanceDetails.getAttendanceTime() != null) {
                attendance.setAttendanceTime(attendanceDetails.getAttendanceTime());
            }

            return attendanceRepository.save(attendance);
        }
        return null;
    }
}
