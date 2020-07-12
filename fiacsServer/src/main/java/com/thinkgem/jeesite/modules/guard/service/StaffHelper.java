package com.thinkgem.jeesite.modules.guard.service;

import com.thinkgem.jeesite.common.utils.StringUtils;

public class StaffHelper {

	/**
	 * build 指纹号
	 * @param areaId
	 * @param fingerNum
	 * @return
	 */
	public static String buildFingerNum(String areaId, Integer fingerNum){
		if(areaId ==null || fingerNum == null ){
			return null;
		}
		return StringUtils.autoGenericCode(areaId, 3)+""+StringUtils.autoGenericCode(String.valueOf(fingerNum), 4);
	}
	
	

	/**
	 * build 指纹号
	 * @param areaId
	 * @param fingerNum
	 * @return
	 */
	public static String buildFingerNum(String areaId, String fingerNum){
		if(areaId ==null || fingerNum == null ){
			return null;
		}
		return StringUtils.autoGenericCode(areaId, 3)+""+StringUtils.autoGenericCode(fingerNum, 4);
	}
}
