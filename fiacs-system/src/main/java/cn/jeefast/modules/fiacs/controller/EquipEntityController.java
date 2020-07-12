package cn.jeefast.modules.fiacs.controller;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.exception.RRException;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.common.validator.group.AddGroup;
import cn.jeefast.modules.fiacs.entity.EquipEntity;
import cn.jeefast.modules.fiacs.service.EquipEntityService;
import cn.jeefast.modules.fiacs.util.EquipStartUtil;

@RestController
@RequestMapping("/fiacs/equip")
public class EquipEntityController extends BaseController {

	@Resource
	private EquipEntityService equipEntityServiceImpl;
	
	@RequestMapping("/list")
	@Log("搜索网络设备")
	public R list(@RequestParam Map<String, Object> params) {
		int type = Integer.valueOf(String.valueOf(params.get("type")));
		try {
			Page<EquipEntity> page = new Page<>(1, 10000);
			page.setRecords(equipEntityServiceImpl.searchEquip(getUserId(), type));
			return R.ok().put("page", page);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return R.error("前端设备超时未应答，请检查前端设备与网络通讯是否正常");
		} catch (ConnectException e) {
			e.printStackTrace();
			return R.error("连接前端设备未成功，请检查1、前端设备电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确");
		} catch (RRException e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("读取数据失败");
		}
	}

	@RequestMapping("/infoOfficeId/{officeId}")
	public R infoOfficeId(@PathVariable("officeId") String officeId) {
		try {
			EquipEntity equipment = new EquipEntity();
			equipment.setOfficeId(officeId);
			EquipEntity eq = equipEntityServiceImpl.queryByCondition(equipment);
			if (eq == null) {
				return R.error("加载数据失败");
			}
			return R.ok().put("equipment", eq);

		} catch (Exception e) {
			e.printStackTrace();
			return R.error("加载数据失败");
		}
	}

	@RequestMapping("/saveDataBase/{deptId}")
	@Log("加入数据库")
	public R saveDataBase(@RequestBody EquipEntity equip, @PathVariable("deptId") Long deptId) {
		try {
			equipEntityServiceImpl.saveDataBase(equip, getUser(), deptId);
			return R.ok();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return R.error("前端设备超时未应答，请检查前端设备与网络通讯是否正常");
		} catch (ConnectException e) {
			e.printStackTrace();
			return R.error("连接前端设备未成功，请检查1、前端设备电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确");
		} catch (RRException e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("保存设备失败");
		}

	}

	/**
	 * 修改设备基本参数 传递给硬件
	 * 
	 * @param equip
	 * @return
	 */
	@RequestMapping("/synEquipInfo")
	@Log("保存设备参数")
	public R synEquipInfo(@RequestBody EquipEntity equip) {
		ValidatorUtils.validateEntity(equip,EquipEntity.SynEquip.class);
		try {
			equipEntityServiceImpl.synEquipInfo(equip);
			return R.ok();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return R.error("前端设备超时未应答，请检查前端设备与网络通讯是否正常");
		} catch (ConnectException e) {
			e.printStackTrace();
			return R.error("连接前端设备未成功，请检查1、前端设备电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确");
		} catch (RRException e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("保存设备失败");
		}

	}

	/**
	 * 修改设备基本参数 传递给硬件
	 * 
	 * @param equip
	 * @return
	 */
	@RequestMapping("/resetEquip")
	@Log("操控设备")
	public R resetEquip(@RequestBody EquipEntity equip) {
		try {
			equipEntityServiceImpl.resetEquip(equip);
			EquipStartUtil.add(equip.getEquipSn(), equip.getResetType());
			return R.ok();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return R.error("前端设备超时未应答，请检查前端设备与网络通讯是否正常");
		} catch (ConnectException e) {
			e.printStackTrace();
			return R.error("连接前端设备未成功，请检查1、前端设备电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确");
		} catch (RRException e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("保存设备失败");
		}

	}

	@RequestMapping("/saveEquip")
	@Log("新增设备")
	public R saveEquip(@RequestBody EquipEntity equip) {
		ValidatorUtils.validateEntity(equip,EquipEntity.updateEquip.class);
		equipEntityServiceImpl.insertEquip(equip);
		return R.ok();
	}

	@RequestMapping("/updateEquip")
	@Log("更新设备")
	public R updateEquip(@RequestBody EquipEntity equip) {
		ValidatorUtils.validateEntity(equip,EquipEntity.updateEquip.class);
		try {
			equipEntityServiceImpl.updateEquip(equip);
			return R.ok();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return R.error("前端设备超时未应答，请检查前端设备与网络通讯是否正常");
		} catch (ConnectException e) {
			e.printStackTrace();
			return R.error("连接前端设备未成功，请检查1、前端设备电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确");
		} catch (RRException e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改设备失败");
		}
	}

	@RequestMapping("/deleteEquip/{officeId}")
	@Log("删除设备")
	public R deleteEquip(@PathVariable("officeId") Integer officeId) {
		equipEntityServiceImpl.deleteEquip(officeId);
		return R.ok();
	}
	
	@RequestMapping("/queryAllEquip")
	public R queryAllEquip() {
		Page<EquipEntity> page = new Page<>(1, 10000);
		page.setRecords(equipEntityServiceImpl.queryAllEquipNotConnect());
		page.setTotal(page.getRecords().size());
		return R.ok().put("page", page);
	}
}
