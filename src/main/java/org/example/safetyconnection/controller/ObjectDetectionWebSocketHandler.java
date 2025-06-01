package org.example.safetyconnection.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.dto.request.DetectedObjectRequestDTO;
import org.example.safetyconnection.dto.response.DetectedObjectResponseDTO;
import org.example.safetyconnection.service.yolov8.Yolov8DetectionSerivce;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
public class ObjectDetectionWebSocketHandler extends TextWebSocketHandler {
  private final Yolov8DetectionSerivce yolov8DetectionSerivce;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    log.info("Connected to yolov8");
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String filename = message.getPayload();
    DetectedObjectRequestDTO detectedObjectRequestDTO = new DetectedObjectRequestDTO(filename);
    DetectedObjectResponseDTO detectedObjectResponseDTO = yolov8DetectionSerivce.detect(detectedObjectRequestDTO);

    session.sendMessage(new TextMessage(detectedObjectResponseDTO.image()));
  }
}
