package com.xk.netty.dao;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xk.netty.entity.CarEntity;

public interface CarDao extends BaseMapper<CarEntity>{
	/**
	 * 查询需要同步的车辆数据  一条
	 * @return
	 */
	CarEntity queryOne(String equipId);
	
	CarEntity queryCarPhoto(String carId);
	/**
	 * 同步成功则更新同步标志
	 * @param map
	 * @return
	 */
	int updateSynStatus(Map<String,Object> map);
	
	/**
	 * 查询车辆达到事件
	 * @param map
	 * @return
	 */
	int insertCarArriveEvent(Map<String,String> map);
}
