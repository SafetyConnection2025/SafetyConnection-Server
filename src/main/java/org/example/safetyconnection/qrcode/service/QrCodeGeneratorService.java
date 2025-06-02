package org.example.safetyconnection.qrcode.service;

import org.example.safetyconnection.qrcode.dto.request.QrGenerateRequestDTO;

public interface QrCodeGeneratorService {
	byte[] createQrCode(QrGenerateRequestDTO qrGenerateRequestDTO);
}
