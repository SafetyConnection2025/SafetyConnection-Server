package org.example.safetyconnection.member.dto.request;

import org.example.safetyconnection.member.entity.enums.MemberType;

public record MemberReqDTO(String username, String password, String name, String email, String phoneNumber, MemberType memberType) {
}
