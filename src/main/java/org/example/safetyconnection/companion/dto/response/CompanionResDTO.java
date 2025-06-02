
package org.example.safetyconnection.companion.dto.response;

import org.example.safetyconnection.member.entity.Member;

import java.io.Serial;
import java.io.Serializable;

public record CompanionResDTO(Long id) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static CompanionResDTO toDTO(Member member) {
        return new CompanionResDTO(member.getUserId());
    }
}