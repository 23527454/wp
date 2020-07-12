package cn.jeefast.rest.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.common.exception.RRException;
import cn.jeefast.common.validator.Assert;
import cn.jeefast.rest.dao.TbUserDao;
import cn.jeefast.rest.entity.TbUser;
import cn.jeefast.rest.service.TbUserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author theodo
 * @since 2017-10-28
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserDao, TbUser> implements TbUserService {

	@Autowired
	private TbUserDao userDao;
	
	@Override
	public TbUser queryByUserId(String userId) {
		return userDao.queryByUserId(userId);
	}
	
	@Override
	public TbUser queryByUsername(String username) {
		return userDao.queryByUsername(username);
	}
	
	@Override
	public List<String> queryPermissions(String userId) {
		return userDao.queryPermissions(userId);
	}
}
