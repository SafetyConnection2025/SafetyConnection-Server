package org.example.safetyconnection.companion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Companion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "COMP_USER_ID", nullable = false, length = 50)
    private Long compUserId;

    @Column(name = "recent_request_date_time")
    private LocalDateTime recentRequestDateTime;

    public Companion(Long userId, Long compUserId, int contactCount) {
        this.userId = userId;
        this.compUserId = compUserId;
    }

    public void setRecentRequestDateTime(LocalDateTime recentRequestDateTime) {
        this.recentRequestDateTime = recentRequestDateTime;
    }

    @Override
    public String toString() {
        return "Companion{" +
                "userId=" + userId +
                ", compUserId='" + compUserId + '\'' +
                '}';
    }
}
