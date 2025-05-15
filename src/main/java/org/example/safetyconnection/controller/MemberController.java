package org.example.safetyconnection.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원 관련 API", description = "회원 정보, 위치, 동행자 관리")
public class MemberController {

  @Autowired
  private MemberService memberService;
  @Autowired
  private CompanionService companionService;
  @Autowired
  private FCMService fcmService;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Operation(description = "username, password 이용하여 로그인")
  @PostMapping("/login")
    public JwtToken getLoginInfo(@RequestBody LogInDTO logInDTO) {
        JwtToken jwtToken = memberService.logIn(logInDTO);

        log.info("name: {}, 로그인이 완료되었습니다.", logInDTO.username());

        return jwtToken;
    }

    @Operation(description = "userId로 유저 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<MemberDTO> getUserById(@PathVariable Long userId) {
        log.info("GET 요청 수신: userId = {}", userId);

        MemberDTO memberDTO = memberService.findUserById(userId);

        log.info("username: {} 유저를 불러옵니다.", memberDTO.username());

        return ResponseEntity.ok(memberDTO);
    }

    @Operation(description = "userId로 유저의 위치정보 조회")
    @GetMapping("/{userId}/location")
    public ResponseEntity<MemberLocationResDTO> getLocationById(@PathVariable Long userId) {
        log.info("L_GET 요청 수신: userId = {}", userId);

        MemberLocationResDTO memberLocationResDTO = memberService.getLocationById(userId);

        log.info("userId: {} 유저의 위치를 불러옵니다.", memberLocationResDTO.id());

        return ResponseEntity.ok(memberLocationResDTO);
    }

    @Operation(description = "해당 user의 동행자 전체 조회")
    @GetMapping("/allcompanions")
    public ResponseEntity<List<CompanionResDTO>> findAllCompanions(HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        log.info("ALL_COMPANIONS_GET 요청 수신: UserID = {}", userId);

        List<CompanionResDTO> companions = companionService.findAllCompanionsByUserId(userId);

        log.info("조회 성공: {}명의 동행자 발견, userID = {}", companions.size(), userId);
        return ResponseEntity.ok(companions);
    }

    @Operation(description = "username으로 해당 유저의 id 조회하기")
    @GetMapping("/{username}/namereq")
    public ResponseEntity<Long> getIdByUsername(@PathVariable String username) {
        log.info("ID_req 요청 수신: userName = {}", username);

        Long userId = memberService.findIdByUsername(username);

        return ResponseEntity.ok(userId);
    }

    @Operation(description = "username으로 해당 유저의 위치 정보 조회")
    @GetMapping("/{username}/compaloc")
    public ResponseEntity<MemberLocationResDTO> getLocationByName(@PathVariable String username) {
        log.info("CL_GET 요청 수신: userName = {}", username);

        MemberLocationResDTO memberLocationResDTO = memberService.getLocationByUsername(username);

        log.info("사용자 조회 성공: userId = {}", memberLocationResDTO.id());

        return ResponseEntity.ok(memberLocationResDTO);
    }

    @Operation(description = "해당 유저 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteById(@PathVariable Long userId) {
        log.info("DELETE 요청 수신: userId = {}", userId);

        memberService.deleteUserById(userId);

        log.info("사용자 삭제에 성공하였습니다, userId: {}", userId);

        return ResponseEntity.ok("삭제 완료");
    }

    @Operation(description = "현재 로그인한 유저에 compId로 해당 동행자 추가하기")
    @PutMapping("/addcomp/{compId}")
    public ResponseEntity<CompanionResDTO> addCompanion(HttpServletRequest httpServletRequest, @PathVariable Long compId) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        log.info("Comp_ADD 요청 수신: userId = {}, compId = {}", userId, compId);

        CompanionResDTO companionResDTO = companionService.addCompanion(userId, compId);

        return ResponseEntity.ok(companionResDTO);
    }

    @Operation(description = "현재 로그인한 유저의 위치 정보 갱신하기")
    @PutMapping("/updateLocation")
    public ResponseEntity<MemberLocationResDTO> addLocation(HttpServletRequest httpServletRequest, @RequestBody MemberLocationReqDTO memberLocationReqDTO) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        log.info("L_POST 요청 수신: userId = {}", userId);

        MemberLocationResDTO memberLocationResDTO = memberService.setLocation(userId, memberLocationReqDTO);

        log.info("사용자 위치 정보 갱신 성공: userId = {}", memberLocationResDTO.id());

        return ResponseEntity.ok(memberLocationResDTO);
    }

    @Operation(description = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody MemberDTO memberDTO) {
        Member member = memberService.registerUser(memberDTO);

        return ResponseEntity.ok(member.getName() + ", 회원가입이 완료되었습니다.");
    }

   
    @Operation(description = "해당 compId의 동행자에게 알림 보내기")
    @GetMapping("/{compId}/sendNotification")
    public ResponseEntity<FCMTokenResDTO> sendNotification(@PathVariable Long compId) {
        log.info("id: {} 사용자의 토큰 조회", compId);

        FCMTokenResDTO fcmTokenResDTO = fcmService.getFCMTokenById(compId);

        log.info("사용자 토큰 조회 완료: token = {}", fcmTokenResDTO.fcmToken());

        return ResponseEntity.ok(fcmTokenResDTO);
    }

    @Operation(description = "해당 유저 토근 업데이트")
    @PutMapping("/{userId}/updateToken")
    public ResponseEntity<FCMTokenResDTO> updateToken(@PathVariable("userId") Long userId, @RequestBody FCMTokenReqDTO fcmTokenReqDTO) {
        log.info("UPDATE_TOKEN 요청 수신: userId = {}", userId);

        FCMTokenResDTO fcmTokenResDTO = fcmService.setFCMToken(userId, fcmTokenReqDTO);

        log.info("FCM 토큰 업데이트 성공: userId = {}", fcmTokenResDTO.userId());

        return ResponseEntity.ok(fcmTokenResDTO);
    }

    @Operation(description = "해당 compId 동행자 삭제하기")
    @DeleteMapping("/delcomp/{compId}")
    public ResponseEntity<String> deleteCompanion(HttpServletRequest httpServletRequest, @PathVariable Long compId) {
        String accessToken = httpServletRequest.getHeader("access");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        log.info("Comp DEL 요청 수신. CompId = {}", compId);
        companionService.deleteCompanion(userId, compId);

        return ResponseEntity.ok("삭제 완료");
    }

}