package org.example.safetyconnection.fcm.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.fcm.dto.request.FCMNotificationRequestDTO;
import org.example.safetyconnection.member.dto.request.FCMTokenReqDTO;
import org.example.safetyconnection.member.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.fcm.service.command.FCMCommandService;
import org.example.safetyconnection.fcm.service.query.FCMQueryService;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.*;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {

  private final FCMQueryService fcmQueryService;
  private final FCMCommandService fcmCommandService;

  public String sendNotification(FCMNotificationRequestDTO fcmNotificationRequestDTO) {
    try {
      FCMNotificationRequestDTO.MessageDTO messageDTO = fcmNotificationRequestDTO.message();
      String fcmToken = messageDTO.token();

      Notification notification = Notification.builder()
          .setTitle(messageDTO.notification().title())
          .setBody(messageDTO.notification().body())
          .build();

      AndroidConfig androidConfig = AndroidConfig.builder()
          .setPriority(messageDTO.android().priority())
          .setNotification(AndroidNotification.builder()
              .setClickAction(messageDTO.android().notification().click_action())
              .build())
          .build();

      Map<String, String> data = new HashMap<>();
      data.put("click_action", messageDTO.data().click_action());

      Message message = Message.builder()
          .setToken(fcmToken)
          .setNotification(notification)
          .putAllData(data)
          .setAndroidConfig(androidConfig)
          .build();

      return FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      return e.getMessage();
    }
  }

  public FCMTokenResDTO getFCMTokenById(Long userId) {
    return fcmQueryService.getTokenById(userId);
  }

  public FCMTokenResDTO setFCMToken(Long userId, FCMTokenReqDTO fcmTokenReqDTO) {
    return fcmCommandService.setFCMToken(userId, fcmTokenReqDTO);
  }

  public FCMTokenResDTO getFCMTokenByUid(String uid) {
    return fcmQueryService.getFCMTokenByUid(uid);
  }
}