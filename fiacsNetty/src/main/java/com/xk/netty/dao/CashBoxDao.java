package com.xk.netty.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xk.netty.entity.CashBoxEntity;

public interface CashBoxDao extends BaseMapper<CashBoxEntity>{
	
	/**
	 * 查询需要同步的款箱信息
	 * @param equipId
	 * @return
	 */
	CashBoxEntity queryOne(String equipId);
	
	int updateSynStatus(Map<String,Object> map);
	
	//==================款箱调拨==============================
	/**
	 * 查询需要同步的款箱调拨信息 
	 * @param equipId
	 * @return
	 */
	List<Map<String,Object>> queryCashBoxAllot(String equipId); 
	
	int updateSynAllotStatus(Map<String,Object> map);
	
	//===================款箱事件上传============================
	int insertCashBoxEvent(Map<String,String> map);
	
	int inserCashBoxEventDetail(Map<String,String> map);
	
	//================款箱预约===============================
	int insertCashBoxOrder(Map<String,String> map);
	
	//==================款箱上缴=============================
	int insertCashBoxReturn(Map<String,String> map);
}
