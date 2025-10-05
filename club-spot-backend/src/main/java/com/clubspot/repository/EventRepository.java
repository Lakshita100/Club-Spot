package com.clubspot.repository;

import com.clubspot.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Fixed Derived Query: Renamed to match Event.java's 'startDateTime' field
    List<Event> findByClubIdAndStartDateTimeAfter(Long clubId, LocalDateTime now);

    // Fixed @Query: Uses 'startDateTime'; filters upcoming events (future only)
    @Query("SELECT e FROM Event e WHERE e.club.id = :clubId AND e.startDateTime > CURRENT_TIMESTAMP ORDER BY e.startDateTime ASC")
    List<Event> findUpcomingEventsByClub(@Param("clubId") Long clubId);

    // Additional Queries for Optimization (DB-level filtering; append these)
    @Query("SELECT e FROM Event e WHERE e.club.id = :clubId AND e.status = :status ORDER BY e.startDateTime ASC")
    List<Event> findByClubIdAndStatus(@Param("clubId") Long clubId, @Param("status") String status);

    @Query("SELECT e FROM Event e WHERE e.startDateTime <= CURRENT_TIMESTAMP AND e.endDateTime >= CURRENT_TIMESTAMP AND e.club.id = :clubId")
    List<Event> findOngoingEventsByClub(@Param("clubId") Long clubId);

    @Query("SELECT e FROM Event e WHERE e.status = :status ORDER BY e.startDateTime ASC")
    List<Event> findByStatus(@Param("status") String status);

    @Query("SELECT e FROM Event e WHERE e.createdBy = :creatorId ORDER BY e.startDateTime DESC")
    List<Event> findByCreatedBy(@Param("creatorId") Long creatorId);

    @Query("SELECT e FROM Event e WHERE e.startDateTime > :startDate AND e.startDateTime < :endDate ORDER BY e.startDateTime ASC")
    List<Event> findByStartDateTimeBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY e.startDateTime ASC")
    List<Event> searchByTitleOrDescription(@Param("keyword") String keyword);

    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType ORDER BY e.startDateTime ASC")
    List<Event> findByEventType(@Param("eventType") String eventType);

    @Query("SELECT e FROM Event e WHERE e.aicteCategory = :aicteCategory ORDER BY e.startDateTime ASC")
    List<Event> findByAicteCategory(@Param("aicteCategory") String aicteCategory);

    @Query("SELECT e FROM Event e WHERE e.aicteHours >= :threshold ORDER BY e.aicteHours DESC")
    List<Event> findByAicteHoursGreaterThanEqual(@Param("threshold") Integer threshold);

    @Query("SELECT e FROM Event e WHERE LOWER(e.club.name) LIKE LOWER(CONCAT('%', :clubName, '%')) ORDER BY e.startDateTime ASC")
    List<Event> findByClubNameContaining(@Param("clubName") String clubName);

    @Query("SELECT e FROM Event e WHERE e.imageUrl IS NOT NULL AND e.imageUrl != '' ORDER BY e.startDateTime ASC")
    List<Event> findByImageUrlNotNull();

    // Derived queries (simple ones)
    List<Event> findByAicteCategoryAndClubId(String aicteCategory, Long clubId);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.club.id = :clubId AND e.startDateTime > CURRENT_TIMESTAMP")
    Long countUpcomingEventsByClub(@Param("clubId") Long clubId);
}
