package org.example.safetyconnection.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.safetyconnection.dto.request.CompanionReqDTO;
import org.example.safetyconnection.dto.response.MemberResDTO;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.qrcodeGenerator.service.QrCodeGeneratorService;
import org.example.safetyconnection.dto.request.FCMNotificationRequestDTO;
import org.example.safetyconnection.jwt.JwtTokenProvider;
import org.example.safetyconnection.service.command.CompanionCommandService;
import org.example.safetyconnection.service.facade.FCMService;
import org.example.safetyconnection.service.facade.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

  private final FCMService fcmService;
  private final JwtTokenProvider jwtTokenProvider;
  private final QrCodeGeneratorService qrCodeGeneratorService;
  private final CompanionCommandService companionCommandService;
  private final MemberService memberService;

  @PostMapping("/send-noti")
  public ResponseEntity<String> sendRequest(HttpServletRequest httpServletRequest,
                                                   @RequestBody FCMNotificationRequestDTO fcmNotificationRequestDTO) {
    String accessToken = httpServletRequest.getHeader("access");
    Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

    String notiResult = fcmService.sendNotification(fcmNotificationRequestDTO);
    
    companionCommandService.updateCompanionRequest(
        new CompanionReqDTO(
            userId,
            fcmNotificationRequestDTO.compId(),
            fcmNotificationRequestDTO.latitude(),
            fcmNotificationRequestDTO.longitude()
        )
    );

    log.info("메세지 전송 성공");

    return ResponseEntity.ok(notiResult);
  }
}
