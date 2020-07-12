package cn.jeefast.modules.equipment.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.RegEx;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.RegExPatternMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jeefast.common.exception.RRException;
import cn.jeefast.modules.equipment.entity.Equipment;
import cn.jeefast.modules.equipment.service.EquipmentService;
import cn.jeefast.modules.equipment.service.SearchEquipmentService;
import cn.jeefast.modules.equipment.util.ByteUtil;
import cn.jeefast.modules.equipment.util.DateUtil;
import cn.jeefast.modules.equipment.util.OperateUtil;
import cn.jeefast.modules.equipment.util.RegExpValidatorUtils;
import cn.jeefast.modules.equipment.util.ReturnUtil;
import cn.jeefast.modules.equipment.util.SocketUtil;
import cn.jeefast.modules.equipment.util.UDPReceiveClient;
import cn.jeefast.modules.equipment.util.UDPUtil;
import cn.jeefast.system.entity.SysDept;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.service.SysDeptService;

@Service
public class SearchEquipmentServiceImpl implements SearchEquipmentService{
	
	@Autowired
	private EquipmentService equipmentServiceImpl;
	@Autowired
	private SysDeptService sysDeptServiceImpl;
	/**
	 * 搜索设备
	 */
	@Override
	public List<Equipment> searchList(Long userId,int type) throws Exception {
		
		List<Equipment> searchEquip = UDPUtil.sendMsgForSearchEquipment("255.255.255.255",6000, OperateUtil.snReadEquipment());
		//数据库中设备(根据角色中设备权限中过滤)
		List<Equipment> dbEquip = equipmentServiceImpl.queryByDeptIds(userId);
		
		if(dbEquip==null||dbEquip.size()==0) {
			return searchEquip;
		}
		
		List<Equipment> newEquip = new ArrayList<Equipment>();//未加入数据库
		List<Equipment> oldEquip = new ArrayList<Equipment>();//已加入数据库
		
		for(Equipment equip : searchEquip) {
			if(dbEquip==null||dbEquip.size()==0) {
				newEquip.add(equip);
			}
			for(int i=0;i<dbEquip.size();i++) {
				if(equip.getEquipSn().equals(dbEquip.get(i).getEquipSn())) {
					equip.setDeptName(dbEquip.get(i).getDeptName());
					equip.setOtherName(dbEquip.get(i).getOtherName());
					oldEquip.add(equip);
					dbEquip.remove(i);
					break;
				}
				if(i==dbEquip.size()-1) {
					newEquip.add(equip);
				}
			}
		}
		if(type==0) {
			newEquip.addAll(oldEquip);
		}
		return newEquip;
	}
	
