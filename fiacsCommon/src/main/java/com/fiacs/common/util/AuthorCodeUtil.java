package com.fiacs.common.util;

import java.io.UnsupportedEncodingException;

public class AuthorCodeUtil {
	
	/**
	 * 
	 * @param authcode
	 * @param equipSn 设备sn
	 * @param seqNum 包序号
	 * @return
	 */
	public static boolean checkBySn(String authcode, String equipSn, int seqNum) {
		try {
			String desStr = DesUtil.encrypt(equipSn, Constants.COMMUNICATION_KEY_FACTOR);
			String desStr2 = DesUtil.encrypt(SeqnumUtil.getSegnumF(seqNum),desStr);
			String paramCode = SeqnumUtil.getXor(desStr2, Constants.XOR_CODE);
			//return true;
			return authcode.equalsIgnoreCase(paramCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param authcode
	 * @param equipSn 设备sn
	 * @param seqNum 包序号
	 * @return
	 */
	public static boolean checkBySnOfSearch(String authcode, String equipSn, int seqNum) {
		try {
			String desStr = DesUtil.encrypt(equipSn, Constants.SEARCH_KEY_FACTOR);
			String desStr2 = DesUtil.encrypt(SeqnumUtil.getSegnumF(seqNum),desStr);
			String paramCode = SeqnumUtil.getXor(desStr2, Constants.XOR_CODE);
			return authcode.equalsIgnoreCase(paramCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param authcode
	 * @param equipSn 设备sn
	 * @param seqNum 包序号
	 * @return
	 */
	public static boolean checkByOldKey(String authcode, String equipSn, int seqNum) {
		try {
			String desStr = DesUtil.encrypt(equipSn, Constants.COMMUNICATION_KEY_FACTOR_OLD);
			String desStr2 = DesUtil.encrypt(SeqnumUtil.getSegnumF(seqNum),desStr);
			String paramCode = SeqnumUtil.getXor(desStr2, Constants.XOR_CODE);
			return authcode.equalsIgnoreCase(paramCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 生成新的authcode
	 * @param equipSn
	 * @param seqNum
	 * @return
	 */
	public static String generateCode(String equipSn,int seqNum,String factKey) {
		try {
		String desStr = DesUtil.encrypt(equipSn, factKey);
		String desStr2 = DesUtil.encrypt(SeqnumUtil.getSegnumF(seqNum),desStr);
		return SeqnumUtil.getXor(desStr2, Constants.XOR_CODE).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws Exception {
		//System.out.println(ByteUtil.bytesToInt(ByteUtil.hexStringToBytes("33277C6B")));
		//System.out.println(generateCode("1001200102100001", 11540, "AE0827C89B1010E6"));
	}
}
