package org.example.safetyconnection.companion.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.companion.dto.response.CompanionResDTO;
import org.example.safetyconnection.companion.entity.Companion;
import org.example.safetyconnection.common.exception.CompanionNotFoundException;
import org.example.safetyconnection.companion.repository.CompanionRepository;
import org.example.safetyconnection.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanionCommandService {

    private final MemberRepository memberRepository;
    private final CompanionRepository companionRepository;

    @Transactional
    public CompanionResDTO addCompanion(Long userId, Long compId) {
        companionRepository.save(new Companion(userId, compId, 0));

        return memberRepository.findById(compId)
            .map(CompanionResDTO::toDTO)
            .orElseThrow(() -> new CompanionNotFoundException(compId));
    }

    @Transactional
    public void deleteCompanion(Long userId, Long compId) {
        memberRepository.deleteCompanionByUserIdAndCompanionId(userId, compId);
    }
}
