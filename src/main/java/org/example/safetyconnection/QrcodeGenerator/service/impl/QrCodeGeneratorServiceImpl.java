package org.example.safetyconnection.QrcodeGenerator.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.example.safetyconnection.QrcodeGenerator.dto.request.QrGenerateReqeustDTO;
import org.example.safetyconnection.QrcodeGenerator.service.QrCodeGeneratorService;
import org.example.safetyconnection.car.entity.Car;
import org.example.safetyconnection.car.repository.CarRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QrCodeGeneratorServiceImpl implements QrCodeGeneratorService {
	private final CarRepository carRepository;

	@Override
	public byte[] createQrCode(QrGenerateReqeustDTO qrGenerateReqeustDTO) {
		Car car = carRepository.findByMember_Username(qrGenerateReqeustDTO.username())
			.orElseThrow(() -> new UsernameNotFoundException(qrGenerateReqeustDTO.username()));

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		String text = car.getCarId();
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
}
