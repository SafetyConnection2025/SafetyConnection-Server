package org.example.safetyconnection.service.command;

import lombok.RequiredArgsConstructor;
import org.example.safetyconnection.dto.LogInDTO;
import org.example.safetyconnection.dto.MemberDTO;
import org.example.safetyconnection.dto.request.FCMTokenReqDTO;
import org.example.safetyconnection.dto.request.MemberLocationReqDTO;
import org.example.safetyconnection.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.dto.response.MemberLocationResDTO;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.entity.enums.MemberType;
import org.example.safetyconnection.exception.LocationSaveFailedException;
import org.example.safetyconnection.exception.UserAlreadyExistsException;
import org.example.safetyconnection.exception.UserIdNotFoundException;
import org.example.safetyconnection.exception.UserNameAlreadyExistsException;
import org.example.safetyconnection.exception.UserNameNotFoundException;
import org.example.safetyconnection.jwt.JwtToken;
import org.example.safetyconnection.jwt.JwtTokenProvider;
import org.example.safetyconnection.repository.MemberRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Member registerUser(MemberDTO memberDTO) {
        String username = memberDTO.username();
        String name = memberDTO.name();
        String password = memberDTO.password();
        String email = memberDTO.email();
        String phoneNumber = memberDTO.phoneNumber();
        MemberType memberType = memberDTO.memberType();

        if (memberRepository.existsByUsername(username)) {
            throw new UserNameAlreadyExistsException(username);
        }

        return memberRepository.save(new Member(username, name, passwordEncoder.encode(password), email, phoneNumber, memberType));
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
