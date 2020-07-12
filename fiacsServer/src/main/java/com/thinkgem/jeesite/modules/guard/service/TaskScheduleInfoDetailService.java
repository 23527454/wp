/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.entity.TaskLineInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfoDetail;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerEventDao;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.LineDao;
import com.thinkgem.jeesite.modules.guard.dao.LineNodesDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffDao;
import com.thinkgem.jeesite.modules.guard.dao.TaskLineInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.TaskScheduleInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.TaskScheduleInfoDetailDao;

/**
 * 排班明细Service
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class TaskScheduleInfoDetailService extends CrudService<TaskScheduleInfoDetailDao, TaskScheduleInfoDetail> {
	@Autowired
	private EventDetailDao eventDetailDao;
	@Autowired
	private TaskLineInfoDao taskLineInfoDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private TaskScheduleInfoDao taskScheduleInfoDao;
	private TaskScheduleInfoDetailDao taskScheduleInfodetailDao;

	public TaskScheduleInfoDetail get(String id) {
		TaskScheduleInfoDetail taskScheduleInfoDetail = super.get(id);
		taskScheduleInfoDetail.setEventDetailList(eventDetailDao.findList(new EventDetail(taskScheduleInfoDetail)));
		
		TaskLineInfo tf = new TaskLineInfo();
		tf.setId(taskScheduleInfoDetail.getId());
		tf.setTaskScheduleId(taskScheduleInfoDetail.getId());
		taskScheduleInfoDetail.setTaskLineInfoList(taskLineInfoDao.findList(tf));
		String strNam="";
		for (EventDetail eventDetail : taskScheduleInfoDetail.getEventDetailList()) {
			 Staff sf =staffDao.getWithDel(eventDetail.getPersonId());
			 if(sf!=null) {
			
			 if(!strNam.contains(sf.getName())) {
				 strNam += sf.getName();
				 eventDetail.setStaff(sf);
/*				 eventDetail.setStaff(staffDao.getWithDel(eventDetail.getPersonId()));
*/			 }
			 }
		}
		taskScheduleInfoDetail.setTaskScheduleInfo(taskScheduleInfoDao.get(taskScheduleInfoDetail.getTaskId()));
		return taskScheduleInfoDetail;
	}

	public List<TaskScheduleInfoDetail> findList(TaskScheduleInfoDetail taskScheduleInfoDetail) {
		for (Role r : taskScheduleInfoDetail.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				taskScheduleInfoDetail.getSqlMap().put("dsf", dataScopeFilter(taskScheduleInfoDetail.getCurrentUser(), "h", ""));
			} else {
				taskScheduleInfoDetail.getSqlMap().put("dsf", dataScopeFilter(taskScheduleInfoDetail.getCurrentUser(), "b", ""));
			}
		}
		
		return super.findList(taskScheduleInfoDetail);
	}

	public Page<TaskScheduleInfoDetail> findPage(Page<TaskScheduleInfoDetail> page,
			TaskScheduleInfoDetail taskScheduleInfoDetail) {
		for (Role r : taskScheduleInfoDetail.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				taskScheduleInfoDetail.getSqlMap().put("dsf", dataScopeFilter(taskScheduleInfoDetail.getCurrentUser(), "b", ""));
/*				taskScheduleInfoDetail.getSqlMap().put("dsf", dataScopeFilter(taskScheduleInfoDetail.getCurrentUser(), "h", ""));
*/			} else {
				taskScheduleInfoDetail.getSqlMap().put("dsf", dataScopeFilter(taskScheduleInfoDetail.getCurrentUser(), "b", ""));
			}
		}
		
		
		
		Page<TaskScheduleInfoDetail> pageTaskDetail = super.findPage(page, taskScheduleInfoDetail);
		for (int i = 0; i < pageTaskDetail.getList().size(); i++) {
			TaskScheduleInfoDetail taskDetail = pageTaskDetail.getList().get(i);
			taskDetail.setEventDetailList(eventDetailDao.findList(new EventDetail(taskDetail)));
			for (int n = 0; taskDetail.getEventDetailList() != null
					&& n < taskDetail.getEventDetailList().size(); n++) {
				taskDetail.getEventDetailList().get(n)
						.setStaff(staffDao.get(taskDetail.getEventDetailList().get(n).getPersonId()));
			}
			taskDetail.setTaskScheduleInfo(taskScheduleInfoDao.get(taskDetail.getTaskId()));
			
		}

		return pageTaskDetail;
	}

	@Transactional(readOnly = false)
	public void save(TaskScheduleInfoDetail taskScheduleInfoDetail) {
		super.save(taskScheduleInfoDetail);
	}

	@Transactional(readOnly = false)
	public void delete(TaskScheduleInfoDetail taskScheduleInfoDetail) {
		super.delete(taskScheduleInfoDetail);
	}

}