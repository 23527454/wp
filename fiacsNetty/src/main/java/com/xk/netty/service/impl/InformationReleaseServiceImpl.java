package com.xk.netty.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fiacs.common.util.DateUtil;
import com.xk.netty.dao.InformationReleaseDao;
import com.xk.netty.entity.InformationRelease;
import com.xk.netty.service.InformationReleaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class InformationReleaseServiceImpl extends ServiceImpl<InformationReleaseDao, InformationRelease> implements InformationReleaseService {
	
	@Resource
	private InformationReleaseDao informationReleaseDao;
	
	@Override
	public InformationRelease queryOne(String equipId) {
		return informationReleaseDao.queryOne(equipId);
	}
	
	@Override
	public int updateSynStatus(int downId) {
		Map<String,Object> map = new HashMap<>();
		map.put("downTime", DateUtil.getCurrentDate());
		map.put("id", downId);
		return informationReleaseDao.update(map);
	}
	

}
