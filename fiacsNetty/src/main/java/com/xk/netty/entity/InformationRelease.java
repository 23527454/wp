package com.xk.netty.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.fiacs.common.util.Encodes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InformationRelease extends Model<InformationRelease> {
	private String id;

	private String DownloadID;

	private String text;

	private String beginTime;

	private String endTime;

	public String getDownloadID() {
		return DownloadID;
	}

	public void setDownloadID(String downloadID) {
		DownloadID = downloadID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	protected Serializable pkVal() {
		return null;
	}

	public Map<String,String> toMap(){
		Map<String,String> returnMap = new HashMap<>();
		returnMap.put("ID",this.id);
		returnMap.put("Text", this.text);
		returnMap.put("Startdate",this.beginTime.substring(0,10).replace("-",""));
		returnMap.put("Enddate",this.endTime.substring(0,10).replace("-",""));
		return returnMap;
	}
}
