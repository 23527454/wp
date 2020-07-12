package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.FileEntity;

@MyBatisDao
public interface FileEntityDao extends CrudDao<FileEntity>{
	
	public FileEntity findByName(FileEntity fileEntity);
}
