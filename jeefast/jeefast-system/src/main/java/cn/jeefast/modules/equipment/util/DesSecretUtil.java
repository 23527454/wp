package cn.jeefast.modules.equipment.util;

import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class DesSecretUtil {

	public static byte[] ecode(String msg) {
		// KeyGenerator提供对称密钥生成器的功能，支持各种算法

		KeyGenerator keygen;

		// SecretKey负责保存对称密钥

		SecretKey deskey;

		// Cipher负责完成加密或解密工作

		Cipher c;

		Security.addProvider(new com.sun.crypto.provider.SunJCE());

		// 实例化支持3DES算法的密钥生成器，算法名称用DESede

		try {
			keygen = KeyGenerator.getInstance("DESede");
			// 生成密钥

			deskey = keygen.generateKey();

			// 生成Cipher对象，指定其支持3DES算法

			c = Cipher.getInstance("DESede");

			c.init(Cipher.ENCRYPT_MODE, deskey);

			byte[] src = msg.getBytes();

			// 加密，结果保存进enc

			//return c.	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(new String(ecode("11111111111111111111111")));
	}
}
