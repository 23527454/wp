package cn.jeefast.rest.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jeefast.rest.entity.Equipment;
import cn.jeefast.rest.service.EquipmentService;
import cn.jeefast.rest.service.SearchEquipmentService;
import cn.jeefast.rest.util.OperateUtil;
import cn.jeefast.rest.util.ReturnUtil;
import cn.jeefast.rest.util.SocketUtil;

@Service
public class SearchEquipmentServiceImpl implements SearchEquipmentService{
	@Autowired
	private EquipmentService equipmentServiceImpl;
	/**
	 * 读取控制板参数
	 */
	@Override
	public Map<String,Object> readInfo(String ip,Integer port, String readType) throws Exception {
		//读取工作模式
		if(readType.equals("1")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.readWorkMode());
			return ReturnUtil.returnWorkMode(resultBytes, new Equipment());
		}else if(readType.equals("2")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.readParamsTime());
			return ReturnUtil.returnTimeParam(resultBytes, new Equipment());
		}else if(readType.equals("3")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.readParamsAlarm());
			return ReturnUtil.returnAlarmParam(resultBytes, new Equipment());
		}else if(readType.equals("4")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.readDjParams());
			return ReturnUtil.returnDjParam(resultBytes, new Equipment());
		}else if(readType.equals("5")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.readVersion());
			return ReturnUtil.returnVersion(resultBytes, new Equipment());
		}else if(readType.equals("6")) { 
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.readStatus());
			return ReturnUtil.returnReadStatus(resultBytes);
		}else if(readType.equals("7")) { 
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.readRecord());
			return ReturnUtil.returnReadRecord(resultBytes);
		}
		return null;
	}
	
	@Override
	public void settingBaseInfo(String ip, Integer port, Equipment equip, String settingType) throws Exception {
		byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingBaseInfo(equip));
		ReturnUtil.settingReturn(resultBytes);
		equipmentServiceImpl.updateBySn(equip);
	}
	
	/**
	 * 设置控制板参数
	 */
	@Override
	public void settingInfo(String ip,Integer port,String masterValue,String slaveValue,String settingType) throws Exception {
		//更改面板基本信息
		 if(settingType.equals("zjType")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingMachineType(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("djType")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingElectricalMachineType(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zjModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingMachinePatten(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("leftCrossModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingLeftPatten(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("rightCrossModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingRightPatten(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("remember")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingRemember(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("fxwpCross")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingFxwpcx(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("babyCross")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingBabyGo(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zjWorkModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingMachineOperatePatten(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("yzwsgzModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingYzWsGz(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("crAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingIntrudeAlarm(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("wsAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingWsAlarm(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zlAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingRetetionAlarm(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zjAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSelfInspectionAlarm(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("qhAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingBackAlarm(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("dbcl")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamDbcz(Short.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zmdWorkSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingMainMDSpeed(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("fmdWorkSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingMDSpeed(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("mdMaxWorkTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingMdMaxTime(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("hwjcTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingRedLineTime(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("txjgTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingGoIntervalTime(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("ddryjrTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingWaitPepoleGo(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("ryzlTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingPepoleRetetion(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("ysgzTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingDelayCloseTime(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zytxjgTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingFreeGoIntervalTime(Integer.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zkzSpeed")||settingType.equals("ckzSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamKzsd(Short.valueOf(masterValue), Short.valueOf(slaveValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zgzSpeed")||settingType.equals("cgzSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamGzsd(Short.valueOf(masterValue), Short.valueOf(slaveValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zzdElectric")||settingType.equals("czdElectric")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamZddl(Short.valueOf(masterValue), Short.valueOf(slaveValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zkzTime")||settingType.equals("ckzTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamKzsj(Short.valueOf(masterValue), Short.valueOf(slaveValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zgzTime")||settingType.equals("cgzTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamGzsj(Short.valueOf(masterValue), Short.valueOf(slaveValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zkzAngle")||settingType.equals("ckzAngle")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamKzjd(Short.valueOf(masterValue), Short.valueOf(slaveValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("qtmcs")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamQtmcs(Short.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("qthfTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamQthfsj(Short.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zdftAngle")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingSfdjParamZdftjd(Short.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("kzjgTime")||settingType.equals("gzjgTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip, port, OperateUtil.settingOldModeKgIntervalTime(Short.valueOf(masterValue), Short.valueOf(slaveValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zdModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip,port, OperateUtil.settingSfdjParamZdmsxz(Short.valueOf(masterValue)));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("synEquipTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip,port, OperateUtil.settingEquipTime(masterValue));
			ReturnUtil.settingReturn(resultBytes);
		}
	}
	
	
	/**
	 * 操纵设备
	 */
	@Override
	public void operateEquip(String ip,Integer port, String operateType) throws Exception {
		byte[] resultBytes = SocketUtil.sendMsgForEquipment(ip,port, OperateUtil.controllEquipment(Integer.valueOf(operateType)));
		ReturnUtil.settingReturn(resultBytes);
	}
}
