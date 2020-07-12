package com.xk.netty.service;

import java.util.List;
import java.util.Map;

import com.xk.netty.entity.CashBoxEntity;

public interface CashBoxService {
	
	/**
	 * 查询需要同步的款箱信息
	 * @param equipId
	 * @return
	 */
	CashBoxEntity queryOne(String equipId);
	
	int updateSynStatus(Integer id);
	
	/**
	 * 查询需要同步的款箱调拨信息 
	 * @param equipId
	 * @return
	 */
	Map<String,Object> queryCashBoxAllot(String equipId); 
	
	int updateSynAllotStatus(List<String> id);
	
	/**
	 * 上传款箱事件
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadCashBoxEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 上传款箱预约事件
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadCashBoxOrderEvent(Map<String,Object> map,String equipId);
	
	/**
	 * 上传款箱上缴事件
	 * @param map
	 * @param equipId
	 * @return
	 */
	int uploadCashBoxReturnEvent(Map<String,Object> map,String equipId);
}
