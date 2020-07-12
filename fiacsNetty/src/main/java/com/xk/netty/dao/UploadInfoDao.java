package com.xk.netty.dao;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface UploadInfoDao extends BaseMapper<Object>{
	//===============上传照片======================
	int uploadAlarmEventImag(Map<String,Object> map);
	
	int uploadAccessEventImag(Map<String,Object> map);
	
	int uploadAccessAlarmEventImag(Map<String,Object> map);
	//===============查询设备=====================
	Map<String,Object> queryEquipBySn(String equipSn);
	
	int updateEquipStatusBySn(Map<String,Object> map);
	/**
	 * 新增交接事件
	 * @param map
	 * @return
	 */
	int insertConnectEvent(Map<String,String> map);
	
	/**
	 * 存抓拍照片记录
	 */
	int insertConnectEventPhoto(Map<String,String> map);
	
	/**
	 * 专员信息记录存储
	 */
	int insertConnectWorPerson(Map<String,String> map);
	
	/**
	 * 判断此交接事件是否存在
	 * @param map
	 * @return
	 */
	int countConnectEvent(Map<String,String> map);
	
	//====================门禁出入事件=====================
	int insertAccessEvent(Map<String,Object> map);
	
	int insertAccessEventDetail(Map<String,Object> map);
	
	//================门禁报警事件========================
	int insertAccessAlarmEvent(Map<String,Object> map);
	
	int insertAccessAlarmEventDetail(Map<String,Object> map);
	//===============异常报警事件======================
	int countAlarmEvent(Map<String,String> map);
	
	int insertAlarmEvent(Map<String,String> map);
	
	int insertAlarmEventDetail(Map<String,String> map);
	
	int insertAlarmEventOfCashBox(Map<String,String> map);
	
	//查询是否使用加密狗秘钥
	int queryIsAllowSuperDogCode();
}
