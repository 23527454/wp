package cn.jeefast.rest.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.rest.entity.Equipment;

public interface EquipmentDao extends BaseMapper<Equipment>{
	
	List<Equipment> selectPageList(Page<Equipment> page, Map<String, Object> map);
	
	int deleteBatch(Object[] id);
	
	void updateBySn(Equipment equip);
}
