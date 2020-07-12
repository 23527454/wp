package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public abstract class DownloadEntity<T> extends DataEntity<T> {
	private static final long serialVersionUID = -3988025542133891990L;

	private String registerTime;		// 请求时间
	private String downloadTime;		// 下载时间
	private String isDownload;		// 下载状态
	private String downloadType;		// 下载类型
	
	public static final String DOWNLOAD_TYPE_ADD="0";//添加
	public static final String DOWNLOAD_TYPE_DEL="2";//删除
	
	public DownloadEntity() {
	}

	public DownloadEntity(String id) {
		super(id);
	}
	
	public String getRegisterTime() {
		return registerTime;
	}

	public String getDownloadTime() {
		return downloadTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}

	
	@Length(min=0, max=1, message="下载状态长度必须介于 0 和 1 之间")
	public String getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(String isDownload) {
		this.isDownload = isDownload;
	}
	
	@Length(min=1, max=1, message="下载类型长度必须介于 1 和 1 之间")
	public String getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}
	
}
