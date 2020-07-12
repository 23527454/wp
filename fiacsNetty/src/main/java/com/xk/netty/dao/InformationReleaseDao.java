package com.xk.netty.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xk.netty.entity.InformationRelease;
import com.xk.netty.entity.TaskClassEntity;

import java.util.List;
import java.util.Map;

public interface InformationReleaseDao extends BaseMapper<InformationRelease>{

	InformationRelease queryOne(String equipId);

	int update(Map<String, Object> map);
}
