/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.dao.ClassCarInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.CompanyDao;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.CarImage;
import com.thinkgem.jeesite.modules.guard.entity.ClassCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.Company;
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.CarService;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 车辆信息Controller
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/car")
public class CarController extends BaseController {

	@Autowired
	private CarService carService;

	@Autowired
	private ClassCarInfoDao classCarInfoDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private AreaDao areaDao;

	@Value("${projectPath.images}")
	private String projectPathImages;
	
	private Company company = new Company();
	
	
	@ModelAttribute
	public Car get(@RequestParam(required = false) String id) {
		Car entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = carService.get(id);
		}
		if (entity == null) {
			entity = new Car();
		}
		return entity;
	}

	@RequiresPermissions("guard:car:view")
	@RequestMapping(value = { "index" })
	public String index(Car car, Model model) {
		return "modules/guard/carIndex";
	}

	@RequiresPermissions("guard:car:view")
	@RequestMapping(value = { "list", "" })
	public String list(Car car, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(car.getCompany()==null) {
			car.setCompany(company);
		}else {
			company = car.getCompany();
		}

		Page<Car> page = carService.findPage(new Page<Car>(request, response), car);
		model.addAttribute("page", page);
		model.addAttribute("car", car);
		return "modules/guard/carList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param car
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:car:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(Car car, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			car.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, Car.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(car, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		Page<Car> page = carService.findPage(new Page<Car>(request, response), car);
		List<Car> carList = page.getList();
		List<ClassCarInfo> carInfoList = classCarInfoDao.findList(new ClassCarInfo());
		List<Car> carL = new ArrayList<Car>();

		//2017-12-10 根据区域id查车辆
		for (int i = 0; i < carList.size(); i++) {
			if ((carListPD(carList, carInfoList, i, car) && "0".equals(carList.get(i).getWorkStatus()))) {
				carL.add(carList.get(i));
			}
		}
	/*	for (int i = 0; i < carList.size(); i++) {
			if ("0".equals(carList.get(i).getWorkStatus())) {
				carL.add(carList.get(i));
			}
		}*/
		page.setList(carL);
		if (page.getList().size() == 0) {
			return "";
		}
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("workStatus")) {
				String s = DictUtils.getDictLabel(e.getString("workStatus"), "work_status", "");
				e.put("workStatus", s);
			}
			if (e.has("delFlag")) {
				String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
				e.put("delFlag", s);
			}
		}
		return jo.toString();
	}
	
	@RequiresPermissions("guard:car:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Car car, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		try {
			String fileName = "车辆数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Car> page = carService.findPage(new Page<Car>(request, response, -1), car);
			new ExportExcel("车辆数据", Car.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出车辆数据失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/car/list?repage";
	}


	// 车辆数据不在表中
	private boolean carListPD(List<Car> carList, List<ClassCarInfo> carInfoList, int i, Car car) {
		for (int n = 0; n < carInfoList.size(); n++) {
			if (carList.get(i).getId().equals(carInfoList.get(n).getCarId())) {
				if (car.getClassTaskId().equals(carInfoList.get(n).getClassTaskId())) {
					return true;
				}
				return false;
			}
		}
		return true;
	}

	@RequiresPermissions("guard:car:edit")
	@RequestMapping(value = "download")
	public String download(Car car, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedCompanyId")String selectedCompanyId) {
		if (!beanValidator(model, car)) {
			return form(car, model);
		}
		carService.insentDownload(car, DownloadEntity.DOWNLOAD_TYPE_ADD);
		addMessage(redirectAttributes, "车辆同步成功");
		return backListPage(selectedCompanyId);
	}

	private String backListPage(String selectedCompanyId) {
		if(!StringUtils.isBlank(selectedCompanyId)){
			return "redirect:" + Global.getAdminPath() + "/guard/car/list?company.id="+selectedCompanyId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/car/?repage";
		}
	}

	@RequiresPermissions("guard:car:view")
	@RequestMapping(value = "form")
	public String form(Car car, Model model) {
		return backForm(car, model);
	}
	
	private String backForm(Car car, Model model) {
		model.addAttribute("car", car);
		return "modules/guard/carForm";
	}

	@RequiresPermissions("guard:car:edit")
	@RequestMapping(value = "save")
	public String save(Car car, BindingResult result , Model model, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes,
			@RequestParam("selectedCompanyId")String selectedCompanyId) throws IOException {
		if (!beanValidator(model, car)) {
			return backForm(car, model);
		}

		// 判断是否是修改
		if (!car.getIsNewRecord()) {
			// 判断是否在排班（false为在排班）
			if (deletePD(car) == false) {
				// 判断是否提交过来的人员为离职或者休假
				if ("1".equals(car.getWorkStatus())) {
					addMessage(redirectAttributes, "不可修改在排班车辆工作状态");
					return "redirect:" + Global.getAdminPath() + "/guard/car/?repage";
				}
			}
		}
		
		if(carService.countByCardNum(car.getId(), car.getCardNum()) > 0) {
			result.rejectValue("cardNum", "duplicate", "车辆卡号已经存在");
		}
		if(carService.countByCarplate(car.getId(), car.getCarplate()) > 0) {
			result.rejectValue("carplate", "duplicate", "车牌号已经存在");
		}
		
		if(result.hasErrors()) {
			return backForm(car, model);
		}
		
		if (car.getFile() != null) {
			
			CarImage carImage = new CarImage();
			// 车辆照片处理
			String slack = File.separator;
			String rootPath = getJarPath()+projectPathImages;
			File root = new File(rootPath);
			root.mkdirs();
			String fileName = car.getFile().getOriginalFilename();
			if(fileName.contains("\\")) {
				fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
			}
			String coverPath = slack + "car" + slack + fileName;
			String absPath = rootPath + coverPath;
			if (car.getFile().getSize() > 0) {
				// 复制文件
				File newFile = new File(absPath);
				newFile.mkdirs();
				newFile.delete();
				byte[] carByte =car.getFile().getBytes();
				car.getFile().transferTo(newFile);
				coverPath = "/images/car/" + fileName;
				carImage.setImagePath(coverPath);
				carImage.setImgData(carByte);
				car.setCarImage(carImage);
			}
		}
		String companyId = car.getCompany().getId();
		if((!"".equals(companyId))&& null != companyId) {
			Company byId = companyDao.getById(companyId);
			Area area = areaDao.getById(byId.getArea().getId());
			car.setArea(area);
		}
		try {
			carService.save(car);
		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存车辆信息成功");
		if(null != selectedCompanyId && selectedCompanyId.equals(car.getCompany().getId())){
			return "redirect:" + Global.getAdminPath() + "/guard/car/list?company.id="+selectedCompanyId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/car/?repage";
		}
	}

	@RequiresPermissions("guard:car:edit")
	@RequestMapping(value = "delete")
	public String delete(Car car, RedirectAttributes redirectAttributes,
			@RequestParam("selectedCompanyId")String selectedCompanyId) {
		if (deletePD(car)) {
			carService.delete(car);
			addMessage(redirectAttributes, "删除车辆信息成功");
		} else {
			addMessage(redirectAttributes, "不能删除在排班的车辆");
		}
		return backListPage(selectedCompanyId);
	}

	private boolean deletePD(Car car) {
		List<ClassCarInfo> carInfoList = classCarInfoDao.findList(new ClassCarInfo());
		for (int i = 0; i < carInfoList.size(); i++) {
			if (carInfoList.get(i).getCarId().equals(car.getId())) {
				return false;
			}
		}
		return true;
	}
	
	private String getJarPath() {
		ApplicationHome h = new ApplicationHome(getClass());
		File jarF = h.getSource();
		return jarF.getParentFile().toString();
	}
}