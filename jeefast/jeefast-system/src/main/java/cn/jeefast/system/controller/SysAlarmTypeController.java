package cn.jeefast.system.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.R;
import cn.jeefast.system.entity.SysAlarmType;
import cn.jeefast.system.service.SysAlarmTypeService;

@RestController
@RequestMapping("/sys/alarmType")
public class SysAlarmTypeController extends BaseController{
	
	@Autowired
	private SysAlarmTypeService sysAlarmTypeService;
	
	/**
	 * 存在的警报通知类型
	 * @return
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select(){
		List<SysAlarmType> list = sysAlarmTypeService.queryList();
				
		return R.ok().put("list", list);
	}
}
