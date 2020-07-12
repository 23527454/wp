package cn.jeefast.modules.equipment.controller;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.exception.RRException;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.modules.equipment.entity.Equipment;
import cn.jeefast.modules.equipment.entity.ReadRecord;
import cn.jeefast.modules.equipment.entity.ReadStatusEntity;
import cn.jeefast.modules.equipment.service.SearchEquipmentService;

/**
 * 设备搜索 
 * @author zgx
 *
 */
@RestController
@RequestMapping("/searchEquipment")
public class SearchEquipmentController extends BaseController{
	
	private static Logger log = LoggerFactory.getLogger(SearchEquipmentController.class);
	
	@Autowired
	private SearchEquipmentService searchEquipmentServiceImpl;
	
	/**
	 * 搜索设备列表
	 * @param params
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("searchEquipment:list")
	public R list(@RequestParam Map<String, Object> params){
		int type = Integer.valueOf(String.valueOf(params.get("type")));
		Map<String, Object> map;
		try {
			Page<Equipment> page = new Page<>(1, 10000);
			page.setRecords(searchEquipmentServiceImpl.searchList(getUserId(),type));
			return R.ok().put("page", page);
		}catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (ConnectException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接前端闸机未成功，请检查1、前端闸机电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("读取数据异常"); 
		}
	}
	
	/**
	 * 读取各页签详情
	 * @param equipment
	 * @param paramType
	 * @return
	 */
	@Log("读取设备参数")
	@RequestMapping("/readInfo/{paramType}")
	@RequiresPermissions("equipParamSet:list")
	public R readInfo(@RequestBody Equipment equipment,@PathVariable("paramType") String paramType){
		Map<String, Object> map;
		try {
			map = searchEquipmentServiceImpl.readInfo(equipment, paramType);
			for(int i=0;i<2;i++) {
				if(String.valueOf(map.get("result")).equals("fail")) {
					Thread.sleep(30);
					map = searchEquipmentServiceImpl.readInfo(equipment, paramType);
				}else {
					break;
				}
			}
			if(String.valueOf(map.get("result")).equals("fail")) {
				throw new RRException(String.valueOf(map.get("msg")));
			}
			return R.ok().put("equipment", (Equipment)map.get("data"));
		}catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (ConnectException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接前端闸机未成功，请检查1、前端闸机电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确"); 
		}catch (SocketException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("读取数据异常"); 
		}
	}
	
	/**
	 * 读取状态
	 * @param params
	 * @return
	 */
	@Log("读取状态")
	@RequestMapping("/readStatusInfo")
	@RequiresPermissions("equipParamSet:list")
	public R readStatusInfo(@RequestParam Map<String, Object> params){
		
		Equipment equip = new Equipment();
		equip.setIp(String.valueOf(params.get("ip")));
		equip.setPort(Integer.valueOf(String.valueOf(params.get("port"))));
		
		Map<String, Object> map;
		try {
			map = searchEquipmentServiceImpl.readInfo(equip, "readstatus");
			
			for(int i=0;i<2;i++) {
				if(String.valueOf(map.get("result")).equals("fail")) {
					Thread.sleep(30);
					map = searchEquipmentServiceImpl.readInfo(equip, "readstatus");
				}else {
					break;
				}
			}
			
			/*if(String.valueOf(map.get("result")).equals("fail")) {
				throw new Exception(String.valueOf(map.get("msg")));
				//return R.error(String.valueOf(map.get("msg")));
			}*/
			Page<ReadStatusEntity> page = new Page<ReadStatusEntity>(1, 30);
			page.setRecords((List<ReadStatusEntity>)map.get("data"));
			return R.ok().put("page", page);
		}catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (ConnectException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接前端闸机未成功，请检查1、前端闸机电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确"); 
		}catch (SocketException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("读取数据异常"); 
		}
		
	}
	
	/**
	 * 读取状态
	 * @param params
	 * @return
	 */
	@Log("读取记录")
	@RequestMapping("/readRecordInfo")
	@RequiresPermissions("equipParamSet:list")
	public R readRecordInfo(@RequestParam Map<String, Object> params){
		
		Equipment equip = new Equipment();
		equip.setIp(String.valueOf(params.get("ip")));
		equip.setPort(Integer.valueOf(String.valueOf(params.get("port"))));
		
		Map<String, Object> map;
		try {
			map = searchEquipmentServiceImpl.readInfo(equip, "readrecord");
			
			for(int i=0;i<2;i++) {
				if(String.valueOf(map.get("result")).equals("fail")) {
					Thread.sleep(10);
					map = searchEquipmentServiceImpl.readInfo(equip, "readrecord");
				}else {
					break;
				}
			}
			
			if(String.valueOf(map.get("result")).equals("fail")) {
				throw new RRException(String.valueOf(map.get("msg"))); 
				//return R.error(String.valueOf(map.get("msg")));
			}
			Page<ReadRecord> page = new Page<ReadRecord>(1, 30);
			page.setRecords((List<ReadRecord>)map.get("data"));
			return R.ok().put("page", page);
		}catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (ConnectException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接前端闸机未成功，请检查1、前端闸机电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确"); 
		}catch (SocketException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("读取数据异常"); 
		}
		
	}
	
