package com.xk.netty.util;

import java.util.concurrent.ConcurrentHashMap;

public class Snippet {

public final static String PERSON = "person";
	
	public final static String CAR = "car";

	public final static String CASH_BOX = "cashBox";
	
	public final static String CASH_BOX_ALLOT = "cashBoxAllot";
	
	public final static String DOOR_PARAM = "doorParam";
	
	public final static String DOOR_TIME_ZONE = "doorTimeZone";
	
	public final static String TASK_CLASS = "taskClass";

	public final static String INFORMATION_RELEASE = "informationRelease";
	
	public static ConcurrentHashMap<String, Object> downIdMap = new ConcurrentHashMap<String, Object>();
	
	public static ConcurrentHashMap<String,String> seqNumMap = new ConcurrentHashMap<String, String>();
	
	public static ConcurrentHashMap<String,String> bigDataMap = new ConcurrentHashMap<String,String>();
	
	public static Object getDownId(String equipSn) {
		return downIdMap.get(equipSn);
	}
	
	public static void addDownId(String type,Object downId,String equipSn) {
		seqNumMap.put(equipSn, type);
		downIdMap.put(equipSn, downId);
	}
	
	
	public static void removeDownId(String equipSn) {
		seqNumMap.remove(equipSn);
		downIdMap.remove(equipSn);
	}
	
	public static void addBigData(String equipSn,String bigData) {
	/*	if(bigDataMap.get(equipSn)==null) {
			bigDataMap.put(equipSn, bigData);
		}else {*/
			String hasData = bigDataMap.get(equipSn);
			bigDataMap.put(equipSn, hasData+bigData);
	}
	
}

