package com.fiacs.common.util;

public enum ResultEnum {
	
	result_0(0,"成功"),
	result_1(1,"无此资源"),
	result_2(2,"保存交接事件或异常出错"),
	result_3(3,"没有需要同步的资源"),
	result_4(4,"交接事件记录重复"),
	result_0xE0(0xE0,"版本不符，命令未执行"),
	result_0xE1(0xE1,"接受的命令帧，数据校验不对"),
	result_0xE2(0xE2,"不能识别的命令格式"),
	result_0xE3(0xE3,"数据信息部分有无效数据"),
	result_0xE4(0xE4,"系统认证不成功"),
	result_0xE5(0xE5,"信息项修改不成功"),
	result_0xE6(0xE6,"信息项存储已满"),
	result_0xE7(0xE7,"信息项存储清空"),
	result_0xE8(0xE8,"无相应信息项"),
	result_0xE9(0xE9,"信息项重复");
	
	private final Integer code;
	
	private final String message;
	
	ResultEnum(int code,String message) {
		this.code=code;
		this.message = message;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public static String getMsgByCode(int code) {
		
		ResultEnum result = getByCode(code);
		if(result==null) {
			return null;
		}
		return result.getMessage();
	}
	
	public static ResultEnum getByCode(int code) {
		for(ResultEnum result : values()) {
			System.out.println(result.getCode());
			if(result.getCode()==code) {
				return result;
			}
		}
		return null;
	}
}
