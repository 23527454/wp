package cn.jeefast.modules.fiacs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fiacs.common.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.common.exception.RRException;
import cn.jeefast.common.utils.DateUtils;
import cn.jeefast.modules.fiacs.dao.EquipEntityDao;
import cn.jeefast.modules.fiacs.entity.EquipEntity;
import cn.jeefast.modules.fiacs.entity.FiacsDept;
import cn.jeefast.modules.fiacs.entity.Staff;
import cn.jeefast.modules.fiacs.service.EquipEntityService;
import cn.jeefast.modules.fiacs.service.FiacsDeptService;
import cn.jeefast.modules.fiacs.util.EquipStartUtil;
import cn.jeefast.modules.fiacs.util.SocketUtil;
import cn.jeefast.modules.fiacs.util.UDPSend;
import cn.jeefast.system.entity.SysUser;

@Service
public class EquipEntityServiceImpl extends ServiceImpl<EquipEntityDao, EquipEntity> implements EquipEntityService {
	@Resource
	private EquipEntityDao equipEntityDao;

	@Resource
	private FiacsDeptService fiacsDeptServiceImpl;

	@Override
	
	public EquipEntity queryByCondition(EquipEntity entity) {
		return equipEntityDao.queryByCondition(entity);
	}

	@Override
	
