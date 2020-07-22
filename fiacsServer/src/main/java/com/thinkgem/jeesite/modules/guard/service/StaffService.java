/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import com.thinkgem.jeesite.WebApplication;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.dao.*;
import com.thinkgem.jeesite.modules.guard.entity.*;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 人员信息管理Service
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@Service
@Transactional(readOnly = true)
public class StaffService extends CrudService<StaffDao, Staff> {
	
	private static Logger log = LoggerFactory.getLogger(WebApplication.class); 
	
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private FingerInfoDao fingerInfoDao;
	@Autowired
	private StaffExFamilyDao staffExFamilyDao;
	@Autowired
	private StaffImageDao staffImageDao;
	@Autowired
	private StaffExWorkDao staffExWorkDao;
	OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	@Autowired
	private DownloadPersonService downloadPersonService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private DispatchPersonInfoService dispatchPersonInfoService;
	@Autowired
	private SafeGuardDispatchService safeGuardDispatchService;
	public Staff get(String id) {
		Staff staff = super.get(id);
		if (staff != null) {
			staff.setFingerInfoList(fingerInfoDao.findList(new FingerInfo(staff)));
			staff.setStaffExFamilyList(staffExFamilyDao.findList(new StaffExFamily(staff)));
			staff.setStaffImageList(staffImageDao.findList(new StaffImage(staff)));
			staff.setStaffExWorkList(staffExWorkDao.findList(new StaffExWork(staff)));
		}

		return staff;
	}

	//public List<Staff> findAll(String name,String workNum,Integer pageIndex,Integer size){return staffDao.findAll(name,workNum,pageIndex,size);}

	public List<Staff> findAll(String name,String workNum,String equipmentId,String doorPos,Integer pageIndex,Integer size){return staffDao.findAll(name,workNum,equipmentId,doorPos,pageIndex,size);}

	public List<Staff> findByOfficeId(String officeId){return staffDao.findByOfficeId(officeId);}

	public List<Staff> findList(Staff staff) {
		return super.findList(staff);
	}
	
	public int getNewFingerId(String areaId) {
		FingerInfo fingerInfo = new FingerInfo();
		fingerInfo.setAreaId(areaId);
		fingerInfo.setDelFlag(FingerInfo.DEL_FLAG_NORMAL);
		List<FingerInfo> list = fingerInfoDao.findList(fingerInfo);
		if (list != null) {
			for (int i = 0; i < 500; i++) {
				boolean found = false;
				for (FingerInfo f : list) {
					String fingerNum = f.getFingerNum();
					String sub = fingerNum.substring(3, fingerNum.length());
					if (StringUtils.isNotBlank(sub)) {
						try {
							if (i == Integer.parseInt(sub)) {
								found = true;
								break;
							}
						} catch (NumberFormatException e) {
						}
					}
				}
				if (!found) {
					return i;
				}
			}
		} else {
			return 0;
		}

		return -1;
	}

	public Page<Staff> findPage(Page<Staff> page, Staff staff) {
		for (Role r : staff.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") ) {
				staff.getSqlMap().put("dsf", dataScopeFilter(staff.getCurrentUser(), "o4", "u"));
			} else if(r.getDataScope().equals("3")){
				//staff.getSqlMap().put("dsf", dataScopeFilter(staff.getCurrentUser(), "o3", "u"));
				staff.getSqlMap().put("dsf", dataScopeFilter(staff, "o3", "u"));
			}
			else{
				staff.getSqlMap().put("dsf", dataScopeFilter(staff.getCurrentUser(), "o3", "u"));
			}
		}
		
		
		
