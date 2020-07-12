package cn.jeefast.system.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationHome;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.modules.fiacs.util.FileUtil;
import cn.jeefast.system.entity.SysLog;
import cn.jeefast.system.service.SysLogService;


/**
 * 系统日志
 * 
 * @author theodo
 * @email 36780272@qq.com
 * @date 2017-03-08 10:40:56
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends BaseController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		Page<SysLog> pageUtil = new Page<SysLog>(query.getPage(), query.getLimit());
		
		Page<SysLog> page = sysLogService.selectPageList(pageUtil,query);
		
		return R.ok().put("page", page);
	}
	
	@RequestMapping("/downloadFile/{fileName}")
	@ResponseBody
	public void Download(HttpServletResponse response,@PathVariable("fileName") String fileName) {
		if(!fileName.endsWith(".log")) {
			fileName +=".log";
		}
		FileUtil.downloadFileWindows(response, fileName,getPath() + "/logs/");
	}
	
	@ResponseBody
	@RequestMapping("/downFileList")
	@RequiresPermissions("user:list:logDown")
	public R downFileList() {
		File file = new File(getPath() + "/logs");
		
		List<SysLog> sysLogs = new ArrayList<>();
		
		if(file.exists()&&file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f : files) {
				if(f.isFile()) {
					SysLog sl = new SysLog();
					sl.setUsername(f.getName());
					sysLogs.add(sl);
				}
			}
		}
		
		Page<SysLog> page =new Page<>(1, 100);
		if(sysLogs.size()>1) {
			Collections.reverse(sysLogs);
		}
		page.setRecords(sysLogs);
		page.setTotal(sysLogs.size());
		return R.ok().put("page", page);
	}
	
	public String getPath() {
		ApplicationHome h = new ApplicationHome(getClass());
		File jarF = h.getSource();
		return jarF.getParentFile().toString();
	}
}
