package cn.jeefast.system.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.system.entity.SysUser;

/**
 * <p>
  * 系统用户 Mapper 接口
 * </p>
 *
 * @author theodo
 * @since 2017-10-28
 */
public interface SysUserDao extends BaseMapper<SysUser> {
	
	List<SysUser> queryPageList(Page<SysUser> page, Map<String, Object> map);

	List<SysUser> queryList(Map<String, Object> map);
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(String userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(String userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUser queryByUserName(String username);
	
	int deleteBatch(Object[] id);	
	
	/**
	 * 是否拥有监听警报权限
	 */
	Map<String,Object> countAlarmAuth(Long userId);
	
	SysUser selectById(String userId);
	
	void updateByIdNew(SysUser user);
}