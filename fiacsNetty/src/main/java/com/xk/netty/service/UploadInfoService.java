package com.xk.netty.service;

import java.util.Map;

public interface UploadInfoService {
	Map<String,Object> queryEquipBySn(String equipSn);
	
	/**
	 * 更新设备在线状态
	 * @param equipSn
	 * @param status 1 在线  0不在线
	 * @return
	 */
	int updateEquipStatusBySn(String equipSn,int status);
	/**
	 * 上传交接事件
	 * @param map
	 * @return
	 */
	int uploadConnectEvent(Map<String,Object> map,String equipId);
	
	
	/**
	 * 上传异常警报事件
	 * @param map
	 * @return
	 */
	int uploadAlarmEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 上传门禁出入事件
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadAccessEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 上传门禁警报事件
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadAccessAlarmEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 上传异常警报 抓拍照片
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadAlarmEventImag(Map<String,Object> map,String equipId);
	
	/**
	 * 大数据信息--上传专员门禁出入照片
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadAccessEventImag(Map<String,Object> map,String equipId);
	
	/**
	 * 大数据信息--上传门禁报警抓拍照片
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadAccessAlarmEventImag(Map<String,Object> map,String equipId);
	
	/**
	 * 查询是否使用加密狗秘钥
	 * @return
	 */
	int queryIsAllowSuperDogCode();
}
