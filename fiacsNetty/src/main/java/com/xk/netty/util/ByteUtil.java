package com.xk.netty.util;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.util.GenericSignature.ArrayTypeSignature;

public class ByteUtil {
	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();
	}

	public static char[] getChars(byte[] bytes) {
		char[] res = new char[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			res[i] = (char) bytes[i];
		}
		return res;
	}

	public final static byte[] getBytes(short s, boolean asc) {
		byte[] buf = new byte[2];
		if (asc)
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x00ff);
				s >>= 8;
			}
		else
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x00ff);
				s >>= 8;
			}
		return buf;
	}

	public final static byte[] getBytes(int s, boolean asc) {
		byte[] buf = new byte[4];
		if (asc)
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x000000ff);
				s >>= 8;
			}
		else
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x000000ff);
				s >>= 8;
			}
		return buf;
	}

	public final static byte[] getBytes(long s, boolean asc) {
		byte[] buf = new byte[8];
		if (asc)
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x00000000000000ff);
				s >>= 8;
			}
		else
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x00000000000000ff);
				s >>= 8;
			}
		return buf;
	}

	public final static byte intToByte(int x) {
		return (byte) x;
	}

	public final static short getShort(byte[] buf, boolean asc) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 2) {
			throw new IllegalArgumentException("byte array size > 2 !");
		}
		short r = 0;
		if (asc)
			for (int i = buf.length - 1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x00ff);
			}
		else
			for (int i = 0; i < buf.length; i++) {
				r <<= 8;
				r |= (buf[i] & 0x00ff);
			}
		return r;
	}

	public final static int getInt(byte[] buf, boolean asc) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 4) {
			throw new IllegalArgumentException("byte array size > 4 !");
		}
		int r = 0;
		if (asc)
			for (int i = buf.length - 1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x000000ff);
			}
		else
			for (int i = 0; i < buf.length; i++) {
				r <<= 8;
				r |= (buf[i] & 0x000000ff);
			}
		return r;
	}

	public final static long getLong(byte[] buf, boolean asc) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 8) {
			throw new IllegalArgumentException("byte array size > 8 !");
		}
		long r = 0;
		if (asc)
			for (int i = buf.length - 1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x00000000000000ff);
			}
		else
			for (int i = 0; i < buf.length; i++) {
				r <<= 8;
				r |= (buf[i] & 0x00000000000000ff);
			}
		return r;
	}

	/**
	 * 
	 * @Title. byteArrayToString @Description. byte数组转为String
	 * 
	 * @param bytes
	 * @return String @exception.
	 */
	public final static String byteArrayToString(byte[] bytes) {
		StringBuffer str = new StringBuffer();
		for (byte byt : bytes) {
			str.append(String.format("%8s", Integer.toBinaryString(byt & 0xFF)).replace(' ', '0'));
		}
		return str.toString();
	}

	/**
	 * 
	 * @Title. shortToBytes @Description. short 转byte
	 * 
	 * @param rec
	 * @return byte[] @exception.
	 */
	public final static byte[] shortToBytes(short rec) {
		byte[] res = new byte[2];
		res[0] = (byte) ((rec >> 8) & 255);
		res[1] = (byte) (rec & 255);
		return res;
	}

	/**
	 * 字符串格式为00000010101010.. 转为bit 再转为byte
	 */
	public final static byte[] strToBytes(String str) {
		byte[] res = {};
		if (null == str) {
			return res;
		}
		int length = str.length();
		if (length % 8 != 0) {
			return res;
		}

		int size = length / 8;
		for (int i = 1; i < size + 1; i++) {
			res = ArrayUtils.add(res, BitToByte(str.substring(i * 8 - 8, i * 8)));
		}
		return res;
	}

	/**
	 * Bit转Byte
	 */
	public static byte BitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit处理
			if (byteStr.charAt(0) == '0') {// 正数
				re = Integer.parseInt(byteStr, 2);
			} else {// 负数
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit处理
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}
	

	/**
	 * byte 转成 bit 字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToBit(byte b) {
		StringBuffer sb = new StringBuffer();
		sb.append((byte) ((b >> 7) & 0x1)).append(((b >> 6) & 0x1)).append(((b >> 5) & 0x1)).append(((b >> 4) & 0x1))
				.append(((b >> 3) & 0x1)).append(((b >> 2) & 0x1)).append(((b >> 1) & 0x1)).append(((b >> 0) & 0x1));
		return sb.reverse().toString();
	}

	public final static short bytesToShort(byte[] bytes) {
		short res = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			// res |= (0xff << (i- bytes.length + 1) * 8) & (bytes[i] << (i- bytes.length +
			// 1) * 8);
			res |= (0xff << (bytes.length - i - 1) * 8) & (bytes[i] << (bytes.length - i - 1) * 8);
		}
		return res;
	}

	public final static int bytesToInt(byte[] bytes) {
		int res = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			// res |= (0xff << (i- bytes.length + 1) * 8) & (bytes[i] << (i- bytes.length +
			// 1) * 8);
			res |= (0xff << (bytes.length - i - 1) * 8) & (bytes[i] << (bytes.length - i - 1) * 8);
		}
		return res;
	}

	/**
	 * 4字节转ip字符串
	 * 
	 * @param addr
	 * @return
	 */
	public static String binaryArray2Ipv4Address(byte[] addr) {
		String ip = "";
		for (int i = 0; i < addr.length; i++) {
			ip += (addr[i] & 0xFF) + ".";
		}
		return ip.substring(0, ip.length() - 1);
	}

	/**
	 * ip转4字节
	 * 
	 * @param ipAdd
	 * @return
	 */
	public static byte[] ipv4Address2BinaryArray(String ipAdd) {
		byte[] binIP = new byte[4];
		String[] strs = ipAdd.split("\\.");
		for (int i = 0; i < strs.length; i++) {
			binIP[i] = (byte) Integer.parseInt(strs[i]);
		}
		return binIP;
	}

	/**
	 * mac转字节数组
	 * 
	 * @param mac
	 * @return
	 */
	public byte[] getMacBytes(String mac) {
		byte[] macBytes = new byte[6];
		String[] strArr = mac.split(":");

		for (int i = 0; i < strArr.length; i++) {
			int value = Integer.parseInt(strArr[i], 16);
			macBytes[i] = (byte) value;
		}
		return macBytes;
	}
	
	/**
	 * bytes转MAC字符串
	 * @param macBytes
	 * @return
	 */
	public static String bytesToMac(byte[] macBytes) {
		 String value = "";
		  for(int i = 0;i < macBytes.length; i++){
		   String sTemp = Integer.toHexString(0xFF &  macBytes[i]);
		   value = value+sTemp+":";
		  }
		    
		  value = value.substring(0,value.lastIndexOf(":"));
		  return value;
	} 
	
	
	 public static String bytesToHexString(byte[] src){  
	      StringBuilder stringBuilder = new StringBuilder("");  
	      if (src == null || src.length <= 0) {  
	          return null;  
	      }  
	     for (int i = 0; i < src.length; i++) {  
        int v = src[i] & 0xFF;  
	         String hv = Integer.toHexString(v);  
	         if (hv.length() < 2) {  
	             stringBuilder.append(0);  
	         }  
	         stringBuilder.append(hv);  
	     }  
	     return stringBuilder.toString();  
	 }  
	 
	 /**
	  * mac字节数组 转 设备sn码
	  * @param data
	  * @return
	  */
	 public static String bytesToSn(byte[] data) {
		 String pre = bytesToHexString(Arrays.copyOfRange(data, 1, 3));
		 String end = addZeroForNum(String.valueOf(bytesToInt(Arrays.copyOfRange(data, 3, 6))),8);
		 return pre+end;
	 }
	 
	 /**
	  * sn码转字节数组
	  * @param sn
	  * @return
	  */
	 public static byte[] SnToBytes(String sn) {
		 byte[] result= {0x12};
		 result = ArrayUtils.addAll(result, ByteUtil.hexStringToBytes(sn.substring(0, 4)));
		 result = ArrayUtils.addAll(result, ByteUtil.intToBytes(Integer.parseInt(sn.substring(4)),3));
		 return result;
	 }
	 
	 public static byte[] intToBytes(int value, int len) {
	        byte[] b = new byte[len];
	        for (int i = 0; i < len; i++) {
	            b[len - i - 1] = (byte)((value >> 8 * i) & 0xff);
	        }
	        return b;

	 }
	 /**
	  * 字节数组转序列号
	  * @param data
	  * @return
	  */
	 public static String bytesToReq(byte[] data) {
		 String pre = bytesToHexString(Arrays.copyOfRange(data, 0, 3));
		 String end = bytesToSn(Arrays.copyOfRange(data, 3, 9));
		 return "20"+pre+end;
	 }
	 
	 public static byte[] reqToBytes(String req) {
		 byte[] data = {};
		 data = ArrayUtils.addAll(data, ByteUtil.hexStringToBytes(req.substring(2, 8)));
		 data = ArrayUtils.addAll(data, SnToBytes(req.substring(8)));
		 return data;
	 }
	 
	 private static String addZeroForNum(String str, int strLength) {
		 int strLen = str.length();
		 StringBuffer sb = null;
		 while (strLen < strLength) {
		 sb = new StringBuffer();
		 sb.append("0").append(str);// 左补0
		 // sb.append(str).append("0");//右补0
		 str = sb.toString();
		 strLen = str.length();
		 }
		 return str;
	}
	 
	 public static byte[] hexStringToBytes(String hexString) {  
		    if (hexString == null || hexString.equals("")) {  
		        return null;  
		    }  
		    hexString = hexString.toUpperCase();  
		    int length = hexString.length() / 2;  
		    char[] hexChars = hexString.toCharArray();  
		    byte[] d = new byte[length];  
		    for (int i = 0; i < length; i++) {  
		        int pos = i * 2;  
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		    }  
		    return d;  
		}  
	 
	 private static byte charToByte(char c) {  
		    return (byte) "0123456789ABCDEF".indexOf(c);  
		}  
	 
	 /**
	  * 八字节数组转时间格式字符串
	  * @param data
	  * @return
	  */
	 public static String bytesToTime(byte[] data) {
		 String year = bytesToHexString(Arrays.copyOfRange(data, 0, 2));
		 String month = bytesToHexString(Arrays.copyOfRange(data, 2, 3));
		 String day = bytesToHexString(Arrays.copyOfRange(data, 3, 4));
		 String week = bytesToHexString(Arrays.copyOfRange(data, 4, 5));
		 String hours = bytesToHexString(Arrays.copyOfRange(data, 5, 6));
		 String minutes = bytesToHexString(Arrays.copyOfRange(data, 6, 7));
		 String second = bytesToHexString(Arrays.copyOfRange(data, 7, 8));
		 StringBuffer sb = new StringBuffer();
		 sb.append(year).append("-").append(month).append("-").append(day);
		 sb.append(" ").append(hours).append(":").append(minutes).append(":").append(second);
		 return sb.toString();
	 }

	 public static void main(String[] args) throws UnsupportedEncodingException {
		 System.out.println(new String(new byte[]{0x00,0x30,0x30,0x30,0x30,0x30,0x30,0x30,0x30,0x30,0x30,0x30},"ascii"));
		System.out.println(bytesToInt(new byte[]{0x33}));
		System.out.println(bytesToHexString(new String("S").getBytes("ascii")));
	 }
}
