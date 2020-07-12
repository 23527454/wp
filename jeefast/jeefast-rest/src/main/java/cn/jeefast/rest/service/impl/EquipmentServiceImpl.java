package cn.jeefast.rest.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.rest.dao.EquipmentDao;
import cn.jeefast.rest.entity.Equipment;
import cn.jeefast.rest.service.EquipmentService;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentDao, Equipment> implements EquipmentService{
	
	@Autowired
	private EquipmentDao equipmentDao;
	
	@Override
	//@DataFilter(tableAlias="e",user=false)
	public Page<Equipment> queryPageList(Page<Equipment> page, Map<String, Object> map) {
		page.setRecords(equipmentDao.selectPageList(page, map));
		return page;
	}
	
	@Override
	public int deleteBatch(Object[] id) {
		return equipmentDao.deleteBatch(id);
	}
	
	@Override
	public void updateBySn(Equipment euqip) {
		equipmentDao.updateBySn(euqip);
	}
}
