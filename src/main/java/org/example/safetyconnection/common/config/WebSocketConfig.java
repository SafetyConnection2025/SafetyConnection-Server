package org.example.safetyconnection.common.config;

import lombok.RequiredArgsConstructor;
import org.example.safetyconnection.yolov8.controller.ObjectDetectionWebSocketHandler;
import org.example.safetyconnection.yolov8.service.Yolov8DetectionSerivce;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
  private final Yolov8DetectionSerivce yolov8DetectionSerivce;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new ObjectDetectionWebSocketHandler(yolov8DetectionSerivce),
        "/ws/detection")
        .setAllowedOrigins("*");
  }

}
