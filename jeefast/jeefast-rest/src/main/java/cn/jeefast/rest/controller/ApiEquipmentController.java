package cn.jeefast.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.common.annotation.LogForRest;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.rest.entity.AlarmEntity;
import cn.jeefast.rest.entity.Equipment;
import cn.jeefast.rest.entity.ReadRecord;
import cn.jeefast.rest.entity.ReadStatusEntity;
import cn.jeefast.rest.service.AlarmEntityService;
import cn.jeefast.rest.service.EquipmentService;
import cn.jeefast.rest.service.SearchEquipmentService;
import cn.jeefast.rest.util.Constants;
import cn.jeefast.rest.util.DESCoder;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class ApiEquipmentController{
	
	private static Logger log = LoggerFactory.getLogger(ApiEquipmentController.class);
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private SearchEquipmentService searchEquipmentServiceImpl;
	
	@Autowired
	private AlarmEntityService alarmEntityServiceImpl;
	
	@PostMapping("queryEquipments")
	@ApiOperation(value="查询数据库中设备列表",httpMethod="POST")
	public R list(@RequestAttribute("paramMap") Map<String, Object> params,@RequestAttribute("userId") Long userId){
		try {
			params.put("userId", userId);
			Query query = new Query(params);
			Page<Equipment> pageUtil = new Page<Equipment>(query.getPage(), query.getLimit());
			Page<Equipment> page = equipmentService.queryPageList(pageUtil,query);
			
			Map<String,Object> returnMap = new HashMap<>();
			returnMap.put("totalCount", page.getTotal());
			returnMap.put("dataList", page.getRecords());
			String sign =JSONObject.toJSONString(page.getRecords());
			
				return R.ok().put("totalCount", page.getTotal()).put("data",DESCoder.encode3Des(Constants.data_key, sign));
			} catch (Exception e) {
				log.error("userid:"+userId+"===="+e.getMessage(),e);
				e.printStackTrace();
				return R.error(e.getMessage());
			}
	}
	
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
				return R.ok().put("data", DESCoder.encode3Des(Constants.data_key,JSONObject.toJSONString((ReadStatusEntity)map.get("data"))));
				//读取记录
			}else if("7".equals(readType)){
				return R.ok().put("data", DESCoder.encode3Des(Constants.data_key,JSONObject.toJSONString((List<ReadRecord>)map.get("data"))));
			}else {
				return R.ok().put("data", DESCoder.encode3Des(Constants.data_key,JSONObject.toJSONString((Equipment)map.get("data"))));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		}
	}

	
	/**
	 * 更新设备信息
	 * @param equipment
	 * @param paramType
	 * @return
	 */
	@LogForRest("账号-更新设备参数")
	@RequestMapping("/settingInfo")
	public R settingInfo(@RequestAttribute("paramMap") Map<String, Object> params,@RequestAttribute("userName") String userName){
		String settingType = String.valueOf(params.get("settingType"));
		String ip = String.valueOf(params.get("ip"));
		Integer port = Integer.valueOf(String.valueOf(params.get("port")));
		
		Equipment equip = new Equipment();
		try {
			if("baseInfo".equals(settingType)) {
				equip.setAddress(String.valueOf(params.get("equipmentAddress")));
				equip.setIp(String.valueOf(params.get("equipmentIp")));
				equip.setPort(Integer.valueOf(String.valueOf(params.get("equipmentPort"))));
				equip.setIpCenter(String.valueOf(params.get("equipmentUploadIp")));
				equip.setPortCenter(Integer.valueOf(String.valueOf(params.get("equipmentUploadPort"))));
				equip.setEquipSn(String.valueOf(params.get("equipmentSn")));
				equip.setEquipReq(String.valueOf(params.get("equipmentReq")));
				equip.setSubnetMask(String.valueOf(params.get("equipmentSubnetMask")));
				equip.setGateWay(String.valueOf(params.get("equipmentGateway")));
				searchEquipmentServiceImpl.settingBaseInfo(ip, port, equip, settingType);
				return R.ok().put("equipment", equip);
			}else {
				searchEquipmentServiceImpl.settingInfo(ip, port, String.valueOf(params.get("masterValue")), String.valueOf(params.get("slaveValue")), settingType);
				return R.ok();
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
	
	
	/**
	 * 操控设备
	 * @param equipment
	 * @param paramType
	 * @return
	 */
	@LogForRest("账号-操控设备")
	@RequestMapping("/operateEquip")
	public R operateEquip(@RequestAttribute("paramMap") Map<String, Object> params,@RequestAttribute("userName") String userName){
		String operateType = String.valueOf(params.get("operateType"));
		String ip = String.valueOf(params.get("ip"));
		Integer port = Integer.valueOf(String.valueOf(params.get("port")));
		
		try {
			searchEquipmentServiceImpl.operateEquip(ip, port, operateType);
			return R.ok();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
	
	@PostMapping("queryAlarm")
	@ApiOperation(value="查询警报信息",httpMethod="POST")
	public R queryAlarm(@RequestAttribute("paramMap") Map<String, Object> params,@RequestAttribute("userId") Long userId){
		try {
			params.put("userId", userId);
			Query query = new Query(params);
			Page<AlarmEntity> pageUtil = new Page<AlarmEntity>(query.getPage(), query.getLimit());
			query.put("userId",userId);
			Page<AlarmEntity> page = alarmEntityServiceImpl.queryPageList(pageUtil,query);
			
			Map<String,Object> returnMap = new HashMap<>();
			returnMap.put("totalCount", page.getTotal());
			returnMap.put("dataList", page.getRecords());
			String sign =JSONObject.toJSONString(page.getRecords());
			
				return R.ok().put("totalCount", page.getTotal()).put("data",DESCoder.encode3Des(Constants.data_key, sign));
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				e.printStackTrace();
				return R.error(e.getMessage());
			}
	}
}
