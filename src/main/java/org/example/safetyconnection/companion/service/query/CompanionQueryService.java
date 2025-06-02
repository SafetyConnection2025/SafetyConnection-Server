package org.example.safetyconnection.companion.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.companion.dto.response.CompanionResDTO;
import org.example.safetyconnection.companion.entity.Companion;
import org.example.safetyconnection.common.exception.UserIdNotFoundException;
import org.example.safetyconnection.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanionQueryService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<CompanionResDTO> findAllCompanionsByUserId(Long userId) {
        List<Companion> companionList = memberRepository.findAllCompanionsByUserId(userId)
            .orElseThrow(() -> new UserIdNotFoundException(userId));

        return companionList.stream()
            .map(Companion::getCompUserId)
            .map(memberRepository::findByUserId)
            .map(CompanionResDTO::toDTO)
            .toList();
    }
}
