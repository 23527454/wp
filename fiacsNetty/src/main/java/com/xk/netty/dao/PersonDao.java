package com.xk.netty.dao;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xk.netty.entity.PersonEntity;

public interface PersonDao extends BaseMapper<PersonEntity>{
	
	PersonEntity queryOne(String equipId);
	
	int updateSynStatus(Map<String,Object> map);
	
	PersonEntity queryPersonPhoto(Map<String,String> map);
	
	PersonEntity queryFingerMode(String staffId);
	
	int updateZFingerMode(Map<String,Object> map);
	
	int updateFFingerMode(Map<String,Object> map);

	int updateCFingerMode(Map<String,Object> map);
	/**
	 * 更新专员的抓拍照片
	 * @param map
	 * @return
	 */
	int uploadSuperGoEventImag(Map<String,Object> map);
	
	/**
	 * 更新维保员照片
	 * @param map
	 * @return
	 */
	int uploadSafeGuardEventImag(Map<String,Object> map);
	//================押解员事件=====================
	int insertSuperGoEvent(Map<String,String> map);
	
	int insertinsertSuperGoImag(Map<String,String> map);
	
	//=================专员事件==========================
	int insertCommissionerEvent(Map<String,String> map);
	
	//=================维保员事件=========================
	int insertSafeGuardEvent(Map<String,Object> map);
	
	int insertSafeGuardEventDetail(Map<String,Object> map);
}
