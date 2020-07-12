package com.xk.netty.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.pointers.Pointer;

import SuperDog.util.SuperDogTools;

@Component
public class SuperDogUtil {

	private static Logger log = LoggerFactory.getLogger(SuperDogUtil.class);
	//是否禁用加密狗  true 是    false 不是
	public final static boolean disabled = true;
	
	/**
	 * 检查加密狗
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean checkDog() throws Exception {
		
		if (!SuperDogTools.ZxywSuperDog_CheckDog()) {
			StringBuilder sb = new StringBuilder();
			sb.append("\r\n======================================================================\r\n");
			sb.append("\r\n 未检测到加密狗！");
			sb.append("\r\n======================================================================\r\n");
			log.error(sb.toString());
			return false;
		}
		return true;
	}

	/**
	 * 读取服务器参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean readServerAttribute() throws Exception {
		if (!SuperDogTools.ZxywSuperDog_ReadServerAttribute()) {
			StringBuilder sb = new StringBuilder();
			sb.append("\r\n======================================================================\r\n");
			sb.append("\r\n 未检测到服务器属性！");
			sb.append("\r\n======================================================================\r\n");
			log.error(sb.toString());
			return false;
		}
		return true;
	}

	/**
	 * 检验是否过了有效期
	 * 
	 * @return
	 */
	public static boolean checkDogSqDate() throws Exception{

		if (SuperDogTools.ZxywSuperDog_CheckDogSqDate()) {
			StringBuilder sb = new StringBuilder();
			sb.append("\r\n======================================================================\r\n");
			sb.append("\r\n 已过有效期！");
			sb.append("\r\n======================================================================\r\n");
			log.error(sb.toString());
			return false;
		}
		return true;
	}

	/**
	 * 获取允许最大连接数
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static int readSqData() throws Exception {
		byte[] data = SuperDogTools.ZxywSuperDog_ReadSqData();
		int total = data[7] * 256 * 256 * 256 + data[7] * 256 * 256
				+ data[9] * 256 +data[10] ;
		return total;
	}
	
	
	/**
	 * 获取加密狗秘钥
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static String readSystemCode() throws Exception {
		byte[] data = SuperDogTools.ZxywSuperDog_ReadSystemCode();
		return ByteUtil.bytesToHexString(data).toUpperCase();
	}
	
	
}
