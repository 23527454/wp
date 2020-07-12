package cn.jeefast.modules.equipment.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.modules.equipment.dao.AlarmEntityDao;
import cn.jeefast.modules.equipment.entity.AlarmEntity;
import cn.jeefast.modules.equipment.service.AlarmEntityService;

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
		if(pageUtil.getRecords().size()>0) {
			alarmEntityDao.updateReadLog(map);
		}
		return pageUtil;
	}

}
