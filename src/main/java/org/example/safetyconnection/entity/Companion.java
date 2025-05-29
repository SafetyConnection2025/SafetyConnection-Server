package org.example.safetyconnection.entity;

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


    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member member; //호출자 아이디

    @ManyToOne
    @JoinColumn(name = "COMP_USER_ID")
    private Member companion; //호출자 아이디


    @Column(name = "recent_request_date_time")
    private LocalDateTime recentRequestDateTime;

    private Double longitude;

    private Double latitude;

    public Companion(Member member, Member companion) {
        this.member = member;
        this.companion = companion;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setRecentRequestDateTime(LocalDateTime recentRequestDateTime) {
        this.recentRequestDateTime = recentRequestDateTime;
    }


    public Object getCompUserId() {
        return companion.getUserId();
    }
}
