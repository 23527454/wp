package com.thinkgem.jeesite.modules.guard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.FileEntityDao;
import com.thinkgem.jeesite.modules.guard.entity.FileEntity;

@Service
public class FileEntityService extends CrudService<FileEntityDao, FileEntity>{
	
	@Autowired
	private FileEntityDao fileEntityDao;
	
	@Transactional(readOnly = false)
	public int update(FileEntity fileEntity) {
		return fileEntityDao.update(fileEntity);
	}
	
	@Transactional
	public void save(FileEntity fileEntity) {
		//覆盖文件名称相同的  所以先删除之前数据在新增
		FileEntity fe = fileEntityDao.findByName(fileEntity);
		
		if(fe!=null) {
			fileEntityDao.delete(fe);
		}
		
		fileEntity.preInsert();
		fileEntityDao.insert(fileEntity);
	}
	
}
