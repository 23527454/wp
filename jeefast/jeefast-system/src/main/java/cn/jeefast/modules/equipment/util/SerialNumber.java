package cn.jeefast.modules.equipment.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SerialNumber {

	// 静态变量存储最大值
	private static final AtomicInteger atomicNum = new AtomicInteger(1);

	public static String getNewAutoNum() {
		int newNum = atomicNum.incrementAndGet();
		// 数字长度为5位，长度不够数字前面补0
		String newStrNum = String.format("%010d", binaryToDecimal(newNum));
		return newStrNum;
	}
	
	/**
	 * 整数转换为二进制形式数
	 * @param n
	 * @return
	 */
	public static int binaryToDecimal(int n) {
		String str = "";
		while (n != 0) {
			str = n % 2 + str;
			n = n / 2;
		}
		return Integer.valueOf(str);
	}
	
}
