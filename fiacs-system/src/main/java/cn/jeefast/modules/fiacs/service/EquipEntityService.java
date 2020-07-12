package cn.jeefast.modules.fiacs.service;

import java.util.List;

import cn.jeefast.modules.fiacs.entity.EquipEntity;
import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.system.entity.SysUser;


public interface EquipEntityService extends IService<EquipEntity>{
	
	public EquipEntity queryByCondition(EquipEntity entity);
	
	/**
	 * 搜索网络中设备
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<EquipEntity> searchEquip(String userId,int type) throws Exception;
	
	/**
	 * 设备加入数据库
	 * @param equip
	 * @param user
	 * @param deptId
	 * @throws Exception
	 */
	void saveDataBase(EquipEntity equip,SysUser user,Long deptId) throws Exception;
	
	
	/**
	 * 同步基本信息到硬件设备
	 * @param equip
	 * @throws Exception
	 */
	void synEquipInfo(EquipEntity equip) throws Exception;
	
	/**
	 * 重启或者初始化设备
	 * @param equip
	 * @throws Exception
	 */
	void resetEquip(EquipEntity equip) throws Exception;
	
	int insertEquip(EquipEntity equip);
	
	int updateEquip(EquipEntity equip) throws Exception;
	
	int deleteEquip(int officeId);
	
	List<EquipEntity> queryAllEquipNotConnect();
}
