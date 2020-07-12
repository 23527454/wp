package cn.jeefast.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

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
import cn.jeefast.modules.equipment.entity.Equipment;
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
			if("settingInfo".equals(methodName)) {
				Equipment equipment = (Equipment)args[0];
				sysLog.setEquipSn(equipment.getEquipSn());
				String paramType = (String)args[1];
				if("baseInfo".equals(paramType)) {
					sysLog.setOperation(log.value()+"-面板信息");
				}else if("zjType".equals(paramType)) {
					sysLog.setOperation(log.value()+"-闸机类型");
				}else if("djType".equals(paramType)) {
					sysLog.setOperation(log.value()+"-电机类型");
				}else if("zjModel".equals(paramType)) {
					sysLog.setOperation(log.value()+"-闸机模式");
				}else if("leftCross".equals(paramType)) {
					sysLog.setOperation(log.value()+"-左通行模式");
				}else if("rightCross".equals(paramType)) {
					sysLog.setOperation(log.value()+"-右通行模式");
				}else if("remember".equals(paramType)) {
					sysLog.setOperation(log.value()+"-记忆功能");
				}else if("fxwpCross".equals(paramType)) {
					sysLog.setOperation(log.value()+"-反向物品穿行");
				}else if("babyCross".equals(paramType)) {
					sysLog.setOperation(log.value()+"-儿童刷卡通行");
				}else if("zjWorkModel".equals(paramType)) {
					sysLog.setOperation(log.value()+"-闸机运行模式");
				}else if("yzwsgz".equals(paramType)) {
					sysLog.setOperation(log.value()+"-翼闸尾随关闸");
				}else if("crAlarm".equals(paramType)) {
					sysLog.setOperation(log.value()+"-闯入报警");
				}else if("wsAlarm".equals(paramType)) {
					sysLog.setOperation(log.value()+"-尾随报警");
				}else if("zlAlarm".equals(paramType)) {
					sysLog.setOperation(log.value()+"-滞留报警");
				}else if("zjAlarm".equals(paramType)) {
					sysLog.setOperation(log.value()+"-自检报警");
				}else if("qhAlarm".equals(paramType)) {
					sysLog.setOperation(log.value()+"-潜回类型");
				}else if("dbcl".equals(paramType)) {
					sysLog.setOperation(log.value()+"-挡板材料");
				}else if("zmdWorkSpeed".equals(paramType)) {
					sysLog.setOperation(log.value()+"-主马达运行速度");
				}else if("fmdWorkSpeed".equals(paramType)) {
					sysLog.setOperation(log.value()+"-副马达运行速度");
				}else if("mdMaxWorkTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-马达最大运行时间");
				}else if("hwjcTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-红外检测时间");
				}else if("txjgTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-通行间隔时间");
				}else if("ddryjrTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-等待人员进入时间");
				}else if("ryzlTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-人员滞留时间");
				}else if("ysgzTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-延时关闸时间");
				}else if("zytxjgTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-自由通行间隔时间");
				}else if("zkzSpeed".equals(paramType)||"ckzSpeed".equals(paramType)) {
					sysLog.setOperation(log.value()+"-主从开闸速度");
				}else if("zgzSpeed".equals(paramType)||"cgzSpeed".equals(paramType)) {
					sysLog.setOperation(log.value()+"-主从关闸速度");
				}else if("zzdElectric".equals(paramType)||"czdElectric".equals(paramType)) {
					sysLog.setOperation(log.value()+"-主从阻挡电流");
				}else if("zkzTime".equals(paramType)||"ckzTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-主从开闸时间");
				}else if("zgzTime".equals(paramType)||"cgzTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-主从关闸时间");
				}else if("zkzAngle".equals(paramType)||"ckzAngle".equals(paramType)) {
					sysLog.setOperation(log.value()+"-主从开闸角度");
				}else if("qtmcs".equals(paramType)) {
					sysLog.setOperation(log.value()+"-强推脉冲数");
				}else if("qthfTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-强推恢复时间");
				}else if("zdftAngle".equals(paramType)) {
					sysLog.setOperation(log.value()+"-阻挡反弹角度");
				}else if("kzjgTime".equals(paramType)||"gzjgTime".equals(paramType)) {
					sysLog.setOperation(log.value()+"-开关闸间隔时间");
				}
			}else if("operateEquip".equals(methodName)) {
				Equipment equipment = (Equipment)args[0];
				sysLog.setEquipSn(equipment.getEquipSn());
				String operateType = (String)args[1];
				if("0".equals(operateType)) {
					sysLog.setOperation(log.value()+"-左开闸");
				}else if("1".equals(operateType)) {
					sysLog.setOperation(log.value()+"-右开闸");
				}else if("2".equals(operateType)) {
					sysLog.setOperation(log.value()+"-关闸");
				}else if("3".equals(operateType)) {
					sysLog.setOperation(log.value()+"-急停");
				}else if("4".equals(operateType)) {
					sysLog.setOperation(log.value()+"-取消急停");
				}else if("5".equals(operateType)) {
					sysLog.setOperation(log.value()+"-锁离合");
				}else if("6".equals(operateType)) {
					sysLog.setOperation(log.value()+"-开离合");
				}else if("7".equals(operateType)) {
					sysLog.setOperation(log.value()+"-手动设零");
				}else if("8".equals(operateType)) {
					sysLog.setOperation(log.value()+"-自动设零");
				}
			}else if("readInfo".equals(methodName)) {
				Equipment equipment = (Equipment)args[0];
				sysLog.setEquipSn(equipment.getEquipSn());
			}else if("readStatusInfo".equals(methodName)||"readRecordInfo".equals(methodName)) {
				Map<String, Object> params = (Map<String, Object>)args[0];
				sysLog.setEquipSn(String.valueOf(params.get("equipSn")));
			}
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
