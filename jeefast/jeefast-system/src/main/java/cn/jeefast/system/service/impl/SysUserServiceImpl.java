package cn.jeefast.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.common.annotation.DataFilter;
import cn.jeefast.system.dao.SysUserDao;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.service.SysAlarmTypeService;
import cn.jeefast.system.service.SysUserRoleService;
import cn.jeefast.system.service.SysUserService;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author theodo
 * @since 2017-10-28
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysAlarmTypeService sysAlarmTypeService;
	@Override
	//@DataFilter(tableAlias = "u", user = false)
	public Page<SysUser> queryPageList(Page<SysUser> page, Map<String, Object> map) {
		page.setRecords(sysUserDao.queryPageList(page, map));
		return page;
	}
	
	@Override
	@DataFilter(tableAlias = "u", user = false)
	public List<SysUser> queryList(Map<String, Object> map){
		return sysUserDao.queryList(map);
	}
	
	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	@Override
	public SysUser queryByUserName(String username) {
		return sysUserDao.queryByUserName(username);
	}
	
	@Override
	@Transactional
	public void save(SysUser user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		sysUserDao.insert(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		
		sysAlarmTypeService.saveOrUpdate(String.valueOf(user.getUserId()), user.getAlarmIdList());
	}

	@Override
	@Transactional
	public void update(SysUser user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		sysUserDao.updateById(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		sysAlarmTypeService.saveOrUpdate(String.valueOf(user.getUserId()), user.getAlarmIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
		for(Long id :userId) {
			sysUserRoleService.deleteByUserId(id);
			sysAlarmTypeService.deleteByUserId(String.valueOf(id));
		}
	}
	
	@Override
	public Map<String,Object> countAlarmAuth(Long userId) {
		return sysUserDao.countAlarmAuth(userId);
	}
	
}
