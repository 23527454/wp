package com.fiacs.common.util;


import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.ArrayUtils;

public class SeqnumUtil {
	
	private volatile static AtomicLong autoLong = new AtomicLong(0);
	
	/**
	 * 得到新的包序号
	 * @return
	 */
	public static  int getNextSeqNum() {
		if(autoLong.get()>=(0x7FFFFFFF-10000)) {
			autoLong.set(0);
		}
		long seqNum = autoLong.getAndIncrement();
		return Long.valueOf(seqNum).intValue();
	}
	
	/**
	 * 跟句包序号得到包因子
	 * @param seqNum 16进制字符串
	 * @return
	 */
	public static String getSegnumF(String seqNum) {
		byte[] seqBytes = ByteUtil.intToBytes(Integer.valueOf(seqNum), 4);
		byte[] result = new byte[4];
		for(int i=0;i<seqBytes.length;i++) {
			result[i] = ByteUtil.intToByte(~seqBytes[i]);
		}
		return ByteUtil.bytesToHexString(ArrayUtils.addAll(seqBytes, result));
	}
	
	/**
	 * 跟句包序号得到包因子
	 * @param seqNum 16进制字符串
	 * @return
	 */
	public static String getSegnumF(int seqNum) {
		byte[] seqBytes = ByteUtil.intToBytes(seqNum,4);
		byte[] result = new byte[4];
		for(int i=0;i<seqBytes.length;i++) {
			result[i] = ByteUtil.intToByte(~seqBytes[i]);
		}
		return ByteUtil.bytesToHexString(ArrayUtils.addAll(seqBytes, result));
	}
	
	/**
	 * 异或
	 * @param segNum 16进制字符串
	 * @param xorNum 16进制字符串
	 * @return
	 */
	public static String getXor(String segNum,String xorNum) {
		byte[] segBytes = ByteUtil.hexStringToBytes(segNum);
		byte[] xorBytes = ByteUtil.hexStringToBytes(xorNum);
		byte[] resultBytes = new byte[8];
		for(int i=0;i<8;i++) {
			String segbit = ByteUtil.byteToBit(segBytes[i]);
			String xorbit = ByteUtil.byteToBit(xorBytes[i]);
			StringBuffer sb = new StringBuffer();
			for(int j=0;j<8;j++) {
				if(segbit.charAt(j)==xorbit.charAt(j)) {
					sb.append("0");
				}else {
					sb.append("1");
				}
			}
			resultBytes[i] = ByteUtil.BitToByte(sb.toString());
		}
	/*	byte[] resultBytes1 = new byte[8];
		for(int i=0;i<8;i++) {
			resultBytes1[i]=ByteUtil.intToByte(segBytes[i]^xorBytes[i]);
		}
		System.out.println(ByteUtil.bytesToHexString(resultBytes1));*/
		return ByteUtil.bytesToHexString(resultBytes);
	}
	
	
	public static void main(String[] args) {
		System.out.println(getXor("3E5AEFC0819CBDA9","AE0827C89B1010E6"));
	}
}
