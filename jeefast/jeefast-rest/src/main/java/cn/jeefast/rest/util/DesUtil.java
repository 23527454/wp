package cn.jeefast.rest.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class DesUtil {
	
	public static final String DESCRET="tokenDes";
	
	
	
	private byte[] desKey;


	// 解密数据
	public static String decrypt(String message, String key) throws Exception {
	byte[] bytesrc = convertHexString(message);
	Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
	cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
	byte[] retByte = cipher.doFinal(bytesrc);
	return new String(retByte);
	}


	public static String encrypt(String message, String key) throws Exception {
	Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
	cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
	return toHexString(cipher.doFinal(message.getBytes("UTF-8")));
	}


	public static byte[] convertHexString(String ss) {
	byte digest[] = new byte[ss.length() / 2];
	for (int i = 0; i < digest.length; i++) {
	String byteString = ss.substring(2 * i, 2 * i + 2);
	int byteValue = Integer.parseInt(byteString, 16);
	digest[i] = (byte) byteValue;
	}
	return digest;
	}

	public static void main(String[] args) throws Exception {
	String key = "pio-tech1111";
	System.out.println(key.substring(0,8));
	String value = "1";
	//String jiami = java.net.URLEncoder.encode(value, "utf-8").toLowerCase();
	String jiami = TokenGenerator.generateValue();
	System.out.println("加密数据:" + jiami);
	String a = encrypt(jiami, key.substring(0,8)).toUpperCase();


	System.out.println("加密后的数据为:" + a);
	String b = decrypt(a, key);
	System.out.println("解密后的数据:" + b);
	}


	public static String toHexString(byte b[]) {
	StringBuffer hexString = new StringBuffer();
	for (int i = 0; i < b.length; i++) {
	String plainText = Integer.toHexString(0xff & b[i]);
	if (plainText.length() < 2)
	plainText = "0" + plainText;
	hexString.append(plainText);
	}
	return hexString.toString();
	}
}
