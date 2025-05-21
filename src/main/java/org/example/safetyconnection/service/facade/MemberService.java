package org.example.safetyconnection.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.LogInDTO;
import org.example.safetyconnection.dto.request.MemberReqDTO;
import org.example.safetyconnection.dto.request.MemberLocationReqDTO;
import org.example.safetyconnection.dto.response.MemberLocationResDTO;
import org.example.safetyconnection.dto.response.MemberResDTO;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.jwt.JwtToken;
import org.example.safetyconnection.service.command.MemberCommandService;
import org.example.safetyconnection.service.query.MemberQueryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    public JwtToken logIn(LogInDTO logInDTO) {
        return memberCommandService.logIn(logInDTO);
    }

    public MemberResDTO registerUser(MemberReqDTO memberReqDTO) {
        return memberCommandService.registerUser(memberReqDTO);
    }

    @CacheEvict(value = {"memberLocation", "memberById"}, key = "#userId")
    public MemberLocationResDTO setLocation(Long userId, MemberLocationReqDTO userLocationDTO) {
        return memberCommandService.setLocation(userId, userLocationDTO);
    }

    @Cacheable(value = "memberById", key = "#userId", unless = "#result == null")
    public MemberResDTO findUserById(Long userId) {
        return memberQueryService.findUserById(userId);
    }

    @Cacheable(value = "memberLocation", key = "#userId", unless = "#result == null")
    public MemberLocationResDTO getLocationById(Long userId) {
        return memberQueryService.getLocationById(userId);
    }

    @Cacheable(value = "memberLocationByUsername", key = "#username", unless = "#result == null")
    public MemberLocationResDTO getLocationByUsername(String username) {
        return memberQueryService.getLocationById(findIdByUsername(username));
    }

    @Caching(evict = {
        @CacheEvict(value = "memberById", key = "#userId"),
        @CacheEvict(value = "memberLocation", key = "#userId"),
        @CacheEvict(value = "memberToken", key = "#userId")
    })
    public void deleteUserById(Long userId) {
        memberCommandService.deleteUserById(userId);
    }

    @Cacheable(value = "memberIdByUsername", key = "#username", unless = "#result == 0")
    public long findIdByUsername(String username) {
        return memberQueryService.findIdByUsername(username);
    }
}
