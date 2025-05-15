package org.example.safetyconnection.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.entity.Companion;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.repository.CompanionRepository;
import org.example.safetyconnection.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanionCommandService {

    private final MemberRepository memberRepository;
    private final CompanionRepository companionRepository;

    public CompanionResDTO addCompanion(Long userId, Long compId) {
        companionRepository.save(new Companion(userId, compId, 0));

        return memberRepository.findById(compId)
            .map(CompanionResDTO::toDTO)
            .orElseThrow(() -> new RuntimeException("해당 id를 가진 동행자를 찾을 수 없습니다."));
    }

    public void deleteCompanion(Long userId, Long compId) {
        memberRepository.deleteCompanionByUserIdAndCompanionId(userId, compId);
    }
}
