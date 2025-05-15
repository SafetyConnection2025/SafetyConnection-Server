package org.example.safetyconnection.dto;

import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.entity.enums.MemberType;

import java.io.Serial;
import java.io.Serializable;

public record MemberDTO(String username, String name, String password, String email, String phoneNumber, MemberType memberType) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static MemberDTO toDTO(Member member) {
        return new MemberDTO(member.getUsername(), member.getName(), member.getPassword(), member.getEmail(), member.getPhoneNumber(), member.getMemberType());
    }
}
