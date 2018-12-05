package com.shop.service.app.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.shop.util.Constants;

@Service
public class SecurityService {

	private static Cipher ecipher;
	private static Cipher dcipher;

	/**
	 * DES 64 bit encryption
	 */
	public SecurityService() {
		try {
			IvParameterSpec iv = new IvParameterSpec(Constants.IV_KEY.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(Constants.SECRET_KEY.getBytes("UTF-8"), "DES");

			dcipher = Cipher.getInstance("DES/CBC/PKCS5PADDING");
			dcipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			ecipher = Cipher.getInstance("DES/CBC/PKCS5PADDING");
			ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encryptDES64(String secret, String ivKey, String value) {
		try {

			byte[] encrypted = ecipher.doFinal(value.getBytes());
			return Base64.getUrlEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String decryptDES64(String secret, String ivKey, String encrypted) {
		try {

			byte[] original = dcipher.doFinal(Base64.getUrlDecoder().decode(encrypted));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}