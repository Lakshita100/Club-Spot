package com.clubspot.service;

import com.clubspot.entity.User;
import com.clubspot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Cache for AICTE hours for quick lookups
    private final Map<Long, Double> userAicteHoursCache = new HashMap<>();

    // Authenticate user
    public User authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt.get();
        }
        return null;
    }

    // Create a new user
    public User createUser(User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default AICTE hours if not provided
        if (user.getAicteHours() == null) {
            user.setAicteHours(0.0);
        }

        User savedUser = userRepository.save(user);

        // Update cache
        userAicteHoursCache.put(savedUser.getId(), savedUser.getAicteHours());

        return savedUser;
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user details
    public User updateUser(Long id, User userDetails) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Update username if provided and different
            if (userDetails.getUsername() != null && !userDetails.getUsername().equals(user.getUsername())) {
                if (userRepository.existsByUsername(userDetails.getUsername())) {
                    throw new RuntimeException("Username already exists: " + userDetails.getUsername());
                }
                user.setUsername(userDetails.getUsername());
            }

            // Update email if provided and different
            if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
                if (userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
                    throw new RuntimeException("Email already exists: " + userDetails.getEmail());
                }
                user.setEmail(userDetails.getEmail());
            }

            // Update password if provided
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            // Update role if provided
            if (userDetails.getRole() != null) {
                user.setRole(userDetails.getRole());
            }

            // Update AICTE hours if provided
            if (userDetails.getAicteHours() != null) {
                user.setAicteHours(userDetails.getAicteHours());
                // Update cache
                userAicteHoursCache.put(user.getId(), user.getAicteHours());
            }

            return userRepository.save(user);
        }
        return null;
    }

    // Delete a user
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            // Clean up cache
            userAicteHoursCache.remove(id);
            return true;
        }
        return false;
    }

    // Update AICTE hours
    public boolean updateAicteHours(Long userId, Double hours) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Double currentHours = user.getAicteHours() != null ? user.getAicteHours() : 0.0;
            user.setAicteHours(currentHours + hours);
            userRepository.save(user);

            // Update cache
            userAicteHoursCache.put(userId, user.getAicteHours());
            return true;
        }
        return false;
    }

    // Get AICTE hours for a user
    public Double getUserAicteHours(Long userId) {
        // Check cache first
        if (userAicteHoursCache.containsKey(userId)) {
            return userAicteHoursCache.get(userId);
        }

        // If not in cache, fetch from database
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Double aicteHours = userOpt.get().getAicteHours();
            if (aicteHours != null) {
                userAicteHoursCache.put(userId, aicteHours);
                return aicteHours;
            }
            return 0.0;
        }
        return 0.0;
    }

    // Set AICTE hours (absolute value, not additive)
    public boolean setAicteHours(Long userId, Double hours) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setAicteHours(hours);
            userRepository.save(user);

            // Update cache
            userAicteHoursCache.put(userId, hours);
            return true;
        }
        return false;
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Get users by role
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .toList();
    }

    // Get all admin users
    public List<User> getAdminUsers() {
        return getUsersByRole(User.Role.ADMIN);
    }

    // Get all student users
    public List<User> getStudentUsers() {
        return getUsersByRole(User.Role.STUDENT);
    }

    // Change user password
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Verify old password
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // Reset password (admin function)
    public boolean resetPassword(Long userId, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Promote user to admin
    public boolean promoteToAdmin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(User.Role.ADMIN);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Demote admin to student
    public boolean demoteToStudent(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(User.Role.STUDENT);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Check if user is admin
    public boolean isAdmin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.isPresent() && userOpt.get().getRole() == User.Role.ADMIN;
    }

    // Check if user is student
    public boolean isStudent(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.isPresent() && userOpt.get().getRole() == User.Role.STUDENT;
    }

    // Validate user credentials without returning user object
    public boolean validateCredentials(String username, String password) {
        return authenticate(username, password) != null;
    }

    // Get user count
    public long getUserCount() {
        return userRepository.count();
    }

    // Get admin count
    public long getAdminCount() {
        return getAdminUsers().size();
    }

    // Get student count
    public long getStudentCount() {
        return getStudentUsers().size();
    }

    // Clear AICTE hours cache (for maintenance)
    public void clearAicteHoursCache() {
        userAicteHoursCache.clear();
    }

    // Refresh AICTE hours cache for a specific user
    public void refreshAicteHoursCache(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            userAicteHoursCache.put(userId, userOpt.get().getAicteHours());
        } else {
            userAicteHoursCache.remove(userId);
        }
    }
}