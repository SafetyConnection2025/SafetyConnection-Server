package org.example.safetyconnection.dto.request;

import com.google.firebase.messaging.AndroidConfig;

public record FCMNotificationRequestDTO(MessageDTO message) {
  public record MessageDTO(String token, NotificationDTO notification, DataDTO data, AndroidDTO android) {}

  public record NotificationDTO(String title, String body) {}

  public record DataDTO(String click_action) {}

  public record AndroidDTO(AndroidConfig.Priority priority, AndroidNotificationDTO notification) {}

  public record AndroidNotificationDTO(String click_action) {}
}
