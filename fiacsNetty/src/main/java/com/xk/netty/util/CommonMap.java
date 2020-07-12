package com.xk.netty.util;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来存储请求信息  用于异步处理结果使用
 * @author zgx
 *
 */
public class CommonMap {/*
	
	public final static String PERSON = "person";
	
	public final static String CAR = "car";

	public final static String CASH_BOX = "cashBox";
	
	public final static String CASH_BOX_ALLOT = "cashBoxAllot";
	
	public final static String DOOR_PARAM = "doorParam";
	
	public final static String DOOR_TIME_ZONE = "doorTimeZone";
	
	public static ConcurrentHashMap<String, Integer> personMap = new ConcurrentHashMap<String, Integer>();
	
	public static ConcurrentHashMap<String, Integer> carMap = new ConcurrentHashMap<String, Integer>();
	
	public static ConcurrentHashMap<String, Integer> cashBoxMap = new ConcurrentHashMap<String, Integer>();
	
	public static ConcurrentHashMap<String, Integer> doorParamMap = new ConcurrentHashMap<String, Integer>();

	public static ConcurrentHashMap<String, Integer> doorTimeZoneMap = new ConcurrentHashMap<String, Integer>();
	
	public static ConcurrentHashMap<String, List<String>> cashBoxAllotMap = new ConcurrentHashMap<String, List<String>>();
	
	public static ConcurrentHashMap<String,String> seqNumMap = new ConcurrentHashMap<String, String>();
	
	public static int getDownId(String type,int num) {
		int seqNum = num-1;
		if(PERSON.equals(type)) {
			return personMap.get(seqNum);
		}else if(CAR.equals(type)) {
			return carMap.get(seqNum);
		}else if(CASH_BOX.equals(type)) {
			return cashBoxMap.get(seqNum);
		}else if(DOOR_PARAM.equals(type)) {
			return doorParamMap.get(seqNum);
		}else if(DOOR_TIME_ZONE.equals(type)) {
			return doorTimeZoneMap.get(seqNum);
		}
		return 0;
	}
	
	public static int getDownId(String type,String equipSn) {
		if(PERSON.equals(type)) {
			return personMap.get(equipSn);
		}else if(CAR.equals(type)) {
			return carMap.get(equipSn);
		}else if(CASH_BOX.equals(type)) {
			return cashBoxMap.get(equipSn);
		}else if(DOOR_PARAM.equals(type)) {
			return doorParamMap.get(equipSn);
		}else if(DOOR_TIME_ZONE.equals(type)) {
			return doorTimeZoneMap.get(equipSn);
		}
		return 0;
	}
	
	public static List<String> getDownIds(String type,int num){
		int seqNum = num-1;
		if(CASH_BOX_ALLOT.equals(type)) {
			return cashBoxAllotMap.get(seqNum);
		}
		return null;
	}
	
	public static List<String> getDownIds(String type,String equipSn){
		if(CASH_BOX_ALLOT.equals(type)) {
			return cashBoxAllotMap.get(equipSn);
		}
		return null;
	}
	
	public static void addDownId(String type,int seqNum,int downId,String equipSn) {
		//seqNum为请求时候值 应答需加1
		//int seqNum = num+1;
		seqNumMap.put(equipSn, type);
		if(PERSON.equals(type)) {
			personMap.put(seqNum, downId);
		}else if(CAR.equals(type)) {
			carMap.put(seqNum, downId);
		}else if(CASH_BOX.equals(type)) {
			 cashBoxMap.put(seqNum,downId);
		}else if(DOOR_PARAM.equals(type)) {
			doorParamMap.put(seqNum,downId);
		}else if(DOOR_TIME_ZONE.equals(type)) {
			doorTimeZoneMap.put(seqNum,downId);
		}
	}
	
	public static void addDownId(String type,int downId,String equipSn) {
		//seqNum为请求时候值 应答需加1
		seqNumMap.put(equipSn, type);
		if(PERSON.equals(type)) {
			personMap.put(equipSn, downId);
		}else if(CAR.equals(type)) {
			carMap.put(equipSn, downId);
		}else if(CASH_BOX.equals(type)) {
			 cashBoxMap.put(equipSn,downId);
		}else if(DOOR_PARAM.equals(type)) {
			doorParamMap.put(equipSn,downId);
		}else if(DOOR_TIME_ZONE.equals(type)) {
			doorTimeZoneMap.put(equipSn,downId);
		}
	}
	
	public static void addDownIds(String type,int seqNum,List<String> ids,String equipSn) {
		//int seqNum = num+1;
		seqNumMap.put(equipSn, type);
		if(CASH_BOX_ALLOT.equals(type)) {
			cashBoxAllotMap.put(seqNum, ids);
		}
	}
	
	public static void addDownIds(String type,List<String> ids,String equipSn) {
		seqNumMap.put(equipSn, type);
		if(CASH_BOX_ALLOT.equals(type)) {
			cashBoxAllotMap.put(equipSn, ids);
		}
	}
	
	public static void removeDownId(String type,int num,String equipSn) {
		int seqNum = num-1;
		seqNumMap.remove(equipSn);
		if("person".equals(type)) {
			personMap.remove(seqNum);
		}else if("car".equals(type)) {
			carMap.remove(seqNum);
		}else if(CASH_BOX.equals(type)) {
			 cashBoxMap.remove(seqNum);
		}else if(CASH_BOX_ALLOT.equals(type)) {
			cashBoxAllotMap.remove(seqNum);
		}else if(DOOR_PARAM.equals(type)) {
			doorParamMap.remove(seqNum);
		}else if(DOOR_TIME_ZONE.equals(type)) {
			doorTimeZoneMap.remove(seqNum);
		}
	}
	public static void removeDownId(String type,String equipSn) {
		seqNumMap.remove(equipSn);
		if("person".equals(type)) {
			personMap.remove(equipSn);
		}else if("car".equals(type)) {
			carMap.remove(equipSn);
		}else if(CASH_BOX.equals(type)) {
			 cashBoxMap.remove(equipSn);
		}else if(CASH_BOX_ALLOT.equals(type)) {
			cashBoxAllotMap.remove(equipSn);
		}else if(DOOR_PARAM.equals(type)) {
			doorParamMap.remove(equipSn);
		}else if(DOOR_TIME_ZONE.equals(type)) {
			doorTimeZoneMap.remove(equipSn);
		}
	}
*/}
