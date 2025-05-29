package org.example.safetyconnection.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.safetyconnection.dto.request.CompanionCallRequestDTO;
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
      @RequestBody CompanionCallRequestDTO requestDTO) {
    String accessToken = httpServletRequest.getHeader("access");
    Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

    FCMNotificationRequestDTO fcmDTO = new FCMNotificationRequestDTO(requestDTO.message());
    String notiResult = fcmService.sendNotification(fcmDTO);

    companionCommandService.updateCompanionRequest(
        new CompanionReqDTO(
            userId,
            requestDTO.companionId(),
            requestDTO.latitude(),
            requestDTO.longitude()
        )
    );

    return ResponseEntity.ok(notiResult);
  }
}
