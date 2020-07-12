/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.CarImage;
import com.thinkgem.jeesite.modules.guard.dao.CarImageDao;

/**
 * 车辆信息Service
 * @author Jumbo
 * @version 2017-06-27
 */
@Service
@Transactional(readOnly = true)
public class CarImageService extends CrudService<CarImageDao, CarImage> {

	@Autowired
	private CarImageDao carImageDao;
	
	public CarImage getByCarId(String carId) {
		CarImage c = new CarImage();
		c.setCarId(carId);
		return super.get(c);
	}
	
	@Override
	public void save(CarImage carImage) {
		CarImage persistCarImage = carImageDao.get(carImage);
		if(null == persistCarImage) {
			carImageDao.insert(carImage);
		}else {
			persistCarImage.setImagePath(carImage.getImagePath());
			persistCarImage.setImgData(carImage.getImgData());
			carImageDao.update(persistCarImage);
		}
	}
	
}