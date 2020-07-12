/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.entity.ClassCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassPersonInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.entity.StaffExFamily;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.guard.dao.ClassCarInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.ClassPersonInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.ClassTaskInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.EquipmentDao;
import com.thinkgem.jeesite.modules.guard.dao.LineNodesDao;

/**
 * 班组信息Service
 * 
 * @author Jumbo
 * @version 2017-07-04
 */
@Service
@Transactional(readOnly = true)
public class ClassTaskInfoService extends CrudService<ClassTaskInfoDao, ClassTaskInfo> {
	ClassTaskInfoDao classTaskInfoDao = SpringContextHolder.getBean(ClassTaskInfoDao.class);
	@Autowired
	private ClassCarInfoDao classCarInfoDao;
	@Autowired
	private ClassPersonInfoService classPersonInfoService;
	@Autowired
	private LineNodesDao lineNodesDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private EquipmentDao equipmentDao;

	public ClassTaskInfo get(String id) {
		if(!StringUtils.isBlank(id)){
			ClassTaskInfo taskInfo = super.get(id);
			taskInfo.setClassCarInfoList(classCarInfoDao.findList(new ClassCarInfo(taskInfo)));
			taskInfo.setClassPersonInfoList(classPersonInfoService.findList(new ClassPersonInfo(taskInfo)));
			taskInfo.setLinNodesList(lineNodesDao.findList(new LineNodes(taskInfo.getLine())));
			if (taskInfo.getArea() != null && "0".equals(taskInfo.getArea().getId())) {
				taskInfo.getArea().setName("所有区域");
			}
			if(!CollectionUtils.isEmpty(taskInfo.getLinNodesList())){
				List<LineNodes> lineNodesList = taskInfo.getLinNodesList();
				for (Iterator iterator = lineNodesList.iterator(); iterator
						.hasNext();) {
					LineNodes lineNodes = (LineNodes) iterator.next();
					lineNodes.setOffice(officeDao.get(lineNodes.getOffice().getId()));
					Equipment e = equipmentDao.findOne(new Equipment(lineNodes.getOffice()));
					if(e == null){
						iterator.remove();
					}
					lineNodes.setEquipmentId(e.getId());
				}
			}
			return taskInfo;
		}else{
			return new ClassTaskInfo();
		}
	}

	public List<ClassTaskInfo> findList(Boolean isAll) {
		ClassTaskInfo classTaskInfo = new ClassTaskInfo();
		classTaskInfo.getSqlMap().put("dsf", dataScopeFilterArea(classTaskInfo.getCurrentUser(), "a4", "u"));
		List<ClassTaskInfo> classTaskInfoList = classTaskInfoDao.findList(classTaskInfo);
		for (int i = 0; i < classTaskInfoList.size(); i++) {
			if (classTaskInfoList.get(i).getArea() != null && "0".equals(classTaskInfoList.get(i).getArea().getId())) {
				classTaskInfoList.get(i).getArea().setName("所有区域");
			}
		}
		return classTaskInfoList;
	}

