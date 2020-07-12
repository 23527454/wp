package com.xk.netty.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fiacs.common.util.DateUtil;
import com.xk.netty.dao.CarDao;
import com.xk.netty.entity.CarEntity;
import com.xk.netty.service.CarService;

@Service
public class CarServiceImpl extends ServiceImpl<CarDao, CarEntity> implements CarService{
	
	@Resource
	private CarDao carDao;
	
	@Override
	public CarEntity queryOne(String equipId) {
		return carDao.queryOne(equipId);
	}
	
	@Override
	public int updateSynStatus(int downId) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime",DateUtil.getCurrentDate());
		map.put("id", downId);
		return carDao.updateSynStatus(map);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public int insertCarArriveEvent(Map<String, Object> map, String equipId) {
		List<Map<String,String>> mapList = (List<Map<String, String>>) map.get("CarEventTable");
		for(Map<String,String> seqMap : mapList) {
			seqMap.put("equipId", equipId);
			map.put("EventID",seqMap.get("EventID"));
			map.put("CarID",seqMap.get("CarID"));
			map.put("EventDate",seqMap.get("EventDate"));
			carDao.insertCarArriveEvent(seqMap);
		}
		return 0;
	}
	
	@Override
	public CarEntity queryCarPhoto(String carId) {
		return carDao.queryCarPhoto(carId);
	}

}
