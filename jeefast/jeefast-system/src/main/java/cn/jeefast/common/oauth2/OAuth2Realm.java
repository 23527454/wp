package cn.jeefast.common.oauth2;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.jeefast.modules.equipment.util.DesUtil;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.entity.SysUserToken;
import cn.jeefast.system.service.ShiroService;

import java.util.Set;

/**
 * 认证
 *
 * @author theodo
 * @email 36780272@qq.com
 * @date 2017-05-20 14:00
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser user = (SysUser)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        
        try {
			String[] tokens = DesUtil.decrypt(accessToken, DesUtil.DESCRET).split("&&");
			if(tokens.length!=2) {
				throw new RuntimeException("token失效");
			}
			//根据accessToken，查询用户信息
			SysUserToken tokenEntity = shiroService.queryByToken(tokens[1]);
			//token失效
			if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
				throw new IncorrectCredentialsException("token失效");
			}
			
			if(!tokens[0].equals(String.valueOf(tokenEntity.getUserId()))) {
				throw new RuntimeException("token失效");
			}
			//查询用户信息
			SysUser user = shiroService.queryUser(tokenEntity.getUserId());
			//账号锁定
			if(user.getStatus() == 0){
				throw new LockedAccountException("账号已被锁定,请联系管理员");
			}
			
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
			return info;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
    }
}
