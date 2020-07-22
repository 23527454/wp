/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessParaInfo;
import com.thinkgem.jeesite.modules.tbmj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构Controller
 * 
 * @author ThinkGem
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

//	private String status = "";
	@Autowired
	private AreaService areaService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private AccessParaInfoService accessParaInfoService;
	@Autowired
	private DictService dictService;

	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return officeService.get(id);
		} else {
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "" })
	public String index(Office office, Model model) {
		// model.addAttribute("list", officeService.findAll());
		return "modules/sys/officeIndex";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "list" })
	public String list(Office office, 
			Model model) {
		List<Office> officeList = officeService.findList(new Office());
		model.addAttribute("list", officeList);
		model.addAttribute("selectedOfficeId", office.getId());
//		model.addAttribute("status", status);
//		status = "";
		return "modules/sys/officeList";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "refreshParent" })
	public String refreshParent(@RequestParam("selectedOfficeId") String selectedOfficeId, 
			Model model) {
		model.addAttribute("selectedOfficeId", selectedOfficeId);
		return "modules/sys/officeRefreshParent";
	}


	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, 
			@RequestParam("selectedOfficeId") String selectedOfficeId,
			Model model) {
		model.addAttribute("selectedOfficeId", selectedOfficeId);
		User user = UserUtils.getUser();
		if(office.getIsNewRecord()){
			if (office.getParent() == null || office.getParent().getId() == null) {
				office.setParent(user.getOffice()); 
			}
			
			office.setParent(officeService.get(office.getParent().getId()));
			if (office.getArea() == null) {
				if (office.getParent() != null) {
					if (office.getParent().getArea() != null) {
						office.setArea(office.getParent().getArea());
					} else {
						if (areaService.findList(new Area()).size()>0) {
							office.setArea(areaService.findList(new Area()).get(0));
						}
					}
				} else {
					if (areaService.findList(new Area()).size()>0) {
						office.setArea(areaService.findList(new Area()).get(0));
					}
				}
			}
			office.setType(Office.TYPE_YYWD);
			if(office.getParent()!=null && (Office.TYPE_ZXJK.equals(office.getParent().getType()) || Office.TYPE_ZYYWD.equals(office.getParent().getType()))){
				office.setParent(null);
			}
			if(office.getParent()!=null && Office.TYPE_YYWD.equals(office.getParent().getType())){
				office.setType(Office.TYPE_ZYYWD);
			}
			
// 自动获取排序号
//			if (StringUtils.isBlank(office.getId()) && office.getParent() != null) {
//				int size = 0;
//				List<Office> list = officeService.findAll(new Office());
//				for (int i = 0; i < list.size(); i++) {
//					Office e = list.get(i);
//					if (e.getParent() != null && e.getParent().getId() != null
//							&& e.getParent().getId().equals(office.getParent().getId())) {
//						size++;
//					}
//				}
//				office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size + 1 : 1), 3, "0"));
//			}
		}
		
		return backForm(office, model);
	}

	private String backForm(Office office, Model model) {
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, BindingResult result, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId") String selectedOfficeId)  {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/list?id=" + office.getParentId() + "&parentIds=" + office.getParentIds();
		}
		if(!"1".equals(office.getId())){
			if(StringUtils.isEmpty(office.getParent().getName())){
				result.rejectValue("parent", "duplicate", "上级网点不能为空");
			}
		}
		if (!beanValidator(model, office)) {
			return form(office, selectedOfficeId, model);
		}
		
		if(officeService.countByName(office.getId(), office.getName(), office.getArea()) > 0) {
			result.rejectValue("name", "duplicate", "网点名称已经存在");
		}
		if(officeService.countByCode(office.getId(), office.getCode())> 0) {
			result.rejectValue("code", "duplicate", "网点编码已经存在");
		}
		if(Office.TYPE_ZXJK.equals(office.getType()) && officeService.countZxjkByAreaId(office.getId(), office.getArea())> 0){
			result.rejectValue("type", "duplicate", "该区域已经存在中心金库");
		}	
		//如果挂靠设备 则不需要修改网点类型为支行和分行
		if(Office.TYPE_YJZH.equals(office.getType())||Office.TYPE_YJFH.equals(office.getType())) {
			if(!office.getIsNewRecord()) {
				Equipment equ = new Equipment();
				equ.setOffice(office);
				Equipment resultEqu = equipmentService.findOne(equ);
				if(resultEqu!=null) {
					result.rejectValue("type", "duplicate", "网点已关联设备，不可更新类型为一级支行或一级分行");
				}
			}
		}
		if(result.hasErrors()) {
			return backForm(office, model);
		}
		
		try {
			Office parent = officeService.get(office.getParent().getId());
			if(office.getGrade() == null){
				try {
					office.setGrade(String.valueOf(Integer.parseInt(parent.getGrade()) + 1));
				} catch (Exception e) {
					office.setGrade("1");
				}
			}
			office.setUseable(String.valueOf(1));
			officeService.save(office);
		} catch (Exception e) {
			throw new ServiceException("保存数据错误", e);  
		}
		