	/**
	 * 读取控制板参数
	 */
	@Override
	public Map<String,Object> readInfo(Equipment equip, String readType) throws Exception {
		if(equip.getIp()==null) {
			equip = equipmentServiceImpl.queryBySn(equip.getEquipSn());
		}
		//读取工作模式
		if(readType.equals("workmodel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.readWorkMode());
			return ReturnUtil.returnWorkMode(resultBytes, equip);
		}else if(readType.equals("timeparams")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.readParamsTime());
			return ReturnUtil.returnTimeParam(resultBytes, equip);
		}else if(readType.equals("alarmparams")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.readParamsAlarm());
			return ReturnUtil.returnAlarmParam(resultBytes, equip);
		}else if(readType.equals("djparams")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.readDjParams());
			return ReturnUtil.returnDjParam(resultBytes, equip);
		}else if(readType.equals("version")) {//增加了时钟信息和有效期信息 2019-4-24
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.readVersion());
			return ReturnUtil.returnVersion(resultBytes, equip);
		}else if(readType.equals("readstatus")) { 
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.readStatus());
			return ReturnUtil.returnReadStatus(resultBytes);
		}else if(readType.equals("readrecord")) { 
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.readRecord());
			return ReturnUtil.returnReadRecord(resultBytes);
		}
		return null;
	}
	
	/**
	 * 设置控制板参数
	 */
	@Override
	public void settingInfo(Equipment equip, String settingType) throws Exception {
		//更改面板基本信息
		if(settingType.equals("baseInfo")) {
			if(!RegExpValidatorUtils.isIP(equip.getIp())) {
				throw new RRException("网络地址格式错误");
			}
			
			if(!RegExpValidatorUtils.isIP(equip.getGateWay())) {
				throw new RRException("网关格式错误");
			}
			
			if(!RegExpValidatorUtils.isIP(equip.getIpCenter())) {
				throw new RRException("上传地址格式错误");
			}
			
			if(!RegExpValidatorUtils.isIP(equip.getSubnetMask())) {
				throw new RRException("子网掩码格式错误");
			}
			
			if(!RegExpValidatorUtils.isPort(equip.getPort())) {
				throw new RRException("网络端口错误,范围为0~65535");
			}
			
			if(!RegExpValidatorUtils.isPort(equip.getPortCenter())) {
				throw new RRException("上传地址端口错误,范围为0~65535");
			}
			
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getOldIp(), equip.getOldPort(), OperateUtil.settingBaseInfo(equip));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zjType")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingMachineType(Integer.valueOf(equip.getZjType())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("djType")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingElectricalMachineType(Integer.valueOf(equip.getDjType())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zjModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingMachinePatten(Integer.valueOf(equip.getZjModel())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("leftCross")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingLeftPatten(Integer.valueOf(equip.getLeftCrossModel())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("rightCross")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingRightPatten(Integer.valueOf(equip.getRightCrossModel())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("remember")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingRemember(Integer.valueOf(equip.getRemember())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("fxwpCross")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingFxwpcx(Integer.valueOf(equip.getFxwpCross())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("babyCross")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingBabyGo(Integer.valueOf(equip.getBabyCross())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zjWorkModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingMachineOperatePatten(Integer.valueOf(equip.getZjWorkModel())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("yzwsgz")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingYzWsGz(Integer.valueOf(equip.getYzwsgzModel())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("crAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingIntrudeAlarm(Integer.valueOf(equip.getCrAlarm())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("wsAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingWsAlarm(Integer.valueOf(equip.getWsAlarm())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zlAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingRetetionAlarm(Integer.valueOf(equip.getZlAlarm())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zjAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSelfInspectionAlarm(Integer.valueOf(equip.getZjAlarm())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("qhAlarm")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingBackAlarm(Integer.valueOf(equip.getQhAlarm())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("dbcl")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamDbcz(Short.valueOf(equip.getDbcl())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zmdWorkSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingMainMDSpeed(Integer.valueOf(equip.getZmdWorkSpeed())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("fmdWorkSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingMDSpeed(Integer.valueOf(equip.getFmdWorkSpeed())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("mdMaxWorkTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingMdMaxTime(Integer.valueOf(equip.getMdMaxWorkTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("hwjcTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingRedLineTime(Integer.valueOf(equip.getHwjcTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("txjgTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingGoIntervalTime(Integer.valueOf(equip.getTxjgTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("ddryjrTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingWaitPepoleGo(Integer.valueOf(equip.getDdryjrTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("ryzlTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingPepoleRetetion(Integer.valueOf(equip.getRyzlTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("ysgzTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingDelayCloseTime(Integer.valueOf(equip.getYsgzTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zytxjgTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingFreeGoIntervalTime(Integer.valueOf(equip.getZytxjgTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zkzSpeed")||settingType.equals("ckzSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamKzsd(Short.valueOf(equip.getZkzSpeed()), Short.valueOf(equip.getCkzSpeed())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zgzSpeed")||settingType.equals("cgzSpeed")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamGzsd(Short.valueOf(equip.getZgzSpeed()), Short.valueOf(equip.getCgzSpeed())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zzdElectric")||settingType.equals("czdElectric")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamZddl(Short.valueOf(equip.getZzdElectric()), Short.valueOf(equip.getCzdElectric())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zkzTime")||settingType.equals("ckzTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamKzsj(Short.valueOf(equip.getZkzTime()), Short.valueOf(equip.getCkzTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zgzTime")||settingType.equals("cgzTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamGzsj(Short.valueOf(equip.getZgzTime()), Short.valueOf(equip.getCgzTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zkzAngle")||settingType.equals("ckzAngle")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamKzjd(Short.valueOf(equip.getZkzAngle()), Short.valueOf(equip.getCkzAngle())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("qtmcs")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamQtmcs(Short.valueOf(equip.getQtmcs())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("qthfTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamQthfsj(Short.valueOf(equip.getQthfTime())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("zdftAngle")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamZdftjd(Short.valueOf(equip.getZdftAngle())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("kzjgTime")||settingType.equals("gzjgTime")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingOldModeKgIntervalTime(Short.valueOf(equip.getKzjgTime()), Short.valueOf(equip.getGzjgTime())));
			ReturnUtil.settingReturn(resultBytes);
			//阻挡模式选择
		}else if(settingType.equals("zdModel")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingSfdjParamZdmsxz(Short.valueOf(equip.getZdModel())));
			ReturnUtil.settingReturn(resultBytes);
		}else if(settingType.equals("synEquipTime")) {
			this.checkDate(equip, settingType);
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingEquipTime(equip));
			ReturnUtil.settingReturn(resultBytes);
			//恢复出厂设置
		}else if(settingType.equals("restore")) {
			byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.settingRestore());
			ReturnUtil.settingReturn(resultBytes);
		}
	}
	
	private void checkDate(Equipment equip,String type) {
		if(type.equals("synEquipTime")) {
			StringBuffer sb = new StringBuffer();
			sb.append(equip.getYear());
			sb.append("-").append(equip.getMonth()).append("-");
			sb.append(equip.getDay()).append(" ").append(equip.getHours());
			sb.append(":").append(equip.getMinute()).append(":").append(equip.getSecond());
			if(!DateUtil.checkDate(sb.toString(), "yyyy-MM-dd HH:mm:ss")) {
				throw new RRException("时钟时间有误，请更改！");
			}
			if(equip.getWeek()>6||equip.getWeek()<0) {
				throw new RRException("星期的设置范围为0~6");
			}
		}else {
			StringBuffer sb = new StringBuffer();
			sb.append(equip.getYear());
			sb.append("-").append(equip.getMonth()).append("-");
			sb.append(equip.getDay());
			
			if(!DateUtil.checkDate(sb.toString(), "yyyy-MM-dd")) {
				throw new RRException("有效期时间有误，请更改！");
			}
		}
	}
	
	/**
	 * 主动上传记录
	 */
	@Override
	public void uploadRecord(Equipment equip) throws Exception {
		byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIpCenter(), equip.getPortCenter(), OperateUtil.uploadRecord(equip));
		ReturnUtil.settingReturn(resultBytes);
	}
	
	@Override
	public void uploadStatus(Equipment equip) throws Exception {
		byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIpCenter(), equip.getPortCenter(), OperateUtil.uploadStatus(equip));
		ReturnUtil.settingReturn(resultBytes);
	}
	/**
	 * 将搜索的设备 保存到数据库
	 */
	@Override
	public void saveDataBase(List<Equipment> equipList,SysUser sysUser,Long deptId) throws Exception {
		//数据库已存设备信息
		List<Equipment> dataBaseEquipmentList = equipmentServiceImpl.queryByDeptIds(sysUser.getUserId());
		//得到部门信息   新增设备 需要设置默认别名
		SysDept dept = sysDeptServiceImpl.findByDeptId(deptId);
		
		if(dataBaseEquipmentList!=null&&dataBaseEquipmentList.size()>0) {
			List<String> snDataList = this.dataToString(dataBaseEquipmentList);
			List<String> snList = this.dataToString(equipList);
			
			
			//校验是否存在ip冲突
			for(Equipment e : equipList) {
				for(Equipment equ : dataBaseEquipmentList) {
					if(equ.getIp().equals(e.getIp())&&!equ.getEquipSn().equals(e.getEquipSn())) {
						throw new RRException("ip:"+equ.getIp()+"已存在,不可添加");
					}
				}
			}
			
			for(Equipment e : equipList) {
				for(String sn : snList) {
					if(e.getEquipSn().equals(sn)) {
						if(snDataList.contains(e.getEquipSn())) {
							e.setModifyTime(new Timestamp(System.currentTimeMillis()));
							e.setModifyUser(String.valueOf(sysUser.getUserId()));
							e.setDeptId(deptId.intValue());
							equipmentServiceImpl.updateBySn(e);
						}else {
							e.setDeptId(deptId.intValue());
							e.setOtherName(getOtherName(dataBaseEquipmentList,"闸机",1));
							dataBaseEquipmentList.add(e);
							equipmentServiceImpl.insert(e);
						}
						break;
					}
				}
			}
		}else {
			for(int i=0;i<equipList.size();i++) {
				Equipment e = equipList.get(i);
				e.setDeptId(deptId.intValue());
				e.setOtherName("闸机"+(i+1));
				equipmentServiceImpl.insert(e);
			}
		}
	}
	
	private String getOtherName(List<Equipment> equipList,String deptName,int i) {
		boolean result = true;
		String otherName = deptName+i;
		for(Equipment equip : equipList) {
			if(equip.getOtherName().equals(otherName)) {
				result=false;
				break;
			}
		}
		if(result) {
			return otherName;
		}else {
			return getOtherName(equipList, deptName, i+1);
		}
	}
	
	private List<String> dataToString(List<Equipment> equipList ){
		List<String> snList = new ArrayList<String>();
		for(Equipment equip : equipList) {
			snList.add(equip.getEquipSn());
		}
		
		return snList;
	}
	
	/**
	 * 操纵设备
	 */
	@Override
	public void operateEquip(Equipment equip, String operateType) throws Exception {
		byte[] resultBytes = SocketUtil.sendMsgForEquipment(equip.getIp(), equip.getPort(), OperateUtil.controllEquipment(Integer.valueOf(operateType)));
		ReturnUtil.settingReturn(resultBytes);
	}
	
	/**
	 * 修改通讯密码
	 */
	@Override
	public void updatePassWord(Equipment equip) throws Exception {
		
		
	}
}
