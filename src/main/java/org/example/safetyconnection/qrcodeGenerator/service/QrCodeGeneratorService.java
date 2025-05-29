package org.example.safetyconnection.qrcodeGenerator.service;

import org.example.safetyconnection.qrcodeGenerator.dto.request.QrGenerateRequestDTO;

public interface QrCodeGeneratorService {
	byte[] createQrCode(QrGenerateRequestDTO qrGenerateRequestDTO);
}
