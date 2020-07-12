package cn.jeefast.modules.fiacs.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.modules.fiacs.entity.FiacsDept;


public interface FiacsDeptService extends IService<FiacsDept>{
	public List<FiacsDept> queryList();
	
	public FiacsDept queryByDeptId(long deptId);
	
	public FiacsDept queryDeptInfo(long deptId) ;
}
