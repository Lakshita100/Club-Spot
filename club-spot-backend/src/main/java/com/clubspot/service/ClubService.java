package com.clubspot.service;

import com.clubspot.entity.Club;
import com.clubspot.entity.User;
import com.clubspot.entity.Membership;
import com.clubspot.repository.ClubRepository;
import com.clubspot.repository.UserRepository;
import com.clubspot.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    // Create a new club
    public Club createClub(Club club, Long creatorId) {
        // Validate creator exists
        Optional<User> creatorOpt = userRepository.findById(creatorId);
        if (!creatorOpt.isPresent()) {
            throw new IllegalArgumentException("Creator not found with ID: " + creatorId);
        }

        User creator = creatorOpt.get();

        // Set creation details (handled by @PrePersist)
        if (club.getCreatedAt() == null) {
            club.setCreatedAt(LocalDateTime.now());
        }
        if (club.getUpdatedAt() == null) {
            club.setUpdatedAt(LocalDateTime.now());
        }
        if (club.getIsActive() == null) {
            club.setIsActive(true);
        }

        // Save club
        Club savedClub = clubRepository.save(club);

        // Create membership for creator as PRESIDENT
        Membership creatorMembership = new Membership(savedClub, creator);
        creatorMembership.setRole("PRESIDENT");
        creatorMembership.setStatus("ACTIVE");
        membershipRepository.save(creatorMembership);

        return savedClub;
    }

    // Get all clubs
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    // Get active clubs
    public List<Club> getActiveClubs() {
        return clubRepository.findAll().stream()
                .filter(Club::isActive)
                .collect(Collectors.toList());
    }

    // Get club by ID
    public Optional<Club> getClubById(Long id) {
        return clubRepository.findById(id);
    }

    // Get clubs by category
    public List<Club> getClubsByCategory(String category) {
        return clubRepository.findByCategory(category);
    }

    // Search clubs by name
    public List<Club> searchClubsByName(String name) {
        return clubRepository.findByNameContainingIgnoreCase(name);
    }

    // Search clubs with multiple criteria
    public List<Club> searchClubs(String keyword) {
        return clubRepository.searchClubs(keyword);
    }

    // Get clubs with upcoming events
    public List<Club> getClubsWithUpcomingEvents() {
        return clubRepository.findClubsWithUpcomingEvents(LocalDateTime.now());
    }

    // Get clubs created in date range
    public List<Club> getClubsCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return clubRepository.findByCreatedAtBetween(startDate, endDate);
    }

    // Get clubs managed by user
    public List<Club> getClubsManagedByUser(Long userId) {
        return clubRepository.findClubsManagedByUser(userId);
    }

    // Get clubs where user is a member
    public List<Club> getClubsByMember(Long userId) {
        return clubRepository.findClubsByMember(userId);
    }

    // Update club information
    public Club updateClub(Long id, Club clubDetails) {
        Optional<Club> clubOpt = clubRepository.findById(id);
        if (clubOpt.isPresent()) {
            Club club = clubOpt.get();

            if (clubDetails.getName() != null) {
                club.setName(clubDetails.getName());
            }
            if (clubDetails.getDescription() != null) {
                club.setDescription(clubDetails.getDescription());
            }
            if (clubDetails.getCategory() != null) {
                club.setCategory(clubDetails.getCategory());
            }
            if (clubDetails.getContactEmail() != null) {
                club.setContactEmail(clubDetails.getContactEmail());
            }
            if (clubDetails.getContactPhone() != null) {
                club.setContactPhone(clubDetails.getContactPhone());
            }
            if (clubDetails.getLogoUrl() != null) {
                club.setLogoUrl(clubDetails.getLogoUrl());
            }
            if (clubDetails.getBannerUrl() != null) {
                club.setBannerUrl(clubDetails.getBannerUrl());
            }
            if (clubDetails.getSocialLinks() != null) {
                club.setSocialLinks(clubDetails.getSocialLinks());
            }
            if (clubDetails.getMeetingSchedule() != null) {
                club.setMeetingSchedule(clubDetails.getMeetingSchedule());
            }
            if (clubDetails.getLocation() != null) {
                club.setLocation(clubDetails.getLocation());
            }

            club.setUpdatedAt(LocalDateTime.now());
            return clubRepository.save(club);
        }
        return null;
    }

    // Activate club
    public boolean activateClub(Long id) {
        Optional<Club> clubOpt = clubRepository.findById(id);
        if (clubOpt.isPresent()) {
            Club club = clubOpt.get();
            club.activate();
            clubRepository.save(club);
            return true;
        }
        return false;
    }

    // Deactivate club
    public boolean deactivateClub(Long id) {
        Optional<Club> clubOpt = clubRepository.findById(id);
        if (clubOpt.isPresent()) {
            Club club = clubOpt.get();
            club.deactivate();
            clubRepository.save(club);
            return true;
        }
        return false;
    }

    // Delete club
    public boolean deleteClub(Long id) {
        if (clubRepository.existsById(id)) {
            clubRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get club statistics
    public Map<String, Object> getClubStatistics(Long clubId) {
        Optional<Club> clubOpt = clubRepository.findById(clubId);
        if (!clubOpt.isPresent()) {
            throw new IllegalArgumentException("Club not found with ID: " + clubId);
        }

        Map<String, Object> stats = new HashMap<>();

        // Active member count
        Long activeMemberCount = clubRepository.getActiveMemberCount(clubId);
        stats.put("activeMemberCount", activeMemberCount);

        // Admin count
        Long adminCount = clubRepository.getAdminCount(clubId);
        stats.put("adminCount", adminCount);

        return stats;
    }

    // Check if user can join club (validation logic)
    public boolean canUserJoinClub(Long clubId, Long userId) {
        // Check if club exists and is active
        Optional<Club> clubOpt = clubRepository.findById(clubId);
        if (!clubOpt.isPresent() || !clubOpt.get().isActive()) {
            return false;
        }

        // Check if user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return false;
        }

        // Check if user is already a member
        List<Club> userClubs = clubRepository.findClubsByMember(userId);
        return !userClubs.stream().anyMatch(club -> club.getId().equals(clubId));
    }

    // Check if user is admin of club
    public boolean isUserClubAdmin(Long clubId, Long userId) {
        List<Club> adminClubs = clubRepository.findClubsManagedByUser(userId);
        return adminClubs.stream().anyMatch(club -> club.getId().equals(clubId));
    }

    // Get club member count
    public Long getClubMemberCount(Long clubId) {
        return clubRepository.getActiveMemberCount(clubId);
    }

    // Get recently created clubs
    public List<Club> getRecentlyCreatedClubs(int limit) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return clubRepository.findByCreatedAtAfterOrderByCreatedAtDesc(oneMonthAgo)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get clubs ordered by member count
    public List<Club> getClubsOrderedByMemberCount() {
        return clubRepository.findClubsOrderByMemberCount();
    }

    // Get clubs without events
    public List<Club> getClubsWithoutEvents() {
        return clubRepository.findClubsWithoutEvents();
    }

    // Get inactive clubs
    public List<Club> getInactiveClubs(LocalDateTime thresholdDate) {
        return clubRepository.findInactiveClubs(thresholdDate);
    }

    // Get club activity summary
    public List<Object[]> getClubActivitySummary() {
        return clubRepository.getClubActivitySummary();
    }

    // Get membership statistics
    public List<Object[]> getClubMembershipStats() {
        return clubRepository.getClubMembershipStats();
    }

    // Get most active clubs
    public List<Object[]> getMostActiveClubs(LocalDateTime fromDate) {
        return clubRepository.getMostActiveClubs(fromDate);
    }

    // Find clubs with minimum members
    public List<Club> findClubsWithMinMembers(Long minMembers) {
        return clubRepository.findClubsWithMinMembers(minMembers);
    }

    // Get clubs by event count in date range
    public List<Object[]> getClubsByEventCount(LocalDateTime startDate, LocalDateTime endDate) {
        return clubRepository.getClubsByEventCount(startDate, endDate);
    }

    // Get club statistics by category
    public List<Object[]> getClubStatsByCategory() {
        return clubRepository.getClubStatsByCategory();
    }

    // Find clubs created on specific date
    public List<Club> findClubsCreatedOnDate(LocalDateTime date) {
        return clubRepository.findClubsCreatedOnDate(date);
    }

    // Check if club name exists
    public boolean isClubNameTaken(String name) {
        return clubRepository.existsByNameIgnoreCase(name);
    }

    // Get recently updated clubs
    public List<Club> getRecentlyUpdatedClubs(int limit) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return clubRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(oneWeekAgo)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
