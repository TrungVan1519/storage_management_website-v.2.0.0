package com.trungvan.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class HashingPassword {

	static final String SALT = "storage_management";
	
	public static String encrypt(String originPassword) {
		
		String result = null;
		try {
			// Su dung thuat toan SHA-512 to encode
			MessageDigest md = MessageDigest.getInstance("SHA-512"); 
			
			// Ket hop SALT vs originPassword duoi dang byte thanh password moi
			md.update(SALT.getBytes());
			byte[] hashedPassword = md.digest(originPassword.getBytes(StandardCharsets.US_ASCII));

			// > Ma hoa password moi va truyen ve String
			// > Cat String va chi lay String tu 0 den 32 thay vi lay toan bo password da hash, 
			// 		ngoai ra co the lay toan bo password da hash cung duoc, khong nhat thiet phai lam nhu nay
			result = Base64.getEncoder().encodeToString(hashedPassword).substring(0, 32);
			
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		
		return result;
	}
}