		Page<Staff> pageStaff = super.findPage(page, staff);
		//Page<Staff> pageStaff = findPageCom(page, staff);
		for (Staff s : pageStaff.getList()) {
			s.setFingerInfoList(fingerInfoDao.findList(new FingerInfo(s)));
		}
		return pageStaff;
	}
	public Page<Staff> findPageCom(Page<Staff> page, Staff entity) {
		entity.setPage(page);
		//List<Company> comList = new ArrayList<Company>();
		List<Staff> staList = new ArrayList<Staff>();
		if(entity.getCompanyIds().size()>0) {
				for (Company company : entity.getCompanyIds()) {
					entity.setCompany(company);	
					 for (Staff staff : dao.findList(entity)) {
						 staList.add(staff);
					}
			
				
			}
		}
		page.setList(staList);
		return page;
	}
	@Transactional(readOnly = false)
	public void saveStaff(Staff staff) {
		if (StringUtils.isBlank(staff.getId())) {
			staff.preInsert();
			staffDao.insert(staff);
		}
	}

	@Transactional(readOnly = false)
	public void audit(String id) {
		Staff persist = staffDao.get(id);
		if(Staff.STATUS_CREATED.equals(persist.getStatus())){
			persist.setStatus(Staff.STATUS_AUDITED);
			staffDao.update(persist);
			LogUtils.saveLog(Servlets.getRequest(), "审核通过:["+id+"]");
		}
	}
	
	@Transactional(readOnly = false)
	public void approval(String id) {
		Staff persist = staffDao.get(id);
		if(Staff.STATUS_AUDITED.equals(persist.getStatus())){
			persist.setStatus(Staff.STATUS_APPROVAL);
			staffDao.update(persist);
			this.insentDownload(persist, DownloadEntity.DOWNLOAD_TYPE_ADD);
			LogUtils.saveLog(Servlets.getRequest(), "审批通过:["+id+"]");
		}
	}
	
	
	@Transactional(readOnly = false)
	public void save(Staff staff) {
		if (!staff.getIsNewRecord()) {
			// 人员是交接专员 (能修改网点)
			if (Staff.STAFF_TYPE_HANDOVER_CLERK.equals(staff.getStaffType())) {
				Staff oldStaff = get(staff.getId());
				if (!oldStaff.getOffice().getId().equals(staff.getOffice().getId())) {
					insentDownload(oldStaff, DownloadEntity.DOWNLOAD_TYPE_DEL);
					//insentDownload(staff,  DownloadEntity.DOWNLOAD_TYPE_ADD);
				}
			}
		}
		
		if(Staff.STAFF_TYPE_HANDOVER_CLERK.equals(staff.getStaffType())){
			Assert.notNull(staff.getOffice().getId(), "'Office' must not be null");
		}else{
			Assert.notNull(staff.getCompany().getId(), "'Company' must not be null");
		}
		
		boolean messageType;
		if (staff.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		
		super.save(staff);
		for (FingerInfo fingerInfo : staff.getFingerInfoList()) {
			if (fingerInfo.getId() == null) {
				continue;
			}
			if (FingerInfo.DEL_FLAG_NORMAL.equals(fingerInfo.getDelFlag())) {
//				if(ArrayUtils.isEmpty(fingerInfo.getFingerTemplate()) && ArrayUtils.isEmpty(fingerInfo.getBackupFp())){
//					continue;
//				}
				if(!ArrayUtils.isEmpty(fingerInfo.getFingerTemplate())){
					String template = new String(fingerInfo.getFingerTemplate());
					template = new String(Encodes.decodeBase64(template));
					fingerInfo.setFingerTemplate(Encodes.decodeHex(template));
				}
				if(!ArrayUtils.isEmpty(fingerInfo.getBackupFp())){
					String backup = new String(fingerInfo.getBackupFp());
					backup = new String(Encodes.decodeBase64(backup));
					fingerInfo.setBackupFp(Encodes.decodeHex(backup));
				}
				if(!ArrayUtils.isEmpty(fingerInfo.getCoerceTemplate())){
					String coerce = new String(fingerInfo.getCoerceTemplate());
					coerce = new String(Encodes.decodeBase64(coerce));
					fingerInfo.setCoerceTemplate(Encodes.decodeHex(coerce));
				}
				try {
					InetAddress netAddress = InetAddress.getLocalHost();
					String ip = netAddress.getHostAddress();
					fingerInfo.setHost(ip);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				
				//指纹号生成规则 首位代表人员类型
				if("0".equals(staff.getStaffType())) {
					fingerInfo.setFingerNum("11"+fingerInfo.getFingerNum());
				}else if("1".equals(staff.getStaffType())) {
					fingerInfo.setFingerNum("21"+fingerInfo.getFingerNum());
				}else {
					fingerInfo.setFingerNum("31"+fingerInfo.getFingerNum());
				}
				
				if (StringUtils.isBlank(fingerInfo.getId())) {
					fingerInfo.setStaffId(staff.getId());
					fingerInfo.preInsert();
					fingerInfoDao.insert(fingerInfo);
				} else {
					fingerInfo.preUpdate();
					fingerInfoDao.update(fingerInfo);
				}
			} else {
				fingerInfoDao.delete(fingerInfo);
			}
		}
		for (StaffExFamily staffExFamily : staff.getStaffExFamilyList()) {
			if (staffExFamily.getId() == null) {
				continue;
			}
			if (StaffExFamily.DEL_FLAG_NORMAL.equals(staffExFamily.getDelFlag())) {
				if (StringUtils.isBlank(staffExFamily.getId())) {
					staffExFamily.setStaffId(staff.getId());
					staffExFamily.preInsert();
					staffExFamilyDao.insert(staffExFamily);
				} else {
					staffExFamily.preUpdate();
					staffExFamilyDao.update(staffExFamily);
				}
			} else {
				staffExFamilyDao.delete(staffExFamily);
			}
		}
		for (StaffImage staffImage : staff.getStaffImageList()) {
			if (staffImage.getId() == null) {
				continue;
			}
			if (StaffImage.DEL_FLAG_NORMAL.equals(staffImage.getDelFlag())) {
				
				if(staffImage.getImgData() != null){
					
					if (StringUtils.isBlank(staffImage.getId())) {
						staffImage.setStaffId(staff.getId());
						staffImage.preInsert();
						staffImageDao.insert(staffImage);
					} else {
						staffImage.preUpdate();
						staffImageDao.update(staffImage);
					}
				}
			} else {
				staffImageDao.delete(staffImage);
			}
		}
		for (StaffExWork staffExWork : staff.getStaffExWorkList()) {
			if (staffExWork.getId() == null) {
				continue;
			}
			if (StaffExWork.DEL_FLAG_NORMAL.equals(staffExWork.getDelFlag())) {
				if (StringUtils.isBlank(staffExWork.getId())) {
					staffExWork.setStaffId(staff.getId());
					staffExWork.preInsert();
					staffExWorkDao.insert(staffExWork);
				} else {
					staffExWork.preUpdate();
					staffExWorkDao.update(staffExWork);
				}
			} else {
				staffExWorkDao.delete(staffExWork);
			}
		}
		//modify 2019-9-19 维保员同步规则修改
		if(!Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType())){
			this.insentDownload(staff, DownloadEntity.DOWNLOAD_TYPE_ADD);
		}/*else{
			//新增人员不会有派遣任务 所以无需同步
			if(!messageType&&staff.isApproved()){
				//维保员的修改  只对其派遣的网点重新同步
				DispatchPersonInfo query = new DispatchPersonInfo();
				query.setStaffId(staff.getId());
				List<DispatchPersonInfo> dispatchPersonInfoList = dispatchPersonInfoService.findList(query);
				safeGuardDispatchService.insertDownPerson(dispatchPersonInfoList,null,null,DownloadPerson.DOWNLOAD_TYPE_ADD);
			}
		}*/
		if(messageType){
			LogUtils.saveLog(Servlets.getRequest(), "新增人员:["+staff.getId()+"]");
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改人员:["+staff.getId()+"]");
		}
		
	}

	// 只有在审批通过和点击同步的时候进入
	@Transactional(readOnly = false)
	public void insentDownload(Staff staff, String downloadType) {
		if(!staff.isApproved()){
			return;
		}
		if (Staff.STAFF_TYPE_SUPERCARGO.equals(staff.getStaffType())) {
			String area_id = staff.getArea().getId();
			
			List<Office> off = officeDao.findOfficesByAreaIdHasEquipment(area_id); //押款员， 维保员同步同一个区域内所有设备
			for (int i = 0; i < off.size(); i++) {
				String officeID = off.get(i).getId();
				Equipment equipment = equipmentService.getByOfficeId(officeID);
				if(null == equipment){
					continue;
				}
				DownloadPerson downloadPerson = new DownloadPerson();
				downloadPerson.setPersonId(staff.getId());
				downloadPerson.setEquipId(equipment.getId());
				downloadPerson.setIsDownload("0");
				downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
				downloadPerson.setDownloadType(downloadType);
				if(downloadPersonService.countByEntity(downloadPerson)==0){
					downloadPersonService.save(downloadPerson);
				}
			}
			//维保员在人员管理界面任何操作不同步
		}else if(Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType())){
			/*DispatchPersonInfo query = new DispatchPersonInfo();
			query.setStaffId(staff.getId());
			List<DispatchPersonInfo> dispatchPersonInfoList = dispatchPersonInfoService.findList(query);
			safeGuardDispatchService.insertDownPerson(dispatchPersonInfoList,null,null,downloadType);*/
		} else {
			//交接员只同步到自己[所属网点设备]以及[网点下属子网点设备]；
			List<Office> officeList = officeService.findParentIDList(staff.getOffice());
			for (Office office : officeList) {
				Equipment equipment = equipmentService.getByOfficeId(office.getId());
				if(null == equipment){
					continue;
				}
				DownloadPerson downloadPerson = new DownloadPerson();
				downloadPerson.setPersonId(staff.getId());
				downloadPerson.setEquipId(equipment.getId());
				downloadPerson.setIsDownload("0");
				downloadPerson.setDownloadType(downloadType);
				downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
				if(downloadPersonService.countByEntity(downloadPerson)==0){
					downloadPersonService.save(downloadPerson);
				}
			}
		}
		LogUtils.saveLog(Servlets.getRequest(), "同步人员:["+staff.getId()+"]");
	}

	@Transactional(readOnly = false)
	public void delete(Staff staff) {
		super.delete(staff);
		fingerInfoDao.delete(new FingerInfo(staff));
		staffExFamilyDao.delete(new StaffExFamily(staff));
		staffImageDao.delete(new StaffImage(staff));
		staffExWorkDao.delete(new StaffExWork(staff));
		
		DownloadPerson downloadPerson = new DownloadPerson();
		downloadPerson.setPersonId(staff.getId());
	
		downloadPerson.setDownloadType("0");
		downloadPerson.setIsDownload("0");
		int deleted = downloadPersonService.delete1(downloadPerson);
		//if(deleted<1) {
			this.insentDownload(staff, DownloadEntity.DOWNLOAD_TYPE_DEL);
		//}
		LogUtils.saveLog(Servlets.getRequest(), "删除人员:["+staff.getId()+"]");
	}

