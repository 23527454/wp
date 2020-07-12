package cn.jeefast.rest.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.jeefast.common.exception.RRException;
import cn.jeefast.rest.annotation.AuthIgnore;
import cn.jeefast.rest.entity.TbToken;
import cn.jeefast.rest.entity.TbUser;
import cn.jeefast.rest.service.TbTokenService;
import cn.jeefast.rest.service.TbUserService;
import cn.jeefast.rest.util.Constants;
import cn.jeefast.rest.util.DESCoder;
import cn.jeefast.rest.util.DesUtil;

/**
 * 权限(Token)验证
 * @author theodo
 * @email 36780272@qq.com
 * @date 2017-03-23 15:38
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private TbTokenService tokenService;
    @Autowired
    private TbUserService tbUserService;

    public static final String USER_KEY = "userId";
    
    public static final String USERNAME_KEY = "userName";
    
    public static final String USER_DEPTID = "deptId";
    
    public static final String PARAM_MAP = "paramMap";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthIgnore annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthIgnore.class);
        }else{
            return true;
        }

        //如果有@IgnoreAuth注解，则不验证token
        if(annotation != null){
            return true;
        }

        //从header中获取token
        String desStr = request.getParameter("desStr");
        
        if(StringUtils.isEmpty(desStr)) {
        	throw new RRException("数据错误");
        }
        
        Map<String,String> paramMap = (Map<String, String>) JSONObject.parse(DESCoder.decode3Des(Constants.KEY, desStr));
       
        String token = paramMap.get("token");
        //token为空
        if(StringUtils.isBlank(token)||"null".equals(token)){
        	throw new RRException("token不能为空");
        }
        
        String[] tokens = DesUtil.decrypt(token, DesUtil.DESCRET).split("&&");
		if(tokens.length!=2) {
			throw new RuntimeException("token失效");
		}
        
        //查询token信息
        TbToken tokenEntity = tokenService.queryByToken(tokens[1]);
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new RRException("token失效，请重新登录");
        }
        
        if(!tokens[0].equals(String.valueOf(tokenEntity.getUserId()))) {
			throw new RuntimeException("token失效");
		}
        
        TbUser user = tbUserService.queryByUserId(tokens[0]);
        if(user==null) {
        	throw new RuntimeException("账号不存在");
        }
        
        //判断是否拥有对应权限
        List<String> permissions = tbUserService.queryPermissions(String.valueOf(tokenEntity.getUserId()));
        if(permissions==null||permissions.size()==0) {
        	 throw new RRException("权限不足");
        }else {
        	String method = paramMap.get("method");
        	if(StringUtils.isEmpty(method)) {
        		throw new RRException("method参数不能为空");
        	}
        	if(!permissions.contains(method)) {
        		throw new RRException("权限不足");
        	}
        }
        
        String sign = paramMap.get("sign");
        paramMap.remove("sign");
        paramMap.remove("token");
        paramMap.remove("method");
        JsonParser parser = new JsonParser();  
        JsonObject obj = (JsonObject) parser.parse(JSONObject.toJSONString(paramMap));  
        JsonParser parser1 = new JsonParser();  
        JsonObject obj1 = (JsonObject) parser1.parse(DESCoder.decode3Des( Constants.data_key,sign));  
        if(!obj.equals(obj1)) {
        	throw new RRException("数据错误");
        }
        
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, tokenEntity.getUserId());
        request.setAttribute(USERNAME_KEY, user.getUsername());
        request.setAttribute(USER_DEPTID, tokenEntity.getDeptId());
        request.setAttribute(PARAM_MAP, paramMap);
        return true;
    }
}
