package com.xk.netty.service;

import com.xk.netty.entity.DoorParamEntity;

import java.util.Map;

public interface DoorService {

	DoorParamEntity queryDoorParam(String equipId);
	
	Map<String,Object> queryDoorTimeZone(String equipId);
	
	int updateParamSynsStatus(int downLoadId);
	
	int updateTimeZoneSynsStatus(int downLoadId);
}
