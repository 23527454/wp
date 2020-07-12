package com.xk.netty.dao;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xk.netty.entity.DoorParamEntity;

public interface DoorDao extends BaseMapper<Object>{

	DoorParamEntity queryDoorParam(String equipId);
	
	int updateParamSynsStatus(Map<String,Object> map);
	
	Map<String,Object> queryDoorTimeZone(String equipId);
	
	int updateTimeZoneSynsStatus(Map<String,Object> map);
}
