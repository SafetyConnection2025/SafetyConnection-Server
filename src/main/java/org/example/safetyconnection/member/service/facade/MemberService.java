package org.example.safetyconnection.member.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.member.dto.request.LogInDTO;
import org.example.safetyconnection.member.dto.request.MemberReqDTO;
import org.example.safetyconnection.member.dto.request.MemberLocationReqDTO;
import org.example.safetyconnection.member.dto.response.MemberLocationResDTO;
import org.example.safetyconnection.member.dto.response.MemberResDTO;
import org.example.safetyconnection.jwt.JwtToken;
import org.example.safetyconnection.member.service.command.MemberCommandService;
import org.example.safetyconnection.member.service.query.MemberQueryService;
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

    public MemberLocationResDTO setLocation(Long userId, MemberLocationReqDTO userLocationDTO) {
        return memberCommandService.setLocation(userId, userLocationDTO);
    }

    public MemberResDTO findUserById(Long userId) {
        return memberQueryService.findUserById(userId);
    }

    public MemberLocationResDTO getLocationById(Long userId) {
        return memberQueryService.getLocationById(userId);
    }

    public MemberLocationResDTO getLocationByUsername(String username) {
        return memberQueryService.getLocationById(findIdByUsername(username));
    }

    public void deleteUserById(Long userId) {
        memberCommandService.deleteUserById(userId);
    }

    public long findIdByUsername(String username) {
        return memberQueryService.findIdByUsername(username);
    }
}
