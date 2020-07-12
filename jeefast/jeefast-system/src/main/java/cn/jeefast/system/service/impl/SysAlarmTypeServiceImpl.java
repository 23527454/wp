package cn.jeefast.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.system.dao.SysAlarmTypeDao;
import cn.jeefast.system.entity.SysAlarmType;
import cn.jeefast.system.service.SysAlarmTypeService;

@Service
public class SysAlarmTypeServiceImpl extends ServiceImpl<SysAlarmTypeDao, SysAlarmType> implements SysAlarmTypeService{
	
	@Autowired
	private SysAlarmTypeDao sysAlarmTypeDao;
	
	@Override
	public List<SysAlarmType> queryList() {
		// TODO Auto-generated method stub
		return sysAlarmTypeDao.queryList();
	}

	@Override
	public List<Long> queryAlarmIdByUserId(String userId) {
		// TODO Auto-generated method stub
		return sysAlarmTypeDao.queryAlarmIdByUserId(userId);
	}
	
	@Override
	public void saveOrUpdate(String userId, List<Long> alarmIds) {
			if(alarmIds.size()==0) {
				return;
			}
			
			sysAlarmTypeDao.deleteByUserId(userId);
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userId", userId);
			map.put("alarmList", alarmIds);
			
			sysAlarmTypeDao.save(map);
	}
	
	@Override
	public void deleteByUserId(String userId) {
		sysAlarmTypeDao.deleteByUserId(userId);
	}
}
