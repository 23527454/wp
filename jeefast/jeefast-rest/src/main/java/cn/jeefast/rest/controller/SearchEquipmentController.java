package cn.jeefast.rest.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.rest.entity.Equipment;
import cn.jeefast.rest.entity.ReadRecord;
import cn.jeefast.rest.entity.ReadStatusEntity;
import cn.jeefast.rest.service.SearchEquipmentService;
import cn.jeefast.rest.util.Constants;

/**
 * 设备搜索 
 * @author zgx
 *
 */
@RestController
@RequestMapping("/api")
public class SearchEquipmentController{/*
	
	@Autowired
	private SearchEquipmentService searchEquipmentServiceImpl;
	
	*//**
	 * 读取各页签详情
	 * @param paramType
	 * @return
	 *//*
	//@Log("读取设备参数")
	@PostMapping("readInfo")
	public R readInfo(@RequestAttribute("paramMap") Map<String, Object> params){
		String readType = String.valueOf(params.get("readType"));
		String ip = String.valueOf(params.get("ip"));
		Integer port = Integer.valueOf(String.valueOf(params.get("port")));
		Map<String, Object> map;
		try {
			map = searchEquipmentServiceImpl.readInfo(ip,port,readType);

			while(String.valueOf(map.get("result")).equals("fail")&&!"6".equals(readType)&&!"7".equals(readType)) {
				map = searchEquipmentServiceImpl.readInfo(ip,port,readType);
			}
			if(String.valueOf(map.get("result")).equals("fail")) {
				throw new Exception(String.valueOf(map.get("msg")));
			}
			//读取状态  
			if("6".equals(readType)) {
				return R.ok().put("data", DESCoderFUIOU.desEncrypt(JSONObject.toJSONString((ReadStatusEntity)map.get("data")), DESCoderFUIOU.getKeyLength8(Constants.KEY)));
				//读取记录
			}else if("7".equals(readType)){
				return R.ok().put("data", DESCoderFUIOU.desEncrypt(JSONObject.toJSONString((List<ReadRecord>)map.get("data")), DESCoderFUIOU.getKeyLength8(Constants.KEY)));
			}else {
				return R.ok().put("data", DESCoderFUIOU.desEncrypt(JSONObject.toJSONString((Equipment)map.get("data")), DESCoderFUIOU.getKeyLength8(Constants.KEY)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return R.error(e.getMessage()); 
		}
	}

	
	*//**
	 * 更新设备信息
	 * @param equipment
	 * @param paramType
	 * @return
	 *//*
	@Log("更新设备参数")
	@RequestMapping("/settingInfo")
	public R settingInfo(@RequestAttribute("paramMap") Map<String, Object> params){
		return R.ok();
	}
	
	*//**
	 * 更新设备信息
	 * @param equipment
	 * @param paramType
	 * @return
	 *//*
	@Log("操作设备")
	@RequestMapping("/operateEquip/{opearteType}")
	@RequiresPermissions("searchEquipment:operator")
	public R operateEquip(@RequestBody Equipment equipment,@PathVariable("opearteType") String opearteType){
		try {
			searchEquipmentServiceImpl.operateEquip(equipment, opearteType);
			return R.ok();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
	
	
	
	@Log("加入数据库")
	@RequestMapping("/saveDataBase")
	@RequiresPermissions("searchEquipment:update")
	public R saveDataBase(@RequestBody String[] snList){
		try {
			searchEquipmentServiceImpl.saveDataBase(snList, getUser());
			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
	
	@Log("更新设备密码")
	@RequestMapping("/updatePassword")
	@RequiresPermissions("searchEquipment:password")
	public R updatePassword(@RequestBody Equipment equipment){
		try {
			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
*/}
