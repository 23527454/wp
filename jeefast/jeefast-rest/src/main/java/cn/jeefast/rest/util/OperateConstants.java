package cn.jeefast.rest.util;


public class OperateConstants {
	
	//枕头长度1
	public static final int headFieldLength=1;
	
	public static final byte headField = 0x7E;
	//贞尾长度1
	public static final int tailFieldLength=1;
	
	public static final byte tailField = 0x7E;
	
	//设备类码长度1
	public static final int equipmentTypeLength=1;
	
	//通道闸主控板固定为0x90
	public static final byte equipmentType = (byte)0x90;
	
	//组内地址长度
	public static final int addressFieldLength = 1;
	//组内地址默认值1
	public static final byte address = 0x01;
	
	public static final int ccontFieldLength = 2;
	
	public static final int ccmdFieldLength = 1;
	
	public static final int plenFieldLength = 2;
	
	public static final int crcFieldLength = 2;
	//读取 ccmd
	public static final byte operateRead = 0x3A;
	//权限命令 ccmd
	public static final byte operateModifyPassword = 0x38;
	//设置 ccmd
	public static final byte operateSetting = 0x39;
	//上传  ccmd
	public static final byte operateUpload = 0x3B;
	//读取控制板信息ccmd
	public static final byte operateSeq = 0x3C;
	// cctn 传输正确
	public static final byte crtnReq00 = 0x00;
	//校验错误
	public static final byte crtnReq01 = 0x01;
	//不能识别的ccmd命令
	public static final byte crtnReq02 = 0x02;
	//返回码 成功
	public static final byte returnReq00 = 0x00;
	//权限认证错误或者无法识别的命令
	public static final byte returnReq01 = 0x01;
	//数据参数错误
	public static final byte returnReq02 = 0x02;
	//存储空间已满
	public static final byte returnReq03 = 0x03;
	//数据存储空间已空
	public static final byte returnReq04 = 0x04;
	//密码认证
	/*public static final byte[] DCMD_PASSWORD_AUTH = new byte[] {0x11,0x00};
	
	public static final byte[] DCMD_PASSWORD_MODIFY = new byte[] {(byte)0xD2,0x00};
	
	public static final byte[] DCMD_SETTING_TIME = new byte[] {(byte)0xD0,0x00};*/
	
}
