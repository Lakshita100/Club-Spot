-- schema.sql: Complete Database Schema for ClubSpot (College Club Management System)
-- Run this in MySQL Workbench or auto-load via Spring Boot
-- Compatible with MySQL 8+ (strict mode; no zero-dates)

-- Create Database (if not exists)
CREATE DATABASE IF NOT EXISTS clubspot_db;
USE clubspot_db;

-- Drop tables in reverse order (to handle foreign keys)
DROP TABLE IF EXISTS attendances;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS memberships;
DROP TABLE IF EXISTS clubs;
DROP TABLE IF EXISTS users;

-- Table: users (Admins, Members)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- Hashed (e.g., BCrypt)
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
    aicte_hours INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for users
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Table: clubs (Clubs with AICTE focus)
CREATE TABLE clubs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    banner_url VARCHAR(500),
    certificate_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for clubs
CREATE INDEX idx_clubs_name ON clubs(name);

-- Table: events (Events with AICTE categories, status, etc.)
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_date_time DATETIME NOT NULL,  -- Fixed: No more event_date
    end_date_time DATETIME NOT NULL,
    location VARCHAR(200),
    status ENUM('UPCOMING', 'ONGOING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'UPCOMING',
    aicte_hours INT DEFAULT 0,
    aicte_category VARCHAR(50),  -- e.g., 'TECHNICAL', 'SEMINAR'
    event_type VARCHAR(50),  -- e.g., 'WORKSHOP', 'SEMINAR', 'COMPETITION'
    image_url VARCHAR(500),
    max_attendees INT DEFAULT 50,
    attendees_count INT DEFAULT 0,  -- Denormalized for quick checks
    club_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE RESTRICT
);

-- Indexes for events
CREATE INDEX idx_events_club_id ON events(club_id);
CREATE INDEX idx_events_status ON events(status);
CREATE INDEX idx_events_start_date_time ON events(start_date_time);
CREATE INDEX idx_events_created_by ON events(created_by);
CREATE INDEX idx_events_aicte_hours ON events(aicte_hours);

-- Table: attendances (User  attendance for events; tracks AICTE hours)
CREATE TABLE attendances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    attended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- When attended (for completed events)
    registration_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- When registered (fixed: NOT NULL but defaulted)
    is_present BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (event_id, user_id)  -- One attendance per user/event
);

-- Indexes for attendances
CREATE INDEX idx_attendances_event_id ON attendances(event_id);
CREATE INDEX idx_attendances_user_id ON attendances(user_id);
CREATE INDEX idx_attendances_registration_date ON attendances(registration_date);

-- ========================================
-- SAMPLE DATA (Safe: Uses NOW() for dates)
-- ========================================

-- Insert Users (Admin + Members; passwords are BCrypt-hashed examples)
INSERT INTO users (username, password, email, full_name, role, aicte_hours) VALUES
('admin1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@clubspot.com', 'Admin User', 'ADMIN', 10),  -- password: 'password'
('member1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'member1@clubspot.com', 'Member One', 'MEMBER', 5),  -- password: 'password'
('member2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'member2@clubspot.com', 'Member Two', 'MEMBER', 0);  -- password: 'password'

-- Insert Clubs
INSERT INTO clubs (name, description, banner_url, certificate_url) VALUES
('Tech Innovators Club', 'AICTE-approved club for technical workshops and seminars.', 'https://example.com/tech-banner.jpg', 'https://example.com/tech-cert.pdf');

-- Insert Events (1 Upcoming, 1 Completed; future/past dates)
INSERT INTO events (title, description, start_date_time, end_date_time, location, status, aicte_hours, aicte_category, event_type, image_url, max_attendees, club_id, created_by) VALUES
-- Upcoming Event (future date)
('AICTE Spring Boot Workshop', 'Hands-on session on building club management systems.', '2025-10-10 10:00:00', '2025-10-10 12:00:00', 'Campus Auditorium A', 'UPCOMING', 2, 'TECHNICAL', 'WORKSHOP', 'https://example.com/workshop.jpg', 50, 1, 1),
-- Completed Event (past date)
('Past AICTE Seminar', 'Introduction to AICTE guidelines for college clubs.', '2024-09-15 14:00:00', '2024-09-15 16:00:00', 'Online via Zoom', 'COMPLETED', 1, 'SEMINAR', 'SEMINAR', 'https://example.com/seminar.jpg', 30, 1, 1);

-- Insert Attendances (For the completed event; updates user AICTE hours via app logic)
INSERT INTO attendances (event_id, user_id, attended_at, registration_date, is_present) VALUES
(2, 2, '2024-09-15 15:30:00', '2024-09-10 09:00:00', TRUE);  -- Member1 attended the past seminar

-- ========================================
-- VERIFICATION QUERIES (Run these to check)
-- ========================================
-- SELECT * FROM users;
-- SELECT * FROM clubs;
-- SELECT * FROM events ORDER BY start_date_time ASC;
-- SELECT * FROM attendances;
-- SHOW TABLES;
-- DESCRIBE events;  -- Verify columns like start_date_time

-- End of schema.sql