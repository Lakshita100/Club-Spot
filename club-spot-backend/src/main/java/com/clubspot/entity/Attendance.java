package com.clubspot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnoreProperties({ "attendances" })
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "memberships", "attendances" })
    private User user;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime attendanceTime;

    @Column(nullable = false)
    private String status = "REGISTERED"; // REGISTERED, ATTENDED, ABSENT, CANCELLED

    @Column
    private String feedback;

    @Column
    private Integer rating; // 1-5 stars rating for the event

    @Column
    private Boolean certificateGenerated = false;

    @Column
    private String certificateUrl;

    @Column
    private Integer aicteHoursAwarded;

    // Constructors
    public Attendance() {
        this.registrationDate = LocalDateTime.now();
    }

    public Attendance(Event event, User user) {
        this();
        this.event = event;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(LocalDateTime attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getCertificateGenerated() {
        return certificateGenerated;
    }

    public void setCertificateGenerated(Boolean certificateGenerated) {
        this.certificateGenerated = certificateGenerated;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public Integer getAicteHoursAwarded() {
        return aicteHoursAwarded;
    }

    public void setAicteHoursAwarded(Integer aicteHoursAwarded) {
        this.aicteHoursAwarded = aicteHoursAwarded;
    }

    // Utility Methods
    public boolean isAttended() {
        return "ATTENDED".equals(status);
    }

    public boolean isRegistered() {
        return "REGISTERED".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }

    public void markAttended() {
        this.status = "ATTENDED";
        this.attendanceTime = LocalDateTime.now();

        // Award AICTE hours if event has them
        if (event != null && event.getAicteHours() != null) {
            this.aicteHoursAwarded = event.getAicteHours();
        }
    }

    public void markAbsent() {
        this.status = "ABSENT";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", eventTitle='" + (event != null ? event.getTitle() : "null") + '\'' +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", status='" + status + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Attendance))
            return false;
        Attendance that = (Attendance) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}