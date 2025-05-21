package org.example.safetyconnection.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.request.FCMTokenReqDTO;
import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.dto.LogInDTO;
import org.example.safetyconnection.dto.MemberDTO;
import org.example.safetyconnection.dto.request.MemberLocationReqDTO;
import org.example.safetyconnection.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.dto.response.MemberLocationResDTO;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.jwt.JwtToken;
import org.example.safetyconnection.jwt.JwtTokenProvider;
import org.example.safetyconnection.service.facade.CompanionService;
import org.example.safetyconnection.service.facade.FCMService;
import org.example.safetyconnection.service.facade.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class MemberController {

  @Autowired
  private MemberService memberService;
  @Autowired
  private CompanionService companionService;
  @Autowired
  private FCMService fcmService;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @PostMapping("/login")
    public JwtToken getLoginInfo(@RequestBody LogInDTO logInDTO) {
        JwtToken jwtToken = memberService.logIn(logInDTO);

        log.info("name: {}, 로그인이 완료되었습니다.", logInDTO.username());

        return jwtToken;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MemberDTO> getUserById(@PathVariable Long userId) {
        log.info("GET 요청 수신: userId = {}", userId);

        MemberDTO memberDTO = memberService.findUserById(userId);

        log.info("username: {} 유저를 불러옵니다.", memberDTO.username());

        return ResponseEntity.ok(memberDTO);
    }

    @GetMapping("/{userId}/location")
    public ResponseEntity<MemberLocationResDTO> getLocationById(@PathVariable Long userId) {
        log.info("L_GET 요청 수신: userId = {}", userId);

        MemberLocationResDTO memberLocationResDTO = memberService.getLocationById(userId);

        log.info("userId: {} 유저의 위치를 불러옵니다.", memberLocationResDTO.id());

        return ResponseEntity.ok(memberLocationResDTO);
    }

    @GetMapping("/allcompanions")
    public ResponseEntity<List<CompanionResDTO>> findAllCompanions(HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        log.info("ALL_COMPANIONS_GET 요청 수신: UserID = {}", userId);

        List<CompanionResDTO> companions = companionService.findAllCompanionsByUserId(userId);

        log.info("조회 성공: {}명의 동행자 발견, userID = {}", companions.size(), userId);
        return ResponseEntity.ok(companions);
    }

    @GetMapping("/{username}/namereq")
    public ResponseEntity<Long> getIdByUsername(@PathVariable String username) {
        log.info("ID_req 요청 수신: userName = {}", username);

        Long userId = memberService.findIdByUsername(username);

        return ResponseEntity.ok(userId);
    }

    @GetMapping("/{username}/compaloc")
    public ResponseEntity<MemberLocationResDTO> getLocationByName(@PathVariable String username) {
        log.info("CL_GET 요청 수신: userName = {}", username);

        MemberLocationResDTO memberLocationResDTO = memberService.getLocationByUsername(username);

        log.info("사용자 조회 성공: userId = {}", memberLocationResDTO.id());

        return ResponseEntity.ok(memberLocationResDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteById(@PathVariable Long userId) {
        log.info("DELETE 요청 수신: userId = {}", userId);

        memberService.deleteUserById(userId);

        log.info("사용자 삭제에 성공하였습니다, userId: {}", userId);

        return ResponseEntity.ok("삭제 완료");
    }

    @PutMapping("/addcomp/{compId}")
    public ResponseEntity<CompanionResDTO> addCompanion(HttpServletRequest httpServletRequest, @PathVariable Long compId) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        log.info("Comp_ADD 요청 수신: userId = {}, compId = {}", userId, compId);

        CompanionResDTO companionResDTO = companionService.addCompanion(userId, compId);

        return ResponseEntity.ok(companionResDTO);
    }

    @PutMapping("/updateLocation")
    public ResponseEntity<MemberLocationResDTO> addLocation(HttpServletRequest httpServletRequest, @RequestBody MemberLocationReqDTO memberLocationReqDTO) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        log.info("L_POST 요청 수신: userId = {}", userId);

        MemberLocationResDTO memberLocationResDTO = memberService.setLocation(userId, memberLocationReqDTO);

        log.info("사용자 위치 정보 갱신 성공: userId = {}", memberLocationResDTO.id());

        return ResponseEntity.ok(memberLocationResDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody MemberDTO memberDTO) {
        Member member = memberService.registerUser(memberDTO);

        return ResponseEntity.ok(member.getName() + ", 회원가입이 완료되었습니다.");
    }

   
    @GetMapping("/{compId}/sendNotification")
    public ResponseEntity<FCMTokenResDTO> sendNotification(@PathVariable Long compId) {
        log.info("id: {} 사용자의 토큰 조회", compId);

        FCMTokenResDTO fcmTokenResDTO = fcmService.getFCMTokenById(compId);

        log.info("사용자 토큰 조회 완료: token = {}", fcmTokenResDTO.fcmToken());

        return ResponseEntity.ok(fcmTokenResDTO);
    }

    @PutMapping("/{userId}/updateToken")
    public ResponseEntity<FCMTokenResDTO> updateToken(@PathVariable("userId") Long userId, @RequestBody FCMTokenReqDTO fcmTokenReqDTO) {
        log.info("UPDATE_TOKEN 요청 수신: userId = {}", userId);

        FCMTokenResDTO fcmTokenResDTO = fcmService.setFCMToken(userId, fcmTokenReqDTO);

        log.info("FCM 토큰 업데이트 성공: userId = {}", fcmTokenResDTO.userId());

        return ResponseEntity.ok(fcmTokenResDTO);
    }

    @DeleteMapping("/delcomp/{compId}")
    public ResponseEntity<String> deleteCompanion(HttpServletRequest httpServletRequest, @PathVariable Long compId) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        log.info("Comp DEL 요청 수신. CompId = {}", compId);
        companionService.deleteCompanion(userId, compId);

        return ResponseEntity.ok("삭제 완료");
    }

}