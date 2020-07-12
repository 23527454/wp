package com.fiacs.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;

public class EncodeToXmlUtil {
	
	private static String getBaseHead(String type,int seqNum,String code) {
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"uft-8\"?>\r");
		sb.append("<ProtocolRoot type=\""+type+"\" SeqNum=\"");
		sb.append(seqNum+1);
		sb.append("\" TotalCount=\"1\" CurrIndex=\"1\">");
		sb.append("<AuthorCode>"+code+"</AuthorCode>");
		return sb.toString();
	}
	
	/**
	 * personMap转xml
	 * @param map
	 * @param code
	 * @return
	 */
	public static String personMapToXml(Map<String,String> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.SYN_PERSON_TYPE,seqNum, code));
		sb.append("<PersonTable>");
		sb.append("<PersonInfo OperateType=\""+map.get("OperateType")+"\">");
		sb.append("<PersonID>"+map.get("PersonID")+"</PersonID>");
		sb.append("<Name>"+map.get("Name")+"</Name>");
		sb.append("<Sex>"+map.get("Sex")+"</Sex>");
		sb.append("<WorkNum>"+map.get("WorkNum")+"</WorkNum>");
		sb.append("<FingerID>"+map.get("FingerID")+"</FingerID>");
		sb.append("<FingerModelSize>"+map.get("FingerModelSize")+"</FingerModelSize>");
		sb.append("<PassWord>"+map.get("PassWord")+"</PassWord>");
		sb.append("<CoerceWord>"+map.get("CoerceWord")+"</CoerceWord>");
		//sb.append("<PassWord/>");
		sb.append("<CardNum>"+map.get("CardNum")+"</CardNum>");
		//sb.append("<CardNum/>");
		sb.append("<Validate>"+map.get("Validate")+"</Validate>");
		sb.append("<IDNum>"+map.get("IDNum")+"</IDNum>");
		sb.append("<WorkClass>"+map.get("WorkClass")+"</WorkClass>");
		sb.append("<PhotolSize>"+map.get("PhotolSize")+"</PhotolSize>");
		sb.append("</PersonInfo>");
		sb.append("</PersonTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * carMap转xml
	 * @param map
	 * @param code
	 * @return
	 */
	public static String carMapToXml(Map<String,String> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.SYN_CAR_TYPE,seqNum, code));
		sb.append("<CarTable>");
		sb.append("<CarInfo OperateType=\""+map.get("OperateType")+"\">");
		sb.append("<CarID>"+map.get("CarID")+"</CarID>");
		sb.append("<PlateNum>"+map.get("PlateNum")+"</PlateNum>");
		sb.append("<CarCard>"+map.get("CarCard")+"</CarCard>");
		sb.append("<CarColor>"+map.get("CarColor")+"</CarColor>");
		sb.append("<PhotolSize>"+map.get("PhotolSize")+"</PhotolSize>");
		sb.append("</CarInfo>");
		sb.append("</CarTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	public static String taskClassMapToXml(Map<String,Object> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer(); 
		sb.append(getBaseHead(Constants.SYN_TASK_CLASS,seqNum, code));
		sb.append("<TaskTable>");
		sb.append("<TaskInfo OperateType=\""+map.get("OperateType")+"\">");
		sb.append("<TaskID>"+map.get("TaskID")+"</TaskID>");
		sb.append("<GroupID>"+map.get("GroupID")+"</GroupID>");
		sb.append("<TaskClass>"+map.get("TaskClass")+"</TaskClass>");
		sb.append("<WorkPersonComfirm>"+map.get("WorkPersonComfirm")+"</WorkPersonComfirm>");
		sb.append("<CheckCarCard>"+map.get("CheckCarCard")+"</CheckCarCard>");
		sb.append("<CashboxComfirm>"+map.get("CashboxComfirm")+"</CashboxComfirm>");
		sb.append("<TaskValidate>"+map.get("TaskValidate")+"</TaskValidate>");
		sb.append("<WaitTimeout>"+map.get("WaitTimeout")+"</WaitTimeout>");
		sb.append("<SuperNum>"+map.get("SuperNum")+"</SuperNum>");
		sb.append("<InterNum>"+map.get("InterNum")+"</InterNum>");
		sb.append("<TaskDate>"+map.get("TaskDate")+"</TaskDate>");	
		
		sb.append("<Car dataType=\"list\">");
		for(String carNum : (List<String>)map.get("carList")) {
			sb.append("<Card>");
			sb.append(carNum);
			sb.append("</Card>");
		}
		sb.append("</Car>");
		
		sb.append("<SuperCarGo dataType=\"list\">");
		for(String fingerId : (List<String>)map.get("superCarGoList")) {
			sb.append("<FingerID>");
			sb.append(fingerId);
			sb.append("</FingerID>");
		}
		sb.append("</SuperCarGo>");
		sb.append("</TaskInfo>");
		sb.append("</TaskTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	/**
	 * 打包图片数据包
	 * @param map
	 * @param equipSn
	 * @param seqNum
	 * @param times 总分包数
	 * @param seq 当前包数
	 * @return
	 */
	public static String EncodeBinaryPacket(Map<String,Object> map,String equipSn,int seqNum,int times,int seq) {
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"uft-8\"?>");
		sb.append("<ProtocolRoot type=\""+Constants.BIG_DATA_TYPE+"\" SeqNum =\"");
		sb.append(seqNum+seq);
		sb.append("\" TotalCount=\""+times+"\" CurrIndex=\""+seq+"\">");
		sb.append("<AuthorCode>"+AuthorCodeUtil.generateCode(equipSn, seqNum+seq, Constants.COMMUNICATION_KEY_FACTOR)+"</AuthorCode>");
		sb.append("<OptionType>");
		sb.append(map.get("OptionType"));
		sb.append("</OptionType>");
		sb.append("<InfoType>");
		sb.append(map.get("InfoType"));
		sb.append("</InfoType>");
		sb.append("<Id>");
		sb.append(map.get("Id"));
		sb.append("</Id>");
		sb.append("<FingerID>");
		sb.append(map.get("FingerID"));
		sb.append("</FingerID>");
		sb.append("<data>");
		sb.append(map.get("data"));
		sb.append("</data>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 打包参数数据包
	 * @param map
	 * @param code
	 * @param seqNum
	 * @return
	 */
	public static String EncodeParamPacket(Map<String,Object> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.PARAM_SETTING_TYPE,seqNum, code));
		sb.append("<AlarmPos>");
		sb.append(map.get("AlarmPos"));
		sb.append("</AlarmPos>");
		sb.append("<ConnectPos>");
		sb.append(map.get("ConnectPos"));
		sb.append("</ConnectPos>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	/**
	 * cashBoxMap转xml
	 * @param map
	 * @param code
	 * @return
	 */
	public static String cashBoxMapToXml(Map<String,String> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.CASHBOX_INFO_TYPE,seqNum, code));
		sb.append("<CashboxTable>");
		sb.append("<CashboxInfo OperateType=\""+map.get("OperateType")+"\">");
		sb.append("<CashboxID>"+map.get("CashboxID")+"</CashboxID>");
		sb.append("<CashboxCode>"+map.get("CashboxCode")+"</CashboxCode>");
		sb.append("<CashboxCard>"+map.get("CashboxCard")+"</CashboxCard>");
		sb.append("<CashboxType>"+map.get("CashboxType")+"</CashboxType>");
		sb.append("<CashboxAddr>"+map.get("CashboxAddr")+"</CashboxAddr>");
		sb.append("</CashboxInfo>");
		sb.append("</CashboxTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}

	/**
	 * informationRelease转xml
	 * @param map
	 * @param code
	 * @return
	 */
	public static String informationReleaseMapToXml(Map<String,String> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.INFORMATION_RELEASE_TYPE,seqNum, code));
		sb.append("<MessageTable>");
		sb.append("<MessageInfo>");
		sb.append("<ID>"+map.get("ID")+"</ID>");
		sb.append("<Text>"+map.get("Text")+"</Text>");
		sb.append("<Startdate>"+map.get("Startdate")+"</Startdate>");
		sb.append("<Enddate>"+map.get("Enddate")+"</Enddate>");
		sb.append("</MessageInfo>");
		sb.append("</MessageTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	
	/**
	 * cashBoxMap转xml
	 * @param map 其中key:Cashbox为List<String>类型
	 * @param code
	 * @return
	 */
	public static String cashBoxAllotMapToXml(Map<String,Object> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.CASHBOX_ALLOT_TYPE,seqNum, code));
		sb.append("<CashboxAllotTable>");
		sb.append("<CashboxAllotInfo OperateType=\""+map.get("OperateType")+"\">");
		sb.append("<GroupID>"+map.get("GroupID")+"</GroupID>");
		sb.append("<CashboxAllotID>"+map.get("CashboxAllotID")+"</CashboxAllotID>");
		sb.append("<AllotType>"+map.get("AllotType")+"</AllotType>");
		sb.append("<AllotDate>"+map.get("AllotDate")+"</AllotDate>");
		
		@SuppressWarnings("unchecked")
		List<String> cashBoxIdList=(List<String>) map.get("Cashbox");
		sb.append("<Cashbox dataType=\"list\">");
		for(String cashBoxId : cashBoxIdList) {
			sb.append("<CashboxID>");
			sb.append(cashBoxId);
			sb.append("</CashboxID>");
		}
		sb.append("</Cashbox>");
		sb.append("</CashboxAllotInfo>");
		sb.append("</CashboxAllotTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 读取产品序列号xml生成
	 * @return
	 */
	public static String readEquipSn() {
		int seqNum=SeqnumUtil.getNextSeqNum();
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.READ_EQUIPSN_TYPE,seqNum, AuthorCodeUtil.generateCode(Constants.SAFE_EQUIP_SN, seqNum+1, Constants.COMMUNICATION_KEY_FACTOR)));
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 设置产品序列号
	 * @param equipSn
	 * @return
	 */
	public static String resetEquipSn(String equipSn) {
		int seqNum=SeqnumUtil.getNextSeqNum();
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.SETTING_EQUIPSN_TYPE,seqNum, AuthorCodeUtil.generateCode(Constants.SAFE_EQUIP_SN, seqNum+1, Constants.COMMUNICATION_KEY_FACTOR)));
		sb.append("<EquipSN>");
		sb.append(equipSn);
		sb.append("</EquipSN>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	
	}
	
	/**
	 * 搜索局域网设备
	 * @return
	 */
	public static String searchEquip() {
		int seqNum=SeqnumUtil.getNextSeqNum();
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.SEARCH_ALL_TYPE,seqNum, AuthorCodeUtil.generateCode(Constants.SEARCH_EQUIP_SN, seqNum+1, Constants.SEARCH_KEY_FACTOR)));
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	
	/**
	 * 时钟校对
	 * @param equipSn
	 * @param seqNum
	 * @return
	 */
	public static String EncodeTimePacket(String equipSn,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.CLOCK_CHECK_TYPE,seqNum, AuthorCodeUtil.generateCode(equipSn, seqNum+1, Constants.COMMUNICATION_KEY_FACTOR)));
		sb.append("<ConfigDate>");
		Date d = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		sb.append(sdf.format(d));
		sb.append("</ConfigDate>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 设备详情
	 * @param map
	 * @param seqNum
	 * @return
	 *//*
	public static String EncodeSetDeviceNetworkParamRequest(Map<String,String> map,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.MODIFY_EQUIP_TYPE,seqNum, AuthorCodeUtil.generateCode(map.get("EquipSN"), seqNum, Constants.COMMUNICATION_KEY_FACTOR)));
		sb.append("<EquipName>");
		sb.append(map.get("EquipName"));
		sb.append("</EquipName>");
		sb.append("<EquipSN>");
		sb.append(map.get("EquipSN"));
		sb.append("</EquipSN>");
		sb.append("<EquipIP>");
		sb.append(map.get("EquipIP"));
		sb.append("</EquipIP>");
		sb.append("<EquipPort>");
		sb.append(map.get("EquipPort"));
		sb.append("</EquipPort>");
		sb.append("<Gateway>");
		sb.append(map.get("Gateway"));
		sb.append("</Gateway>");
		sb.append("<SubnetMask>");
		sb.append(map.get("SubnetMask"));
		sb.append("</SubnetMask>");
		sb.append("<CenterIP>");
		sb.append(map.get("CenterIP"));
		sb.append("</CenterIP>");
		sb.append("<CenterPort>");
		sb.append(map.get("CenterPort"));
		sb.append("</CenterPort>");
		sb.append("<PrinterIP>");
		sb.append(map.get("PrinterIP"));
		sb.append("</PrinterIP>");
		sb.append("<PrinterPort>");
		sb.append(map.get("PrinterPort"));
		sb.append("</PrinterPort>");
		sb.append("<EquipType>");
		sb.append(map.get("EquipType"));
		sb.append("</EquipType>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}*/
	
	/**
	 * 门禁参数
	 * @param map
	 * @param seqNum
	 * @return
	 */
	public static String EncodeAccessParametersPacket(Map<String,Object> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.ACCESS_PARAM_TYPE,seqNum, code));
		sb.append("<AccessParaTable>");
		sb.append("<AccessParaInfo OperateType=\""+String.valueOf(map.get("AccessParametersSysStatus"))+"\">");
		sb.append("<DoorPos>");
		sb.append(String.valueOf(map.get("nDoor_pos")));
		sb.append("</DoorPos>");
		sb.append("<DoorRelayTime>");
		sb.append(map.get("nRelayactiontime"));
		sb.append("</DoorRelayTime>");
		sb.append("<DoorDelayTime>");
		sb.append(map.get("nDelayclosetime"));
		sb.append("</DoorDelayTime>");
		sb.append("<DoorOverTime>");
		sb.append(map.get("nAlarmtime"));
		sb.append("</DoorOverTime>");
		sb.append("<CombNum>");
		sb.append(map.get("nCombinationnumber"));
		sb.append("</CombNum>");
		sb.append("<CenterPermit>");
		sb.append(map.get("nCenterpermit"));
		sb.append("</CenterPermit>");
		sb.append("<TimeZoneNum>");
		sb.append(map.get("nTimeZonenumber"));
		sb.append("</TimeZoneNum>");
		sb.append("<OpenType>");
		sb.append(map.get("OpenType"));
		sb.append("</OpenType>");
		sb.append("</AccessParaInfo>");
		sb.append("</AccessParaTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 门禁时区信息
	 * @param map
	 * @param seqNum
	 * @return
	 */
	public static String EncodeDoorTimeZonePacket(Map<String,Object> map,String code,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.ACCESS_TIMEZONE_TYPE,seqNum, code));
		sb.append("<TimeZoneTable>");
		sb.append("<TimeZoneInfo OperateType=\""+map.get("DoorTimeZoneSysStatus")+"\">");
		sb.append("<DoorPos>");
		sb.append(map.get("nDoor_pos"));
		sb.append("</DoorPos>");
		sb.append("<TimeZoneNum>");
		sb.append(map.get("nTimeZonenumber"));
		sb.append("</TimeZoneNum>");
		sb.append("<WeekNum>");
		sb.append(map.get("nWeekNumber"));
		sb.append("</WeekNum>");
		sb.append("<TFstart1>");
		sb.append(map.get("szTimeframestart1"));
		sb.append("</TFstart1>");
		sb.append("<TFend1>");
		sb.append(map.get("szTimeframeend1"));
		sb.append("</TFend1>");
		sb.append("<TFstart2>");
		sb.append(map.get("szTimeframestart2"));
		sb.append("</TFstart2>");
		sb.append("<TFend2>");
		sb.append(map.get("szTimeframeend2"));
		sb.append("</TFend2>");
		sb.append("<TFstart3>");
		sb.append(map.get("szTimeframestart3"));
		sb.append("</TFstart3>");
		sb.append("<TFend3>");
		sb.append(map.get("szTimeframeend3"));
		sb.append("</TFend3>");
		sb.append("<TFstart4>");
		sb.append(map.get("szTimeframestart4"));
		sb.append("</TFstart4>");
		sb.append("<TFend4>");
		sb.append(map.get("szTimeframeend4"));
		sb.append("</TFend4>");
		sb.append("</TimeZoneInfo>");
		sb.append("</TimeZoneTable>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 修改ip等信息
	 * @param map
	 * @param seqNum
	 * @return
	 */
	public static String modifyEquip(Map<String,Object> map,int seqNum) {
		StringBuffer sb=new StringBuffer();
		sb.append(getBaseHead(Constants.MODIFY_EQUIP_TYPE,seqNum, AuthorCodeUtil.generateCode(String.valueOf(map.get("EquipSN")), seqNum+1, Constants.SEARCH_KEY_FACTOR)));
		sb.append("<EquipName>");
		sb.append(map.get("EquipName"));
		sb.append("</EquipName>");
		sb.append("<EquipSN>");
		sb.append(map.get("EquipSN"));
		sb.append("</EquipSN>");
		sb.append("<EquipIP>");
		sb.append(map.get("EquipIP"));
		sb.append("</EquipIP>");
		sb.append("<EquipPort>");
		sb.append(map.get("EquipPort"));
		sb.append("</EquipPort>");
		sb.append("<Gateway>");
		sb.append(map.get("Gateway"));
		sb.append("</Gateway>");
		sb.append("<SubnetMask>");
		sb.append(map.get("SubnetMask"));
		sb.append("</SubnetMask>");
		sb.append("<CenterIP>");
		sb.append(map.get("CenterIP"));
		sb.append("</CenterIP>");
		sb.append("<CenterPort>");
		sb.append(map.get("CenterPort"));
		sb.append("</CenterPort>");
		sb.append("<PrinterIP>");
		sb.append(map.get("PrinterIP"));
		sb.append("</PrinterIP>");
		sb.append("<PrinterPort>");
		sb.append(map.get("PrinterPort"));
		sb.append("</PrinterPort>");
		sb.append("<EquipType>");
		sb.append(map.get("EquipType"));
		sb.append("</EquipType>");
		sb.append("<SocketIP>");
		sb.append(map.get("SocketIP"));
		sb.append("</SocketIP>");
		sb.append("<SocketPort>");
		sb.append(map.get("SocketPort"));
		sb.append("</SocketPort>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 恢复缺省通讯秘钥
	 * @param seqNum
	 * @param authcode
	 * @return
	 */
	public static String restoreDefaultCommPwd(String authcode,int seqNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.RESET_PASSWORD_TYPE,seqNum, authcode));
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 修改通信秘钥
	 * @param map
	 * @param seqNum
	 * @return
	 */
	public static String modifyCommPwd(Map<String,Object> map,int seqNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.MODIFY_PASSWORD_TYPE,seqNum, AuthorCodeUtil.generateCode(String.valueOf(map.get("EquipSN")), seqNum+1, Constants.COMMUNICATION_KEY_FACTOR_OLD)));
		try {
			sb.append("<OldKeys>");
			sb.append(DesUtil.encrypt(Constants.COMMUNICATION_KEY_FACTOR_OLD,Constants.SECRET_DES_FACTOR));
			sb.append("</OldKeys>");
			sb.append("<NewKeys>");
			sb.append(DesUtil.encrypt(Constants.COMMUNICATION_KEY_FACTOR,Constants.SECRET_DES_FACTOR));
			sb.append("</NewKeys>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("<SysName>");
		sb.append(map.get("SysName"));
		//sb.append("银行出入口智能管控系统");
		sb.append("</SysName>");
		sb.append("<SuperNum>");
		sb.append(map.get("SuperNum"));
		sb.append("</SuperNum>");
		sb.append("<InterNum>");
		sb.append(map.get("InterNum"));
		sb.append("</InterNum>");
		sb.append("<EquipType>");
		sb.append(map.get("EquipType"));
		sb.append("</EquipType>");
		sb.append("<EquipSN>");
		sb.append(map.get("EquipSN"));
		sb.append("</EquipSN>");
		sb.append("<SocketIP>");
		sb.append(map.get("SocketIP"));
		sb.append("</SocketIP>");
		sb.append("<SocketPort>");
		sb.append(map.get("SocketPort"));
		sb.append("</SocketPort>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 
	 * @param resetType 0 重启app  1重启系统
	 * @param seqNum
	 * @param equipSn 设备sn码
	 * @param ip 设备地址
	 * @param port 设备端口
	 * @return
	 */
	public static String resetEquip(int resetType,int seqNum,String equipSn,String ip,String port) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.RESTART_TYPE,seqNum, AuthorCodeUtil.generateCode(equipSn, seqNum+1, Constants.SEARCH_KEY_FACTOR)));
		sb.append("<ResetType>");
		sb.append(resetType);
		sb.append("</ResetType>");
		sb.append("<EquipSN>");
		sb.append(equipSn);
		sb.append("</EquipSN>");
		if(!StringUtils.isEmpty(ip)) {
			sb.append("<SocketIP>");
			sb.append(ip);
			sb.append("</SocketIP>");
		}
		if(!StringUtils.isEmpty(port)) {
			sb.append("<SocketPort>");
			sb.append(port);
			sb.append("</SocketPort>");
		}
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}

	public static String controlDoor(Map<String,Object> map,int seqNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.CONTROL_DOOR_TYPE,seqNum, AuthorCodeUtil.generateCode(String.valueOf(map.get("EquipSN")), seqNum+1, Constants.COMMUNICATION_KEY_FACTOR_OLD)));
		sb.append("<ResetType>");
		sb.append(Integer.valueOf(String.valueOf(map.get("ResetType"))));
		sb.append("</ResetType>");
		sb.append("<EquipSN>");
		sb.append(String.valueOf(map.get("EquipSN")));
		sb.append("</EquipSN>");
		sb.append("<SocketIP>");
		sb.append(String.valueOf(map.get("SocketIP")));
		sb.append("</SocketIP>");
		sb.append("<SocketPort>");
		sb.append(Integer.valueOf(String.valueOf(map.get("SocketPort"))));
		sb.append("</SocketPort>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}

	/**
	 * 时间参数配置
	 * @param connectPos
	 * @param AlarmPos
	 * @param code
	 * @param seqNum
	 * @return
	 */
	public static String encodeEventParam(int connectPos,int AlarmPos,String code,int seqNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.PARAM_SETTING_TYPE,seqNum, code));
		sb.append("<ConnectPos>");
		sb.append(connectPos);
		sb.append("</ConnectPos>");
		sb.append("<AlarmPos>");
		sb.append(AlarmPos);
		sb.append("</AlarmPos>");
		sb.append("</ProtocolRoot>");
		return sb.toString();
	}
	
	/**
	 * 应答
	 * @param seqNum
	 * @param result
	 * @return
	 */
	public static byte[] returnResult(int seqNum,int result,String equipSn) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.RESPONSE_TYPE,seqNum, AuthorCodeUtil.generateCode(equipSn, seqNum+1, Constants.COMMUNICATION_KEY_FACTOR)));
		sb.append("<ResultCode>");
		sb.append(result);
		sb.append("</ResultCode>");
		sb.append("</ProtocolRoot>");
		return sb.toString().getBytes();
	}
	
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.REQUEST_SYN_TYPE,1, AuthorCodeUtil.generateCode("2222222222222222", 1+1, Constants.COMMUNICATION_KEY_FACTOR)));
		sb.append("</ProtocolRoot>");
		System.out.println(ByteUtil.bytesToHexString(sb.toString().getBytes()));
		
		 sb = new StringBuffer();
		sb.append(getBaseHead(Constants.VERIFY_IDENTIFY,1, AuthorCodeUtil.generateCode("2222222222222222", 1+1, Constants.COMMUNICATION_KEY_FACTOR)));
		sb.append("<EquipSN>2222222222222222</EquipSN>");
		sb.append("<EquipName>科技园支行 </EquipName>");
		sb.append("</ProtocolRoot>");
		System.out.println(ByteUtil.bytesToHexString(sb.toString().getBytes()));
	}
	
	/**
	 * 修改设备ip等信息的应答
	 * @param seqNum
	 * @param result
	 * @return
	 */
	public static byte[] returnResultForSearch(int seqNum,int result,String equipSn) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseHead(Constants.RESPONSE_TYPE,seqNum, AuthorCodeUtil.generateCode(equipSn, seqNum+1, Constants.SEARCH_KEY_FACTOR)));
		sb.append("<ResultCode>");
		sb.append(result);
		sb.append("</ResultCode>");
		sb.append("</ProtocolRoot>");
		return sb.toString().getBytes();
	}
}
