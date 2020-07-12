package cn.jeefast.modules.equipment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.common.utils.Constant;
import cn.jeefast.modules.equipment.dao.EquipmentDao;
import cn.jeefast.modules.equipment.entity.EquipTree;
import cn.jeefast.modules.equipment.entity.Equipment;
import cn.jeefast.modules.equipment.service.EquipmentService;
import cn.jeefast.modules.equipment.service.SearchEquipmentService;
import cn.jeefast.system.entity.SysDept;
import cn.jeefast.system.service.SysDeptService;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentDao, Equipment> implements EquipmentService{
	
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private SearchEquipmentService searchEquipmentServiceImpl;
	@Autowired
	private SysDeptService sysDeptServiceImpl;
	
	@Override
	//@DataFilter(tableAlias="e",user=false)
	public Page<Equipment> queryPageList(Page<Equipment> page, Map<String, Object> map) {
		page.setRecords(equipmentDao.selectPageList(page, map));
		return page;
	}
	
	@Override
	public int deleteBatch(Long[] equipmentIds) {
		return equipmentDao.deleteBatch(equipmentIds);
	}
	
	
	@Override
	public List<Equipment> selectAllByDeptId(String deptId) {
		return equipmentDao.selectAllByDeptId(deptId);
	}
	
	@Override
	public void updateBySn(Equipment euqip) {
		equipmentDao.updateBySn(euqip);
	}
	
	@Override
	public Equipment queryBySn(String sn) {
		return equipmentDao.queryBySn(sn);
	}
	
	@Override
	public void updateEquip(Equipment equip) throws Exception{
		/*Equipment e = equipmentDao.selectById(equip.getId());
		//远程更新设备参数
		if(!compareEquip(e,equip)) {
			searchEquipmentServiceImpl.settingInfo(equip, "baseInfo");
		}*/
		//更新数据参数
		super.updateById(equip);
	}
	
	@Override
	public List<EquipTree> equipTree(Long userId,String param) {
		List<SysDept> deptList = null;
		List<Equipment> equipList = null;
		if(userId==Constant.SUPER_ADMIN) {
			deptList = sysDeptServiceImpl.queryList(new HashMap<>());
			equipList = equipmentDao.queryAll();
		}else {
			deptList = sysDeptServiceImpl.queryListByUserId(userId);
			equipList = equipmentDao.queryByDeptIds(userId);
		}
		List<EquipTree> returnList = new ArrayList<>();
		for(SysDept dept : deptList) {
			EquipTree dtree = new EquipTree(String.valueOf(dept.getDeptId()),dept.getName(),String.valueOf(dept.getParentId()),0);
			returnList.add(dtree);
			for(int i=0;i<equipList.size();i++) {
				Equipment e = equipList.get(i);
				if(dept.getDeptId().intValue()==e.getDeptId()) {
					EquipTree etree = new EquipTree(e.getEquipSn(),e.getOtherName(),String.valueOf(e.getDeptId()),1);
					returnList.add(etree);
				}
			}
		}
		return returnList;
	}
	
	/**
	 * 根据所拥有的设备权限 查询出数据库中对应的列表数据
	 */
	@Override
	public List<Equipment> queryByDeptIds(Long userId) {
		return equipmentDao.queryByDeptIds(userId);
	}
	
	private boolean compareEquip(Equipment equip,Equipment equ) {
		if(equip.getAddress().equals(equ.getAddress())&&equip.getIp().equals(equ.getIp())&&equip.getPort()==equ.getPort()
				&&equip.getIpCenter().equals(equ.getIpCenter())&&equip.getPortCenter()==equ.getPortCenter()&&equip.getGateWay().equals(equ.getGateWay())
						&&equip.getSubnetMask().equals(equ.getSubnetMask())) {
			return true;
		}else {
			return false;
		}
	}
}
