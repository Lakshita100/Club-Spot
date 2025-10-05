package com.clubspot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memberships")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    @JsonIgnoreProperties({ "memberships", "events" })
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "memberships", "attendances" })
    private User user;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime leaveDate;

    @Column(nullable = false)
    private String role = "MEMBER"; // MEMBER, ADMIN, PRESIDENT, VICE_PRESIDENT, SECRETARY, TREASURER

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, SUSPENDED, LEFT

    @Column
    private String position; // Custom position title

    @Column
    private String responsibilities;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveDate;

    @Column
    private Integer contributionScore = 0; // Points based on participation

    @Column
    private String notes; // Admin notes about the member

    // Constructors
    public Membership() {
        this.joinDate = LocalDateTime.now();
        this.lastActiveDate = LocalDateTime.now();
    }

    public Membership(Club club, User user) {
        this();
        this.club = club;
        this.user = user;
    }

    public Membership(Club club, User user, String role) {
        this(club, user);
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public LocalDateTime getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public Integer getContributionScore() {
        return contributionScore;
    }

    public void setContributionScore(Integer contributionScore) {
        this.contributionScore = contributionScore;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Utility Methods
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role) || "PRESIDENT".equals(role) ||
                "VICE_PRESIDENT".equals(role) || "SECRETARY".equals(role) ||
                "TREASURER".equals(role);
    }

    public boolean isMember() {
        return "MEMBER".equals(role);
    }

    public void promoteToAdmin() {
        this.role = "ADMIN";
    }

    public void demoteToMember() {
        this.role = "MEMBER";
    }

    public void suspend() {
        this.status = "SUSPENDED";
    }

    public void activate() {
        this.status = "ACTIVE";
    }

    public void leave() {
        this.status = "LEFT";
        this.leaveDate = LocalDateTime.now();
    }

    public void updateActivity() {
        this.lastActiveDate = LocalDateTime.now();
    }

    public void addContributionPoints(Integer points) {
        if (points != null && points > 0) {
            this.contributionScore = (this.contributionScore != null ? this.contributionScore : 0) + points;
        }
    }

    public long getDaysInClub() {
        LocalDateTime endDate = leaveDate != null ? leaveDate : LocalDateTime.now();
        return java.time.Duration.between(joinDate, endDate).toDays();
    }

    public long getDaysSinceLastActive() {
        if (lastActiveDate == null)
            return -1;
        return java.time.Duration.between(lastActiveDate, LocalDateTime.now()).toDays();
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", clubName='" + (club != null ? club.getName() : "null") + '\'' +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Membership))
            return false;
        Membership that = (Membership) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
