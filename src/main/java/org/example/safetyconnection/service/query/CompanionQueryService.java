package org.example.safetyconnection.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.response.CompanionListResDTO;
import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.entity.Companion;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.exception.UserIdNotFoundException;
import org.example.safetyconnection.repository.CompanionRepository;
import org.example.safetyconnection.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanionQueryService {
    private final MemberRepository memberRepository;
    private final CompanionRepository companionRepository;

    @Transactional(readOnly = true)
    public List<CompanionResDTO> findAllCompanionsByUserId(Long userId) {
        List<Companion> companionList = memberRepository.findAllCompanionsByUserId(userId)
            .orElseThrow(() -> new UserIdNotFoundException(userId));

        return companionList.stream()
            .map(Companion::getCompanion)
            .map(CompanionResDTO::toDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<CompanionResDTO> getRecentCompanions(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new UserIdNotFoundException(memberId));
        return companionRepository.findByMemberOrderByRecentRequestDateTimeDesc(member).stream()
            .map(companion -> CompanionResDTO.toDTO(companion.getCompanion()))
            .toList();
    }
}
