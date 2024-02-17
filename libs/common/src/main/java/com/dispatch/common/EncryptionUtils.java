package com.dispatch.common;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

public class EncryptionUtils {

	@Value("${encryption-key}")
	private String encryptionKey;

	@SneakyThrows
	public String encrypt(String toEncrypt) {
		String encryptionAlgorithm = "AES";

		byte[] keyBytes = Base64.getDecoder().decode(this.encryptionKey);

		Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

		SecretKey secretKeySpec = new SecretKeySpec(keyBytes, encryptionAlgorithm);

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		return Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes(StandardCharsets.UTF_8)));
	}

	@SneakyThrows
	public String decrypt(String toDecrypt) throws RuntimeException {
		byte[] keyBytes = Base64.getDecoder().decode(this.encryptionKey);

		SecretKey secretKeySpec = new SecretKeySpec(keyBytes, "AES");

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

		byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(toDecrypt));

		return new String(decryptedData, StandardCharsets.UTF_8);
	}
}
