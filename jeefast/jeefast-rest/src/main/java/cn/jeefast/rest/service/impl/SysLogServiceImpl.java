package cn.jeefast.rest.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.rest.dao.SysLogDao;
import cn.jeefast.rest.entity.SysLog;
import cn.jeefast.rest.service.SysLogService;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author theodo
 * @since 2017-10-28
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLog> implements SysLogService {

	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public Page<SysLog> selectPageList(Page<SysLog> page, Map<String, Object> map) {
		page.setRecords(sysLogDao.selectPageList(page, map));
		return page;
	}
	
}
