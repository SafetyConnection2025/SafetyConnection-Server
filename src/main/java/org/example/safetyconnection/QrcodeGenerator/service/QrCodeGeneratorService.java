package org.example.safetyconnection.QrcodeGenerator.service;

import org.example.safetyconnection.QrcodeGenerator.dto.request.QrGenerateReqeustDTO;

public interface QrCodeGeneratorService {
	byte[] createQrCode(QrGenerateReqeustDTO qrGenerateReqeustDTO);
}
