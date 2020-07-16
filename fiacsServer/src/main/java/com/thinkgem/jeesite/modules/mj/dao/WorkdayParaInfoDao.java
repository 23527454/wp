/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.mj.entity.WorkdayParaInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * access_workdayDAO接口
 * @author demo
 * @version 2020-07-02
 */
@MyBatisDao
public interface WorkdayParaInfoDao extends CrudDao<WorkdayParaInfo> {
    public List<WorkdayParaInfo> findAllByEIdAndYear(WorkdayParaInfo workdayParaInfo);

    public Integer modifyRestDayById(WorkdayParaInfo workdayParaInfo);

    public Integer deleteAllByEId(@Param("eId") String eId);
}