package com.dispatch.common;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {
	@Value("${jwt.secret}")
	private String jwtKey;

	@Autowired
	EncryptionUtils encryptionUtils;

	private Key key() {
		return Keys.hmacShaKeyFor(this.jwtKey.getBytes());
	}

	public boolean isJwtTokenValid(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SneakyThrows
	public String generateJwtToken(String email) {
		JwtPayload jwtPayload = new JwtPayload(email);

		String payload = new ObjectMapper().writeValueAsString(jwtPayload);

		String encryptedValue = this.encryptionUtils.encrypt(payload);

		return Jwts.builder()
				.claim("payload", encryptedValue)
				.signWith(this.key(), SignatureAlgorithm.HS256)
				.setExpiration(this.dateAfterOneMonth())
				.compact();
	}

	private Date dateAfterOneMonth() {
		long currentTimestamp = new Date().getTime();

		int daysInAMonth = 30;

		long millisecondsInAMonth = Duration.ofDays(daysInAMonth).toMillis();

		return new Date(currentTimestamp + millisecondsInAMonth);
	}
}
