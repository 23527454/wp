package cn.jeefast.modules.equipment.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.modules.equipment.entity.AlarmEntity;

public interface AlarmEntityDao extends BaseMapper<AlarmEntity>{
	
	List<AlarmEntity> selectPageList(Page<AlarmEntity> page, Map<String, Object> map);
	//更新状态为已读
	void updateReadLog(Map<String, Object> map);
}
