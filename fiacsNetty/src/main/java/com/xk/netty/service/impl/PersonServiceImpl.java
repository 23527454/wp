package com.xk.netty.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fiacs.common.util.DateUtil;
import com.fiacs.common.util.Encodes;
import com.xk.netty.dao.PersonDao;
import com.xk.netty.entity.PersonEntity;
import com.xk.netty.service.PersonService;

@Service
public class PersonServiceImpl extends ServiceImpl<PersonDao, PersonEntity> implements PersonService{
	@Resource
	private PersonDao personDao;
	
	@Override
	public PersonEntity queryOne(String equipId) {
		return personDao.queryOne(equipId);
	}
	
	@Override
	public int updateSynStatus(int downLoadId) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime",DateUtil.getCurrentDate());
		map.put("id", downLoadId);
		return personDao.updateSynStatus(map);
	}
	
	@Override
	public PersonEntity queryFingerMode(String staffId) {
		return personDao.queryFingerMode(staffId);
	}
	
	@Override
	public PersonEntity queryPersonPhoto(String staffId,String imageType,String orderBy)
	{
		Map<String,String> map = new HashMap<>();

		map.put("staffId",staffId);
		map.put("imageType",imageType);
		map.put("orderBy",orderBy);

		return personDao.queryPersonPhoto(map);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadSuperGoEvent(Map<String, Object> map, String equipId) {
		List<Map<String,String>> mapList = (List<Map<String, String>>) map.get("SuperCarGoEventTable");
		for(Map<String,String> seqMap : mapList) {
			seqMap.put("equipId", equipId);
			if(!seqMap.containsKey("AuthorType")){
				seqMap.put("AuthorType","1");
			}
			personDao.insertSuperGoEvent(seqMap);
			personDao.insertinsertSuperGoImag(seqMap);
		}
		return 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadCommissionerEvent(Map<String, Object> map, String equipId) {
		List<Map<String,String>> mapList = (List<Map<String, String>>) map.get("WorkPersonEventTable");
		for(Map<String,String> seqMap : mapList) {
			seqMap.put("equipId", equipId);
			if(!seqMap.containsKey("AuthorType")){
				seqMap.put("AuthorType","1");
			}
			personDao.insertCommissionerEvent(seqMap);
		}
		return 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadSafeGuardEvent(Map<String, Object> map, String equipId) {
		List<Map<String,Object>> mapList = (List<Map<String, Object>>) map.get("SafeguardEventTable");
		for(Map<String,Object> seqMap : mapList) {
			map.put("EventID",seqMap.get("EventID"));
			map.put("PersonID",seqMap.get("PersonID"));
			map.put("EventDate",seqMap.get("EventDate"));
			seqMap.put("equipId", equipId);
			if(!seqMap.containsKey("AuthorType")){
				seqMap.put("AuthorType","1");
			}
			personDao.insertSafeGuardEvent(seqMap);
			personDao.insertSafeGuardEventDetail(seqMap);
		}
		return 0;
	}
	
	@Override
	public int uploadSuperGoEventImag(Map<String, Object> map, String equipId) {
		map.put("equipId", equipId);
		byte[] photo = Encodes.decodeBase64(String.valueOf(map.get("data")));
		map.put("photo", photo);
		map.put("photoSize", photo.length);
		return personDao.uploadSuperGoEventImag(map);
	}
	
	@Override
	public int uploadSafeGuardEventImag(Map<String, Object> map, String equipId) {
		map.put("equipId", equipId);
		byte[] photo = Encodes.decodeBase64(String.valueOf(map.get("data")));
		map.put("photo", photo);
		map.put("photoSize", photo.length);
		return personDao.uploadSafeGuardEventImag(map);
	}
	
	@Override
	public int updateFingerModeImag(Map<String, Object> map, String equipId) {
		Map<String,Object> opeMap = new HashMap<>();
		opeMap.put("staffId", map.get("Id"));
		byte[] fingerPhoto = Encodes.decodeBase64(String.valueOf(map.get("data")));
		opeMap.put("fingerMode", fingerPhoto);
		int fingerId = Integer.valueOf(String.valueOf(map.get("FingerID")));
		if (fingerId % 100000000 > 30000000) {
			//胁迫指纹  2019-9-18 add
			return personDao.updateCFingerMode(opeMap);
		}
		else if (fingerId % 100000000 > 20000000) {
			return personDao.updateFFingerMode(opeMap);
		} else {
			return personDao.updateZFingerMode(opeMap);
		}
	}
}
