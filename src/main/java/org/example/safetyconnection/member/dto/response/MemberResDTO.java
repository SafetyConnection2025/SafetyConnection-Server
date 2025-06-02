package org.example.safetyconnection.member.dto.response;

import org.example.safetyconnection.member.entity.Member;

import java.io.Serial;
import java.io.Serializable;

public record MemberResDTO(String username, String name, String email, String phoneNumber) implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public static MemberResDTO toDTO(Member member) {
    return new MemberResDTO(member.getUsername(), member.getName(), member.getEmail(), member.getPhoneNumber());
  }
}
