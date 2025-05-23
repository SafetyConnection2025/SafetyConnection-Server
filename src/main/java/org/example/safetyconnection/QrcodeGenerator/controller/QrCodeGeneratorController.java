package org.example.safetyconnection.QrcodeGenerator.controller;

import org.example.safetyconnection.QrcodeGenerator.dto.request.QrGenerateReqeustDTO;
import org.example.safetyconnection.QrcodeGenerator.service.QrCodeGeneratorService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	public ResponseEntity<byte[]> generateQr(@PathVariable String username) {
		QrGenerateReqeustDTO qrGenerateReqeustDTO = new QrGenerateReqeustDTO(username);
		byte[] qrImage = qrCodeGeneratorService.createQrCode(qrGenerateReqeustDTO);

		return ResponseEntity.ok(qrImage);
	}


}
