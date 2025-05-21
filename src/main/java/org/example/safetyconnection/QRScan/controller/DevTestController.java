package org.example.safetyconnection.QRScan.controller;

import lombok.RequiredArgsConstructor;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.jwt.JwtToken;
import org.example.safetyconnection.jwt.JwtTokenProvider;
import org.example.safetyconnection.repository.MemberRepository;
import org.example.safetyconnection.service.facade.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevTestController {

	private final MemberRepository memberRepository;
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/issue-token")
	public ResponseEntity<JwtToken> issueToken(@RequestParam String username) {
		Member member = memberRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("사용자 없음: " + username));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

		JwtToken token = jwtTokenProvider.generateToken(authentication, member);

		return ResponseEntity.ok(token);
	}
}
