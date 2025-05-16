package org.example.safetyconnection.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.MemberDTO;
import org.example.safetyconnection.dto.request.FCMTokenReqDTO;
import org.example.safetyconnection.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.dto.response.MemberLocationResDTO;
import org.example.safetyconnection.exception.UserIdNotFoundException;
import org.example.safetyconnection.exception.UserNameNotFoundException;
import org.example.safetyconnection.repository.MemberRepository;
import org.example.safetyconnection.entity.Member;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberQueryService {
    private final MemberRepository memberRepository;

    public MemberDTO findUserById(Long userId) {
        return memberRepository.findById(userId)
                .map(MemberDTO::toDTO)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    public MemberLocationResDTO getLocationById(Long userId) {
        return memberRepository.findById(userId)
                .map(MemberLocationResDTO::toDTO)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    public long findIdByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UserNameNotFoundException(username));

        return member.getUserId();
    }
}
