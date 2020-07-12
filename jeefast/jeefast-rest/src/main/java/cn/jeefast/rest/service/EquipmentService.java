package cn.jeefast.rest.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.rest.entity.Equipment;

public interface EquipmentService extends IService<Equipment>{
	
	Page<Equipment> queryPageList(Page<Equipment> pageUtil, Map<String, Object> map);
	
	int deleteBatch(Object[] id);
	
	void updateBySn(Equipment euqip);
}
