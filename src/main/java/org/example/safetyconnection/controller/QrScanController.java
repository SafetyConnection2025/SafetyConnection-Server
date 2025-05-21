package org.example.safetyconnection.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.safetyconnection.dto.request.QrScanRequestDTO;
import org.example.safetyconnection.dto.request.FCMNotificationRequestDTO;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.repository.MemberRepository;
import org.example.safetyconnection.service.facade.FCMService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.messaging.AndroidConfig;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
@Slf4j
public class QrScanController {

	private final MemberRepository memberRepository;
	private final FCMService fcmService;

	@PostMapping("/scan")
	public ResponseEntity<Void> handleQrScan(
			@RequestBody QrScanRequestDTO request
	) {
		String scannedUsername = request.scannedUsername();

		Member owner = memberRepository.findByUsername(scannedUsername)
			.orElseThrow(() -> new UsernameNotFoundException(scannedUsername));

		// 1. 대상 유저의 FCM 토큰
		String fcmToken = owner.getFcmToken();
		if (fcmToken == null) {
			throw new IllegalStateException("대상 유저의 FCM 토큰이 없습니다.");
		}

		// 임시 알림 DTO
		FCMNotificationRequestDTO fcmNotificationRequestDTO = new FCMNotificationRequestDTO(
			new FCMNotificationRequestDTO.MessageDTO(
				fcmToken,
				new FCMNotificationRequestDTO.NotificationDTO(
					"차량 이동 요청",
					"차량 이동 요청이 도착했어요!"
				),
				new FCMNotificationRequestDTO.DataDTO("req" + scannedUsername),
				new FCMNotificationRequestDTO.AndroidDTO(
					AndroidConfig.Priority.HIGH,
					new FCMNotificationRequestDTO.AndroidNotificationDTO("FLUTTER_NOTIFICATION_CLICK")
				)
			)
		);

		// 3. FCM 전송
		String response = fcmService.sendNotification(fcmNotificationRequestDTO);
		log.info("FCM 응답: {}", response);

		return ResponseEntity.ok().build();
	}

}
