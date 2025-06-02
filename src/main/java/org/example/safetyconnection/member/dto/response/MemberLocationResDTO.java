package org.example.safetyconnection.member.dto.response;

import org.example.safetyconnection.member.entity.Member;

import java.io.Serial;
import java.io.Serializable;

public record MemberLocationResDTO(Long id, Double longitude, Double latitude) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static MemberLocationResDTO toDTO(Member member) {
        return new MemberLocationResDTO(member.getUserId(), member.getLongitude(), member.getLatitude());
    }

}
