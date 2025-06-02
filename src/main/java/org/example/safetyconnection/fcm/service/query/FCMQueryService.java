package org.example.safetyconnection.fcm.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.qrcode.Repository.QrGeneratorRepository;
import org.example.safetyconnection.member.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.common.exception.UserIdNotFoundException;
import org.example.safetyconnection.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMQueryService {
  private final MemberRepository memberRepository;
  private final QrGeneratorRepository qrGeneratorRepository;

  public FCMTokenResDTO getTokenById(Long userId) {
    return memberRepository.findById(userId)
        .map(FCMTokenResDTO::toDTO)
        .orElseThrow(() -> new UserIdNotFoundException(userId));
  }

  public FCMTokenResDTO getFCMTokenByUid(String uid) {
    String username = (qrGeneratorRepository.findByUid(uid)
        .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.")))
        .getUsername();
    return memberRepository.findByUsername(username)
        .map(FCMTokenResDTO::toDTO)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
