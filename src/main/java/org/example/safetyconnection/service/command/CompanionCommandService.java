package org.example.safetyconnection.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.entity.Companion;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.exception.CompanionNotFoundException;
import org.example.safetyconnection.exception.UserAlreadyExistsException;
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

    @Transactional
    public CompanionResDTO addCompanion(Long userId, Long compId) {
       /* if (companionRepository.existsById(compId)) {
            throw new UserAlreadyExistsException(compId);
        };*/

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
