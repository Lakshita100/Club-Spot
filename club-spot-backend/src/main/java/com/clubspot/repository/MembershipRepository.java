package com.clubspot.repository;

import com.clubspot.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    // Find membership by club and user
    Optional<Membership> findByClubIdAndUserId(Long clubId, Long userId);

    // Find memberships by club ID
    List<Membership> findByClubId(Long clubId);

    // Find memberships by user ID
    List<Membership> findByUserId(Long userId);

    // Find memberships by status
    List<Membership> findByStatus(String status);

    // Find memberships by role
    List<Membership> findByRole(String role);

    // Find memberships by club and status
    List<Membership> findByClubIdAndStatus(Long clubId, String status);

    // Find memberships by user and status
    List<Membership> findByUserIdAndStatus(Long userId, String status);

    // Find memberships by club and role
    List<Membership> findByClubIdAndRole(Long clubId, String role);

    // Find memberships by user and role
    List<Membership> findByUserIdAndRole(Long userId, String role);

    // Find active memberships by club
    List<Membership> findByClubIdAndStatusOrderByJoinDateAsc(Long clubId, String status);

    // Find active memberships by user
    List<Membership> findByUserIdAndStatusOrderByJoinDateAsc(Long userId, String status);

    // Find memberships within date range
    List<Membership> findByJoinDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find memberships by club within date range
    List<Membership> findByClubIdAndJoinDateBetween(Long clubId, LocalDateTime startDate, LocalDateTime endDate);

    // Find recent memberships
    List<Membership> findByJoinDateAfterOrderByJoinDateDesc(LocalDateTime date);

    // Find memberships with leave date
    List<Membership> findByLeaveDateIsNotNull();

    // Find expired memberships
    List<Membership> findByLeaveDateBefore(LocalDateTime date);

    // Custom queries

    // Find club admins
    @Query("SELECT m FROM Membership m WHERE m.club.id = :clubId AND m.role IN ('ADMIN', 'PRESIDENT', 'VICE_PRESIDENT', 'SECRETARY', 'TREASURER') AND m.status = 'ACTIVE'")
    List<Membership> findClubAdmins(@Param("clubId") Long clubId);

    // Find club members (non-admin)
    @Query("SELECT m FROM Membership m WHERE m.club.id = :clubId AND m.role = 'MEMBER' AND m.status = 'ACTIVE'")
    List<Membership> findClubMembers(@Param("clubId") Long clubId);

    // Find all active memberships for a club with user details
    @Query("SELECT m FROM Membership m JOIN FETCH m.user u WHERE m.club.id = :clubId AND m.status = 'ACTIVE' ORDER BY m.joinDate ASC")
    List<Membership> findActiveClubMembersWithUserDetails(@Param("clubId") Long clubId);

    // Find all active memberships for a user with club details
    @Query("SELECT m FROM Membership m JOIN FETCH m.club c WHERE m.user.id = :userId AND m.status = 'ACTIVE' ORDER BY m.joinDate ASC")
    List<Membership> findActiveUserMembershipsWithClubDetails(@Param("userId") Long userId);

    // Count active members in a club
    @Query("SELECT COUNT(m) FROM Membership m WHERE m.club.id = :clubId AND m.status = 'ACTIVE'")
    Long countActiveMembersByClub(@Param("clubId") Long clubId);

    // Count active memberships for a user
    @Query("SELECT COUNT(m) FROM Membership m WHERE m.user.id = :userId AND m.status = 'ACTIVE'")
    Long countActiveMembershipsByUser(@Param("userId") Long userId);

    // Find memberships by contribution score range
    @Query("SELECT m FROM Membership m WHERE m.contributionScore BETWEEN :minScore AND :maxScore ORDER BY m.contributionScore DESC")
    List<Membership> findByContributionScoreRange(@Param("minScore") Integer minScore,
            @Param("maxScore") Integer maxScore);

    // Find top contributors in a club
    @Query("SELECT m FROM Membership m WHERE m.club.id = :clubId AND m.status = 'ACTIVE' ORDER BY m.contributionScore DESC")
    List<Membership> findTopContributorsByClub(@Param("clubId") Long clubId);

    // Find inactive members (not active recently)
    @Query("SELECT m FROM Membership m WHERE m.status = 'ACTIVE' AND m.lastActiveDate < :thresholdDate")
    List<Membership> findInactiveMembers(@Param("thresholdDate") LocalDateTime thresholdDate);

    // Find inactive members in a specific club
    @Query("SELECT m FROM Membership m WHERE m.club.id = :clubId AND m.status = 'ACTIVE' AND m.lastActiveDate < :thresholdDate")
    List<Membership> findInactiveMembersByClub(@Param("clubId") Long clubId,
            @Param("thresholdDate") LocalDateTime thresholdDate);

    // Find long-term members (joined before specific date)
    @Query("SELECT m FROM Membership m WHERE m.joinDate < :date AND m.status = 'ACTIVE' ORDER BY m.joinDate ASC")
    List<Membership> findLongTermMembers(@Param("date") LocalDateTime date);

    // Find new members (joined after specific date)
    @Query("SELECT m FROM Membership m WHERE m.joinDate > :date AND m.status = 'ACTIVE' ORDER BY m.joinDate DESC")
    List<Membership> findNewMembers(@Param("date") LocalDateTime date);

    // Get membership statistics by club
    @Query("SELECT m.club.id, m.status, COUNT(m) FROM Membership m WHERE m.club.id = :clubId GROUP BY m.club.id, m.status")
    List<Object[]> getMembershipStatsByClub(@Param("clubId") Long clubId);

    // Get membership statistics by role
    @Query("SELECT m.role, COUNT(m) FROM Membership m WHERE m.club.id = :clubId AND m.status = 'ACTIVE' GROUP BY m.role")
    List<Object[]> getMembershipStatsByRole(@Param("clubId") Long clubId);

    // Find memberships with specific position
    @Query("SELECT m FROM Membership m WHERE m.position IS NOT NULL AND LOWER(m.position) LIKE LOWER(CONCAT('%', :position, '%'))")
    List<Membership> findByPositionContaining(@Param("position") String position);

    // Check if user is admin of any club
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Membership m WHERE m.user.id = :userId AND m.role IN ('ADMIN', 'PRESIDENT', 'VICE_PRESIDENT') AND m.status = 'ACTIVE'")
    Boolean isUserAdminOfAnyClub(@Param("userId") Long userId);

    // Check if user is admin of specific club
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Membership m WHERE m.club.id = :clubId AND m.user.id = :userId AND m.role IN ('ADMIN', 'PRESIDENT', 'VICE_PRESIDENT') AND m.status = 'ACTIVE'")
    Boolean isUserAdminOfClub(@Param("clubId") Long clubId, @Param("userId") Long userId);

    // Check if user is member of specific club
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Membership m WHERE m.club.id = :clubId AND m.user.id = :userId AND m.status = 'ACTIVE'")
    Boolean isUserMemberOfClub(@Param("clubId") Long clubId, @Param("userId") Long userId);

    // Find members who joined in a specific month/year
    @Query("SELECT m FROM Membership m WHERE YEAR(m.joinDate) = :year AND MONTH(m.joinDate) = :month")
    List<Membership> findMembersJoinedInMonth(@Param("year") Integer year, @Param("month") Integer month);

    // Get average membership duration for a club
    @Query("SELECT AVG(DATEDIFF(COALESCE(m.leaveDate, CURRENT_DATE), m.joinDate)) FROM Membership m WHERE m.club.id = :clubId")
    Double getAverageMembershipDuration(@Param("clubId") Long clubId);

    // Find memberships with notes
    @Query("SELECT m FROM Membership m WHERE m.notes IS NOT NULL AND m.notes != '' ORDER BY m.joinDate DESC")
    List<Membership> findMembershipsWithNotes();

    // Get membership history for a user
    @Query("SELECT m FROM Membership m JOIN FETCH m.club c WHERE m.user.id = :userId ORDER BY m.joinDate DESC")
    List<Membership> getUserMembershipHistory(@Param("userId") Long userId);

    // Get user's current active memberships
    @Query("SELECT m FROM Membership m JOIN FETCH m.club c WHERE m.user.id = :userId AND m.status = 'ACTIVE' ORDER BY m.joinDate DESC")
    List<Membership> getUserActiveMemberships(@Param("userId") Long userId);

    // Find memberships ending soon (for clubs with membership terms)
    @Query("SELECT m FROM Membership m WHERE m.leaveDate IS NOT NULL AND m.leaveDate BETWEEN :startDate AND :endDate")
    List<Membership> findMembershipsEndingSoon(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
