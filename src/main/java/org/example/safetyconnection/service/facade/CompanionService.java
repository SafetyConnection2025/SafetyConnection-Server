package org.example.safetyconnection.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.service.command.CompanionCommandService;
import org.example.safetyconnection.service.query.CompanionQueryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "userCompanions", key = "#userId")
    public CompanionResDTO addCompanion(Long userId, Long compId) {
        return companionCommandService.addCompanion(userId, compId);
    }

    @CacheEvict(value = "userCompanions", key = "#userId")
    public void deleteCompanion(Long userId, Long compId) {
        companionCommandService.deleteCompanion(userId, compId);
    }
}
