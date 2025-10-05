package com.clubspot.repository;

import com.clubspot.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find attendance by event ID
    List<Attendance> findByEventId(Long eventId);

    // Find attendance by user ID
    List<Attendance> findByUserId(Long userId);

    // Find attendance by event and user
    Optional<Attendance> findByEventIdAndUserId(Long eventId, Long userId);

    // Find attendance by status
    List<Attendance> findByStatus(String status);

    // Find attendance by event and status
    List<Attendance> findByEventIdAndStatus(Long eventId, String status);

    // Find attendance by user and status
    List<Attendance> findByUserIdAndStatus(Long userId, String status);

    // Find attendance records with AICTE hours awarded
    List<Attendance> findByAicteHoursAwardedIsNotNull();

    // Find attendance records by user with AICTE hours
    List<Attendance> findByUserIdAndAicteHoursAwardedIsNotNull(Long userId);

    // Find attendance records with certificate generated
    List<Attendance> findByCertificateGeneratedTrue();

    // Find attendance records by user with certificates
    List<Attendance> findByUserIdAndCertificateGeneratedTrue(Long userId);

    // Find attendance records by rating
    List<Attendance> findByRating(Integer rating);

    // Find attendance records by event with rating
    List<Attendance> findByEventIdAndRatingIsNotNull(Long eventId);

    // Find attendance records within date range
    List<Attendance> findByRegistrationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find attendance records by user within date range
    List<Attendance> findByUserIdAndRegistrationDateBetween(Long userId, LocalDateTime startDate,
            LocalDateTime endDate);

    // Custom queries

    // Count attendees for an event
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.event.id = :eventId AND a.status = 'ATTENDED'")
    Long countAttendeesForEvent(@Param("eventId") Long eventId);

    // Count registered users for an event
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.event.id = :eventId AND a.status = 'REGISTERED'")
    Long countRegisteredForEvent(@Param("eventId") Long eventId);

    // Get attendance statistics for an event
    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.event.id = :eventId GROUP BY a.status")
    List<Object[]> getAttendanceStatsByEvent(@Param("eventId") Long eventId);

    // Get user's attendance history with event details
    @Query("SELECT a FROM Attendance a JOIN FETCH a.event e WHERE a.user.id = :userId ORDER BY a.registrationDate DESC")
    List<Attendance> getUserAttendanceHistory(@Param("userId") Long userId);

    // Get events attended by user in a date range
    @Query("SELECT a FROM Attendance a JOIN FETCH a.event e WHERE a.user.id = :userId AND a.status = 'ATTENDED' AND e.startDateTime BETWEEN :startDate AND :endDate ORDER BY e.startDateTime DESC")
    List<Attendance> getUserAttendedEventsInDateRange(@Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Get total AICTE hours earned by user
    @Query("SELECT COALESCE(SUM(a.aicteHoursAwarded), 0) FROM Attendance a WHERE a.user.id = :userId AND a.status = 'ATTENDED'")
    Integer getTotalAicteHoursByUser(@Param("userId") Long userId);

    // Get AICTE hours by category for user
    @Query("SELECT e.aicteCategory, COALESCE(SUM(a.aicteHoursAwarded), 0) FROM Attendance a JOIN a.event e WHERE a.user.id = :userId AND a.status = 'ATTENDED' AND e.aicteCategory IS NOT NULL GROUP BY e.aicteCategory")
    List<Object[]> getAicteHoursByCategoryForUser(@Param("userId") Long userId);

    // Get top rated events
    @Query("SELECT a.event.id, AVG(a.rating) as avgRating FROM Attendance a WHERE a.rating IS NOT NULL GROUP BY a.event.id HAVING AVG(a.rating) >= :minRating ORDER BY avgRating DESC")
    List<Object[]> getTopRatedEvents(@Param("minRating") Double minRating);

    // Get attendance records with feedback
    @Query("SELECT a FROM Attendance a WHERE a.feedback IS NOT NULL AND a.feedback != '' ORDER BY a.registrationDate DESC")
    List<Attendance> getAttendanceWithFeedback();

    // Get recent attendance records
    @Query("SELECT a FROM Attendance a JOIN FETCH a.event e JOIN FETCH a.user u ORDER BY a.registrationDate DESC")
    List<Attendance> getRecentAttendanceRecords();

    // Check if user is registered for event
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Attendance a WHERE a.event.id = :eventId AND a.user.id = :userId")
    Boolean isUserRegisteredForEvent(@Param("eventId") Long eventId, @Param("userId") Long userId);

    // Get attendance rate for an event
    @Query("SELECT (CAST(SUM(CASE WHEN a.status = 'ATTENDED' THEN 1 ELSE 0 END) AS DOUBLE) / COUNT(a)) * 100 FROM Attendance a WHERE a.event.id = :eventId")
    Double getAttendanceRateForEvent(@Param("eventId") Long eventId);
}
