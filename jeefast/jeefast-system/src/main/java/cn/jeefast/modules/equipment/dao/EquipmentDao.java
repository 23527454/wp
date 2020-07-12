package cn.jeefast.modules.equipment.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.modules.equipment.entity.Equipment;

public interface EquipmentDao extends BaseMapper<Equipment>{
	
	List<Equipment> selectPageList(Page<Equipment> page, Map<String, Object> map);
	
	int deleteBatch(Long[] equipmentIds);
	
	List<Equipment> selectAllByDeptId(String deptId);
	
	void updateBySn(Equipment equip);
	
	Equipment queryBySn(String sn);
	
	List<Equipment> queryAll();
	
	List<Equipment> queryByDeptIds(Long userId);
}
