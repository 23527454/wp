package com.xk.netty.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.xk.netty.entity.DoorParamEntity;
import org.springframework.stereotype.Service;

import com.fiacs.common.util.DateUtil;
import com.xk.netty.dao.DoorDao;
import com.xk.netty.service.DoorService;

@Service
public class DoorServiceImpl implements DoorService{
	
	@Resource
	private DoorDao doorDao;

	@Override
	public DoorParamEntity queryDoorParam(String equipId) {
		return doorDao.queryDoorParam(equipId);
	}

	@Override
	public Map<String, Object> queryDoorTimeZone(String equipId) {
		return doorDao.queryDoorTimeZone(equipId);
	}

	@Override
	public int updateParamSynsStatus(int downLoadId) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime", DateUtil.getCurrentDate());
		map.put("id", downLoadId);
		return doorDao.updateParamSynsStatus(map);
	}

	@Override
	public int updateTimeZoneSynsStatus(int downLoadId) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime", DateUtil.getCurrentDate());
		map.put("id", downLoadId);
		return doorDao.updateTimeZoneSynsStatus(map);
	}
}
