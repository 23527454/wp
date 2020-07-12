/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 第三方公司Entity
 * @author Jumbo
 * @version 2017-07-19
 */
public class CompanyEx extends DataEntity<CompanyEx> {
	
	private static final long serialVersionUID = 1L;
	private String companyId;		// 公司编号 父类
	private String imageType;		// 照片类型
	private String imagePath;		// 照片路径
	private byte[] imgData;   // 公司图片数据
	private MultipartFile file;
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	public CompanyEx() {
		super();
	}

	public CompanyEx(String id){
		super(id);
	}

	public CompanyEx(Company companyId){
		if(companyId != null)
			this.companyId = companyId.getId();
	}

	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public byte[] getImgData() {
		return imgData;
	}

	public void setImgData(byte[] imgData) {
		this.imgData = imgData;
	}

	@Length(min=1, max=1, message="照片类型长度必须介于 1 和 1 之间")
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	@Length(min=1, max=255, message="照片路径长度必须介于 1 和 255 之间")
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}