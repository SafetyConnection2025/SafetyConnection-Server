package org.example.safetyconnection.dto.request;

import org.example.safetyconnection.entity.enums.MemberType;

public record MemberReqDTO(String username, String password, String name, String email, String phoneNumber, MemberType memberType) {
}
