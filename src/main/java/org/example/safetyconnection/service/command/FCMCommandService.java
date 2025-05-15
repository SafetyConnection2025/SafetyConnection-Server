package org.example.safetyconnection.service.command;

import lombok.RequiredArgsConstructor;
import org.example.safetyconnection.dto.request.FCMTokenReqDTO;
import org.example.safetyconnection.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FCMCommandService {
  private final MemberRepository memberRepository;

  @Transactional
  public FCMTokenResDTO setFCMToken(Long userId, FCMTokenReqDTO fcmTokenReqDTO) {
    Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

    member.setFCMToken(fcmTokenReqDTO.fcmToken());

    memberRepository.save(member);

    return memberRepository.findById(userId)
        .map(FCMTokenResDTO::toDTO)
        .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
  }
}
