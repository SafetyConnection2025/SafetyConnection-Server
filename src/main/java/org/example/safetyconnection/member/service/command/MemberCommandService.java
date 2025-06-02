package org.example.safetyconnection.member.service.command;

import lombok.RequiredArgsConstructor;
import org.example.safetyconnection.member.dto.request.LogInDTO;
import org.example.safetyconnection.member.dto.request.MemberReqDTO;
import org.example.safetyconnection.member.dto.request.FCMTokenReqDTO;
import org.example.safetyconnection.member.dto.request.MemberLocationReqDTO;
import org.example.safetyconnection.member.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.member.dto.response.MemberLocationResDTO;
import org.example.safetyconnection.member.dto.response.MemberResDTO;
import org.example.safetyconnection.member.entity.Member;
import org.example.safetyconnection.member.entity.enums.MemberType;
import org.example.safetyconnection.common.exception.LocationSaveFailedException;
import org.example.safetyconnection.common.exception.UserIdNotFoundException;
import org.example.safetyconnection.common.exception.UserNameAlreadyExistsException;
import org.example.safetyconnection.common.exception.UserNameNotFoundException;
import org.example.safetyconnection.jwt.JwtToken;
import org.example.safetyconnection.jwt.JwtTokenProvider;
import org.example.safetyconnection.member.repository.MemberRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtToken logIn(LogInDTO logInDTO) {
        String username = logInDTO.username();
        String password = logInDTO.password();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UserNameNotFoundException(username));

        return jwtTokenProvider.generateToken(authentication, member);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        memberRepository.deleteById(userId);
    }

    @Transactional
    public MemberResDTO registerUser(MemberReqDTO memberReqDTO) {
        String username = memberReqDTO.username();
        String name = memberReqDTO.name();
        String password = memberReqDTO.password();
        String email = memberReqDTO.email();
        String phoneNumber = memberReqDTO.phoneNumber();
        MemberType memberType = memberReqDTO.memberType();

        if (memberRepository.existsByUsername(username)) {
            throw new UserNameAlreadyExistsException(username);
        }

        return MemberResDTO.toDTO(
            memberRepository.save(new Member
                (username,
                    name,
                    passwordEncoder.encode(password),
                    email, phoneNumber,
                    memberType
                )
            )
        );
    }

    @Transactional
    public MemberLocationResDTO setLocation(Long userId, MemberLocationReqDTO userLocationReqDTO) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundException(userId));

        member.setLongitude(userLocationReqDTO.longitude());
        member.setLatitude(userLocationReqDTO.latitude());

        memberRepository.save(member);

        return memberRepository.findById(userId).map(MemberLocationResDTO::toDTO)
                .orElseThrow(() -> new LocationSaveFailedException(userId));
    }

    @Transactional
    public FCMTokenResDTO setFCMToken(Long userId, FCMTokenReqDTO fcmTokenReqDTO) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new UserIdNotFoundException(userId));

        member.setFCMToken(fcmTokenReqDTO.fcmToken());

        memberRepository.save(member);

        return memberRepository.findById(userId)
                .map(FCMTokenResDTO::toDTO)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }
}
