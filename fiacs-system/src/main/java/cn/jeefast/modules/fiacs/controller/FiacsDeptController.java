package cn.jeefast.modules.fiacs.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.jeefast.common.utils.R;
import cn.jeefast.modules.fiacs.entity.FiacsDept;
import cn.jeefast.modules.fiacs.service.FiacsDeptService;

@RestController
@RequestMapping("/fiacs/dept")
public class FiacsDeptController {
	
	@Resource
	private FiacsDeptService fiacsDpetServiceImpl;
	
	/**
	 * 设备列表
	 */
	@RequestMapping("/list")
	public List<FiacsDept> list(){
		List<FiacsDept> deptList = fiacsDpetServiceImpl.queryList();
		return deptList;
	}
	
	/**
	 * 机构列表
	 */
	@RequestMapping("/deptInfo/{deptId}")
	public R deptInfo(@PathVariable("deptId") long deptId){
		FiacsDept deptInfo = fiacsDpetServiceImpl.queryDeptInfo(deptId);
		String type = deptInfo.getType();
		if("0".equals(type)) {
			deptInfo.setType("一级支行");
		}else if("1".equals(type)) {
			deptInfo.setType("一级分行");
		}else if("2".equals(type)) {
			deptInfo.setType("中心金库");
		}else if("3".equals(type)) {
			deptInfo.setType("营业网点");
		}else if("4".equals(type)) {
			deptInfo.setType("子营业网点");
		}
		return R.ok().put("dept", deptInfo);
	}
	
	/**
	 * 选择网点
	 */
	@RequestMapping("/select")
	public R select(){
		List<FiacsDept> deptList = fiacsDpetServiceImpl.queryList();
		return R.ok().put("deptList", deptList);
	}
}
