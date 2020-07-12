package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.ClassPersonInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.ClassPersonInfo;

@Service
public class ClassPersonInfoService  extends CrudService<ClassPersonInfoDao, ClassPersonInfo>{
	@Override
	public List<ClassPersonInfo> findList(ClassPersonInfo entity) {
		List<ClassPersonInfo> classPersonInfoList =  super.findList(entity);
////		for (ClassPersonInfo classPersonInfo : classPersonInfoList) {
////			classPersonInfo.setFingerNum(StaffHelper.buildFingerNum(areaId, fingerNum));
//		}
		return classPersonInfoList;
	}
	
	public int deletePerson(String personId){
		return dao.deletePerson(personId);
	}
	public int deleteClass(String classTaskId){
		return dao.deleteClass(classTaskId);
	};
}
