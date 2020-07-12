package cn.jeefast.rest.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.jeefast.common.utils.R;
import cn.jeefast.rest.annotation.AuthIgnore;
import cn.jeefast.rest.entity.TbUser;
import cn.jeefast.rest.service.TbTokenService;
import cn.jeefast.rest.service.TbUserService;
import cn.jeefast.rest.util.Constants;
import cn.jeefast.rest.util.DESCoder;
import cn.jeefast.rest.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API登录授权
 *
 * @author theodo
 * @email 36780272@qq.com
 * @date 2017-10-23 15:31
 */
@RestController
@RequestMapping("/api")
@Api(value="账号登录接口",tags = "接口信息")
public class ApiLoginController {
    @Autowired
    private TbUserService userService;
    @Autowired
    private TbTokenService tokenService;

    /**
     * 登录
     */
    @AuthIgnore
    @PostMapping("login")
    @ApiOperation(value="登录", notes="登录",httpMethod="POST",response=R.class)
    public R login(@RequestParam Map<String, Object> params){

    	String username = String.valueOf(params.get("username"));
    	String password = String.valueOf(params.get("password"));
    	System.out.println(password);
    	try {
			String dePassword = DESCoder.decode3Des( Constants.KEY,password);
			String[] result = dePassword.split("&");
			if(result.length!=2) {
				return R.error();
			}
			if(!result[1].equalsIgnoreCase(Constants.PASSWORD_KEY)) {
				return R.error();
			}
			TbUser user = userService.queryByUsername(username);
			
			if(user == null || !user.getPassword().equals(new Sha256Hash(result[0], user.getSalt()).toHex())) {
				return R.error("账号或密码不正确");
			}
			
			Map<String, Object> map = tokenService.createToken(user.getUserId());
			//map.put("userId", user.getUserId());
			map.put("deptId", user.getDeptId());
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return R.error();
    }
    
    @AuthIgnore
    @RequestMapping("/downloadFile/{fileName}")
	@ResponseBody
	public void Download(HttpServletResponse response,@PathVariable("fileName") String fileName) {
		if(!fileName.endsWith(".log")) {
			fileName +=".log";
		}
		FileUtil.downloadFileWindows(response, fileName);
	}
}
