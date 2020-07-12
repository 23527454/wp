package cn.jeefast.rest.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.jeefast.rest.entity.TbUser;

/**
 * <p>
  * 用户 Mapper 接口
 * </p>
 *
 * @author theodo
 * @since 2017-10-28
 */
public interface TbUserDao extends BaseMapper<TbUser> {
	
	TbUser queryByUsername(String username);
	
	TbUser queryByUserId(String userId);
	
	/**
	 * 查询拥有的权限
	 * @param userId
	 * @return
	 */
	List<String> queryPermissions(String userId);

}