//		if (office.getChildDeptList() != null) {
//			Office childOffice = null;
//			for (String id : office.getChildDeptList()) {
//				childOffice = new Office();
//				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
//				childOffice.setParent(office);
//				childOffice.setArea(office.getArea());
//				childOffice.setType("2");
//				childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade()) + 1));
//				childOffice.setUseable(Global.YES);
//				officeService.save(childOffice);
//			}
//		}else{
//			office.setGrade(String.valueOf(1));
//		}

		addMessage(redirectAttributes, "保存网点'" + office.getName() + "'成功");
		String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
//		status = "refresh";
		
		if(!office.getId().equals(selectedOfficeId)){
			selectedOfficeId = "";
		}
		return "redirect:" + adminPath + "/sys/office/refreshParent?selectedOfficeId="+selectedOfficeId;
//		return "redirect:" + adminPath + "/sys/office/?repage";
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Office office, RedirectAttributes redirectAttributes, Model model) {
		JSONObject json = new JSONObject();
		if (Global.isDemoMode()) {
			json.put("status", "ERROR");
			json.put("message", "不允许操作！");
//			addMessage(redirectAttributes, "不允许操作！");
			return json.toString();
		}
		// if (Office.isRoot(id)){
		// addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
		// }else{

		List<Office> officeList = officeService.findParentIDList(office);
		List<Equipment> equipmentList = officeService.equipmentList();
		for (int i = 0; officeList != null && i < officeList.size(); i++) {
			for (int n = 0; equipmentList != null && n < equipmentList.size(); n++) {
				if (deleteEquipmentJudge(officeList, i, equipmentList, n)) {
					json.put("status", "ERROR");
					json.put("message", "不可删除，存在设备" + officeList.get(i).getName());
					return json.toString();
				}
			}
		}
		officeService.delete(office);
		json.put("status", "SUCCESS");

		return json.toString();
	}

	// 删除时判断是否存在删除网页与设备相同，相同则不可删除
	private boolean deleteEquipmentJudge(List<Office> officeList, int i, List<Equipment> equipmentList, int n) {
		if (officeList != null && equipmentList != null
				&& officeList.get(i).getId().equals(equipmentList.get(n).getId())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取机构JSON数据。
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade
	 *            显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treezeeData")
	//@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treezeeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(false, new Office());
		//Map<String, Object> maps = Maps.newHashMap();
//		if (list.size() > 0) {
//			maps.put("id", "0");
//			maps.put("pId", "0");
//			maps.put("pIds", "0,1,");
//			maps.put("type", null);
//			maps.put("name", "所有网点");
//			mapList.add(maps);
//		}
	/*for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId)
					|| (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && (type.equals("2") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("status", "office");
				map.put("type", e.getType());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}*/
		Map<String, Map<String, Object>> nodes = new HashMap<String, Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if (que(e) == false) {
				retriveNodes(extId, type, grade, nodes, list, e);
			}
		}

		//List<Map<String, Object>> mapList = Lists.newArrayList();
		for (Map<String, Object> node : nodes.values()) {
			mapList.add(node);
		}
		return mapList;
	}


	/**
	 * 门禁信息——基本信息，Tree
	 * @param extId
	 * @param type
	 * @param grade
	 * @param isAll
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treezeeData2")
	public List<Map<String, Object>> treezeeData2(@RequestParam(required = false) String extId,
												  @RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
												  @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		//返回的数据
		List<Map<String, Object>> mapList = Lists.newArrayList();
		//获取所有机构
		List<Office> list = officeService.findList(false, new Office());
		Map<String, Map<String, Object>> nodes = new HashMap<String, Map<String, Object>>();
		//遍历机构
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if (que(e) == false) {
				retriveNodes(extId, type, grade, nodes, list, e);
			}
		}

		List<Map<String, Object>> newNodes=Lists.newArrayList();
		//查询所有门号
		List<Dict> dicts=dictService.findListByType("door_pos");
		for (Map<String, Object> node : nodes.values()) {
			Equipment equipment = equipmentService.getByOfficeId(node.get("id").toString());
			Map<String, Object> newNode = null;
			if(equipment!=null && equipment.getId()!=null) {
				List<AccessParaInfo> accessParaInfos = accessParaInfoService.getByEId(equipment.getId());
				for (AccessParaInfo a : accessParaInfos) {
					newNode = new HashMap<>();
					for(Dict d:dicts){
						if(d.getValue().equals(a.getDoorPos())){
							newNode.put("name", d.getLabel());
						}
					}
					newNode.put("pId", node.get("id").toString());
					newNode.put("id", "d"+a.getId());
					newNode.put("eid", equipment.getId());
					newNode.put("pIds", node.get("pIds").toString() + node.get("id").toString() + ",");
					newNode.put("type", 0);
					newNode.put("status", "office");
					newNodes.add(newNode);
				}
			}
			mapList.add(node);
		}
		for(Map<String, Object> node:newNodes){
			mapList.add(node);
		}
		return mapList;
	}

	/**
	 * 防盗参数，Tree
	 * @param extId
	 * @param type
	 * @param grade
	 * @param isAll
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treezeeData3")
	public List<Map<String, Object>> treezeeData3(@RequestParam(required = false) String extId,
												  @RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
												  @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(false, new Office());
		Map<String, Map<String, Object>> nodes = new HashMap<String, Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if (que(e) == false) {
				retriveNodes(extId, type, grade, nodes, list, e);
			}
		}

		List<Map<String, Object>> newNodes=Lists.newArrayList();
		for (Map<String, Object> node : nodes.values()) {
			Equipment equipment = equipmentService.getByOfficeId(node.get("id").toString());
			Map<String, Object> newNode = null;
			if(equipment!=null && equipment.getId()!=null) {
				List<Dict> dicts=dictService.findListByType("site_type");
				String pre="";
				for(Dict d:dicts){
					if (equipment.getSiteType().equals(d.getValue())){
						newNode = new HashMap<>();
						newNode.put("name", d.getLabel());
						newNode.put("pId", node.get("id").toString());
						newNode.put("id", "a" + equipment.getId());
						newNode.put("pIds", node.get("pIds").toString() + node.get("id").toString() + ",");
						newNode.put("type", 0);
						newNode.put("status", "office");
						newNodes.add(newNode);
					}
				}
			}
			mapList.add(node);
		}
		for(Map<String, Object> node:newNodes){
			mapList.add(node);
		}
		return mapList;
	}

	/**
	 * 设备数据
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade
	 *            显示级别
	 * @param response
	 * @return
	 */
	
	//本项目使用该结构树
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	//@RequestMapping(value = "treezeeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		Map<String, Map<String, Object>> nodes = new HashMap<String, Map<String, Object>>();
		
		List<Office> list = officeService.findList(false, new Office());
		/*if (list.size() > 0) {
			Map<String, Object> maps = Maps.newHashMap();
			maps.put("id", "0");
			maps.put("pId", "0");
			maps.put("pIds", "0,1,");
			maps.put("type", null);
			maps.put("name", "所有网点");
			nodes.put("0", maps);
		}*/
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if (que(e) == false) {
				retriveNodes(extId, type, grade, nodes, list, e);
			}
		}
		
		List<Map<String, Object>> mapList = Lists.newArrayList();
		for (Map<String, Object> node : nodes.values()) {
			mapList.add(node);
		}
		return mapList;
	}


	private void retriveNodes(String extId, String type, Long grade, Map<String, Map<String, Object>> nodes,
			List<Office> list, Office e) {
		if(null ==e){
			return;
		}

		if ((StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
				&& e.getParentIds().indexOf("," + extId + ",") == -1))
				&& (type == null || (type != null && (type.equals("2") ? type.equals(e.getType()) : true)))
				&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
				&& Global.YES.equals(e.getUseable())) {

			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("pIds", e.getParentIds());
			map.put("status", "office");
			map.put("type", e.getType());
			map.put("name", e.getName());
			nodes.put(e.getId(), map);
			
			if(!StringUtils.isBlank(e.getParentIds())) {
				String[] parentIds = e.getParentIds().split(",");
				for (String parentId : parentIds) {
					if(!nodes.containsKey(parentId)) {
						Office o = this.findById(list, parentId);
						retriveNodes(extId, type, grade, nodes, list, o);
					}
				}
			}
		}
	}
	
	private Office findById(List<Office> list, String id) {
		for (Office office : list) {
			if(office.getId().equals(id)) {
				return office;
			}
		}
		return null;
	}
	

	/**
	 * 获取机构JSON数据。
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade
	 *            显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "equipmentTreeData")
	public List<Map<String, Object>> equipmentTreeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(false, new Office());
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			String s = e.getId();
			if (que(e) && ("2".equals(e.getType()) || "3".equals(e.getType()))) {
				if ((StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
						&& e.getParentIds().indexOf("," + extId + ",") == -1))
						&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
						&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
						&& Global.YES.equals(e.getUseable())) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("pIds", e.getParentIds());
					map.put("type", e.getType());
					map.put("status", "office");
					map.put("name", e.getName());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}

	// 判断数据存在设备表中
	private boolean que(Office e) {
		if(Office.TYPE_YJZH.equals(e.getType()) || Office.TYPE_YJFH.equals(e.getType())){
			return false;
		}
		
		//解决结构树查询慢的问题
	/*	String id = e.getId();
		List<Equipment> equipment = officeService.equipmentList();
		for (int j = 0; j < equipment.size(); j++) {
			Equipment l = equipment.get(j);
			String n = l.getOffice().getId();
			if (n.equals(id)) {
				return false;
			}
		}
		return true;*/
		
		return false;
	}

	/**
	 * 获取机构JSON数据。
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade
	 *            显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "screenTreeData")
	public List<Map<String, Object>> screenTreeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(false, new Office());
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			String s = e.getId();
			if (que(e) == false) {
				if ((StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
						&& e.getParentIds().indexOf("," + extId + ",") == -1))
						&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
						&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
						&& Global.YES.equals(e.getUseable())) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("pIds", e.getParentIds());
					map.put("status", "office");
					map.put("name", e.getName());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}

	// 判断数据存在节点表中
	private boolean BG(String s) {
		List<LineNodes> lineNodes = officeService.lineNodeList();
		for (int j = 0; j < lineNodes.size(); j++) {
			LineNodes l = lineNodes.get(j);
			String n = l.getOffice().getId();
			if (n.equals(s)) {
				return false;
			}
		}
		return true;
	}

	// 确认数据是最下级节点
	private boolean Parent(int i, List<Office> list) {
		Office e = list.get(i);
		for (int n = 0; n < list.size(); n++) {
			Office s = list.get(n);
			String a = e.getId();
			String b = s.getParentId();
			if (a.equals(b)) {
				// 这个e就不要
				return false;
			}
		}
		return true;
	}

	@RequiresPermissions("user")
	@RequestMapping(value = "getArea")
	@ResponseBody
	public String getArea(Office office, Model model, RedirectAttributes redirectAttributes) {
		Office com = get(office.getId());
		JSONObject obj = new JSONObject();
		if (!"".equals(com) || com != null) {
			obj.put("areaId", com.getArea().getId());
		}
		return obj.toString();
	}

}
