package com.thinkgem.jeesite.modules.guard.entity;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 维保员派遣实体
 */
public class SafeGuardDispatch extends DataEntity<SafeGuardDispatch> {

    private static final long serialVersionUID = 1L;

   private String dispatchName;

   private List<Office> officeList;

   private String officeIds;

   private List<DispatchPersonInfo> dispatchPersonInfoList;

    public String getDispatchName() {
        return dispatchName;
    }

    public void setDispatchName(String dispatchName) {
        this.dispatchName = dispatchName;
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }

    public String getOfficeIds() {
       return this.officeIds;
    }

    public void setOfficeIds(String officeIds) {
                this.officeIds = officeIds;
    }

    public List<DispatchPersonInfo> getDispatchPersonInfoList() {
        return dispatchPersonInfoList;
    }

    public void setDispatchPersonInfoList(List<DispatchPersonInfo> dispatchPersonInfoList) {
        this.dispatchPersonInfoList = dispatchPersonInfoList;
    }
}
