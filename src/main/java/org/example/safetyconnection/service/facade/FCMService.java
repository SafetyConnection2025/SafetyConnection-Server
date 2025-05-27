package org.example.safetyconnection.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.request.FCMNotificationRequestDTO;
import org.example.safetyconnection.dto.request.FCMTokenReqDTO;
import org.example.safetyconnection.dto.response.FCMTokenResDTO;
import org.example.safetyconnection.service.command.FCMCommandService;
import org.example.safetyconnection.service.query.FCMQueryService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.*;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional(readOnly = true)
  @Cacheable(value = "memberToken", key = "#userId", unless = "#result == null")
  public FCMTokenResDTO getFCMTokenById(Long userId) {
    return fcmQueryService.getTokenById(userId);
  }

  @CachePut(value = "memberToken", key = "#userId")
  public FCMTokenResDTO setFCMToken(Long userId, FCMTokenReqDTO fcmTokenReqDTO) {
    return fcmCommandService.setFCMToken(userId, fcmTokenReqDTO);
  }
}