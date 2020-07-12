package cn.jeefast.modules.equipment.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.modules.equipment.entity.EquipTree;
import cn.jeefast.modules.equipment.entity.Equipment;

public interface EquipmentService extends IService<Equipment>{
	
	Page<Equipment> queryPageList(Page<Equipment> pageUtil, Map<String, Object> map);
	
	int deleteBatch(Long[] equipmentIds);
	
	List<Equipment> selectAllByDeptId(String deptId);
	
	void updateBySn(Equipment euqip);
	
	Equipment queryBySn(String sn);
	
	void updateEquip(Equipment equip) throws Exception;
	
	List<EquipTree> equipTree(Long userId,String param);
	
	List<Equipment> queryByDeptIds(Long userId);
}
