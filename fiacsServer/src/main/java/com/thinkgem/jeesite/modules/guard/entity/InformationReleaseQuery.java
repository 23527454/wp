package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.Date;

/**
 * 信息发布实体
 */
public class InformationReleaseQuery extends DataEntity<InformationReleaseQuery>{

    private static final long serialVersionUID = 1L;

    private Office office;

    private String synStatus;

    private String informationName;

    private String downloadTime;

    private String downloadTimeTwo;

    private String isDownload;

    public String getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(String isDownload) {
        this.isDownload = isDownload;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getDownloadTimeTwo() {
        return downloadTimeTwo;
    }

    public void setDownloadTimeTwo(String downloadTimeTwo) {
        this.downloadTimeTwo = downloadTimeTwo;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getInformationName() {
        return informationName;
    }

    public void setInformationName(String informationName) {
        this.informationName = informationName;
    }

}
