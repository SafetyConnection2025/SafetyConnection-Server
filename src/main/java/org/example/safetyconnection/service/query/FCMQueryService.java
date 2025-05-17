package org.example.safetyconnection.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.exception.UserIdNotFoundException;
import org.example.safetyconnection.repository.MemberRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FCMQueryService {
  private final MemberRepository memberRepository;

  @Cacheable(value = "fcmTokens", key = "#userId", unless = "#result == null")
  public FCMTokenResDTO getTokenById(Long userId) {
    return memberRepository.findById(userId)
        .map(FCMTokenResDTO::toDTO)
        .orElseThrow(() -> new UserIdNotFoundException(userId));
  }
}
