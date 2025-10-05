package com.clubspot.service;

import com.clubspot.entity.Membership;
import com.clubspot.entity.Club;
import com.clubspot.entity.User;
import com.clubspot.repository.MembershipRepository;
import com.clubspot.repository.ClubRepository;
import com.clubspot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new membership (join club)
    public Membership joinClub(Long clubId, Long userId, String role) {
        // Validate club exists and is active
        Optional<Club> clubOpt = clubRepository.findById(clubId);
        if (!clubOpt.isPresent()) {
            throw new IllegalArgumentException("Club not found with ID: " + clubId);
        }

        Club club = clubOpt.get();
        if (!club.isActive()) {
            throw new IllegalStateException("Cannot join an inactive club");
        }

        // Validate user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        User user = userOpt.get();

        // Check if user is already a member
        Optional<Membership> existingMembership = membershipRepository.findByClubIdAndUserId(clubId, userId);
        if (existingMembership.isPresent()) {
            Membership membership = existingMembership.get();
            if ("ACTIVE".equals(membership.getStatus())) {
                throw new IllegalStateException("User is already an active member of this club");
            } else {
                // Reactivate membership
                membership.activate();
                if (role != null && !role.isEmpty()) {
                    membership.setRole(role);
                }
                return membershipRepository.save(membership);
            }
        }

        // Create new membership
        Membership membership = new Membership(club, user);
        if (role != null && !role.isEmpty()) {
            membership.setRole(role);
        } else {
            membership.setRole("MEMBER"); // Default role
        }

        return membershipRepository.save(membership);
    }

    // Leave club (deactivate membership)
    public boolean leaveClub(Long clubId, Long userId) {
        Optional<Membership> membershipOpt = membershipRepository.findByClubIdAndUserId(clubId, userId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            if ("ACTIVE".equals(membership.getStatus())) {
                membership.leave();
                membershipRepository.save(membership);
                return true;
            }
        }
        return false;
    }

    // Update member role
    public boolean updateMemberRole(Long membershipId, String newRole) {
        Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            if ("ACTIVE".equals(membership.getStatus())) {
                membership.setRole(newRole);
                membershipRepository.save(membership);
                return true;
            }
        }
        return false;
    }

    // Update member role by club and user ID
    public boolean updateMemberRoleByClubAndUser(Long clubId, Long userId, String newRole) {
        Optional<Membership> membershipOpt = membershipRepository.findByClubIdAndUserId(clubId, userId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            if ("ACTIVE".equals(membership.getStatus())) {
                membership.setRole(newRole);
                membershipRepository.save(membership);
                return true;
            }
        }
        return false;
    }

    // Get all memberships
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    // Get membership by ID
    public Optional<Membership> getMembershipById(Long id) {
        return membershipRepository.findById(id);
    }

    // Get memberships by club
    public List<Membership> getMembershipsByClub(Long clubId) {
        return membershipRepository.findByClubId(clubId);
    }

    // Get active memberships by club
    public List<Membership> getActiveMembershipsByClub(Long clubId) {
        return membershipRepository.findByClubIdAndStatusOrderByJoinDateAsc(clubId, "ACTIVE");
    }

    // Get memberships by user
    public List<Membership> getMembershipsByUser(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    // Get active memberships by user
    public List<Membership> getActiveMembershipsByUser(Long userId) {
        return membershipRepository.findByUserIdAndStatusOrderByJoinDateAsc(userId, "ACTIVE");
    }

    // Get membership by club and user
    public Optional<Membership> getMembershipByClubAndUser(Long clubId, Long userId) {
        return membershipRepository.findByClubIdAndUserId(clubId, userId);
    }

    // Check if user is member of club
    public boolean isUserMemberOfClub(Long clubId, Long userId) {
        return membershipRepository.isUserMemberOfClub(clubId, userId);
    }

    // Get members by role
    public List<Membership> getMembersByRole(Long clubId, String role) {
        return membershipRepository.findByClubIdAndRole(clubId, role)
                .stream()
                .filter(m -> "ACTIVE".equals(m.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    // Get club admins
    public List<Membership> getClubAdmins(Long clubId) {
        return membershipRepository.findClubAdmins(clubId);
    }

    // Get club presidents
    public List<Membership> getClubPresidents(Long clubId) {
        return membershipRepository.findByClubIdAndRole(clubId, "PRESIDENT")
                .stream()
                .filter(m -> "ACTIVE".equals(m.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    // Get long-term members
    public List<Membership> getLongTermMembers(Long clubId, int months) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(months);
        return membershipRepository.findLongTermMembers(cutoffDate);
    }

    // Get recent members
    public List<Membership> getRecentMembers(Long clubId, int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return membershipRepository.findByClubIdAndJoinDateBetween(clubId, cutoffDate, LocalDateTime.now());
    }

    // Get membership statistics for club
    public Map<String, Object> getClubMembershipStatistics(Long clubId) {
        Map<String, Object> stats = new HashMap<>();

        // Total active members
        Long totalMembers = membershipRepository.countActiveMembersByClub(clubId);
        stats.put("totalActiveMembers", totalMembers);

        // Members by role
        List<Object[]> roleStats = membershipRepository.getMembershipStatsByRole(clubId);
        Map<String, Long> membersByRole = new HashMap<>();
        for (Object[] stat : roleStats) {
            String role = (String) stat[0];
            Long count = (Long) stat[1];
            membersByRole.put(role, count);
        }
        stats.put("membersByRole", membersByRole);

        // Recent joins (last 30 days)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Membership> recentMembers = membershipRepository.findByClubIdAndJoinDateBetween(clubId, thirtyDaysAgo,
                LocalDateTime.now());
        Long recentJoins = (long) recentMembers.size();
        stats.put("recentJoins", recentJoins);

        return stats;
    }

    // Get user membership statistics
    public Map<String, Object> getUserMembershipStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // Active memberships count
        Long activeMemberships = membershipRepository.countActiveMembershipsByUser(userId);
        stats.put("activeMemberships", activeMemberships);

        // Check if user is admin of any club
        Boolean isAdminOfAnyClub = membershipRepository.isUserAdminOfAnyClub(userId);
        stats.put("isAdminOfAnyClub", isAdminOfAnyClub);

        return stats;
    }

    // Transfer leadership (change president)
    public boolean transferLeadership(Long clubId, Long currentPresidentId, Long newPresidentId) {
        // Validate new president is an active member
        Optional<Membership> newPresidentMembership = membershipRepository.findByClubIdAndUserId(clubId,
                newPresidentId);
        if (!newPresidentMembership.isPresent() || !"ACTIVE".equals(newPresidentMembership.get().getStatus())) {
            return false;
        }

        // Update current president to regular member or admin
        Optional<Membership> currentPresidentMembership = membershipRepository.findByClubIdAndUserId(clubId,
                currentPresidentId);
        if (currentPresidentMembership.isPresent()) {
            currentPresidentMembership.get().setRole("ADMIN");
            membershipRepository.save(currentPresidentMembership.get());
        }

        // Update new president
        newPresidentMembership.get().setRole("PRESIDENT");
        membershipRepository.save(newPresidentMembership.get());

        return true;
    }

    // Promote member to admin
    public boolean promoteMemberToAdmin(Long clubId, Long userId) {
        return updateMemberRoleByClubAndUser(clubId, userId, "ADMIN");
    }

    // Demote admin to member
    public boolean demoteAdminToMember(Long clubId, Long userId) {
        return updateMemberRoleByClubAndUser(clubId, userId, "MEMBER");
    }

    // Remove member from club (hard delete)
    public boolean removeMemberFromClub(Long clubId, Long userId) {
        Optional<Membership> membershipOpt = membershipRepository.findByClubIdAndUserId(clubId, userId);
        if (membershipOpt.isPresent()) {
            membershipRepository.delete(membershipOpt.get());
            return true;
        }
        return false;
    }

    // Update contribution score
    public boolean updateContributionScore(Long membershipId, Integer score) {
        Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            membership.setContributionScore(score);
            membershipRepository.save(membership);
            return true;
        }
        return false;
    }

    // Get top contributors by club
    public List<Membership> getTopContributors(Long clubId, int limit) {
        return membershipRepository.findTopContributorsByClub(clubId)
                .stream()
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }

    // Get members with high contribution scores
    public List<Membership> getHighContributionMembers(Long clubId, Integer minScore, Integer maxScore) {
        return membershipRepository.findByContributionScoreRange(minScore, maxScore)
                .stream()
                .filter(m -> m.getClub().getId().equals(clubId) && "ACTIVE".equals(m.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    // Delete membership
    public boolean deleteMembership(Long id) {
        if (membershipRepository.existsById(id)) {
            membershipRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get membership duration in days
    public Long getMembershipDurationInDays(Long membershipId) {
        Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            return membership.getDaysInClub();
        }
        return 0L;
    }

    // Check if user can manage club (is admin or president)
    public boolean canUserManageClub(Long clubId, Long userId) {
        return membershipRepository.isUserAdminOfClub(clubId, userId);
    }

    // Get inactive members
    public List<Membership> getInactiveMembers(Long clubId, int daysThreshold) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(daysThreshold);
        return membershipRepository.findInactiveMembersByClub(clubId, thresholdDate);
    }

    // Get new members (joined recently)
    public List<Membership> getNewMembers(int daysBack) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysBack);
        return membershipRepository.findNewMembers(cutoffDate);
    }

    // Get membership history for user
    public List<Membership> getUserMembershipHistory(Long userId) {
        return membershipRepository.getUserMembershipHistory(userId);
    }

    // Get user's active memberships with club details
    public List<Membership> getUserActiveMemberships(Long userId) {
        return membershipRepository.getUserActiveMemberships(userId);
    }

    // Get active club members with user details
    public List<Membership> getActiveClubMembersWithUserDetails(Long clubId) {
        return membershipRepository.findActiveClubMembersWithUserDetails(clubId);
    }

    // Get active user memberships with club details
    public List<Membership> getActiveUserMembershipsWithClubDetails(Long userId) {
        return membershipRepository.findActiveUserMembershipsWithClubDetails(userId);
    }

    // Get memberships with notes
    public List<Membership> getMembershipsWithNotes() {
        return membershipRepository.findMembershipsWithNotes();
    }

    // Update member notes
    public boolean updateMemberNotes(Long membershipId, String notes) {
        Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            membership.setNotes(notes);
            membershipRepository.save(membership);
            return true;
        }
        return false;
    }

    // Update last active date
    public boolean updateLastActiveDate(Long membershipId) {
        Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            membership.updateActivity();
            membershipRepository.save(membership);
            return true;
        }
        return false;
    }

    // Get average membership duration for club
    public Double getAverageMembershipDuration(Long clubId) {
        return membershipRepository.getAverageMembershipDuration(clubId);
    }

    // Get membership statistics by club
    public List<Object[]> getMembershipStatsByClub(Long clubId) {
        return membershipRepository.getMembershipStatsByClub(clubId);
    }

    // Suspend member
    public boolean suspendMember(Long membershipId) {
        Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            membership.suspend();
            membershipRepository.save(membership);
            return true;
        }
        return false;
    }

    // Reactivate member
    public boolean reactivateMember(Long membershipId) {
        Optional<Membership> membershipOpt = membershipRepository.findById(membershipId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            membership.activate();
            membershipRepository.save(membership);
            return true;
        }
        return false;
    }
}
