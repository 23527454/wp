package com.thinkgem.jeesite.common.utils;

import java.util.Collection;

import com.thinkgem.jeesite.common.service.ServiceException;


public class AssertUtil {
	public static void assertNotBlank(String value, String name){
		if(value == null || value.trim().length() == 0){
			throw new ServiceException(String.format("字段%s不允许为空", name));
		}
	}
	
	public static void assertNotNull(Object value, String name){
		if(value == null){
			throw new ServiceException(String.format("字段%s不允许为空", name));
		}
	}
	
	public static void assertConnectionNotEmpty(Collection value, String name){
		if(value == null || value.isEmpty()){
			throw new ServiceException(String.format("字段%s不允许为空", name));
		}
	}
}
