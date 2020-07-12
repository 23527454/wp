package cn.jeefast.system.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.jeefast.system.entity.SysAlarmType;

public interface SysAlarmTypeDao extends BaseMapper<SysAlarmType>{
	
	List<SysAlarmType> queryList();
	
	List<Long> queryAlarmIdByUserId(String userId);
	
	void deleteByUserId(String userId);
	
	void save(Map<String,Object> map);
}
