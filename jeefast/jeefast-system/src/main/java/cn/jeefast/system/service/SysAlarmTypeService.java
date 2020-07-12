package cn.jeefast.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.system.entity.SysAlarmType;

public interface SysAlarmTypeService extends IService<SysAlarmType>{
	
	List<SysAlarmType> queryList();
	
	List<Long> queryAlarmIdByUserId(String userId);
	
	void saveOrUpdate(String userId,List<Long> alarmIds);
	
	void deleteByUserId(String userId);
}