	public List<ClassTaskInfo> findList(ClassTaskInfo classTaskInfo) {
		classTaskInfo.getSqlMap().put("dsf", dataScopeFilterArea(classTaskInfo.getCurrentUser(), "a4", "u"));
		List<ClassTaskInfo> classTaskInfoList = super.findList(classTaskInfo);
		for (int i = 0; i < classTaskInfoList.size(); i++) {
			if (classTaskInfoList.get(i).getArea() != null && "0".equals(classTaskInfoList.get(i).getArea().getId())) {
				classTaskInfoList.get(i).getArea().setName("所有区域");
			}
		}

		return classTaskInfoList;
	}

	
	public Page<ClassTaskInfo> findPage(Page<ClassTaskInfo> page, ClassTaskInfo classTaskInfo) {
		classTaskInfo.getSqlMap().put("dsf", dataScopeFilterArea(classTaskInfo.getCurrentUser(), "a4", "u"));
		Page<ClassTaskInfo> pageClassTask = super.findPage(page, classTaskInfo);
		for (int i = 0; i < pageClassTask.getList().size(); i++) {
			pageClassTask.getList().get(i).setCar(classTaskInfo.getCar());
			
			pageClassTask.getList().get(i).setStaffTwo(classTaskInfo.getStaffTwo());
			pageClassTask.getList().get(i)
					.setClassCarInfoList(classCarInfoDao.findList(new ClassCarInfo(pageClassTask.getList().get(i))));
			
			pageClassTask.getList().get(i).setClassPersonInfoList(
					classPersonInfoService.findList(new ClassPersonInfo(pageClassTask.getList().get(i))));
			
			pageClassTask.getList().get(i)
					.setLinNodesList(lineNodesDao.findList(new LineNodes(pageClassTask.getList().get(i).getLine())));

			if (pageClassTask.getList().get(i).getArea() != null
					&& "0".equals(pageClassTask.getList().get(i).getArea().getId())) {
				pageClassTask.getList().get(i).getArea().setName("所有区域");
			}
			pageClassTask.getList().get(i).setStaffTwo(null);
		}
/*		classTaskInfo.getSqlMap().put("dsf", dataScopeFilterArea(classTaskInfo.getCurrentUser(), "a4", "u"));
		Page<ClassTaskInfo> pageClassTask = super.findPage(page, classTaskInfo);
		for (int i = 0; i < pageClassTask.getList().size(); i++) {
			pageClassTask.getList().get(i)
			.setClassCarInfoList(classCarInfoDao.findList(new ClassCarInfo(pageClassTask.getList().get(i))));
			pageClassTask.getList().get(i).setClassPersonInfoList(
					classPersonInfoService.findList(new ClassPersonInfo(pageClassTask.getList().get(i))));
			pageClassTask.getList().get(i)
			.setLinNodesList(lineNodesDao.findList(new LineNodes(pageClassTask.getList().get(i).getLine())));
			
			if (pageClassTask.getList().get(i).getArea() != null
					&& "0".equals(pageClassTask.getList().get(i).getArea().getId())) {
				pageClassTask.getList().get(i).getArea().setName("所有区域");
			}
		}
*/
		return pageClassTask;
	}

	@Transactional(readOnly = false)
	public void save(ClassTaskInfo classTaskInfo) {
		boolean messageType;
		if (classTaskInfo.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		
		super.save(classTaskInfo);
		if(messageType){
			LogUtils.saveLog(Servlets.getRequest(), "新增班组:["+classTaskInfo.getId()+"]");
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改班组:["+classTaskInfo.getId()+"]");
		}
		
		classCarInfoDao.deleteClass(classTaskInfo.getId());
		for (ClassCarInfo classCarInfo : classTaskInfo.getClassCarInfoList()) {
			if (classCarInfo.getCarId() == null || classCarInfo.getCarId() == "") {
				continue;
			}
			if (ClassCarInfo.DEL_FLAG_NORMAL.equals(classCarInfo.getDelete())) {
				classCarInfo.setClassTaskId(classTaskInfo.getId());
				classCarInfo.preInsert();
				classCarInfoDao.insert(classCarInfo);
			} else {
				classCarInfoDao.deleteCar(classCarInfo.getCarId());
			}
		}

		classPersonInfoService.deleteClass(classTaskInfo.getId());
		for (ClassPersonInfo classPersonInfo : classTaskInfo.getClassPersonInfoList()) {
			if (classPersonInfo.getPersonId() == null || classPersonInfo.getPersonId() == "") {
				continue;
			}
			if (ClassPersonInfo.DEL_FLAG_NORMAL.equals(classPersonInfo.getDelete())) {
				classPersonInfo.setClassTaskId(classTaskInfo.getId());
//				classPersonInfo.preInsert();
				classPersonInfo.setId(null);
				classPersonInfoService.save(classPersonInfo);
			} else {
				classPersonInfoService.deletePerson(classPersonInfo.getPersonId());
			}
		}

	}

	@Transactional(readOnly = false)
	public void delete(ClassTaskInfo classTaskInfo) {
		super.delete(classTaskInfo);
		classCarInfoDao.deleteClass(classTaskInfo.getId());
		classPersonInfoService.deleteClass(classTaskInfo.getId());
		LogUtils.saveLog(Servlets.getRequest(), "删除班组:["+classTaskInfo.getId()+"]");
	}

	public int countByName(String id, String name) {
		if(null == name) {
			return 0;
		}
		ClassTaskInfo c = new ClassTaskInfo();
		c.setId(id);
		c.setName(name);
		return super.countByColumnExceptSelf(c);
	}

}