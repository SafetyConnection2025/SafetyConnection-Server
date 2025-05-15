package org.example.safetyconnection.dto.response;

import org.example.safetyconnection.entity.Member;

import java.io.Serial;
import java.io.Serializable;

public record FCMTokenResDTO(Long userId, String fcmToken) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static FCMTokenResDTO toDTO(Member member) {
        return new FCMTokenResDTO(member.getUserId(), member.getFcmToken());
    }
}
