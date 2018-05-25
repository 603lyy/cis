package com.yaheen.cis.util.nfc;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/** AES对称加密工具类 */
public class AESUtils {

	public static byte[] encrypt(byte[] sSrc, byte[] sKey) {
		byte[] encrypted = new byte[0];
		try {
			if (sKey != null && 16 == sKey.length) {// 必须为16字节
				SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
				encrypted = cipher.doFinal(sSrc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypted;
	}

	public static byte[] encrypt(String sSrc, String sKey) {
		byte[] encrypted = new byte[0];
		try {
			byte[] bKeys = Base64.decode(sKey);
			byte[] bSrcs = sSrc.getBytes("UTF-8");
			encrypted = encrypt(bSrcs, bKeys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypted;
	}

	public static String encryptToString(byte[] sSrc, byte[] sKey) {
		byte[] encrypted = encrypt(sSrc, sKey);
		return Base64.encode(encrypted);
	}

	public static String encryptToString(String sSrc, String sKey) {
		byte[] encrypted = encrypt(sSrc, sKey);
		return Base64.encode(encrypted);
	}

	public static byte[] decrypt(byte[] sSrc, byte[] sKey) {
		byte[] original = new byte[0];
		try {
			if (sKey != null && 16 == sKey.length) {// 必须为16字节
				SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
				// byte[] encrypted1 = Base64Utils.decode(sSrc);// 先用base64解密
				original = cipher.doFinal(sSrc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return original;
	}

	public static byte[] decrypt(String sSrc, String sKey) {
		byte[] original = new byte[0];
		try {
			byte[] bKeys = Base64.decode(sKey);
			byte[] bSrcs = Base64.decode(sSrc);
			original = decrypt(bSrcs, bKeys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return original;
	}

	public static String decryptToString(byte[] sSrc, byte[] sKey) {
		String retVal = "";
		try {
			byte[] decrypted = decrypt(sSrc, sKey);
			retVal = new String(decrypted, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public static String decryptToString(String sSrc, String sKey) {
		String retVal = "";
		try {
			byte[] decrypted = decrypt(sSrc, sKey);
			retVal = new String(decrypted, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 此处使用AES-128-ECB加密模式，key需要为16位。
		 */
		String cKey = "X2Am6tVLnwMMX8kVgdDk5w==";
		// 需要加密的字串
		String cSrc = "2|广州首润机电工程有限公司|222222|A|2016-10-20|广州|BDD50EF4DC9DFD0482B9A1AE08EAB8C5";
		System.out.println(cSrc);
		// 加密
		String enString = AESUtils.encryptToString(cSrc, cKey);
		System.out.println("加密后的字串是：" + enString);
		// 解密
		String DeString = AESUtils.decryptToString(enString, cKey);
		System.out.println("解密后的字串是：" + DeString);
	}
}
