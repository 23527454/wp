package com.xk.netty.service;

import java.util.Map;

import com.xk.netty.entity.PersonEntity;

public interface PersonService {
	
	PersonEntity queryOne(String equipId);
	
	int updateSynStatus(int downLoadId); 
	
	PersonEntity queryPersonPhoto(String staffId,String imageType,String orderBy);
	
	PersonEntity queryFingerMode(String staffId);
	
	int uploadSuperGoEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 上传专员事件
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadCommissionerEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 上传维保员事件
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadSafeGuardEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 大数据信息-交接事件押解员抓拍信息上传
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadSuperGoEventImag(Map<String,Object> map,String equipId);
	
	/**
	 * 大数据信息-上传专员指纹模板
	 * @param map
	 * @param equipId
	 * @return
	 */
	int updateFingerModeImag(Map<String,Object> map,String equipId);
	
	/**
	 * 大数据信息-上传维保员抓拍照片
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadSafeGuardEventImag(Map<String,Object> map,String equipId);
}
