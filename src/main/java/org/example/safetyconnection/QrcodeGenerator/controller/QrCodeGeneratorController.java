package org.example.safetyconnection.QrcodeGenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/qr/generate")
public class QrCodeGeneratorController {
	@GetMapping()
	public String generateQr() {
		return "qrcode-generator";
	}


}