	@Log("上传记录")
	@RequestMapping("/uploadRecord")
	@RequiresPermissions("searchEquipment:upload")
	public R uploadRecord(@RequestBody Equipment equipment){
		try {
			searchEquipmentServiceImpl.uploadRecord(equipment);
			return R.ok();
		}catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接超时，请重新尝试！"); 
		}catch (ConnectException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接失败"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("服务异常"); 
		}
	}
	
	@Log("上传状态")
	@RequestMapping("/uploadStatus")
	@RequiresPermissions("searchEquipment:upload")
	public R uploadStatus(@RequestBody Equipment equipment){
		ValidatorUtils.validateEntity(equipment.getReadStatus());
		try {
			searchEquipmentServiceImpl.uploadStatus(equipment);
			return R.ok();
		} catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接超时，请重新尝试！"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("服务异常"); 
		}
	}
	
	/**
	 * 更新设备信息
	 * @param equipment
	 * @param paramType
	 * @return
	 */
	@Log("更新设备参数")
	@RequestMapping("/settingInfo/{paramType}")
	@RequiresPermissions("equipParamSet:list")
	public R settingInfo(@RequestBody Equipment equipment,@PathVariable("paramType") String paramType){
		
		try {
			searchEquipmentServiceImpl.settingInfo(equipment, paramType);
			if(paramType.equals("baseInfo")) {
				equipment.setOldIp(equipment.getIp());
				equipment.setOldPort(equipment.getPort());
			}
			return R.ok().put("equipment", equipment);
		}catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (ConnectException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接前端闸机未成功，请检查1、前端闸机电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确"); 
		}catch (SocketException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("设置数据异常"); 
		}
	}
	
	/**
	 * 更新设备信息
	 * @param equipment
	 * @param paramType
	 * @return
	 */
	@Log("操作设备")
	@RequestMapping("/operateEquip/{opearteType}")
	@RequiresPermissions("searchEquipment:operator")
	public R operateEquip(@RequestBody Equipment equipment,@PathVariable("opearteType") String opearteType){
		try {
			searchEquipmentServiceImpl.operateEquip(equipment, opearteType);
			return R.ok();
		}
			catch (SocketTimeoutException e) {
				log.error(getUserId()+"===="+e.getMessage(),e);
				e.printStackTrace();
				return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
			}catch (ConnectException e) {
				log.error(getUserId()+"===="+e.getMessage(),e);
				e.printStackTrace();
				return R.error("连接前端闸机未成功，请检查1、前端闸机电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确"); 
			}catch (SocketException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("操作设备异常"); 
		}
	}
	
	
	
	@Log("加入数据库")
	@RequestMapping("/saveDataBase/{deptId}")
	@RequiresPermissions("searchEquipment:list")
	public R saveDataBase(@RequestBody List<Equipment> snList,@PathVariable("deptId") Long deptId){
		try {
			searchEquipmentServiceImpl.saveDataBase(snList, getUser(),deptId);
			return R.ok();
		}catch (SocketTimeoutException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("前端闸机超时未应答，请检查前端闸机与网络通讯是否正常"); 
		}catch (ConnectException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("连接前端闸机未成功，请检查1、前端闸机电源是否开启；2、IP和端口号是否正确; 3、网络路由参数和防火墙设置是否正确"); 
		}catch (RRException e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage()); 
		} catch (Exception e) {
			log.error(getUserId()+"==服务异常=="+e.getMessage(),e);
			e.printStackTrace();
			return R.error("保存设备异常"); 
		}
	
	}
	
	@Log("更新设备密码")
	@RequestMapping("/updatePassword")
	@RequiresPermissions("searchEquipment:password")
	public R updatePassword(@RequestBody Equipment equipment){
		try {
			return R.ok();
		} catch (Exception e) {
			log.error(getUserId()+"===="+e.getMessage(),e);
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
}
