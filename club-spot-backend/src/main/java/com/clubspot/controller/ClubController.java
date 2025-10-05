package com.clubspot.controller;

import com.clubspot.entity.Club;
import com.clubspot.entity.Event;
import com.clubspot.entity.Membership;
import com.clubspot.service.ClubService;
import com.clubspot.service.EventService;
import com.clubspot.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clubs")
@CrossOrigin(origins = "http://localhost:3000")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @Autowired
    private EventService eventService;

    @Autowired
    private MembershipService membershipService;

    // Get all clubs
    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        return ResponseEntity.ok(clubService.getAllClubs());
    }

    // Get club by ID
    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable Long id) {
        Optional<Club> club = clubService.getClubById(id);
        return club.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new club
    @PostMapping
    public ResponseEntity<?> createClub(@RequestBody Map<String, Object> request) {
        try {
            Club club = new Club();
            club.setName((String) request.get("name"));
            club.setDescription((String) request.get("description"));
            club.setCategory((String) request.get("category"));

            Long creatorId = request.get("creatorId") != null ? ((Number) request.get("creatorId")).longValue() : null;

            if (creatorId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "creatorId is required"));
            }

            Club savedClub = clubService.createClub(club, creatorId);
            return ResponseEntity.ok(savedClub);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update club
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClub(@PathVariable Long id, @RequestBody Club clubDetails) {
        try {
            Club updatedClub = clubService.updateClub(id, clubDetails);
            if (updatedClub != null) {
                return ResponseEntity.ok(updatedClub);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Delete club
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable Long id) {
        boolean deleted = clubService.deleteClub(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Club deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Get clubs by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Club>> getClubsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(clubService.getClubsByCategory(category));
    }

    // Get clubs by status (using active/inactive logic)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Club>> getClubsByStatus(@PathVariable String status) {
        if ("active".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(clubService.getActiveClubs());
        } else {
            // For other statuses, return all clubs and let frontend filter
            return ResponseEntity.ok(clubService.getAllClubs());
        }
    }

    // Search clubs
    @GetMapping("/search")
    public ResponseEntity<List<Club>> searchClubs(@RequestParam String keyword) {
        return ResponseEntity.ok(clubService.searchClubs(keyword));
    }

    // Get active clubs
    @GetMapping("/active")
    public ResponseEntity<List<Club>> getActiveClubs() {
        return ResponseEntity.ok(clubService.getActiveClubs());
    }

    // Get club events
    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getClubEvents(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventsByClubId(id));
    }

    // Get club upcoming events
    @GetMapping("/{id}/events/upcoming")
    public ResponseEntity<List<Event>> getClubUpcomingEvents(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getUpcomingEventsByClub(id));
    }

    // Get club members
    @GetMapping("/{id}/members")
    public ResponseEntity<List<Membership>> getClubMembers(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getMembershipsByClub(id));
    }

    // Get club member count
    @GetMapping("/{id}/members/count")
    public ResponseEntity<Long> getClubMemberCount(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubMemberCount(id));
    }

    // Get club admins
    @GetMapping("/{id}/admins")
    public ResponseEntity<List<Membership>> getClubAdmins(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getClubAdmins(id));
    }

    // Get club statistics
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getClubStats(@PathVariable Long id) {
        try {
            Map<String, Object> stats = clubService.getClubStatistics(id);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Join club
    @PostMapping("/{clubId}/join/{userId}")
    public ResponseEntity<?> joinClub(@PathVariable Long clubId, @PathVariable Long userId,
            @RequestBody(required = false) Map<String, String> request) {
        try {
            String role = (request != null && request.get("role") != null) ? request.get("role") : "MEMBER"; // Default
                                                                                                             // role

            Membership membership = membershipService.joinClub(clubId, userId, role);
            if (membership != null) {
                return ResponseEntity.ok(Map.of("message", "Successfully joined club", "membership", membership));
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to join club"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Leave club
    @DeleteMapping("/{clubId}/leave/{userId}")
    public ResponseEntity<?> leaveClub(@PathVariable Long clubId, @PathVariable Long userId) {
        try {
            boolean left = membershipService.leaveClub(userId, clubId);
            if (left) {
                return ResponseEntity.ok(Map.of("message", "Successfully left club"));
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to leave club"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update club status (activate/deactivate)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateClubStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        boolean updated = false;

        if ("active".equalsIgnoreCase(status)) {
            updated = clubService.activateClub(id);
        } else if ("inactive".equalsIgnoreCase(status)) {
            updated = clubService.deactivateClub(id);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Status must be 'active' or 'inactive'"));
        }

        if (updated) {
            return ResponseEntity.ok(Map.of("message", "Club status updated successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Get club count
    @GetMapping("/count")
    public ResponseEntity<Integer> getClubCount() {
        return ResponseEntity.ok(clubService.getAllClubs().size());
    }

    // Get clubs managed by user
    @GetMapping("/managed-by/{userId}")
    public ResponseEntity<List<Club>> getClubsManagedByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(clubService.getClubsManagedByUser(userId));
    }

    // Get clubs by member
    @GetMapping("/member/{userId}")
    public ResponseEntity<List<Club>> getClubsByMember(@PathVariable Long userId) {
        return ResponseEntity.ok(clubService.getClubsByMember(userId));
    }
}
