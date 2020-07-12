/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.CarImage;
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.DownloadPerson;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.guard.dao.CarDao;
import com.thinkgem.jeesite.modules.guard.dao.CarImageDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffExWorkDao;

/**
 * 车辆信息Service
 * @author Jumbo
 * @version 2017-06-27
 */
@Service
@Transactional(readOnly = true)
public class CarService extends CrudService<CarDao, Car> {
	
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private DownloadCarService downloadCarService;
	@Autowired
	private CarImageService carImageService;
	@Autowired
	private @Lazy EquipmentService equipmentService;
	

	public Car get(String id) {
		Car car = super.get(id);
		if(null != car) {
			car.setCarImage(carImageService.getByCarId(id));
		}
		return car;
	}
	
	public List<Car> findList(Car car) {
		return super.findList(car);
	}
	
	public Page<Car> findPage(Page<Car> page, Car car) {
		car.getSqlMap().put("dsf", dataScopeFilterArea(car.getCurrentUser(), "a4", "u"));
		return super.findPage(page, car);
	}
	
	@Transactional(readOnly = false)
	public void save(Car car) {
		boolean messageType;
		if (car.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		
		super.save(car);
		CarImage carImage = car.getCarImage();
		if(null != carImage) {
			assertNotNull(car.getId());
			carImage.setCarId(car.getId());
			carImageService.save(carImage);
		}

		this.insentDownload(car, DownloadEntity.DOWNLOAD_TYPE_ADD);
		
		if(messageType){
			LogUtils.saveLog(Servlets.getRequest(), "新增车辆:["+car.getId()+"]");
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改车辆:["+car.getId()+"]");
		}
	}
	
	@Transactional(readOnly = false)
	public void insentDownload(Car car,String type) {
		String area_id = car.getArea().getId();
		List<Office> off = officeDao.findOfficesByAreaIdHasEquipment(area_id);
		for (int i = 0; i < off.size(); i++) {
			String officeID = off.get(i).getId();
			DownloadCar downloadCar = new DownloadCar();
			downloadCar.setCarId(car.getId());
			downloadCar.setEquipId(equipmentService.getByOfficeId(officeID).getId());
			downloadCar.setIsDownload("0");
			downloadCar.setDownloadType(type);
			downloadCar.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadCarService.save(downloadCar);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Car car) {
		super.delete(car);
		CarImage carImage = car.getCarImage();
		if(null != carImage) {
			assertNotNull(car.getId());
			carImage.setCarId(car.getId());
			carImageService.delete(carImage);
		}
		
		DownloadCar dc = new DownloadCar();
		dc.setCarId(car.getId());
		dc.setDownloadType("0");
		dc.setIsDownload("0");
		int deleted = downloadCarService.delete1(dc);
		//if(deleted<1) {
			this.insentDownload(car, DownloadEntity.DOWNLOAD_TYPE_DEL);
		//}
		LogUtils.saveLog(Servlets.getRequest(), "删除车辆:["+car.getId()+"]");
	}

	public int countByCardNum(String id, String cardNum) {
		if(StringUtils.isBlank(cardNum)) {
			return 0;
		}
		Car car = new Car();
		car.setId(id);
		car.setCardNum(cardNum);
		return super.countByColumnExceptSelf(car);
	}

	public int countByCarplate(String id, String carplate) {
		if(StringUtils.isBlank(carplate)) {
			return 0;
		}
		Car car = new Car();
		car.setId(id);
		car.setCarplate(carplate);
		return super.countByColumnExceptSelf(car);
	}
	
}