//	private int staffNumber() {
//		JNative n = null;
//		try {
//			n = new JNative("SuperDog.dll", "ReadSqData");
//			n.setRetVal(Type.INT);
//			Pointer param = Pointer.createPointer(500);
//			n.setParameter(0, param);
//			n.invoke();
//
//			// 人员数
//			byte[] bytArrayStaff = new byte[4];
//			bytArrayStaff[0] = param.getAsByte(11);
//			bytArrayStaff[1] = param.getAsByte(12);
//			bytArrayStaff[2] = param.getAsByte(13);
//			bytArrayStaff[3] = param.getAsByte(14);
//			String MessageStaff = ByteUtils.byteToString(bytArrayStaff);
//			int functionStaff = (Integer.parseInt(MessageStaff, 16));
//			return functionStaff;
//		} catch (Exception ie) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("\r\n======================================================================\r\n");
//			sb.append("\r\n 未检测到加密狗 ！");
//			sb.append("\r\n======================================================================\r\n");
//			System.out.println(sb.toString());
//			ie.printStackTrace();
//			return 0;
//		}
//	}
	
	public int countByWorkNum(String id, String workNum) {
		if(org.apache.commons.lang.StringUtils.isBlank(workNum)) {
			return 0;
		}
		Staff staff = new Staff();
		staff.setId(id);
		staff.setWorkNum(workNum);
		return super.countByColumnExceptSelf(staff);
	}

	public int countByIdentifyNumber(String id, String identifyNumber) {
		if(null == identifyNumber) {
			return 0;
		}
		Staff staff = new Staff();
		staff.setId(id);
		staff.setIdentifyNumber(identifyNumber);
		return super.countByColumnExceptSelf(staff);
	}

	public int countByFingerNum(String staffId,String areaId, String fingerNum) {
		if(org.apache.commons.lang.StringUtils.isBlank(fingerNum)) {
			return 0;
		}
		FingerInfo fingerInfo = new FingerInfo();
		fingerInfo.setStaffId(staffId);
		fingerInfo.setFingerNum(fingerNum);
		fingerInfo.setAreaId(areaId);
		return fingerInfoDao.countByColumnExceptSelf(fingerInfo);
	}

	public int countByCardNum(String staffId, String cardNum) {
		if(org.apache.commons.lang.StringUtils.isBlank(cardNum)) {
			return 0;
		}
		FingerInfo fingerInfo = new FingerInfo();
		fingerInfo.setStaffId(staffId);
		fingerInfo.setCardNum(cardNum);
		return fingerInfoDao.countByColumnExceptSelf(fingerInfo);
	}

	static List<Integer> FINGER_NUM_LIST_GLOBAL = new ArrayList<Integer>();//全局指纹
	static List<Integer> FINGER_NUM_LIST_AREA = new ArrayList<Integer>();//区域指纹
	/*static{
		for (int i = 451; i <= 500; i++) {
			FINGER_NUM_LIST_GLOBAL.add(i);
		}
		
		for (int i = 1; i <= 450; i++) {
			FINGER_NUM_LIST_AREA.add(i);
		}
	}*/
	static {
		for(int i=1;i<=1000;i++) {
			FINGER_NUM_LIST_GLOBAL.add(i);
		}
	}
	public List<KeyValuePair> listAvailableFingers(String areaId, String fingerNumIncludeStr,String staffType) {
		AssertUtil.assertNotBlank(areaId, "areaId");
		FingerInfo fingerInfoRequest=  new FingerInfo();
		fingerInfoRequest.setAreaId(areaId);
		//指纹号首位  用户人员类型区分
		fingerInfoRequest.setFingerNum(String.valueOf(Integer.valueOf(staffType)+1));
		Set<Integer> existsFingers = fingerInfoDao.findAllFingerNumList(fingerInfoRequest);
		
		Integer fingerNumInclude = -1;
		//新的指纹规则 后四位为人员对应指纹数字
		if(fingerNumIncludeStr.length()>4) {
			fingerNumIncludeStr = fingerNumIncludeStr.substring(fingerNumIncludeStr.length()-4, fingerNumIncludeStr.length());
		}
		try {
			fingerNumInclude = Integer.parseInt(fingerNumIncludeStr);
		} catch (Exception e) {
		}
		//String root = "0";
		List<KeyValuePair> list = new ArrayList<KeyValuePair>();
		Area area = areaDao.get(areaId);
		/*if(root.equals(area.getParentId())){
			for (Integer fingerNum : FINGER_NUM_LIST_GLOBAL) {
				if(!existsFingers.contains(fingerNum) || fingerNum.longValue() == fingerNumInclude){
					list.add(new KeyValuePair(StaffHelper.buildFingerNum(area.getId(), fingerNum), String.valueOf(fingerNum)));
				}
			}
		}else{
			for (Integer fingerNum : FINGER_NUM_LIST_AREA ) {
				if(!existsFingers.contains(fingerNum) || fingerNum.longValue() == fingerNumInclude){
					list.add(new KeyValuePair(StaffHelper.buildFingerNum(area.getId(), fingerNum), String.valueOf(fingerNum)));
				}
			}
		}*/
		//新规则 区域中号都是1-1000
		for (Integer fingerNum : FINGER_NUM_LIST_GLOBAL) {
			if(!existsFingers.contains(fingerNum)||fingerNum.longValue() == fingerNumInclude){
				list.add(new KeyValuePair(StaffHelper.buildFingerNum(area.getId(), fingerNum), StaffHelper.buildFingerNum(area.getId(), fingerNum)));
			}
		}
		return list;
	}
	
}