package com.xk.netty.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.activerecord.Model;

public class CarEntity extends Model<CarEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String operateType;

	private String equipId;

	private Integer downLoadId;

	private String carNum;

	private String carId;

	private String color;

	private byte[] photo;

	private String photoSize;
	// 品牌类型
	private String carType;
	/**
	 * 有源卡号
	 */
	private String carCard;
	
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

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPhotoSize() {
		return photoSize;
	}

	public void setPhotoSize(String photoSize) {
		this.photoSize = photoSize;
	}

	public String getCarCard() {
		return carCard;
	}

	public void setCarCard(String carCard) {
		this.carCard = carCard;
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,String> toMap(){
		Map<String, String> map = new HashMap<>();
		map.put("OperateType", this.operateType);
		map.put("CarID", this.carId);
		/*try {
			map.put("PlateNum", new String(this.carNum.getBytes(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		map.put("PlateNum",this.carNum);
		map.put("CarCard", this.carCard);
		map.put("CarColor", this.color.substring(1));
		map.put("PhotolSize", String.valueOf(this.photo.length));
		return map;
	}
}
