package com.clubspot.repository;

import com.clubspot.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    // Find club by name
    Optional<Club> findByName(String name);

    // Find club by name (case insensitive)
    Optional<Club> findByNameIgnoreCase(String name);

    // Find clubs by category
    List<Club> findByCategory(String category);

    // Find clubs by category (case insensitive)
    List<Club> findByCategoryIgnoreCase(String category);

    // Find clubs containing name substring
    List<Club> findByNameContainingIgnoreCase(String name);

    // Find clubs by description containing keyword
    List<Club> findByDescriptionContainingIgnoreCase(String keyword);

    // Find clubs created within date range
    List<Club> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find recently created clubs
    List<Club> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);

    // Find recently updated clubs
    List<Club> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime date);

    // Check if club name exists
    Boolean existsByName(String name);

    // Check if club name exists (case insensitive)
    Boolean existsByNameIgnoreCase(String name);

    // Custom queries

    // Find clubs managed by a specific user (where user is an admin/president)
    @Query("SELECT c FROM Club c JOIN c.memberships m WHERE m.user.id = :userId AND m.role IN ('ADMIN', 'PRESIDENT', 'VICE_PRESIDENT')")
    List<Club> findClubsManagedByUser(@Param("userId") Long userId);

    // Find clubs where user is a member
    @Query("SELECT c FROM Club c JOIN c.memberships m WHERE m.user.id = :userId AND m.status = 'ACTIVE'")
    List<Club> findClubsByMember(@Param("userId") Long userId);

    // Find clubs where user has any membership (active or inactive)
    @Query("SELECT c FROM Club c JOIN c.memberships m WHERE m.user.id = :userId")
    List<Club> findAllClubsByUser(@Param("userId") Long userId);

    // Get club statistics
    @Query("SELECT c.id, c.name, COUNT(m) as memberCount FROM Club c LEFT JOIN c.memberships m WHERE m.status = 'ACTIVE' GROUP BY c.id, c.name")
    List<Object[]> getClubMembershipStats();

    // Get club with member count
    @Query("SELECT c, COUNT(m) as memberCount FROM Club c LEFT JOIN c.memberships m WHERE c.id = :clubId AND m.status = 'ACTIVE' GROUP BY c")
    Object[] getClubWithMemberCount(@Param("clubId") Long clubId);

    // Find most active clubs (by event count)
    @Query("SELECT c.id, c.name, COUNT(e) as eventCount FROM Club c LEFT JOIN c.events e WHERE e.createdAt >= :fromDate GROUP BY c.id, c.name ORDER BY eventCount DESC")
    List<Object[]> getMostActiveClubs(@Param("fromDate") LocalDateTime fromDate);

    // Find clubs by minimum member count
    @Query("SELECT c FROM Club c WHERE (SELECT COUNT(m) FROM Membership m WHERE m.club.id = c.id AND m.status = 'ACTIVE') >= :minMembers")
    List<Club> findClubsWithMinMembers(@Param("minMembers") Long minMembers);

    // Find clubs with upcoming events
    @Query("SELECT DISTINCT c FROM Club c JOIN c.events e WHERE e.startDateTime > :currentDate AND e.status = 'UPCOMING'")
    List<Club> findClubsWithUpcomingEvents(@Param("currentDate") LocalDateTime currentDate);

    // Find clubs by event count in date range
    @Query("SELECT c, COUNT(e) as eventCount FROM Club c LEFT JOIN c.events e WHERE e.startDateTime BETWEEN :startDate AND :endDate GROUP BY c ORDER BY eventCount DESC")
    List<Object[]> getClubsByEventCount(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Search clubs by name or category or description
    @Query("SELECT c FROM Club c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Club> searchClubs(@Param("searchTerm") String searchTerm);

    // Get club membership count
    @Query("SELECT COUNT(m) FROM Membership m WHERE m.club.id = :clubId AND m.status = 'ACTIVE'")
    Long getActiveMemberCount(@Param("clubId") Long clubId);

    // Get club admin count
    @Query("SELECT COUNT(m) FROM Membership m WHERE m.club.id = :clubId AND m.role IN ('ADMIN', 'PRESIDENT', 'VICE_PRESIDENT') AND m.status = 'ACTIVE'")
    Long getAdminCount(@Param("clubId") Long clubId);

    // Find clubs without events
    @Query("SELECT c FROM Club c WHERE c.id NOT IN (SELECT DISTINCT e.club.id FROM Event e)")
    List<Club> findClubsWithoutEvents();

    // Find inactive clubs (no recent events or activity)
    @Query("SELECT c FROM Club c WHERE c.id NOT IN (SELECT DISTINCT e.club.id FROM Event e WHERE e.startDateTime >= :thresholdDate)")
    List<Club> findInactiveClubs(@Param("thresholdDate") LocalDateTime thresholdDate);

    // Get clubs ordered by member count
    @Query("SELECT c FROM Club c LEFT JOIN c.memberships m WHERE m.status = 'ACTIVE' GROUP BY c ORDER BY COUNT(m) DESC")
    List<Club> findClubsOrderByMemberCount();

    // Get clubs by category with member count
    @Query("SELECT c.category, COUNT(DISTINCT c.id) as clubCount, COUNT(m) as totalMembers FROM Club c LEFT JOIN c.memberships m WHERE m.status = 'ACTIVE' GROUP BY c.category")
    List<Object[]> getClubStatsByCategory();

    // Find clubs created by date range
    @Query("SELECT c FROM Club c WHERE DATE(c.createdAt) = DATE(:date)")
    List<Club> findClubsCreatedOnDate(@Param("date") LocalDateTime date);

    // Get club activity summary
    @Query("SELECT c.id, c.name, COUNT(DISTINCT m.id) as memberCount, COUNT(DISTINCT e.id) as eventCount FROM Club c LEFT JOIN c.memberships m LEFT JOIN c.events e WHERE m.status = 'ACTIVE' GROUP BY c.id, c.name")
    List<Object[]> getClubActivitySummary();
}
