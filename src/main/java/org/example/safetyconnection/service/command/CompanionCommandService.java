package org.example.safetyconnection.service.command;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.safetyconnection.dto.request.CompanionReqDTO;
import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.entity.Companion;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.exception.CompanionNotFoundException;
import org.example.safetyconnection.exception.UserIdNotFoundException;
import org.example.safetyconnection.repository.CompanionRepository;
import org.example.safetyconnection.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanionCommandService {

    private final MemberRepository memberRepository;
    private final CompanionRepository companionRepository;
    // 동행자 추가
    @Transactional
    public CompanionResDTO addCompanion(Long userId, Long compId) {
        Member member = memberRepository.findByUserId(userId);
        Member companion = memberRepository.findByUserId(compId);
        companionRepository.save(new Companion(member, companion));

        return memberRepository.findById(compId)
            .map(CompanionResDTO::toDTO)
            .orElseThrow(() -> new CompanionNotFoundException(compId));
    }

    @Transactional
    public void deleteCompanion(Long userId, Long compId) {
        memberRepository.deleteCompanionByUserIdAndCompanionId(userId, compId);
    }

    @Transactional
    public void updateCompanionRequest(CompanionReqDTO request) {
        Member member = memberRepository.findByUserId(request.userId());
        Member comp = memberRepository.findByUserId(request.compId());
        Companion companion = companionRepository.findByMemberAndCompanion(member, comp).orElseThrow(
            () -> new CompanionNotFoundException(request.compId())
        );

        companion.setRecentRequestDateTime(LocalDateTime.now());
        companion.setLatitude(request.latitude());
        companion.setLongitude(request.longitude());

        companionRepository.save(companion);
    }
}
