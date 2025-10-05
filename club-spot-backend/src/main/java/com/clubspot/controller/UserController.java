package com.clubspot.controller;

import com.clubspot.entity.User;
import com.clubspot.entity.Membership;
import com.clubspot.service.UserService;
import com.clubspot.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipService membershipService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Get user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get users by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            return ResponseEntity.ok(userService.getUsersByRole(userRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Get admin users
    @GetMapping("/admins")
    public ResponseEntity<List<User>> getAdminUsers() {
        return ResponseEntity.ok(userService.getAdminUsers());
    }

    // Get student users
    @GetMapping("/students")
    public ResponseEntity<List<User>> getStudentUsers() {
        return ResponseEntity.ok(userService.getStudentUsers());
    }

    // Update AICTE hours
    @PutMapping("/{id}/aicte-hours")
    public ResponseEntity<?> updateAicteHours(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double hours = request.get("hours");
        if (hours == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Hours parameter is required"));
        }

        boolean updated = userService.updateAicteHours(id, hours);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "AICTE hours updated successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Set AICTE hours (absolute value)
    @PutMapping("/{id}/aicte-hours/set")
    public ResponseEntity<?> setAicteHours(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double hours = request.get("hours");
        if (hours == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Hours parameter is required"));
        }

        boolean updated = userService.setAicteHours(id, hours);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "AICTE hours set successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Get user AICTE hours
    @GetMapping("/{id}/aicte-hours")
    public ResponseEntity<Map<String, Double>> getUserAicteHours(@PathVariable Long id) {
        Double aicteHours = userService.getUserAicteHours(id);
        return ResponseEntity.ok(Map.of("aicteHours", aicteHours));
    }

    // Change password
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Old password and new password are required"));
        }

        boolean changed = userService.changePassword(id, oldPassword, newPassword);
        if (changed) {
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Failed to change password. Check old password."));
    }

    // Reset password (admin function)
    @PutMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");

        if (newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "New password is required"));
        }

        boolean reset = userService.resetPassword(id, newPassword);
        if (reset) {
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Promote user to admin
    @PutMapping("/{id}/promote")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id) {
        boolean promoted = userService.promoteToAdmin(id);
        if (promoted) {
            return ResponseEntity.ok(Map.of("message", "User promoted to admin successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Demote admin to student
    @PutMapping("/{id}/demote")
    public ResponseEntity<?> demoteToStudent(@PathVariable Long id) {
        boolean demoted = userService.demoteToStudent(id);
        if (demoted) {
            return ResponseEntity.ok(Map.of("message", "User demoted to student successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Check if username exists
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@PathVariable String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Check if email exists
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@PathVariable String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Validate credentials
    @PostMapping("/validate-credentials")
    public ResponseEntity<Map<String, Boolean>> validateCredentials(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("valid", false));
        }

        boolean valid = userService.validateCredentials(username, password);
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    // Get user statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getUserCount());
        stats.put("adminCount", userService.getAdminCount());
        stats.put("studentCount", userService.getStudentCount());
        return ResponseEntity.ok(stats);
    }

    // Get user role
    @GetMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> getUserRole(@PathVariable Long id) {
        Map<String, Object> roleInfo = new HashMap<>();
        roleInfo.put("isAdmin", userService.isAdmin(id));
        roleInfo.put("isStudent", userService.isStudent(id));
        return ResponseEntity.ok(roleInfo);
    }

    // Get user memberships
    @GetMapping("/{id}/memberships")
    public ResponseEntity<List<Membership>> getUserMemberships(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getMembershipsByUser(id));
    }

    // Get user active memberships
    @GetMapping("/{id}/memberships/active")
    public ResponseEntity<List<Membership>> getUserActiveMemberships(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getActiveMembershipsByUser(id));
    }

    // Clear AICTE hours cache
    @PostMapping("/cache/clear-aicte-hours")
    public ResponseEntity<?> clearAicteHoursCache() {
        userService.clearAicteHoursCache();
        return ResponseEntity.ok(Map.of("message", "AICTE hours cache cleared successfully"));
    }

    // Refresh AICTE hours cache for specific user
    @PostMapping("/{id}/cache/refresh-aicte-hours")
    public ResponseEntity<?> refreshAicteHoursCache(@PathVariable Long id) {
        userService.refreshAicteHoursCache(id);
        return ResponseEntity.ok(Map.of("message", "AICTE hours cache refreshed for user"));
    }
}
