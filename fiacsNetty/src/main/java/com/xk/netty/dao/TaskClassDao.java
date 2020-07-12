package com.xk.netty.dao;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xk.netty.entity.TaskClassEntity;

public interface TaskClassDao extends BaseMapper<TaskClassEntity>{
	
	TaskClassEntity queryOne(String equipId);
	
	List<String> queryTaskCar(String taskId);
	
	List<String> queryTaskPerson(String taskId);
	
	int updateSynStatus(Map<String,Object> map);
}
