package org.example.safetyconnection.QrcodeGenerator.service;

import org.example.safetyconnection.QrcodeGenerator.dto.request.QrGenerateRequestDTO;

public interface QrCodeGeneratorService {
	byte[] createQrCode(QrGenerateRequestDTO qrGenerateRequestDTO);
}
