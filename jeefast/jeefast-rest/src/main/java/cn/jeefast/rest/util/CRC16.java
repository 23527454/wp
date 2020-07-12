package cn.jeefast.rest.util;

public class CRC16 {
	public static int crc16_ccitt_xmodem(byte[] bytes) {
	return crc16_ccitt_xmodem(bytes,0,bytes.length);
	}
	 
	/**
	* CRC16-XMODEM算法（四字节）
	* @param bytes
	* @param offset
	* @param count
	* @return
	*/
	public static int crc16_ccitt_xmodem(byte[] bytes,int offset,int count) {
	int crc = 0x0000; // initial value
	int polynomial = 0x1021; // poly value
	for (int index = offset; index < count; index++) {
	byte b = bytes[index];
	for (int i = 0; i < 8; i++) {
	boolean bit = ((b >> (7 - i) & 1) == 1);
	boolean c15 = ((crc >> 15 & 1) == 1);
	crc <<= 1;
	if (c15 ^ bit)
	crc ^= polynomial;
	}
	}
	crc &= 0xffff;
	return crc;
	}}
