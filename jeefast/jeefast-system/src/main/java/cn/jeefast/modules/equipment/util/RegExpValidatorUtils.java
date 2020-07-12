package cn.jeefast.modules.equipment.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpValidatorUtils {

	/**
	 * 验证IP地址
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isIP(String str) {
		String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
		String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
		return match(regex, str);
	}
	
	public static boolean isPort(int port) {
		if(port>=0&&port<65536) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
