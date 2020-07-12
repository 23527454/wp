package com.xk.netty.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fiacs.common.util.DateUtil;
import com.xk.netty.dao.TaskClassDao;
import com.xk.netty.entity.TaskClassEntity;
import com.xk.netty.service.TaskClassService;

@Service
public class TaskClassServiceImpl extends ServiceImpl<TaskClassDao, TaskClassEntity> implements TaskClassService{
	
	@Resource
	private TaskClassDao taskClassDao;
	
	@Override
	public TaskClassEntity queryOne(String equipId) {
		TaskClassEntity entity = taskClassDao.queryOne(equipId);
		if(entity==null) {
			return null;
		}
		entity.setCarNumList(taskClassDao.queryTaskCar(String.valueOf(entity.getTaskId())));
		entity.setFingerNumList(taskClassDao.queryTaskPerson(String.valueOf(entity.getTaskId())));
		return entity;
	}
	
	@Override
	public int updateSynStatus(int downId) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime", DateUtil.getCurrentDate());
		map.put("id", downId);
		return taskClassDao.updateSynStatus(map);
	}
	

}
