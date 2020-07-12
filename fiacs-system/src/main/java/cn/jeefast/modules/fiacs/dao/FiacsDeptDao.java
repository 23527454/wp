package cn.jeefast.modules.fiacs.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.jeefast.modules.fiacs.entity.FiacsDept;


public interface FiacsDeptDao extends BaseMapper<FiacsDept>{
	public List<FiacsDept> queryList();
	
	public FiacsDept queryDeptInfo(String deptId);
	
	public FiacsDept queryByDeptId(String deptId);
}
