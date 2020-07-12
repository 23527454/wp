package cn.jeefast.rest.service;

import java.util.Map;

import cn.jeefast.rest.entity.Equipment;

public interface SearchEquipmentService{
	
	//Map<String,Object> searchList(String ip,int port)  throws Exception;
	
	Map<String,Object> readInfo(String ip,Integer port,String readType) throws Exception;
	
	/*
	
	void uploadRecord(Equipment equip) throws Exception;
	
	void uploadStatus(Equipment equip) throws Exception;
	
	//void saveDataBase(String[] snList,SysUser user) throws Exception;
	
	void updatePassWord(Equipment equip) throws Exception;*/
	
	void operateEquip(String ip,Integer port,String operateType) throws Exception;
	/**
	 * 更新面板基本信息
	 * @param ip
	 * @param port
	 * @param equip
	 * @param settingType
	 * @throws Exception
	 */
	void settingBaseInfo(String ip,Integer port,Equipment equip,String settingType) throws Exception;
	
	/**
	 * 更新设备其他参数
	 * @param ip
	 * @param port
	 * @param masterValue
	 * @param slaveValue
	 * @param settingType
	 * @throws Exception
	 */
	void settingInfo(String ip,Integer port,String masterValue,String slaveValue,String settingType) throws Exception;
}
