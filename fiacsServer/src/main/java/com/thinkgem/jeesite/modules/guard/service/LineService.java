/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.FingerInfo;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.dao.EquipmentDao;
import com.thinkgem.jeesite.modules.guard.dao.LineDao;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.guard.dao.LineNodesDao;

/**
 * 线路信息Service
 * 
 * @author Jumbo
 * @version 2017-06-29
 */
@Service
@Transactional(readOnly = true)
public class LineService extends CrudService<LineDao, Line> {
	LineDao lineDao = SpringContextHolder.getBean(LineDao.class);
	@Autowired
	private LineNodesDao lineNodesDao;

	public Line get(String id) {
		Line line = super.get(id);
		if(line==null){
			return null;
		}
		if (line.getArea()!=null && "0".equals(line.getArea().getId())) {
			line.getArea().setName("所有区域");
		}
		line.setLineNodesList(lineNodesDao.findList(new LineNodes(line)));
		return line;
	}

	public List<Line> findList(Boolean isAll) {
		List<Line> lineList = lineDao.findAllList(new Line());
		for (int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getArea()!=null && "0".equals(lineList.get(i).getArea().getId())) {
				lineList.get(i).getArea().setName("所有区域");
			}
		}
		return lineList;
	}

	public List<Line> findList(Line line) {
		List<Line> lineList = super.findList(line);
		for (int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getArea()!=null &&  "0".equals(lineList.get(i).getArea().getId())) {
				lineList.get(i).getArea().setName("所有区域");
			}
		}
		return lineList;
	}

	public Page<Line> findPage(Page<Line> page, Line lineRequest) {
		lineRequest.getSqlMap().put("dsf", dataScopeFilterArea(lineRequest.getCurrentUser(), "a4", "u"));
		Page<Line> pageLine = super.findPage(page, lineRequest);
		
		for (Line line : pageLine.getList()) {
			if (line.getArea()!=null &&  "0".equals(line.getArea().getId())) {
				line.getArea().setName("所有区域");
			}
			line.setLineNodesList(lineNodesDao.findList(new LineNodes(line)));
		}
		
		return pageLine;
	}

	@Transactional(readOnly = false)
	public void save(Line line) {
		boolean messageType;
		if (line.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		super.save(line);
		if(messageType){
			LogUtils.saveLog(Servlets.getRequest(), "新增线路:["+line.getId()+"]");
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改线路:["+line.getId()+"]");
		}
		
		for (LineNodes lineNodes : line.getLineNodesList()) {
			if (lineNodes.getId() == null) {
				continue;
			}
			if (LineNodes.DEL_FLAG_NORMAL.equals(lineNodes.getDelFlag())) {
				if (StringUtils.isBlank(lineNodes.getId())) {
					lineNodes.setLineId(line.getId());
					lineNodes.preInsert();
					lineNodesDao.insert(lineNodes);
					LogUtils.saveLog(Servlets.getRequest(), "线路:["+line.getId()+"],新增节点:["+lineNodes.getId()+"]");
				} else {
					lineNodes.preUpdate();
					lineNodesDao.update(lineNodes);
					LogUtils.saveLog(Servlets.getRequest(), "线路:["+line.getId()+"],修改节点:["+lineNodes.getId()+"]");
				}
			} else {
				lineNodesDao.delete(lineNodes);
				LogUtils.saveLog(Servlets.getRequest(), "线路:["+line.getId()+"],删除节点:["+lineNodes.getId()+"]");
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(Line line) {
		super.delete(line);
		lineNodesDao.delete(new LineNodes(line));
		
		LogUtils.saveLog(Servlets.getRequest(), "删除线路:["+line.getId()+"]");
	}

	public int countByLineName(String id, String lineName) {
		if(org.apache.commons.lang.StringUtils.isBlank(lineName)) {
			return 0;
		}
		Line line = new Line();
		line.setId(id);
		line.setLineName(lineName);
		return super.countByColumnExceptSelf(line);
	}

}