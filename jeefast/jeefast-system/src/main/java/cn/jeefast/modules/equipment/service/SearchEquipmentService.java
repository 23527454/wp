package cn.jeefast.modules.equipment.service;

import java.util.List;
import java.util.Map;

import cn.jeefast.modules.equipment.entity.Equipment;
import cn.jeefast.system.entity.SysUser;

public interface SearchEquipmentService{
	
	List<Equipment> searchList(Long userId,int type)  throws Exception;
	
	Map<String,Object> readInfo(Equipment equip,String readType) throws Exception;
	
	void settingInfo(Equipment equip,String settingType) throws Exception;
	
	void uploadRecord(Equipment equip) throws Exception;
	
	void uploadStatus(Equipment equip) throws Exception;
	
	void saveDataBase(List<Equipment> equipList,SysUser user,Long deptId) throws Exception;
	
	void operateEquip(Equipment equip,String operateType) throws Exception;
	
	void updatePassWord(Equipment equip) throws Exception;
	
}
