package com.xk.netty.service;

import com.xk.netty.entity.TaskClassEntity;

public interface TaskClassService {
	
	TaskClassEntity queryOne(String equipId);
	
	int updateSynStatus(int downId);
	
}
