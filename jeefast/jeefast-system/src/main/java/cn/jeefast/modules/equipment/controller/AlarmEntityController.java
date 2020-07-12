package cn.jeefast.modules.equipment.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.modules.equipment.entity.AlarmEntity;
import cn.jeefast.modules.equipment.service.AlarmEntityService;

@RestController
@RequestMapping("/alarmEntity")
public class AlarmEntityController extends BaseController{
	
	@Autowired
	private AlarmEntityService alarmEntityService;
	
	@RequestMapping("/list")
	@RequiresPermissions("alarmEntity:list")
	public R list(@RequestParam Map<String, Object> params){
			Query query = new Query(params);
			Page<AlarmEntity> pageUtil = new Page<AlarmEntity>(query.getPage(), query.getLimit());
			query.put("userId", getUserId());
			query.put("deptId", params.get("deptId"));
			query.put("equipSn", params.get("equipSn"));
			Page<AlarmEntity> page = alarmEntityService.queryPageList(pageUtil,query);
			return R.ok().put("page", page);
	}
	
}
