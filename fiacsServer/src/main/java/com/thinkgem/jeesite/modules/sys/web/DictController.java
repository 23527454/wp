/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;

/**
 * 字典Controller
 * @author ThinkGem
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@Value("${projectPath.logoPath}")
	private String logoPath;
	
	@ModelAttribute
	public Dict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}
	
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = {"list", ""})
	public String list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
        Page<Dict> page = dictService.findPage(new Page<Dict>(request, response), dict); 
        model.addAttribute("page", page);
		return "modules/sys/dictList";
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		return "modules/sys/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")//@Valid 
	public String save(@RequestParam(value="file") MultipartFile file,Dict dict, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		String filePath = request.getSession().getServletContext().getRealPath("/static");
		System.out.println(request.getSession().getServletContext().getRealPath("/webapp"));
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
		}
		if (!beanValidator(model, dict)){
			return form(dict, model);
		}
		
		if(StringUtils.isEmpty(dict.getId())) {
			if(dict.getType().equals("sysconfig")&&(dict.getLabel().equals("logoImage")||dict.getLabel().equals("mainColor")||dict.getLabel().equals("titleIco"))) {
				addMessage(redirectAttributes, "不能对此类型新增键值对！");
				return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
			}
		}
		//logo上传
		if(dict.getType().equals("sysconfig")&&dict.getLabel().equals("logoImage")) {
			// 获取文件名
        	String fileName = file.getOriginalFilename();
        	System.out.println("上传的文件名为：" + fileName);
        	// 获取文件的后缀名
        	String suffixName = fileName.substring(fileName.lastIndexOf("."));
        	System.out.println("文件的后缀名为：" + suffixName);
        	if(!suffixName.toUpperCase().equals(".JPG")&&!suffixName.toUpperCase().equals(".PNG")) {
        		addMessage(model, "附件格式不支持！");
        		return this.form(dict, model);
        	}else {
        		String path = filePath+logoPath+fileName;
            	File dest = new File(path);
            	// 检测是否存在目录
            	if (!dest.getParentFile().exists()) {
            		dest.getParentFile().mkdirs();// 新建文件夹
            	}
            	try {
					file.transferTo(dest);
					dict.setValue(logoPath+fileName);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 文件写入
        	}
		}else if(dict.getType().equals("sysconfig")&&dict.getLabel().equals("titleIco")) {
			// 获取文件名
        	String fileName = file.getOriginalFilename();
        	System.out.println("上传的文件名为：" + fileName);
        	// 获取文件的后缀名
        	String suffixName = fileName.substring(fileName.lastIndexOf("."));
        	System.out.println("文件的后缀名为：" + suffixName);
        	if(!suffixName.toUpperCase().equals(".ICO")) {
        		addMessage(model, "附件格式不支持！");
        		return this.form(dict, model);
        	}else {
        		String path = filePath +logoPath+ fileName;
            	File dest = new File(path);
            	// 检测是否存在目录
            	if (!dest.getParentFile().exists()) {
            		dest.getParentFile().mkdirs();// 新建文件夹
            	}
            	try {
					file.transferTo(dest);
					dict.setValue(logoPath+fileName);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 文件写入
        	}
		}
		
		dictService.save(dict);
		addMessage(redirectAttributes, "保存字典'" + dict.getLabel() + "'成功");
		return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
	}
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	public String delete(Dict dict, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/dict/?repage";
		}
		if(dict.getType().equals("sysconfig")&&(dict.getLabel().equals("logoImage")||dict.getLabel().equals("mainColor")||dict.getLabel().equals("titleIco"))) {
			addMessage(redirectAttributes, "不能删除此字典！");
			return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
		}
		dictService.delete(dict);
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String type, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Dict dict = new Dict();
		dict.setType(type);
		List<Dict> list = dictService.findList(dict);
		for (int i=0; i<list.size(); i++){
			Dict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<Dict> listData(@RequestParam(required=false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		return dictService.findList(dict);
	}

}
