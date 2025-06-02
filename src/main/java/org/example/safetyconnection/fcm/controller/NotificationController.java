package org.example.safetyconnection.fcm.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.qrcode.service.QrCodeGeneratorService;
import org.example.safetyconnection.fcm.dto.request.FCMNotificationRequestDTO;
import org.example.safetyconnection.jwt.JwtTokenProvider;
import org.example.safetyconnection.fcm.service.facade.FCMService;
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

  @PostMapping("/send-noti")
  public ResponseEntity<String> sendRequest(HttpServletRequest httpServletRequest,
                                                   @RequestBody FCMNotificationRequestDTO fcmNotificationRequestDTO) {
    String accessToken = httpServletRequest.getHeader("access");
    Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

    String notiResult = fcmService.sendNotification(fcmNotificationRequestDTO);

    log.info("메세지 전송 성공");

    return ResponseEntity.ok(notiResult);
  }
}
