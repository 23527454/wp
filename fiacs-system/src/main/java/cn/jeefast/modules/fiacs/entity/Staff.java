package cn.jeefast.modules.fiacs.entity;

import java.io.Serializable;


public class Staff implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String status;//人员状态
	
	private String staffType;//人员类型
	
	private String officeId;
	
	public static final String STAFF_TYPE_SUPERCARGO = "0";//押款员 公司
	public static final String STAFF_TYPE_HANDOVER_CLERK = "1"; //交接员 网点
	public static final String STAFF_TYPE_MAINTENANCE_CLERK = "2";//维保员 公司
	
	public String getOfficeId() {
		return officeId;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}
	
	public boolean isApproved(){
		return "2".equals(status) || "3".equals(status);
	}
}
