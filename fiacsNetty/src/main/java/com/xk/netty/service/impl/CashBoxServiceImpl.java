package com.xk.netty.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fiacs.common.util.DateUtil;
import com.xk.netty.dao.CashBoxDao;
import com.xk.netty.entity.CashBoxEntity;
import com.xk.netty.service.CashBoxService;

@Service
public class CashBoxServiceImpl extends ServiceImpl<CashBoxDao, CashBoxEntity> implements CashBoxService{
	
	@Resource
	private CashBoxDao cashBoxDao;
	
	@Override
	public CashBoxEntity queryOne(String equipId) {
		return cashBoxDao.queryOne(equipId);
	}

	@Override
	public int updateSynStatus(Integer id) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime",DateUtil.getCurrentDate());
		map.put("id", id);
		return cashBoxDao.updateSynStatus(map);
	}
	
	/**
	 * 查询需同步款箱调拨数据
	 */
	@Override
	public Map<String, Object> queryCashBoxAllot(String equipId) {
		List<Map<String,Object>> mapList = cashBoxDao.queryCashBoxAllot(equipId);
		Map<String,Object> returnMap = new HashMap<>();
		List<String> downLoadIds = new ArrayList<>();
		if(mapList.size()>0) {
			List<String> cashBoxIds = new ArrayList<>();
			for(Map<String,Object> itemMap : mapList) {
				cashBoxIds.add(String.valueOf(itemMap.get("CashboxID")));
				downLoadIds.add(String.valueOf(itemMap.get("downLoadId")));
			}
			returnMap.put("Cashbox", cashBoxIds);
			returnMap.put("downLoadIdList", downLoadIds);
			Map<String,Object> mapOne = mapList.get(0);
			Set<String> keys = mapOne.keySet();
			Iterator<String> inter = keys.iterator();
			while(inter.hasNext()) {
				String kk = inter.next();
				returnMap.put(kk, mapOne.get(kk));
			}
			return returnMap;
		}
		return null;
	}

	@Override
	public int updateSynAllotStatus(List<String> id) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime", DateUtil.getCurrentDate());
		map.put("ids", id);
		return cashBoxDao.updateSynAllotStatus(map);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadCashBoxEvent(Map<String, Object> map, String equipId) {
		List<Map<String,Object>> mapList = (List<Map<String, Object>>) map.get("CashboxEventTable");
		for(Map<String,Object> seqMap : mapList) {
			Map<String,String> zMap = new HashMap<>();
			zMap.put("equipId", equipId);
			zMap.put("EventID", String.valueOf(seqMap.get("EventID")));
			zMap.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
			zMap.put("GroupID", String.valueOf(seqMap.get("GroupID")));
			zMap.put("TaskID", String.valueOf(seqMap.get("TaskID")));
			zMap.put("EventDate", String.valueOf(seqMap.get("EventDate")));
			cashBoxDao.insertCashBoxEvent(zMap);
			
			List<Map<String,String>> detailList = (List<Map<String, String>>) seqMap.get("cashBoxList");
			for(Map<String,String> detailMap : detailList) {
				detailMap.put("equipId", equipId);
				detailMap.put("EventID", String.valueOf(seqMap.get("EventID")));
				detailMap.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
				detailMap.put("EventDate", String.valueOf(seqMap.get("EventDate")));
				cashBoxDao.inserCashBoxEventDetail(detailMap);
			}
		}
		return 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadCashBoxOrderEvent(Map<String, Object> map, String equipId) {
		List<Map<String,Object>> mapList = (List<Map<String, Object>>) map.get("CashboxOrderTable");
		for(Map<String,Object> seqMap : mapList) {
			List<Map<String,String>> fMapList = (List<Map<String, String>>) seqMap.get("CashboxPacket");
			for(Map<String,String> fMap : fMapList) {
				fMap.put("equipId", equipId);
				fMap.put("OrderID", String.valueOf(seqMap.get("OrderID")));
				fMap.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
				fMap.put("OrderDate", String.valueOf(seqMap.get("OrderDate")));
				cashBoxDao.insertCashBoxOrder(fMap);
			}
		}
		return 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int uploadCashBoxReturnEvent(Map<String, Object> map, String equipId) {
		List<Map<String,Object>> mapList = (List<Map<String, Object>>) map.get("CashboxReturnTable");
		for(Map<String,Object> seqMap : mapList) {
			List<Map<String,String>> fMapList = (List<Map<String, String>>) seqMap.get("CashboxPacket");
			for(Map<String,String> fMap : fMapList) {
				fMap.put("equipId", equipId);
				fMap.put("ReturnID", String.valueOf(seqMap.get("ReturnID")));
				fMap.put("EquipSN", String.valueOf(seqMap.get("EquipSN")));
				fMap.put("ReturnDate", String.valueOf(seqMap.get("ReturnDate")));
				cashBoxDao.insertCashBoxReturn(fMap);
			}
		}
		return 0;
	}
}
