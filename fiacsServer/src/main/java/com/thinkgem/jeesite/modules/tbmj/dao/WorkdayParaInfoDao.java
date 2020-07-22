/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.tbmj.entity.WorkdayParaInfo;
import org.apache.ibatis.annotations.Param;

/**
 * access_workdayDAO接口
 * @author demo
 * @version 2020-07-02
 */
@MyBatisDao
public interface WorkdayParaInfoDao extends CrudDao<WorkdayParaInfo> {
    public WorkdayParaInfo findAllByEIdAndYear(WorkdayParaInfo workdayParaInfo);

    public Integer modifyRestDayById(WorkdayParaInfo workdayParaInfo);

    public Integer deleteAllByEId(@Param("eId") String eId);

    public WorkdayParaInfo findByEIdAndDate(WorkdayParaInfo workdayParaInfo);

    public Integer selMaxNum();
}