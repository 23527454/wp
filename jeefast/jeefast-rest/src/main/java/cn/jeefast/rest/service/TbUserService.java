package cn.jeefast.rest.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import cn.jeefast.rest.entity.TbUser;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author theodo
 * @since 2017-10-28
 */
public interface TbUserService extends IService<TbUser> {
	
	TbUser queryByUsername(String username);
	
	TbUser queryByUserId(String userId);
	
	/**
	 * 查询拥有的权限
	 * @param userId
	 * @return
	 */
	List<String> queryPermissions(String userId);
}