	public List<EquipEntity> searchEquip(String userId, int type) throws Exception {
		List<EquipEntity> searchEquip = UDPSend.searchEquip();
		if(searchEquip==null) {
			return new ArrayList<>();
		}
		System.out.println(searchEquip.size());
		List<EquipEntity> dbEquip =  equipEntityDao.queryAllByDeptId(null);
	
		if (dbEquip == null || dbEquip.size() == 0) {
			return searchEquip;
		}

		List<EquipEntity> newEquip = new ArrayList<EquipEntity>();// 未加入数据库
		List<EquipEntity> oldEquip = new ArrayList<EquipEntity>();// 已加入数据库

		for (EquipEntity equip : searchEquip) {
			equip.setOldIP(equip.getEquipIP());
			equip.setOldPort(equip.getEquipPort());
			if (dbEquip == null || dbEquip.size() == 0) {
				newEquip.add(equip);
			}
			for (int i = 0; i < dbEquip.size(); i++) {
				if (equip.getEquipSn().equals(dbEquip.get(i).getEquipSn())) {
					equip.setOfficeId(dbEquip.get(i).getOfficeId());
					equip.setOfficeName(dbEquip.get(i).getOfficeName());
					oldEquip.add(equip);
					dbEquip.remove(i);
					break;
				}
				if (i == dbEquip.size() - 1) {
					newEquip.add(equip);
				}
			}
		}
		if (type == 0) {
			newEquip.addAll(oldEquip);
		}
		return newEquip;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void saveDataBase(EquipEntity equip, SysUser user, Long deptId) throws Exception {
		// 所选网点
		FiacsDept fd = fiacsDeptServiceImpl.queryByDeptId(deptId);
		if (fd == null) {
			throw new RRException("所选网点已不存在");
		}
		//监控界面未注册设备添加
		if(equip.getEquipPort()==null){
			if (fd.getEquipIP() != null && !fd.getEquipSn().equals(equip.getEquipSn())) {
				throw new RRException(fd.getName() + "网点已存在其他设备");
			}
			equip.setOfficeId(String.valueOf(deptId));
			//默认值设定
			equip.setCenterPort("10001");
			equip.setEquipPort("8001");
			equip.setSubnetMask("255.255.255.0");
			equip.setGateway("192.168.1.1");
			equip.setCenterIP(IPUtil.getIp());
			equip.setPrintIP("192.168.1.119");
			equip.setPrintPort("15000");
			if ("2".equals(fd.getType())) {
				equip.setSiteType("1");
			} else {
				equip.setSiteType("0");
			}
			EquipEntity qt = equipEntityDao.queryByCondition(equip);

			equip.setEquipName(fd.getName());
			if (qt != null) {
				equip.setId(qt.getId());
				this.updateById(equip);
			} else {
				this.insert(equip);
			}
		}else{
			if (fd.getEquipIP() != null && !fd.getEquipSn().equals(equip.getEquipSn())) {
				throw new RRException(fd.getName() + "网点已存在其他设备");
			}
			equip.setOfficeId(String.valueOf(deptId));
			if ("2".equals(fd.getType())) {
				equip.setSiteType("1");
			} else {
				equip.setSiteType("0");
			}
			EquipEntity qt = equipEntityDao.queryByCondition(equip);

			equip.setEquipName(fd.getName());
			if (qt != null) {
				equip.setId(qt.getId());
				this.updateById(equip);
			} else {
				this.insert(equip);
			}
			this.insertDownload(equip);
			equip.setResetType(2);
			this.synEquipInfo(equip);
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void synEquipInfo(EquipEntity equip) throws Exception {
		int validval = EquipStartUtil.validDate(equip.getEquipSn(), equip.getResetType());
		if(validval==5) {
			throw new RRException("系统重启后,5分钟内不可进行系统重启、APP重启、初始化、保存操作！剩余时间："+(300-EquipStartUtil.getSecond(equip.getEquipSn(),1))+"秒");
		}else if(validval==15) {
			throw new RRException("APP重启或初始化后,15秒内不可进行系统重启、APP重启、初始化、保存操作！剩余时间："+(15-EquipStartUtil.getSecond(equip.getEquipSn(), 2))+"秒");
		}
		
		if(!StringUtils.isEmpty(equip.getOfficeId())) {
			FiacsDept fd = fiacsDeptServiceImpl.queryByDeptId(Long.parseLong(equip.getOfficeId()));
			if (fd == null) {
				throw new RRException("所选网点已不存在");
			}

			if (fd.getEquipIP() != null && !fd.getEquipSn().equals(equip.getEquipSn())) {
				throw new RRException(fd.getName() + "网点已存在其他设备");
			}
			EntityWrapper<EquipEntity> war = new EntityWrapper<>();
			war.where("ip={0}", equip.getEquipIP()).where("serial_num!={0}", equip.getEquipSn());
			if(equipEntityDao.selectCount(war)>0) {
				throw new RRException("已存在相同的设备IP");
			}
			
			equipEntityDao.updateById(equip);
			
			//this.insertDownload(equip);
		}
		String xml = EncodeToXmlUtil.modifyEquip(equip.toMap(), SeqnumUtil.getNextSeqNum());
		byte[] result = SocketUtil.sendMsg(xml.getBytes("UTF-8"));
		Map<String, Object> resultMap = DecodeToMapUtil.bytesToMap(result);
		if (resultMap != null) {
			// if(AuthorCodeUtil.checkBySnOfSearch(String.valueOf(resultMap.get("AuthorCode")),
			// equip.getEquipSn(),
			// Integer.valueOf(String.valueOf(resultMap.get("SeqNum"))))) {
			int resultCode = Integer.valueOf(String.valueOf(resultMap.get("ResultCode")));
			if (resultCode != 0) {
				throw new RRException(ResultEnum.getMsgByCode(resultCode));
			}
			/*
			 * }else { throw new RRException(ResultEnum.getMsgByCode(0xE4)); }
			 */
		}
	}

	@Override
	public void resetEquip(EquipEntity equip) throws Exception {
		int validval = EquipStartUtil.validDate(equip.getEquipSn(), equip.getResetType());
		if(validval==5) {
			throw new RRException("系统重启后,5分钟内不可进行系统重启、APP重启、初始化、保存操作！剩余时间："+(300-EquipStartUtil.getSecond(equip.getEquipSn(),1))+"秒");
		}else if(validval==15) {
			throw new RRException("APP重启或初始化后,15秒内不可进行系统重启、APP重启、初始化、保存操作！剩余时间："+(15-EquipStartUtil.getSecond(equip.getEquipSn(), 2))+"秒");
		}
		// 初始化
		if (equip.getResetType() == 2) {
			Map<String, Object> map = new HashMap<>();
			map.put("EquipSN", equip.getEquipSn());
			map.put("SocketIP", equip.getOldIP());
			map.put("SocketPort", equip.getOldPort());
			String sysName = equipEntityDao.selectConfig("system_name");
			if(StringUtils.isEmpty(sysName)){
				map.put("SysName", "银行出入口智能管控系统");
			}else{
				map.put("SysName", sysName);
			}

			String superNum = equipEntityDao.selectConfig("nSuperGoNum");
			if(StringUtils.isEmpty(superNum)){
				map.put("SuperNum", 2);
			}else{
				map.put("SuperNum", Integer.parseInt(superNum));
			}

			String interNum = equipEntityDao.selectConfig("nInterNum");
			if(StringUtils.isEmpty(interNum)){
				map.put("InterNum", 1);
			}else{
				map.put("InterNum", Integer.parseInt(interNum));
			}

			EquipEntity ee = new EquipEntity();
			ee.setEquipSn(equip.getEquipSn());
			EquipEntity resultEquip = equipEntityDao.queryByCondition(ee);
			if(resultEquip!=null&&resultEquip.getSiteType().equals("1")) {
				map.put("EquipType",1);
			}else {
				map.put("EquipType", 0);
			}
			

			String xml = EncodeToXmlUtil.modifyCommPwd(map, SeqnumUtil.getNextSeqNum());
			byte[] result = SocketUtil.sendMsg(xml.getBytes("UTF-8"));
			Map<String, Object> resultMap = DecodeToMapUtil.bytesToMap(result);
			if (resultMap != null) {
				// if(AuthorCodeUtil.checkBySn(String.valueOf(resultMap.get("AuthorCode")),
				// equip.getEquipSn(),
				// Integer.valueOf(String.valueOf(resultMap.get("SeqNum"))))) {
				int resultCode = Integer.valueOf(String.valueOf(resultMap.get("ResultCode")));
				if (resultCode != 0) {
					throw new RRException(ResultEnum.getMsgByCode(resultCode));
				}
				/*
				 * }else { throw new RRException(ResultEnum.getMsgByCode(0xE4)); }
				 */
			}
			// 重启
		} else {
			String xml = EncodeToXmlUtil.resetEquip(equip.getResetType(), SeqnumUtil.getNextSeqNum(),
					equip.getEquipSn(), equip.getOldIP(), equip.getOldPort());
			byte[] result = SocketUtil.sendMsg(xml.getBytes("UTF-8"));
			Map<String, Object> resultMap = DecodeToMapUtil.bytesToMap(result);
			if (resultMap != null) {
				// if(AuthorCodeUtil.checkBySn(String.valueOf(resultMap.get("AuthorCode")),
				// equip.getEquipSn(),
				// Integer.valueOf(String.valueOf(resultMap.get("SeqNum"))))) {
				int resultCode = Integer.valueOf(String.valueOf(resultMap.get("ResultCode")));
				if (resultCode != 0) {
					throw new RRException(ResultEnum.getMsgByCode(resultCode));
				}
				/*
				 * }else { throw new RRException(ResultEnum.getMsgByCode(0xE4)); }
				 */
			}
		}
	}

	
	@Override
	@Transactional
	public int insertEquip(EquipEntity equip) {
		// 所选网点
		FiacsDept fd = fiacsDeptServiceImpl.queryByDeptId(Long.parseLong(equip.getOfficeId()));
		if (fd == null) {
			throw new RRException("所选网点已不存在");
		}

		if (fd.getEquipIP() != null) {
			throw new RRException(fd.getName() + "网点已存在其他设备");
		}
		EntityWrapper<EquipEntity> war = new EntityWrapper<>();
		war.where("ip={0}", equip.getEquipIP());
		if(equipEntityDao.selectCount(war)>0) {
			throw new RRException("已存在相同的设备IP");
		}
		war = new EntityWrapper<>();
		war.where("serial_num={0}", equip.getEquipSn());
		if(equipEntityDao.selectCount(war)>0) {
			throw new RRException("已存在相同的设备序列号");
		}
		
		int result = equipEntityDao.insert(equip);
				
		this.insertDownload(equip);
		
		return result;
	}
	
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int updateEquip(EquipEntity equip) throws Exception {
		int validval = EquipStartUtil.validDate(equip.getEquipSn(), equip.getResetType());
		if(validval==5) {
			throw new RRException("系统重启后,5分钟内不可进行系统重启、APP重启、初始化、保存操作！剩余时间："+(300-EquipStartUtil.getSecond(equip.getEquipSn(),1))+"秒");
		}else if(validval==15) {
			throw new RRException("APP重启或初始化后,15秒内不可进行系统重启、APP重启、初始化、保存操作！剩余时间："+(15-EquipStartUtil.getSecond(equip.getEquipSn(), 2))+"秒");
		}
		FiacsDept fd = fiacsDeptServiceImpl.queryByDeptId(Long.parseLong(equip.getOfficeId()));
		if (fd == null) {
			throw new RRException("所选网点已不存在");
		}

		if (fd.getEquipIP() != null && !fd.getEquipSn().equals(equip.getEquipSn())) {
			throw new RRException(fd.getName() + "网点已存在其他设备");
		}
		EntityWrapper<EquipEntity> war = new EntityWrapper<>();
		war.where("ip={0}", equip.getEquipIP()).where("id!={0}", equip.getId());
		if(equipEntityDao.selectCount(war)>0) {
			throw new RRException("已存在相同的设备IP");
		}
		
		int result1 = equipEntityDao.updateById(equip);
		
		//this.insertDownload(equip);

		if(result1>0) {
			//this.synEquipInfo(equip);
			String xml = EncodeToXmlUtil.modifyEquip(equip.toMap(), SeqnumUtil.getNextSeqNum());
			byte[] result = SocketUtil.sendMsg(xml.getBytes("UTF-8"));
			Map<String, Object> resultMap = DecodeToMapUtil.bytesToMap(result);
			if (resultMap != null) {
				// if(AuthorCodeUtil.checkBySnOfSearch(String.valueOf(resultMap.get("AuthorCode")),
				// equip.getEquipSn(),
				// Integer.valueOf(String.valueOf(resultMap.get("SeqNum"))))) {
				int resultCode = Integer.valueOf(String.valueOf(resultMap.get("ResultCode")));
				if (resultCode != 0) {
					throw new RRException(ResultEnum.getMsgByCode(resultCode));
				}
				/*
				 * }else { throw new RRException(ResultEnum.getMsgByCode(0xE4)); }
				 */
			}
		}
		return result1;
	}
	
	
	@Override
	public int deleteEquip(int officeId) {
		EntityWrapper<EquipEntity> war = new EntityWrapper<>();
		war.where("office_id={0}", officeId);
		return equipEntityDao.delete(war);
	}
	
	private void insertDownload(EquipEntity equip) {
		equip = equipEntityDao.selectById(equip.getId());
		insentDownloadStaff(equip);
		insentDownloadCar(equip);
		insentDownloadMoneyBox(equip);
	}
	
	private void insentDownloadStaff(EquipEntity equip) {
		List<Staff> staffList = equipEntityDao.selectStaffList(equip.getOfficeId());

		for(Staff staff : staffList) {
			if(!staff.isApproved()) {
				continue;
			}
			
			String satff_id = staff.getId();
			
			if(Staff.STAFF_TYPE_SUPERCARGO.equals(staff.getStaffType())  || Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType())) {
				Map<String,Object> downloadPerson = new HashMap<>();
				downloadPerson.put("personId", satff_id);
				downloadPerson.put("equipId", equip.getId());
				downloadPerson.put("isDownLoad","0");
				downloadPerson.put("downLoadType", "0");
				downloadPerson.put("registerDate",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				if(equipEntityDao.countDownLoadPerson(downloadPerson)==0) {
					equipEntityDao.inserDownLoadPerson(downloadPerson);
				}
			}else {
				//人员与设备在同一营业网点
				if(staff.getOfficeId().equals(equip.getOfficeId())) {
					Map<String,String> offMap = new HashMap<>();
					offMap.put("id", equip.getOfficeId());
					offMap.put("idLike", "%,"+equip.getOfficeId()+",%");
					//查询出此网点的所有下级网点 包含自己
					List<String> officeIdList = equipEntityDao.findByIdAndParent(offMap);
					for(String officeId : officeIdList) {
						
						if(!officeId.equals(staff.getOfficeId())) {
							continue;
						}
						
						EquipEntity eequip = equipEntityDao.queryByCondition(new EquipEntity(officeId));
						
						if(eequip==null) {
							continue;
						}
						Map<String,Object> downloadPerson = new HashMap<>();
						downloadPerson.put("personId", satff_id);
						downloadPerson.put("equipId", eequip.getId());
						downloadPerson.put("isDownLoad","0");
						downloadPerson.put("downLoadType", "0");
						downloadPerson.put("registerDate",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						
						if(equipEntityDao.countDownLoadPerson(downloadPerson)==0) {
							equipEntityDao.inserDownLoadPerson(downloadPerson);
						}
					}
				}else {
					String parentOfficeId = equipEntityDao.findParentOfficeId(equip.getOfficeId());
					if(staff.getOfficeId().equals(parentOfficeId)) {
						Map<String,String> offMap = new HashMap<>();
						offMap.put("id", parentOfficeId);
						offMap.put("idLike", "%,"+parentOfficeId+",%");
						List<String> officeIdList = equipEntityDao.findByIdAndParent(offMap);
						for(String officeId : officeIdList) {
							
							if(!officeId.equals(equip.getOfficeId())) {
								continue;
							}
							
							EquipEntity eequip = equipEntityDao.queryByCondition(new EquipEntity(officeId));
							if(eequip==null) {
								continue;
							}
							Map<String,Object> downloadPerson = new HashMap<>();
							downloadPerson.put("personId", satff_id);
							downloadPerson.put("equipId", eequip.getId());
							downloadPerson.put("isDownLoad","0");
							downloadPerson.put("downLoadType", "0");
							downloadPerson.put("registerDate",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
							
							if(equipEntityDao.countDownLoadPerson(downloadPerson)==0) {
								equipEntityDao.inserDownLoadPerson(downloadPerson);
							}
						}
					}
				}
				
				
			}
		}
	}
	
	private void insentDownloadCar(EquipEntity equip) {
		List<String> carIds = equipEntityDao.selectCarList(equip.getOfficeId());
		for(String carId : carIds) {
			Map<String,Object> downloadCar = new HashMap<>();
			downloadCar.put("carId", carId);
			downloadCar.put("equipId", equip.getId());
			downloadCar.put("isDownLoad","0");
			downloadCar.put("downLoadType", "0");
			downloadCar.put("registerDate",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if(equipEntityDao.countDownLoadCar(downloadCar)==0) {
				equipEntityDao.insertDownLoadCar(downloadCar);
			}
		}
	}
	
	private void insentDownloadMoneyBox(EquipEntity equip) {
		String areaId = equipEntityDao.selectAreaIdByOfficeId(equip.getOfficeId());
		//中心金库
		if("1".equals(equip.getSiteType()) ){
			Map<String,String> offMap = new HashMap<>();
			offMap.put("officeId", equip.getOfficeId());
			if(StringUtils.isEmpty(areaId)) {
				offMap.put("officeIdLike", "%,"+equip.getOfficeId()+",%");
			}else {
				offMap.put("officeIdLike", "%,"+areaId+",%");
			}
			List<String> moneyBoxIds = equipEntityDao.selectMoneyBoxList(offMap);
			
			Map<String,String> areaMap = new HashMap<>();
			areaMap.put("areaId", areaId);
			areaMap.put("areaIdLike", "%,"+areaId+",%");
			//同步自己的款箱到所有网点
			List<Map<String,Object>> equipList = equipEntityDao.selectEquipmentList(areaMap);
			for(Map<String,Object> e : equipList) {
				if("1".equals(String.valueOf(e.get("siteType")))) {
					for(String moneyboxId : moneyBoxIds) {
						Map<String,String> downloadMoneybox = new HashMap<>();
						downloadMoneybox.put("moneyBoxId", moneyboxId);
						downloadMoneybox.put("equipId", String.valueOf(e.get("id")));
						downloadMoneybox.put("isDownLoad","0");
						downloadMoneybox.put("downLoadType", "0");
						downloadMoneybox.put("registerDate",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						
						if(equipEntityDao.countDownLoadMoneyBox(downloadMoneybox)==0) {
							equipEntityDao.insertDownLoadMoneyBox(downloadMoneybox);
						}
					}
				}
			}
		}else {
			//款箱同步
			//同步当前网点的款箱到到当前网点
			Map<String,String> offMap = new HashMap<>();
			offMap.put("officeId", equip.getOfficeId());
			/*if(StringUtils.isEmpty(areaId)) {
				offMap.put("officeIdLike", "%,"+equip.getOfficeId()+",%");
			}else {
				offMap.put("officeIdLike", "%,"+areaId+",%");
			}*/
			List<String> moneyBoxIds = equipEntityDao.selectMoneyBoxList(offMap);
			for(String moneyboxId : moneyBoxIds) {
				Map<String,String> downloadMoneybox = new HashMap<>();
				downloadMoneybox.put("moneyBoxId", moneyboxId);
				downloadMoneybox.put("equipId", equip.getId()+"");
				downloadMoneybox.put("isDownLoad","0");
				downloadMoneybox.put("downLoadType", "0");
				downloadMoneybox.put("registerDate",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				if(equipEntityDao.countDownLoadMoneyBox(downloadMoneybox)==0) {
					equipEntityDao.insertDownLoadMoneyBox(downloadMoneybox);
				}
			}
			
			//同步中心金库的款箱到当前网点
			Map<String,String> areaMap = new HashMap<>();
			areaMap.put("areaId", areaId);
			areaMap.put("areaIdLike", "%,"+areaId+",%");
			areaMap.put("siteType", "1");
			List<Map<String,Object>> equipList = equipEntityDao.selectEquipmentList(areaMap);
			for(Map<String,Object> e : equipList) {
				Map<String,String> chileEMap = new HashMap<>();
				chileEMap.put("officeId",String.valueOf(e.get("officeId")));
				/*if(StringUtils.isEmpty(areaId)) {
					chileEMap.put("officeIdLike", "%,"+equip.getOfficeId()+",%");
				}else {
					chileEMap.put("officeIdLike", "%,"+areaId+",%");
				}*/
				List<String> childMoneyBoxIds = equipEntityDao.selectMoneyBoxList(chileEMap);
				for(String chileboxId : childMoneyBoxIds) {
					Map<String,String> downloadMoneybox = new HashMap<>();
					downloadMoneybox.put("moneyBoxId", chileboxId);
					downloadMoneybox.put("equipId", String.valueOf(e.get("id")));
					downloadMoneybox.put("isDownLoad","0");
					downloadMoneybox.put("downLoadType", "0");
					downloadMoneybox.put("registerDate",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					
					if(equipEntityDao.countDownLoadMoneyBox(downloadMoneybox)==0) {
						equipEntityDao.insertDownLoadMoneyBox(downloadMoneybox);
					}
				}
			}
		}
	}
	
	@Override
	public List<EquipEntity> queryAllEquipNotConnect() {
		// TODO Auto-generated method stub
		return equipEntityDao.queryAllNotConnect();
	}
}
