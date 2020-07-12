/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.InformationRelease;

import java.util.List;
import java.util.Map;


@MyBatisDao
public interface InformationReleaseDao extends CrudDao<InformationRelease> {

    /**
     * 根据信息发布id删除未同步
     * @param id
     */
    void deleteDownload(String id);

    void insertDownload(InformationRelease info);
}