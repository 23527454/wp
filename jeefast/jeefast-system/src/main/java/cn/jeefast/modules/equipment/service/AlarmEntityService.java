package cn.jeefast.modules.equipment.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.modules.equipment.entity.AlarmEntity;

public interface AlarmEntityService extends IService<AlarmEntity>{
	
	Page<AlarmEntity> queryPageList(Page<AlarmEntity> pageUtil, Map<String, Object> map);
}
