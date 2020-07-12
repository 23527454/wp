package cn.jeefast.modules.equipment.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.common.validator.group.AddGroup;
import cn.jeefast.modules.equipment.entity.EquipTree;
import cn.jeefast.modules.equipment.entity.Equipment;
import cn.jeefast.modules.equipment.service.EquipmentService;

@RestController
@RequestMapping("/equipment")
public class EquipmentController extends BaseController{
	
	private static Logger log = LoggerFactory.getLogger(EquipmentController.class);
	
	@Autowired
	private EquipmentService equipmentService;
	
	@RequestMapping("/list")
	@RequiresPermissions("equipment:list")
	public R list(@RequestParam Map<String, Object> params){
		try {
			Query query = new Query(params);
			Page<Equipment> pageUtil = new Page<Equipment>(query.getPage(), query.getLimit());
			Page<Equipment> page = equipmentService.queryPageList(pageUtil,query);
			return R.ok().put("page", page);
		}catch (Exception e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("加载数据失败");
		}
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{equipmentId}")
	@RequiresPermissions("equipment:update")
	public R info(@PathVariable("equipmentId") Long equipmentId){
		try {
			Equipment equipment = equipmentService.selectById(equipmentId);
			//存储通信地址 防止修改地址后  无老地址信息调用
			equipment.setOldIp(equipment.getIp());
			equipment.setOldPort(equipment.getPort());
			return R.ok().put("equipment", equipment);
			
		}catch (Exception e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("加载数据失败");
		}
	}
	
	@RequestMapping("/delete")
	@RequiresPermissions("equipment:delete")
	public R delete(@RequestBody Long[] equipmentIds){
		try {
			equipmentService.deleteBatch(equipmentIds);
			return R.ok();
		}catch (Exception e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("删除数据失败");
		}
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("equipment:add")
	public R save(@RequestBody Equipment equip){
		
		ValidatorUtils.validateEntity(equip, AddGroup.class);
		try {
			equip.setEquipSn(equip.getEquipReq().substring(8));
			equipmentService.insert(equip);
			return R.ok();
		}catch (Exception e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			if(e.getMessage().indexOf("UNIQUE")!=-1) {
				return R.error("sn码或网络地址已存在");
			}else {
				return R.error("保存数据失败");
			}
		}
		
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("equipment:update")
	public R update(@RequestBody Equipment equip){
		ValidatorUtils.validateEntity(equip);
		try {
			equipmentService.updateEquip(equip);
			return R.ok();
		} catch (Exception e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			if(e.getMessage().indexOf("UNIQUE")!=-1) {
				return R.error("sn码或网络地址已存在");
			}else {
				return R.error("修改数据失败");
			}
		}
	}
	
	@RequestMapping("/equipTree")
	public List<EquipTree> queryEquipTree(@RequestParam Map<String, Object> params){
		return equipmentService.equipTree(getUserId(),String.valueOf(params.get("param")));
	}
}
