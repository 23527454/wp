package cn.jeefast.modules.fiacs.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.jeefast.modules.fiacs.entity.EquipEntity;
import cn.jeefast.modules.fiacs.entity.Staff;


public interface EquipEntityDao extends BaseMapper<EquipEntity>{
	
	public EquipEntity queryByCondition(EquipEntity entity);
	
	public List<EquipEntity> queryAllByDeptId(String deptId);
	
	public int queryByCenterIP(EquipEntity entity);
	
	List<Staff> selectStaffList(String officeId);
	
	void inserDownLoadPerson(Map<String,Object> map);
	
	int countDownLoadPerson(Map<String,Object> map);
	
	List<String> findByIdAndParent(Map<String,String> map);
	
	String findParentOfficeId(String officeId);
	
	List<String> selectCarList(String officeId);
	
	int countDownLoadCar(Map<String,Object> map);
	
	void insertDownLoadCar(Map<String,Object> map);
	
	String selectAreaIdByOfficeId(String officeId);
	
	List<String> selectMoneyBoxList(Map<String,String> map);
	
	List<Map<String,Object>> selectEquipmentList(Map<String,String> map);
	
	int countDownLoadMoneyBox(Map<String,String> map);
	
	void insertDownLoadMoneyBox(Map<String,String> map);
	
	List<EquipEntity> queryAllNotConnect();

	String selectConfig(String id);
}
