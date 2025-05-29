package org.example.safetyconnection.controller;

import java.util.List;

import org.example.safetyconnection.dto.response.CompanionResDTO;
import org.example.safetyconnection.jwt.JwtTokenProvider;
import org.example.safetyconnection.service.query.CompanionQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companions")
public class CompanionController {

	private final JwtTokenProvider jwtTokenProvider;
	private final CompanionQueryService companionQueryService;

	@GetMapping("/recent")
	public ResponseEntity<List<CompanionResDTO>> getRecentCompanions(HttpServletRequest request) {
		String accessToken = request.getHeader("access");
		Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

		List<CompanionResDTO> companions = companionQueryService.getRecentCompanions(userId);
		return ResponseEntity.ok(companions);
	}
}
