package cn.jeefast.modules.fiacs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.lang.time.DateFormatUtils;

import com.fiacs.common.util.DateUtil;

import cn.jeefast.common.utils.DateUtils;


public class EquipStartUtil {
	
	
	public static ConcurrentHashMap<String, Date> equipStartMap = new ConcurrentHashMap<String, Date>();
	
	public static void add(String equipSn,int resetType) {
		equipStartMap.put(equipSn+"_"+(resetType==2?0:resetType), new Date());
	}
	
	public static int validDate(String equipSn,int resetType) {
		if(equipStartMap.containsKey(equipSn+"_1")) {
			Date now = new Date();
			Date old = equipStartMap.get(equipSn+"_1");
			long diff = now.getTime()-old.getTime();
			long min = diff  / (1000 * 60);
			if(min<5) {
				return 5;
			}
		}else {
			if(equipStartMap.containsKey(equipSn+"_0")) {
				Date now = new Date();
				Date old = equipStartMap.get(equipSn+"_0");
				long diff = now.getTime()-old.getTime();
				long min = diff / 1000;
				if(min<15) {
					return 15;
				}
			}
		}
		return 0;
	}
	
	
	public static  long getSecond(String equipSn,int resetType) {
		Date now = new Date();
		Date old;
		if(resetType==1) {
			 old = equipStartMap.get(equipSn+"_1");
		}else {
			 old = equipStartMap.get(equipSn+"_0");
		}
		long diff = now.getTime()-old.getTime();
		return diff / 1000;
	}
}
