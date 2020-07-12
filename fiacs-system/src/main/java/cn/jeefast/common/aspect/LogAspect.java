package cn.jeefast.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.HttpContextUtils;
import cn.jeefast.common.utils.IPUtils;
import cn.jeefast.modules.fiacs.entity.EquipEntity;
import cn.jeefast.modules.fiacs.entity.FiacsDept;
import cn.jeefast.modules.fiacs.service.FiacsDeptService;
import cn.jeefast.system.entity.SysLog;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.service.SysLogService;


/**
 * 系统日志，切面处理类
 * 
 * @author theodo
 * @email 36780272@qq.com
 * @date 2017年3月8日 上午11:07:35
 */
@Aspect
@Component
public class LogAspect {
	@Autowired
	private SysLogService sysLogService;
	@Resource
	private FiacsDeptService fiacsDeptServiceImpl;
	@Pointcut("@annotation(cn.jeefast.common.annotation.Log)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		Log log = method.getAnnotation(Log.class);

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		Object[] args = joinPoint.getArgs();
		
		sysLog.setMethod(className + "." + methodName + "()");
		
		if(log != null){
			//注解上的描述
			sysLog.setOperation(log.value());
			if("resetEquip".equals(methodName)) {
				EquipEntity equip = (EquipEntity)args[0];
				sysLog.setEquipSn(equip.getEquipSn());
				if(equip.getResetType()==0) {
					sysLog.setOperation(log.value()+"-重启APP");
				}else if(equip.getResetType()==1) {
					sysLog.setOperation(log.value()+"-重启系统");
				}else {
					sysLog.setOperation(log.value()+"-初始化");
				}
			}else if("synEquipInfo".equals(methodName)) {
				EquipEntity equip = (EquipEntity)args[0];
				sysLog.setEquipSn(equip.getEquipSn());
			}else if("saveDataBase".equals(methodName)) {
				EquipEntity equip = (EquipEntity)args[0];
				sysLog.setEquipSn(equip.getEquipSn());
			}else if("saveEquip".equals(methodName)) {
				EquipEntity equip = (EquipEntity)args[0];
				sysLog.setEquipSn(equip.getEquipSn());
			}else if("updateEquip".equals(methodName)) {
				EquipEntity equip = (EquipEntity)args[0];
				sysLog.setEquipSn(equip.getEquipSn());
			}/*else if("deleteEquip".equals(methodName)) {
				Integer officeId = (Integer)args[0];
				sysLog.setEquipSn();
			}*/
		}


		//请求的参数
		try{
			//只保存第一个参数
			String params = new Gson().toJson(args[0]);
			//保存全部参数
			//String params = new Gson().toJson(args);
			sysLog.setParams(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		//用户名
		String username = ((SysUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		//保存系统日志
		sysLogService.insert(sysLog);
	}

	
}
