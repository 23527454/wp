package com.thinkgem.jeesite.modules.sys.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.utils.ByteUtils;

import SuperDog.util.SuperDogTools;
@Component
public class SuperDogUtil {
	
	private static Logger log = LoggerFactory.getLogger(SuperDogUtil.class);
	/**
	 * 是否禁止加密狗
	 */
	private final static boolean disabled = true;
	
	public static boolean ReadSqData() {
		if(disabled){
			System.err.println("测试代码， 屏蔽了加密狗");
			return true;
		}
		
		try {
			byte[] data = SuperDogTools.ZxywSuperDog_ReadSqData();
			byte[] bytArray = new byte[2];
			bytArray[0] = data[0];
			bytArray[1] = data[1];
			String Message = ByteUtils.byteToString(bytArray);
			int function = (Integer.parseInt(Message, 16)) & 0x4000;
			if (function == 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("\r\n======================================================================\r\n");
				sb.append("\r\n 未获授权！");
				sb.append("\r\n======================================================================\r\n");
				log.error(sb.toString());
				return false;
			}
		} catch (Exception ie) {
			StringBuilder sb = new StringBuilder();
			sb.append("\r\n======================================================================\r\n");
			sb.append("\r\n 未检测到加密狗 ！");
			sb.append("\r\n======================================================================\r\n");
			log.error(sb.toString());
			ie.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean CheckDogSqDate() {
		if(disabled){
			System.err.println("测试代码， 屏蔽了加密狗");
			return true;
		}
		try {
			boolean result = SuperDogTools.ZxywSuperDog_CheckDogSqDate();
			if (result) {
				StringBuilder sb = new StringBuilder();
				sb.append("\r\n======================================================================\r\n");
				sb.append("\r\n 授权已过期！");
				sb.append("\r\n======================================================================\r\n");
				log.error(sb.toString());
				return false;
			}
		} catch (Exception ie) {
			StringBuilder sb = new StringBuilder();
			sb.append("\r\n======================================================================\r\n");
			sb.append("\r\n 未检测到加密狗 ！");
			sb.append("\r\n======================================================================\r\n");
			log.error(sb.toString());
			ie.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 登录成功获取加密狗数据进行缓存
	 */
	public static void cacheConfineNumber() {
		if(disabled){
			System.err.println("测试代码， 屏蔽了加密狗");
			UserUtils.putCache(UserUtils.CACHE_FUNCTION_USER, 200);
			UserUtils.putCache(UserUtils.CACHE_FUNCTION_EQU, 2000);
			UserUtils.putCache(UserUtils.CACHE_FUNCTION_STAFF, 2000);
			return;
		}
		
		try {
			byte[] data = SuperDogTools.ZxywSuperDog_ReadSqData();
			// 用户数
			byte[] bytArrayUser = new byte[4];
			bytArrayUser[0] = data[15];
			bytArrayUser[1] = data[16];
			bytArrayUser[2] = data[17];
			bytArrayUser[3] = data[18];
			String MessageUser = ByteUtils.byteToString(bytArrayUser);
			int functionUser = (Integer.parseInt(MessageUser, 16));

			// 设备数
			byte[] bytArrayEqu = new byte[4];
			bytArrayEqu[0] = data[7];
			bytArrayEqu[1] = data[8];
			bytArrayEqu[2] = data[9];
			bytArrayEqu[3] = data[10];
			String MessageEqu = ByteUtils.byteToString(bytArrayEqu);
			int functionEqu = (Integer.parseInt(MessageEqu, 16));

			// 人员数
			byte[] bytArrayStaff = new byte[4];
			bytArrayStaff[0] = data[11];
			bytArrayStaff[1] = data[12];
			bytArrayStaff[2] = data[13];
			bytArrayStaff[3] = data[14];
			String MessageStaff = ByteUtils.byteToString(bytArrayStaff);
			int functionStaff = (Integer.parseInt(MessageStaff, 16));
			UserUtils.putCache(UserUtils.CACHE_FUNCTION_USER, functionUser);
			UserUtils.putCache(UserUtils.CACHE_FUNCTION_EQU, functionEqu);
			UserUtils.putCache(UserUtils.CACHE_FUNCTION_STAFF, functionStaff);
//			Object s = UserUtils.getCache(UserUtils.CACHE_FUNCTION_USER);
			log.error("functionUser:"+functionUser);
			log.error("functionEqu:"+functionEqu);
			log.error("functionStaff:"+functionStaff);
		} catch (Exception ie) {
			StringBuilder sb = new StringBuilder();
			sb.append("\r\n======================================================================\r\n");
			sb.append("\r\n 未检测到加密狗 ！");
			sb.append("\r\n======================================================================\r\n");
			log.error(sb.toString());
			ie.printStackTrace();
		}
	}
}	
