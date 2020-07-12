package com.xk.netty.service;

import com.xk.netty.entity.InformationRelease;

public interface InformationReleaseService {
	
	InformationRelease queryOne(String equipId);
	
	int updateSynStatus(int downId);
	
}
