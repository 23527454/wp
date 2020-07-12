package cn.jeefast.rest.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.rest.entity.AlarmEntity;

public interface AlarmEntityService extends IService<AlarmEntity>{
	
	Page<AlarmEntity> queryPageList(Page<AlarmEntity> pageUtil, Map<String, Object> map);
}
