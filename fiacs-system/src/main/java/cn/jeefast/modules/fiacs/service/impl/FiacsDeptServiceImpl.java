package cn.jeefast.modules.fiacs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.modules.fiacs.dao.FiacsDeptDao;
import cn.jeefast.modules.fiacs.entity.FiacsDept;
import cn.jeefast.modules.fiacs.service.FiacsDeptService;


@Service
public class FiacsDeptServiceImpl extends ServiceImpl<FiacsDeptDao, FiacsDept> implements FiacsDeptService{
	
	@Resource
	private FiacsDeptDao fiacsDeptDao;
	
	@Override
	public List<FiacsDept> queryList() {
		return fiacsDeptDao.queryList();
	}
	
	@Override
	public FiacsDept queryDeptInfo(long deptId) {
		return fiacsDeptDao.queryDeptInfo(String.valueOf(deptId));
	}
	
	@Override
	public FiacsDept queryByDeptId(long deptId) {
		return fiacsDeptDao.queryByDeptId(String.valueOf(deptId));
	}
}
