package org.example.safetyconnection.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.response.CompanionListResDTO;
import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.entity.Companion;
import org.example.safetyconnection.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanionQueryService {
    private final MemberRepository memberRepository;

    public List<CompanionResDTO> findAllCompanionsByUserId(Long userId) {
        List<Companion> companionList = memberRepository.findAllCompanionsByUserId(userId)
            .orElseThrow(() -> new RuntimeException("해당 유저에 대한 알맞은 동행자를 찾을 수 없습니다."));

        return companionList.stream()
            .map(Companion::getCompUserId)
            .map(memberRepository::findByUserId)
            .map(CompanionResDTO::toDTO)
            .toList();
    }
}
