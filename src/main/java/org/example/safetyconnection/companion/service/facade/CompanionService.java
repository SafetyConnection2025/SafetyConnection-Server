package org.example.safetyconnection.companion.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.companion.dto.response.CompanionResDTO;
import org.example.safetyconnection.companion.service.command.CompanionCommandService;
import org.example.safetyconnection.companion.service.query.CompanionQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanionService {

    private final CompanionQueryService companionQueryService;
    private final CompanionCommandService companionCommandService;
    
    public List<CompanionResDTO> findAllCompanionsByUserId(Long userId) {
        return companionQueryService.findAllCompanionsByUserId(userId);
    }

    public CompanionResDTO addCompanion(Long userId, Long compId) {
        return companionCommandService.addCompanion(userId, compId);
    }

    public void deleteCompanion(Long userId, Long compId) {
        companionCommandService.deleteCompanion(userId, compId);
    }
}
