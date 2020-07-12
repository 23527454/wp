package com.xk.netty.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.activerecord.Model;
import com.fiacs.common.util.Encodes;

public class PersonEntity extends Model<PersonEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String operateType;
	
	private String equipId;
	
	private Integer downLoadId;

	private String name;

	private String sex;

	private String workNum;

	private String fingerId;
	
	private String personId;
	
	private String password;

	private String coercePassword;
	//身份证
	private String cardId;
	
	//IC卡号
	private String cardNum;
	
	private String address;

	private String type;

	private byte[] photo;

	private String photoSize;
	
	private byte[] fingerTemplate;
	
	private byte[] backUpf;

	private byte[] coerceTemplate;
	
	private String templateSize;
	
	private String validate;

	public String getCoercePassword() {
		return coercePassword;
	}

	public void setCoercePassword(String coercePassword) {
		this.coercePassword = coercePassword;
	}

	public byte[] getCoerceTemplate() {
		return coerceTemplate;
	}

	public void setCoerceTemplate(byte[] coerceTemplate) {
		this.coerceTemplate = coerceTemplate;
	}

	public byte[] getBackUpf() {
		return backUpf;
	}

	public void setBackUpf(byte[] backUpf) {
		this.backUpf = backUpf;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public Integer getDownLoadId() {
		return downLoadId;
	}

	public void setDownLoadId(Integer downLoadId) {
		this.downLoadId = downLoadId;
	}

	public String getPhotoSize() {
		return photoSize;
	}

	public void setPhotoSize(String photoSize) {
		this.photoSize = photoSize;
	}

	public String getTemplateSize() {
		return templateSize;
	}

	public void setTemplateSize(String templateSize) {
		this.templateSize = templateSize;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWorkNum() {
		return workNum;
	}

	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}

	public String getFingerId() {
		return fingerId;
	}

	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public byte[] getFingerTemplate() {
		return fingerTemplate;
	}

	public void setFingerTemplate(byte[] fingerTemplate) {
		this.fingerTemplate = fingerTemplate;
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,String> toMap(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("OperateType", this.operateType);
		map.put("PersonID", this.personId);
		/*try {
			map.put("Name", new String(this.name.getBytes(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		map.put("Name", this.name);
		map.put("Sex", this.sex);
		map.put("WorkNum", this.workNum);
		map.put("FingerID", this.fingerId);
		map.put("FingerModelSize", String.valueOf(this.fingerTemplate==null?0 : this.fingerTemplate.length));
		map.put("PassWord", Encodes.encodeBase64(this.password));
		map.put("Validate", this.validate.substring(0, 10).replace("-", ""));
		map.put("IDNum", this.cardId);
		map.put("WorkClass", this.type);
		map.put("PhotolSize", String.valueOf(this.photo==null ? 0 : this.photo.length));
		map.put("CardNum", this.cardNum);
		map.put("CoerceWord",this.coercePassword==null?"":Encodes.encodeBase64(this.coercePassword));
		return map;
	}
}
