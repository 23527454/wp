package com.xk.netty.service;

import java.util.Map;

import com.xk.netty.entity.CarEntity;

public interface CarService {
	
	CarEntity queryOne(String equipId);
	
	int updateSynStatus(int downId);
	
	int insertCarArriveEvent(Map<String,Object> map,String equipId);
	
	CarEntity queryCarPhoto(String carId);
}
