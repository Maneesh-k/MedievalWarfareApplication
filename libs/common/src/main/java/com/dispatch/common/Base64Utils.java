package com.dispatch.common;

import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class Base64Utils {
	public String decode(String base64String) {
		byte[] decodedBytes = Base64.getUrlDecoder().decode(base64String);

		return new String(decodedBytes);
	}

	public String encode(String string) {
		return Base64.getUrlEncoder().encodeToString(string.getBytes());
	}
}
