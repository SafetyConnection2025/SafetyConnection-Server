package org.example.safetyconnection.member.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.member.dto.response.MemberLocationResDTO;
import org.example.safetyconnection.member.dto.response.MemberResDTO;
import org.example.safetyconnection.common.exception.UserIdNotFoundException;
import org.example.safetyconnection.common.exception.UserNameNotFoundException;
import org.example.safetyconnection.member.repository.MemberRepository;
import org.example.safetyconnection.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberQueryService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResDTO findUserById(Long userId) {
        return memberRepository.findById(userId)
                .map(MemberResDTO::toDTO)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    public MemberLocationResDTO getLocationById(Long userId) {
        return memberRepository.findById(userId)
                .map(MemberLocationResDTO::toDTO)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    public long findIdByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UserNameNotFoundException(username));

        return member.getUserId();
    }
}
