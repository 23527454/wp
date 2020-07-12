package cn.jeefast.rest.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.rest.dao.AlarmEntityDao;
import cn.jeefast.rest.entity.AlarmEntity;
import cn.jeefast.rest.service.AlarmEntityService;

@Service
public class AlarmEntityServiceImpl extends ServiceImpl<AlarmEntityDao, AlarmEntity> implements AlarmEntityService{
	@Autowired
	private AlarmEntityDao alarmEntityDao;
	
	@Override
	@Transactional
	public Page<AlarmEntity> queryPageList(Page<AlarmEntity> pageUtil, Map<String, Object> map) {
		pageUtil.setRecords(alarmEntityDao.selectPageList(pageUtil, map));
		//更新读取状态为已读
		map.put("alarmList", pageUtil.getRecords());
		alarmEntityDao.updateReadLog(map);
		return pageUtil;
	}

}
