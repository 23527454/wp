/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.dao.FingerInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffImageDao;
import com.thinkgem.jeesite.modules.guard.entity.*;
import com.thinkgem.jeesite.modules.guard.service.*;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人员信息管理Controller
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/staff")
public class StaffController extends BaseController {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private OfficeService officeService;

	@Autowired
	private AreaService areaService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ClassPersonInfoService classPersonInfoService;
	
	@Autowired
	private FingerInfoDao fingerInfoDao;
	@Autowired
	private StaffImageDao staffImageDao;

	@Autowired
	private SysConfigService sysConfigService;
	
	@Value("${projectPath.images}")
	private String projectPath;
	
	private Company company = new Company();
	private Office office = new Office();
	private Boolean flag = true;
	@ModelAttribute
	public Staff get(@RequestParam(required = false) String id) {
		Staff entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = staffService.get(id);
		}
		if (entity == null) {
			entity = new Staff();
		}
		return entity;
	}

	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "plan1")
	public ModelAndView plan1(String accessParaInfoId,ModelAndView modelAndView,Model model) {
		model.addAttribute("accessParaInfoId",accessParaInfoId);
		modelAndView.setViewName("modules/mj/selStaff");//跳转到这个jsp页面来渲染表格
		return modelAndView;
	}

	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "selStaff")
	@ResponseBody
	public String selStaff(String name,String workNum,String accessParaInfoId,Integer pageIndex,Integer size,ModelAndView modelAndView) {
		if(pageIndex==null){
			pageIndex=1;
		}
		if(size==null){
			size=10;
		}
		pageIndex=(pageIndex-1)*size;

		List<Staff> list=staffService.findAll(name,workNum, accessParaInfoId,pageIndex,size);
		List<Staff> list2=staffService.findAll(name,workNum,accessParaInfoId,null,null);
		StringBuffer sb=new StringBuffer("");
		StringBuffer sb2=new StringBuffer("");

		for(Staff s:list){
			sb2.append("{\"id\":\""+s.getId()+"\",\"name\":\""+s.getName()+"\",\"workNum\":\""+s.getWorkNum()+"\",\"dept\":\""+s.getDept()+"\",\"phone\":\""+s.getPhone()+"\"},");
		}
		if(sb2.length()>0){
			sb2.deleteCharAt(sb2.length() - 1);
		}
		sb.append("{\"code\": 0,\"msg\": \"\",\"count\": "+list2.size()+",\"data\": ["+sb2.toString()+"]}");

		return sb.toString();
	}


	@RequiresPermissions("guard:staff:view")
	@RequestMapping(value = { "index" })
	public String index(Staff staff, Model model) {
		return "modules/guard/staffIndex";
	}

	@RequiresPermissions("guard:staff:view")
	@RequestMapping(value = { "list", "" })
	public String list(Staff staff, HttpServletRequest request, HttpServletResponse response, Model model) {
		staff.getCurrentUser().setCpmpFlag("0");
		List<Company> list = companyService.findList(new Company());
		
		
		List<String> compIds = new ArrayList<String>();
		/*for (Company company : list) {
			compIds.add(company.getId());
		}*/
	
		
	
		if(staff.getCompany()==null) {
			staff.setCompany(company);
			for (Company company : list) {
				compIds.add(company.getId());
			}
		}else {
			staff.getCurrentUser().setCpmpFlag("1");
			company = staff.getCompany();
			compIds.add(company.getId());
			for (Company company2 : list) {
				if(company2.getParentIds().contains(company.getId())) {	
					compIds.add(company2.getId());
				}
			}
			office = null;
		}
		if(staff.getOffice()==null) {
			staff.setOffice(office);
		}else {
			office = staff.getOffice();
			company = null;
		}
	
	if(compIds.size()>0) {
		staff.setCompIds(compIds);
		//System.out.println(list.toString());
		staff.setCompanyIds(list);
		Page<Staff> page = staffService.findPage(new Page<Staff>(request, response), staff);
		model.addAttribute("page", page);
		model.addAttribute("staff", staff);
	}else {
		Page<Staff> page = new Page<>();
		model.addAttribute("page", page);
		model.addAttribute("staff", staff);
	}
		
		return "modules/guard/staffList";
	}

	/**
	 * 获取押款员列表数据（JSON）
	 * 
	 * @param staff
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:staff:view")
	@RequestMapping(value = "listClassTaskInfoData")
	@ResponseBody
	public String listClassTaskInfoData(Staff staff, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			staff.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList<Field>();
			Reflections.getAllFields(fields, Staff.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(staff, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		// 判断查询出来的列表是否在排班中。
		// 判断人员是否是押款员
		// 判断是否在职
		// 判断是否已经审批通过
		if(staff.getQueryType()==null){
			staff.setQueryType(Staff.queryTypeSuperGO);
		}
		Page<Staff> page = staffService.findPage(new Page<Staff>(request, response), staff);
//		List<Staff> staffL = new ArrayList<Staff>();
//		List<Staff> staff2 = new ArrayList<Staff>();
//		Set<String> temp2 = new HashSet<String>();
//		
//		//获得所有已经排版的押款员
//		List<ClassPersonInfo> classPersonInfos = classPersonInfoService.findList(new ClassPersonInfo());
//		do {
//			List<Staff> staffList = page.getList();
//			for (int i = 0; i < staffList.size(); i++) {
//				// 判断查询出来的列表是否在排班中。
//				// 判断人员是否是押款员
//				// 判断是否在职
//				// 判断是否已经审批通过
//				if (staffListPD(staffList, classPersonInfos, i, staff) && "0".equals(staffList.get(i).getStaffType())
//						&& "1".equals(staffList.get(i).getWorkStatus())) {
//					if ("2".equals(staffList.get(i).getStatus()) || "3".equals(staffList.get(i).getStatus())) {
//						staffL.add(staffList.get(i));
//					}
//				}
//				
//			}
//			 for (Staff taff : staffL) {
//				if(temp2.add(taff.getName())) {
//					staff2.add(taff);
//				}
//			}
//			
//			
//			page.setPageNo(page.getPageNo()+1);
//		} while (page.getPageNo() <= page.getTotalPage());
//		
//		page.setList(staff2);
		if (page.getList().size() == 0) {
			return "";
		}
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("sex")) {
				String s = DictUtils.getDictLabel(e.getString("sex"), "sex_type", "");
				e.put("sex", s);
			}
			if (e.has("identifyType")) {
				String s = DictUtils.getDictLabel(e.getString("identifyType"), "identify_ype", "");
				e.put("identifyType", s);
			}
			if (e.has("staffType")) {
				String s = DictUtils.getDictLabel(e.getString("staffType"), "staff_type", "");
				e.put("staffType", s);
			}
			if (e.has("workStatus")) {
				String s = DictUtils.getDictLabel(e.getString("workStatus"), "nWork_tatus", "");
				e.put("workStatus", s);
			}
			if (e.has("status")) {
				String s = DictUtils.getDictLabel(e.getString("status"), "person_status", "");
				e.put("status", s);
			}
			if (e.has("delFlag")) {
				String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
				e.put("delFlag", s);
			}
		}
		return jo.toString();
	}

	private boolean staffListPD(List<Staff> staffL, List<ClassPersonInfo> personInfoList, int i, Staff staff) {
		for (int n = 0; n < personInfoList.size(); n++) {
			if (staffL.get(i).getId().equals(personInfoList.get(n).getPersonId())) {
				if (staff.getClassTaskId().equals(personInfoList.get(n).getClassTaskId())) {
					return true;
				}
				return false;
			}
		}
		return true;
	}

	@RequiresPermissions("guard:staff:view")
	@RequestMapping(value = "form")
	public String form(Staff staff, 
			@RequestParam(value="selectedOfficeId", required=false)String selectedOfficeId,
			@RequestParam(value="selectedCompanyId", required=false)String selectedCompanyId,
			Model model) {
		if(staff.getIsNewRecord()){
			staff.setOffice(new Office(selectedOfficeId));
			staff.setCompany(new Company(selectedCompanyId));
		}
		return backForm(staff, selectedOfficeId,selectedCompanyId, model);
	}
	
	@RequiresPermissions("guard:staff:view")
	@RequestMapping(value = "json")
	@ResponseBody
	public String json(Staff staff, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (StringUtils.isBlank(staff.getId())) {
			return "{}";
		}
		Staff entity = get(staff.getId());
		if (entity.getFingerInfoList() != null && entity.getFingerInfoList().size() > 0) {
			String template = StringUtils.bytesToHexString(entity.getFingerInfoList().get(0).getFingerTemplate());
			String backup = StringUtils.bytesToHexString(entity.getFingerInfoList().get(0).getBackupFp());
			entity.getFingerInfoList().get(0).setFingerTemplate(template.getBytes());
			entity.getFingerInfoList().get(0).setBackupFp(backup.getBytes());
		}

		return JsonMapper.toJsonString(entity);
	}



	private String backForm(Staff staff,
			String selectedOfficeId,
			String selectedCompanyId,
			Model model) {
		String startDate = null;
		String endDate = null;
		int yearLater = 1;
		
		SysConfig sysConfig = new SysConfig();
		sysConfig.setAttribute("\'staff_validity\'");
		List<SysConfig> sysConfigList = sysConfigService.findList(sysConfig);
		if(sysConfigList!=null) {
			for (SysConfig sysConfig2 : sysConfigList) {
				if("staff_validity".equals(sysConfig2.getAttribute())) {
					try {
						yearLater = Integer.parseInt(sysConfig2.getValue());
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						yearLater = 1;
						e.printStackTrace();
					}
				}
			}
		 sysConfigList.get(0).getAttribute();
		}
		
		if(staff.getIsNewRecord()){
			startDate = DateUtils.getDateTime();
			//endDate = DateUtils.getOneYearLater();
			endDate = DateUtils.getYearLater(yearLater);
		}else{
			if(!CollectionUtils.isEmpty(staff.getFingerInfoList())){
				FingerInfo fingerInfo = staff.getFingerInfoList().get(0);
				startDate = DateUtils.formatDate(fingerInfo.getStartDate());
				endDate = DateUtils.formatDate(fingerInfo.getEndDate());
			}
		}
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("yearLater", yearLater);
		
		if (staff.getFingerInfoList() != null && staff.getFingerInfoList().size() > 0) {
			String template = StringUtils.bytesToHexString(staff.getFingerInfoList().get(0).getFingerTemplate());
			String backup = StringUtils.bytesToHexString(staff.getFingerInfoList().get(0).getBackupFp());
			String coerceTemplate = StringUtils.bytesToHexString(staff.getFingerInfoList().get(0).getCoerceTemplate());
			model.addAttribute("template", Encodes.encodeBase64(template));
			model.addAttribute("backup", Encodes.encodeBase64(backup));
			model.addAttribute("cTemplate", Encodes.encodeBase64(coerceTemplate));
			String fingerNum = staff.getFingerInfoList().get(0).getFingerNum();
			if(fingerNum.length()>7) {
				staff.getFingerInfoList().get(0).setFingerNum(fingerNum.substring(fingerNum.length()-7));
			}else if(fingerNum.length()<7) {
				staff.getFingerInfoList().get(0).setFingerNum(StaffHelper.buildFingerNum(staff.getFingerInfoList().get(0).getAreaId(), fingerNum));
			}
		}
		
		if(null != staff.getOffice() && !org.apache.commons.lang.StringUtils.isBlank(staff.getOffice().getId())) {
			Office office = officeService.get(staff.getOffice().getId());
			staff.setOffice(office);
		}
		
		if(null != staff.getCompany() && !org.apache.commons.lang.StringUtils.isBlank(staff.getCompany().getId())) {
			Company company = companyService.get(staff.getCompany().getId());
			staff.setCompany(company);
			if("1".equals(company.getCompanyType())) {
				staff.setStaffType("2");
			}
		}

		model.addAttribute("staff", staff);
		model.addAttribute("selectedOfficeId", selectedOfficeId);
		model.addAttribute("selectedCompanyId", selectedCompanyId);
		return "modules/guard/staffForm";
	}
	
	private void validateSaveStaff( Staff staff, BindingResult result) {
		if(staffService.countByWorkNum(staff.getId(), staff.getWorkNum()) > 0) {
			result.rejectValue("workNum", "duplicate", "人员工号已经存在");
		}
		if(staffService.countByIdentifyNumber(staff.getId(), staff.getIdentifyNumber()) > 0) {
			result.rejectValue("identifyNumber", "duplicate", "证件号码已经存在");
		}
		
		if(!CollectionUtils.isEmpty( staff.getFingerInfoList())) {
			String fingerNum = staff.getFingerInfoList().get(0).getFingerNum();
			/*if(staffService.countByFingerNum(staff.getId(), staff.getArea().getId(), fingerNum) > 0) {
				result.rejectValue("fingerInfoList[0].fingerNum", "duplicate", "指 纹号已经存在");
			}*/
			if("0".equals(staff.getStaffType())) {
				fingerNum = "11"+fingerNum;
			}else if("1".equals(staff.getStaffType())) {
				fingerNum = "21"+fingerNum;
			}else {
				fingerNum = "31"+fingerNum;
			}
			String fingerStaffId = fingerInfoDao.findByFingerNum(fingerNum);
			if(fingerStaffId!=null) {
				if(staff.getIsNewRecord()) {
					result.rejectValue("fingerInfoList[0].fingerNum", "duplicate", "指 纹号已经存在");
				}else {
					if(!fingerStaffId.equals(staff.getId())) {
						result.rejectValue("fingerInfoList[0].fingerNum", "duplicate", "指 纹号已经存在");
					}
				}
			}
			String cardNum = staff.getFingerInfoList().get(0).getCardNum();
			if(staffService.countByCardNum(staff.getId(), cardNum) > 0) {
				result.rejectValue("fingerInfoList[0].cardNum", "duplicate", "IC卡号已经存在");
			}
		}
		
	}

	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "save")
	public String save(@Validated Staff staff, 
			BindingResult result,  
			Model model, 
			RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId") String selectedOfficeId,
			@RequestParam("selectedCompanyId") String selectedCompanyId) throws IOException {
		model.addAttribute("staff", staff);
		if (!beanValidator(model, staff)) {
			return backForm(staff,  selectedOfficeId,selectedCompanyId,model);
		}
		
		validateSaveStaff(staff, result);
		
		if(result.hasErrors()) {
			return backForm(staff,  selectedOfficeId,selectedCompanyId,model);
		}
		
		// 判断是否是修改
		if (!staff.getIsNewRecord()) {
			// 判断是否在排班（false为在排班）
			if (deletePD(staff) == false) {
				// 判断是否提交过来的人员为离职或者休假
				if ("0".equals(staff.getWorkStatus()) || "2".equals(staff.getWorkStatus())) {
					addMessage(redirectAttributes, "不可修改在排班人员工作状态");
					return "redirect:" + Global.getAdminPath() + "/guard/staff/?repage";
				}
			}
		} else {
			// 插入
			List<Staff> staffList = staffService.findList(new Staff());
			int max = (int) UserUtils.getCache(UserUtils.CACHE_FUNCTION_STAFF);
			logger.error("最大添加人数=====："+max);
			if (staffList.size() >= max) {
				addMessage(redirectAttributes, "最多添加" + max+ "个人员");
				return "redirect:" + Global.getAdminPath() + "/guard/staff/?repage";
			}
		}

		if (staff.getStaffImageList() != null && staff.getStaffImageList().size() > 0) {
			for (StaffImage staffImage : staff.getStaffImageList()) {
				if (staffImage.getFile() != null) {
					// 车辆照片处理
					String slack = File.separator;
					String rootPath = getJarPath()+projectPath;
					String uuid = IdGen.uuid();
					File root = new File(rootPath);
					root.mkdirs();
					String fileName = staffImage.getFile().getOriginalFilename();
					//文件选择的照片
					if (!StringUtils.isEmpty(fileName)) {
						if(fileName.contains("\\")) {
							fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
						}
						String coverPath = slack + "staff" + slack + uuid + slack + fileName;
						String absPath = rootPath + coverPath;

						if (staffImage.getFile().getSize() > 0) {
							// 复制文件
							File newFile = new File(absPath);
							newFile.mkdirs();
							newFile.delete();
							byte[] staffByte =staffImage.getFile().getBytes();
							staffImage.getFile().transferTo(newFile);
							coverPath = "/images/staff/" + uuid + "/" + fileName;
							staffImage.setImgData(staffByte);
							staffImage.setImagePath(coverPath);
						}
						//界面拍照
					} else {
						 fileName = staffImage.getImagePath().substring(staffImage.getImagePath().lastIndexOf("/")+1,staffImage.getImagePath().length());

						String coverPath = slack + "staff" + slack + uuid + slack + fileName;
						String absPath = rootPath + coverPath;
						File oldFile = new File(getJarPath()+"/"+staffImage.getImagePath());
						if(oldFile.exists()&&oldFile.isFile()){
							File newFile = new File(absPath);
							newFile.mkdirs();
							newFile.delete();
							byte[] staffByte = FileUtils.readFileToByteArray(oldFile);
							FileUtils.copyFile(oldFile,newFile);
							//oldFile.transferTo(newFile);
							oldFile.delete();
							coverPath = "/images/staff/" + uuid + "/" + fileName;
							staffImage.setImgData(staffByte);
							staffImage.setImagePath(coverPath);
						}
					}
				}
			}
		}
		try {
			staffService.save(staff);
		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存人员信息成功");
		
		if(!StringUtils.isBlank(selectedOfficeId) && selectedOfficeId.equals(staff.getOffice().getId())){
			return "redirect:" + Global.getAdminPath() + "/guard/staff/list?office.id="+selectedOfficeId;
		}else if(!StringUtils.isBlank(selectedCompanyId) && selectedCompanyId.equals(staff.getCompany().getId())){
			return "redirect:" + Global.getAdminPath() + "/guard/staff/list?company.id="+selectedCompanyId;
		}
		return "redirect:" + Global.getAdminPath() + "/guard/staff/?repage";
	}

	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "saveJson")
	@ResponseBody
	public String saveJson(Staff staff, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, staff)) {
			return form(staff,  null,null, model);
		}
		try {
			staffService.save(staff);
		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存人员信息成功");
		return "{}";
	}

	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "download")
	public String download(Staff staff, 
			Model model, 
			RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId") String selectedOfficeId,
			@RequestParam("selectedCompanyId") String selectedCompanyId) {
		//add 2019-9-21 非专员 office字段为空 导致校验不通过
		if(!Staff.STAFF_TYPE_HANDOVER_CLERK.equals(staff.getStaffType())){
			staff.setOffice(new Office());
		}
		if (!beanValidator(model, staff)) {
			return form(staff, selectedOfficeId,selectedCompanyId, model);
		}
		staffService.insentDownload(staff, DownloadEntity.DOWNLOAD_TYPE_ADD);
		addMessage(redirectAttributes, "人员同步成功");
		return backListPage(selectedOfficeId, selectedCompanyId, model);
	}

	/**
	 * 审批前检查维保人员照片是否上传和指纹
	 * @param staff
	 * @param model
	 * @param redirectAttributes
	 * @return json
	 */
	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "validMaintenance")
	@ResponseBody
	public String validMaintenance(Staff staff, Model model, RedirectAttributes redirectAttributes){
		List<FingerInfo> fingers = fingerInfoDao.findList(new FingerInfo(staff));
		List<String> returnStringList = new ArrayList<>();
		String xiepo = DictUtils.getDictValue("胁迫指纹","audit_valid","0");
		String zjz = DictUtils.getDictValue("证件照片","audit_valid","0");
		for (FingerInfo fingerInfo : fingers) {
			//不强迫胁迫指纹校验  就简单提示
			if(ArrayUtils.isEmpty(fingerInfo.getCoerceTemplate())&&"0".equals(xiepo)){
				returnStringList.add("无胁迫指纹");
			}
			/*if(StringUtils.isEmpty(fingerInfo.getCoercePwd())){
				returnStringList.add("无胁迫密码");
			}*/
		}

	List<StaffImage> staffImages = staffImageDao.findList(new StaffImage(staff));
		//需要校验证件照片 则跳过此校验 直接进入表单校验
		if("1".equals(zjz)){
			return  JsonMapper.toJsonString(returnStringList);
		}
		if(staffImages!=null){
			if(staffImages.size()<2){
				returnStringList.add("无证件照片");
				return JsonMapper.toJsonString(returnStringList);
			}
		}
		int i=0;
		for (StaffImage staffImage : staffImages) {
			if("1".equals(staffImage.getImageType())){
				if(!ArrayUtils.isEmpty(staffImage.getImgData())){
					i++;
				}
			}
		}
		if(i==0){
			returnStringList.add("无证件照片");
			return JsonMapper.toJsonString(returnStringList);
		}
		return  JsonMapper.toJsonString(returnStringList);
	}

	@RequiresPermissions("guard:staff:audit")
	@RequestMapping(value = "audit")
	//审核通过变成成批
	public String audit(Staff staff, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId") String selectedOfficeId,
			@RequestParam("selectedCompanyId") String selectedCompanyId) {
		if(!validateAuditStaff(staff, redirectAttributes, selectedOfficeId, selectedCompanyId)){
			return backListPage(selectedOfficeId, selectedCompanyId, model);
		}
		try {
			staffService.audit(staff.getId());
		} catch (Exception e) {
			throw new RuntimeException("审核数据：" + e.toString(), e);
		}
		return backListPage(selectedOfficeId, selectedCompanyId, model);
	}

	private String backListPage(String selectedOfficeId,
			String selectedCompanyId,
			Model model) {
		model.addAttribute("selectedOfficeId", selectedOfficeId);
		model.addAttribute("selectedCompanyId", selectedCompanyId);
		if(!StringUtils.isBlank(selectedOfficeId)){
			return "redirect:" + Global.getAdminPath() + "/guard/staff/list?office.id="+selectedOfficeId;
		}else if(!StringUtils.isBlank(selectedCompanyId)){
			return "redirect:" + Global.getAdminPath() + "/guard/staff/list?company.id="+selectedCompanyId;
		}
		return "redirect:" + Global.getAdminPath() + "/guard/staff/?repage";
	}

	private boolean validateAuditStaff(Staff staff,RedirectAttributes redirectAttributes,String selectedOfficeId, String selectedCompanyId){
		if(Staff.STAFF_TYPE_SUPERCARGO.equals(staff.getStaffType()) || Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType()) ){
			List<FingerInfo> fingers = fingerInfoDao.findList(new FingerInfo(staff));
			if(fingers.isEmpty()){
				addMessage(redirectAttributes, "请录入指纹");
				return false;
			}else{
				for (FingerInfo fingerInfo : fingers) {
					if(ArrayUtils.isEmpty(fingerInfo.getFingerTemplate())){
						addMessage(redirectAttributes, "请录主指纹");
						return false;
					}else if(ArrayUtils.isEmpty(fingerInfo.getBackupFp())){
						//审核条件必须校验备份指纹
						if("1".equals(DictUtils.getDictValue("备份指纹","audit_valid","1"))){
							addMessage(redirectAttributes, "请录备指纹");
							return false;
						}
					}else if(Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType())&&ArrayUtils.isEmpty(fingerInfo.getCoerceTemplate())){
						//维保员审核条件必须校验胁迫指纹 add 2019-9-29
						if("1".equals(DictUtils.getDictValue("胁迫指纹","audit_valid","1"))){
							addMessage(redirectAttributes, "请胁迫指纹");
							return false;
						}
					}
				}
			}
		}
		List<StaffImage> staffImages = staffImageDao.findList(new StaffImage(staff));
		if(staffImages.isEmpty()){
			addMessage(redirectAttributes, "请上传头像");
			return false;
		}else{
			//校验头像照片
			for (StaffImage staffImage : staffImages) {
				if("1".equals(staffImage.getImageType())){
					continue;
				}
				if(ArrayUtils.isEmpty(staffImage.getImgData())){
					addMessage(redirectAttributes, "请上传头像");
					return false;
				}
			}
			//维保员根据设置 判断是否强制校验证件照
			if(Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType())){
				if("1".equals(DictUtils.getDictValue("证件照片","audit_valid","0"))){
					int i=0;
					for (StaffImage staffImage : staffImages) {
						if("0".equals(staffImage.getImageType())){
							continue;
						}
						i++;
						if(ArrayUtils.isEmpty(staffImage.getImgData())){
							addMessage(redirectAttributes, "请上传证件照片");
							return false;
						}
					}
					if(i==0){
						addMessage(redirectAttributes, "请上传证件照片");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@RequiresPermissions("guard:staff:approval")
	@RequestMapping(value = "approval")
	//审核通过变成成批
	public String approval(Staff staff, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId") String selectedOfficeId,
			@RequestParam("selectedCompanyId") String selectedCompanyId) {
		if(!validateAuditStaff(staff, redirectAttributes, selectedOfficeId, selectedCompanyId)){
			return backListPage(selectedOfficeId, selectedCompanyId, model);
		}
		
		try {
			staffService.approval(staff.getId());
		} catch (Exception e) {
			throw new RuntimeException("审批数据：" + e.toString(), e);
		}
		return backListPage(selectedOfficeId, selectedCompanyId, model);
	}

	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "delete")
	public String delete(Staff staff, 
			RedirectAttributes redirectAttributes,
			Model model,
			@RequestParam("selectedOfficeId") String selectedOfficeId,
			@RequestParam("selectedCompanyId") String selectedCompanyId) {
		if (deletePD(staff)) {
			staffService.delete(staff);
			addMessage(redirectAttributes, "删除人员信息成功");
		} else {
			addMessage(redirectAttributes, "不能删除正在排班的人员");
		}
		
		return backListPage(selectedOfficeId, selectedCompanyId, model);
	}

	private boolean deletePD(Staff staff) {
		List<ClassPersonInfo> personInfoList = classPersonInfoService.findList(new ClassPersonInfo());
		for (int i = 0; i < personInfoList.size(); i++) {
			if (personInfoList.get(i).getPersonId().equals(staff.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 导出用户数据
	 * 
	 * @param staff
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:staff:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Staff staff, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "人员数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Staff> page = staffService.findPage(new Page<Staff>(request, response, -1), staff);
			new ExportExcel("人员数据", Staff.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出人员失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/staff/list?repage";
	}

	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(StaffDaorRu staffs, MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/guard/staff/list?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei;
			if (staffs.getSta_hang() != null && !"".equals(staffs.getSta_hang())) {
				ei = new ImportExcel(file, Integer.parseInt(staffs.getSta_hang()), 0);
			} else {
				ei = new ImportExcel(file, 1, 0);
			}
			List<Staff> list = ei.getDataStaffList(StaffDaorRu.class, staffs);
			for (int i = 0; i < list.size(); i++) {
				List<Office> officeList = officeService.findAll(new Office());
				List<Area> areaList = areaService.findList();
				List<Company> companyList = companyService.findList(new Company());
				if (list != null && list.get(i).getOffice() != null && list.get(i).getOffice().getName() != null) {
					for (int j = 0; j < officeList.size(); j++) {
						if (list.get(i).getOffice().getName().equals(officeList.get(j).getName())) {
							list.get(i).setOffice(officeList.get(j));
							break;
						}
					}
				}
				if (list != null && list.get(i).getArea() != null && list.get(i).getArea().getName() != null) {
					for (int j = 0; j < areaList.size(); j++) {
						if (list.get(i).getArea().getName().equals(areaList.get(j).getName())) {
							list.get(i).setArea(areaList.get(j));
							list.get(i).getFingerInfoList().get(0).setAreaId(areaList.get(j).getId());
							break;
						}
					}
				}
				if (list != null && list.get(i).getCompany() != null
						&& list.get(i).getCompany().getShortName() != null) {
					for (int j = 0; j < companyList.size(); j++) {
						if (list.get(i).getCompany().getShortName().equals(companyList.get(j).getShortName())) {
							list.get(i).setCompany(companyList.get(j));
							break;
						}
					}
				}
				list.get(i).getFingerInfoList().get(0).setId("");
				byte[] a = {};
				list.get(i).getFingerInfoList().get(0).setBackupFp(a);
				list.get(i).getFingerInfoList().get(0).setFingerTemplate(a);

			}

			// 验证用户是否存在
			for (Staff staff : list) {
				try {
					/* if ("true".equals(checkName("", staff.getName()))) { */
					BeanValidators.validateWithException(validator, staff);
					staffService.save(staff);
					successNum++;
					/*
					 * } else { failureMsg.append("<br/>人员名称  " +
					 * staff.getName() + " 已存在; "); failureNum++; }
					 */
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>人员名称 " + staff.getName() + " 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>人员名称  " + staff.getName() + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条人员，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条人员" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人员失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/staff/list?repage";
	}

	/**
	 * 下载导入用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:staff:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "人员数据导入模板.xlsx";
			List<User> staff = Lists.newArrayList();
			staff.add(UserUtils.getUser());
			new ExportExcel("人员数据", Staff.class, 2).setDataList(staff).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/staff/list?repage";
	}

	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "getNewFingerId")
	@ResponseBody
	@Deprecated
	public String getNewFingerId(Staff staff, Model model, RedirectAttributes redirectAttributes) {
		return null;
	}
	
	@RequiresPermissions("guard:staff:edit")
	@RequestMapping(value = "listAvailableFingers")
	@ResponseBody
	public List<KeyValuePair> listAvailableFingers(@RequestParam("id")String id, 
			@RequestParam("fingerNum")String fingerNum, @RequestParam("staffType")String staffType, 
			Model model, RedirectAttributes redirectAttributes) {
		List<KeyValuePair> list = staffService.listAvailableFingers(id, fingerNum,staffType);
//		List<KeyValuePair> list = new ArrayList<KeyValuePair>();
//		list.add(new KeyValuePair("001_001", "1"));
//		list.add(new KeyValuePair("001_002", "2"));
//		list.add(new KeyValuePair("001_003", "3"));
		return list;
	}
	
	private String getJarPath() {
		ApplicationHome h = new ApplicationHome(getClass());
		File jarF = h.getSource();
		return jarF.getParentFile().toString();
	}

}