package org.example.safetyconnection.QrcodeGenerator.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.example.safetyconnection.QrcodeGenerator.Repository.QrGeneratorRepository;
import org.example.safetyconnection.QrcodeGenerator.domain.QrGenerator;
import org.example.safetyconnection.QrcodeGenerator.dto.request.QrGenerateRequestDTO;
import org.example.safetyconnection.QrcodeGenerator.service.QrCodeGeneratorService;
import org.example.safetyconnection.exception.CompanionNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QrCodeGeneratorServiceImpl implements QrCodeGeneratorService {
	private final QrGeneratorRepository qrGeneratorRepository;

	@Transactional
	@Override
	public byte[] createQrCode(QrGenerateRequestDTO qrGenerateRequestDTO) {

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		String username = qrGenerateRequestDTO.username();
		String text = "https://safety2025.duckdns.org/scanned?uid=";

		if (qrGeneratorRepository.findByUsername(username) != null) {
			text += qrGeneratorRepository.findByUsername(username).getUid();
		} else {
			QrGenerator qrGenerator = new QrGenerator(username);
			qrGeneratorRepository.save(qrGenerator);
			text += qrGenerator.getUid();
		}

		int width = 200;
		int height = 200;
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		} catch (WriterException e) {
			throw new RuntimeException(e);
		}

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		try {
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pngOutputStream.toByteArray();
	}

	@Transactional(readOnly = true)
	public String findUsernameByUid(String uid) {
		QrGenerator user = qrGeneratorRepository.findByUid(uid).orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
		return user.getUsername();
	}
}
