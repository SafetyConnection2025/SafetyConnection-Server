package org.example.safetyconnection.QRScan.controller;

import lombok.extern.slf4j.Slf4j;

import org.example.safetyconnection.QRScan.dto.request.QrScanRequestDTO;
import org.example.safetyconnection.dto.request.FCMNotificationRequestDTO;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.repository.MemberRepository;
import org.example.safetyconnection.service.facade.FCMService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.messaging.AndroidConfig;

@RestController
@RequestMapping("/api/qr")
@Slf4j
public class QrScanController {

	private final MemberRepository memberRepository;
	private final FCMService fcmService;

	public QrScanController(MemberRepository memberRepository, FCMService fcmService) {
		this.memberRepository = memberRepository;
		this.fcmService = fcmService;
	}

	@PostMapping("/scan")
	public ResponseEntity<Void> handleQrScan(
		@RequestBody QrScanRequestDTO request,
		@AuthenticationPrincipal UserDetails currentUser) {

		String scanner = currentUser.getUsername();
		String scannedUsername = request.scannedUsername();

		Member owner = memberRepository.findByUsername(scannedUsername)
			.orElseThrow(() -> new UsernameNotFoundException(scannedUsername));

		// 1. 대상 유저의 FCM 토큰
		String fcmToken = owner.getFcmToken();
		if (fcmToken == null) {
			throw new IllegalStateException("대상 유저의 FCM 토큰이 없습니다.");
		}

		// 2. 알림 구성 DTO 생성
		FCMNotificationRequestDTO fcmRequest = new FCMNotificationRequestDTO(
			new FCMNotificationRequestDTO.MessageDTO(
				fcmToken,
				new FCMNotificationRequestDTO.NotificationDTO(
					"QR 스캔 알림",
					scanner + "님이 당신의 QR을 스캔했습니다."
				),
				new FCMNotificationRequestDTO.DataDTO("OPEN_QR_SCAN_RESULT"),
				new FCMNotificationRequestDTO.AndroidDTO(
					AndroidConfig.Priority.HIGH,
					new FCMNotificationRequestDTO.AndroidNotificationDTO("OPEN_QR_SCAN_RESULT")
				)
			)
		);

		// 3. FCM 전송
		String response = fcmService.sendNotification(fcmRequest);
		log.info("FCM 응답: {}", response);

		return ResponseEntity.ok().build();
	}

}
