package com.xk.netty.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fiacs.common.util.ByteUtil;
import com.fiacs.common.util.DecodeToMapUtil;
import com.fiacs.common.util.EncodeToXmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiacs.common.util.Encodes;
import com.xk.netty.dao.UploadInfoDao;
import com.xk.netty.service.UploadInfoService;

@Service
public class UploadInfoServiceImpl implements UploadInfoService {

	private static Logger log = LoggerFactory.getLogger(UploadInfoServiceImpl.class);

	@Resource
	private UploadInfoDao uploadInfoDao;

	@Override
	public Map<String, Object> queryEquipBySn(String equipSn) {
		return uploadInfoDao.queryEquipBySn(equipSn);
	}
	
	@Override
	public int updateEquipStatusBySn(String equipSn, int status) {
		Map<String,Object> map = new HashMap<>();
		map.put("equipSn", equipSn);
		map.put("status", status);
		return uploadInfoDao.updateEquipStatusBySn(map);
	}

	public static void main(String[] args){
		String xml="<ProtocolRoot type=\"4\" TotalCount=\"3\" CurrIndex=\"1\" SeqNum=\"667134153\" ><AuthorCode>2388F4EADFAC547E</AuthorCode><ConnectEventTable><ConnectEventInfo><CarID>2</CarID><CardNum>16777525</CardNum><CashboxPacketID>33</CashboxPacketID><EquipSN>1001200102100001</EquipSN><EventDate>2019-10-19 22:06:57</EventDate><EventID>33</EventID><EventNum>33</EventNum><GroupID>0</GroupID><SuperCarGo dataType=\"list\"><SuperCarGoID FingerID=\"110020001\"  PersonID=\"3\" PhotoSize=\"7861\" AuthorType=\"1\"/><SuperCarGoID FingerID=\"110020002\"  PersonID=\"4\" PhotoSize=\"7566\" AuthorType=\"1\"/></SuperCarGo><TaskClass>1</TaskClass><TaskID>0</TaskID><WorkPerson dataType=\"list\"><WorkPersonID FingerID=\"210020002\" PersonID=\"2\" /></WorkPerson></ConnectEventInfo><ConnectEventInfo><CarID>2</CarID><CardNum>16777525</CardNum><CashboxPacketID>34</CashboxPacketID><EquipSN>1001200102100001</EquipSN><EventDate>2019-10-19 22:12:39</EventDate><EventID>34</EventID><EventNum>33</EventNum><GroupID>0</GroupID><SuperCarGo dataType=\"list\"><SuperCarGoID FingerID=\"110020001\"  PersonID=\"3\" PhotoSize=\"7529\" AuthorType=\"1\"/><SuperCarGoID FingerID=\"110020002\"  PersonID=\"4\" PhotoSize=\"7664\" AuthorType=\"1\"/></SuperCarGo><TaskClass>1</TaskClass><TaskID>0</TaskID><WorkPerson dataType=\"list\"><WorkPersonID FingerID=\"210020002\" PersonID=\"2\" /></WorkPerson></ConnectEventInfo></ConnectEventTable></ProtocolRoot>";
		//Map<String,Object> map = DecodeToMapUtil.xmlToMap(xml);
		//new UploadInfoServiceImpl().uploadConnectEvent(map,"");
		System.out.println(ByteUtil.bytesToHexString(xml.getBytes()).substring(0));
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadConnectEvent(Map<String, Object> map, String equipId) {
			List<Map<String, Object>> connectEventMapList = (List<Map<String, Object>>) map.get("ConnectEventTable");
			Map<String, String> countMap = new HashMap<>();
			countMap.put("EventID", String.valueOf(connectEventMapList.get(0).get("EventID")));
			countMap.put("equipId", equipId);
			countMap.put("EventDate", String.valueOf(connectEventMapList.get(0).get("EventDate")));
			countMap.put("EquipSN", String.valueOf(connectEventMapList.get(0).get("EquipSN")));
			map.put("EventID",countMap.get("EventID"));
			map.put("EventDate",countMap.get("EventDate"));
			if (uploadInfoDao.countConnectEvent(countMap) > 0) {
				log.error("检测到系统中有此交接事件【设备号" + countMap.get("EquipSN") + ",记录号" + countMap.get("EventID") + "】");
				return 4;
			} else {
				for (Map<String, Object> seqMap : connectEventMapList) {
					// 存放记录
					Map<String, String> eventMap = new HashMap<>();
					eventMap.put("EventID", String.valueOf(seqMap.get("EventID")));
					eventMap.put("equipId", equipId);
					eventMap.put("TaskID", String.valueOf(seqMap.get("TaskID")));
					eventMap.put("TaskClass", String.valueOf(seqMap.get("TaskClass")));
					eventMap.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
					eventMap.put("EventDate", String.valueOf(seqMap.get("EventDate")));
					eventMap.put("CarID", String.valueOf(seqMap.get("CarID")));
					eventMap.put("CashboxEventID", String.valueOf(seqMap.get("CashboxPacketID")));
					eventMap.put("CardNum", String.valueOf(seqMap.get("CardNum")));
					eventMap.put("GroupID", String.valueOf(seqMap.get("GroupID")));
					uploadInfoDao.insertConnectEvent(eventMap);

					// 存放抓拍记录
					List<Map<String, String>> photoMapList = (List<Map<String, String>>) seqMap.get("superCarGoList");
					for (Map<String, String> photoMap : photoMapList) {
						photoMap.put("equipId", equipId);
						photoMap.put("EventID", String.valueOf(seqMap.get("EventID")));
						photoMap.put("EventDate", String.valueOf(seqMap.get("EventDate")));
						//add 2019-09-18 添加认证类型 需要兼容老设备 所以此值可能没有 需要判断
						if(photoMap.containsKey("AuthorType")&&photoMap.get("AuthorType")!=null){
							photoMap.put("AuthorType", String.valueOf(photoMap.get("AuthorType")));
						}else{
							log.info("押款员抓拍信息：添加默认值AuthorType");
							photoMap.put("AuthorType", "1");
						}
						uploadInfoDao.insertConnectEventPhoto(photoMap);
					}

					// 专员事件
					List<Map<String, String>> commissionMapList = (List<Map<String, String>>) seqMap
							.get("workPersonList");
					for (Map<String, String> commissionMap : commissionMapList) {
						commissionMap.put("equipId", equipId);
						commissionMap.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
						commissionMap.put("EventID", String.valueOf(seqMap.get("EventID")));
						commissionMap.put("EventDate", String.valueOf(seqMap.get("EventDate")));
						//add 2019-09-18 添加认证类型
						if(commissionMap.containsKey("AuthorType")&&commissionMap.get("AuthorType")!=null){
							commissionMap.put("AuthorType", String.valueOf(commissionMap.get("AuthorType")));
						}else{
							commissionMap.put("AuthorType", "1");
						}
						uploadInfoDao.insertConnectWorPerson(commissionMap);
					}
				}
				return 0;
			}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadAlarmEvent(Map<String, Object> map, String equipId) {
			List<Map<String, Object>> alarmEventMapList = (List<Map<String, Object>>) map.get("AlarmTable");
			Map<String, String> countMap = new HashMap<>();
			countMap.put("EventID", String.valueOf(alarmEventMapList.get(0).get("EventID")));
			countMap.put("equipId", equipId);
			countMap.put("EventDate", String.valueOf(alarmEventMapList.get(0).get("EventDate")));
			countMap.put("EquipSN", String.valueOf(alarmEventMapList.get(0).get("EquipSN")));
			map.put("EventID",countMap.get("EventID"));
			map.put("EventDate",countMap.get("EventDate"));
			if (uploadInfoDao.countAlarmEvent(countMap) > 0) {
				log.error("检测到系统中有此异常警报事件【设备号" + countMap.get("EquipSN") + ",记录号" + countMap.get("EventID") + "】");
				return 4;
			} else {
				for (Map<String, Object> seqMap : alarmEventMapList) {
					Map<String, String> eventMap = new HashMap<>();
					eventMap.put("EventID", String.valueOf(seqMap.get("EventID")));
					eventMap.put("equipId", equipId);
					eventMap.put("TaskID", String.valueOf(seqMap.get("TaskID")));
					eventMap.put("EventClass", String.valueOf(seqMap.get("EventClass")));
					eventMap.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
					eventMap.put("EventDate", String.valueOf(seqMap.get("EventDate")));
					eventMap.put("GroupID", String.valueOf(seqMap.get("GroupID")));
					uploadInfoDao.insertAlarmEvent(eventMap);
					uploadInfoDao.insertAlarmEventDetail(eventMap);
					List<Map<String, String>> cashBoxMapList = (List<Map<String, String>>) seqMap.get("cashBoxList");
					for (Map<String, String> cashBox : cashBoxMapList) {
						cashBox.put("equipId", equipId);
						cashBox.put("EventID", String.valueOf(seqMap.get("EventID")));
						cashBox.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
						cashBox.put("EventDate", String.valueOf(seqMap.get("EventDate")));
						uploadInfoDao.insertAlarmEventOfCashBox(cashBox);
					}
				}
				return 0;
			}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadAccessEvent(Map<String, Object> map, String equipId) {
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) map.get("AccessEventTable");
		for(Map<String,Object> seqMap : mapList) {
			map.put("EventID",seqMap.get("EventID"));
			map.put("PersonID",seqMap.get("PersonID"));
			map.put("EventDate",seqMap.get("EventDate"));
			seqMap.put("equipId", equipId);
			if(!seqMap.containsKey("AuthorType")){
				seqMap.put("AuthorType","1");
			}
			uploadInfoDao.insertAccessEvent(seqMap);
			uploadInfoDao.insertAccessEventDetail(seqMap);
		}
		return 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadAccessAlarmEvent(Map<String, Object> map, String equipId) {
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) map.get("AccessAlarmEventTable");
		for(Map<String,Object> seqMap : mapList) {
			map.put("EventID",seqMap.get("EventID"));
			map.put("PersonID",seqMap.get("PersonID"));
			map.put("EventDate",seqMap.get("EventDate"));
			seqMap.put("equipId", equipId);
			uploadInfoDao.insertAccessAlarmEvent(seqMap);
			uploadInfoDao.insertAccessAlarmEventDetail(seqMap);
		}
		return 0;
	}
	
	@Override
	public int uploadAlarmEventImag(Map<String, Object> map, String equipId) {
		map.put("equipId", equipId);
		byte[] photo = Encodes.decodeBase64(String.valueOf(map.get("data")));
		map.put("photo", photo);
		map.put("photoSize", photo.length);
		return uploadInfoDao.uploadAlarmEventImag(map);
	}
	
	@Override
	public int uploadAccessEventImag(Map<String, Object> map, String equipId) {
		map.put("equipId", equipId);
		byte[] photo = Encodes.decodeBase64(String.valueOf(map.get("data")));
		map.put("photo", photo);
		map.put("photoSize", photo.length);
		return uploadInfoDao.uploadAccessEventImag(map);
	}
	
	@Override
	public int uploadAccessAlarmEventImag(Map<String, Object> map, String equipId) {
		map.put("equipId", equipId);
		byte[] photo = Encodes.decodeBase64(String.valueOf(map.get("data")));
		map.put("photo", photo);
		map.put("photoSize", photo.length);
		return uploadInfoDao.uploadAccessAlarmEventImag(map);
	}
	
	@Override
	public int queryIsAllowSuperDogCode() {
		return uploadInfoDao.queryIsAllowSuperDogCode();
	}
}
