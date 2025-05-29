package org.example.safetyconnection.qrcodeGenerator.controller;

import java.util.Base64;

import org.example.safetyconnection.qrcodeGenerator.dto.request.QrGenerateRequestDTO;
import org.example.safetyconnection.qrcodeGenerator.service.QrCodeGeneratorService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qr")
public class QrCodeGeneratorController {

	private final QrCodeGeneratorService qrCodeGeneratorService;

	public QrCodeGeneratorController(QrCodeGeneratorService qrCodeGeneratorService) {
		this.qrCodeGeneratorService = qrCodeGeneratorService;
	}

	@GetMapping(value = "/generate/{username}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<String> generateQr(@PathVariable String username) {
		QrGenerateRequestDTO qrGenerateRequestDTO = new QrGenerateRequestDTO(username);
		byte[] qrImage = qrCodeGeneratorService.createQrCode(qrGenerateRequestDTO);
		String base64Encoded = Base64.getEncoder().encodeToString(qrImage);

		return ResponseEntity.ok(base64Encoded);
	}
}
