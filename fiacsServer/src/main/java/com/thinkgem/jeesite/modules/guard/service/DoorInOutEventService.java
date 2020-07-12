/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiacs.common.util.Constants;
import com.fiacs.common.util.EncodeToXmlUtil;
import com.fiacs.common.util.SeqnumUtil;
import com.fiacs.common.util.SocketUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.DoorInOutEventDao;
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEvent;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 门禁出入事件Service
 * @author zgx
 * @version 2018-12-29
 */
@Service
@Transactional(readOnly = true)
public class DoorInOutEventService extends CrudService<DoorInOutEventDao, DoorInOutEvent> {
	@Autowired
	private EquipmentService equipmentService;

	@Override
	public DoorInOutEvent get(String id) {
		return super.get(id);
	}
	
	@Override
	public List<DoorInOutEvent> findList(DoorInOutEvent doorInOutEvent) {
		for (Role r : doorInOutEvent.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				doorInOutEvent.getSqlMap().put("dsf", dataScopeFilter(doorInOutEvent.getCurrentUser(), "c", ""));
			} else {
				doorInOutEvent.getSqlMap().put("dsf", dataScopeFilter(doorInOutEvent.getCurrentUser(), "b", ""));
			}
		}
		
		
		return super.findList(doorInOutEvent);
	}
	
	@Override
	public Page<DoorInOutEvent> findPage(Page<DoorInOutEvent> page, DoorInOutEvent doorInOutEvent) {
		for (Role r : doorInOutEvent.getCurrentUser().getRoleList()) {
			if ( r.getDataScope().equals("3")) {
				/*DoorInOutEvent.getSqlMap().put("dsf", dataScopeFilter(DoorInOutEvent.getCurrentUser(), "c", ""));*/
				doorInOutEvent.getSqlMap().put("dsf", dataScopeFilter(doorInOutEvent.getCurrentUser(), "b", ""));
			}else if(r.getDataScope().equals("2")){
				doorInOutEvent.getSqlMap().put("dsf", dataScopeFilter(doorInOutEvent.getCurrentUser(), "c", ""));
			} else {
				doorInOutEvent.getSqlMap().put("dsf", dataScopeFilter(doorInOutEvent.getCurrentUser(), "b", ""));
			}
		}
		return super.findPage(page, doorInOutEvent);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(DoorInOutEvent doorInOutEvent) {
		super.save(doorInOutEvent);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(DoorInOutEvent doorInOutEvent) {
		super.delete(doorInOutEvent);
	}
	
	public List<DoorInOutEvent> getFeeds(String latestId, String nodes){
		DoorInOutEvent doorInOutEvent = new DoorInOutEvent();
		doorInOutEvent.setId(latestId);
		doorInOutEvent.setNodes(nodes);
		return super.dao.getFeeds(doorInOutEvent);
	}

	public Map<String,Object> controlDoor(String resetType,String nodes){
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("code",0);
		try {
			if (StringUtils.isEmpty(nodes)) {
				returnMap.put("errorMsg", "请选择要控制的网点");
				return returnMap;
			}
			Map<String, Object> map = new HashMap<>();
			String[] nodeArray = nodes.split(",");
			for (String node : nodeArray) {
				Equipment equip = equipmentService.getByOfficeId(node);
				if (equip == null) {
					continue;
				}
				returnMap.put("code",1);
				map.put("type", Constants.CONTROL_DOOR_TYPE);
				map.put("EquipSN", equip.getSerialNum());
				map.put("SocketIP", equip.getIp());
				map.put("SocketPort", equip.getPort());
				map.put("ResetType", resetType);

				String xml = EncodeToXmlUtil.controlDoor(map, SeqnumUtil.getNextSeqNum());
				byte[] result = SocketUtil.sendMsg("127.0.0.1", 10002, xml.getBytes("UTF-8"));
			}
			return returnMap;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			returnMap.put("errorMsg", "前端设备超时未应答，请检查前端设备与网络通讯是否正常");
			return returnMap;
		} catch (ConnectException e) {
			e.printStackTrace();
			returnMap.put("errorMsg", "连接通讯服务未成功，请检查通讯服务是否正常启动");
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("errorMsg", "操控失败");
			return returnMap;
		}
	}
}