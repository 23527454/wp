package com.fiacs.common.util;

public class Constants {

	/**
	 * 通讯秘钥因子缺省默认值
	 */
	public static String COMMUNICATION_KEY_FACTOR = "FB1810A3BC1005E0";

	/**
	 * 加密狗秘钥
	 */
	public static String COMMUNICATION_KEY_FACTOR_OLD = "FB1810A3BC1005E0";

	/**
	 * 异或码
	 */
	public static String XOR_CODE = "FB0819B8CE0926F9";

	/**
	 * 通信最小长度
	 */
	public static int MIN_LENGTH = 120;

	public static int MAX_IMAGE_SEND_SIZE = 1100;

	//=========================暗火平板安全设置通讯=================================
	/**
	 * 安卓平板安全设置通讯的秘钥因子
	 */
	public static String SAFE_KEY_FACTOR = "DE0163E1BC1859C0";

	/**
	 * 安卓平板安全设置固定产品序列号
	 */
	public static String SAFE_EQUIP_SN = "4FFCE7C5884C23D5";

	public static String SECRET_DES_FACTOR = "AB0113E2DF1009C5";

	//=======================搜索安卓平板设备=======================================
	//============6.18=========
	public static String SEARCH_KEY_FACTOR = "AE0827C89B1010E6";

	public static String SEARCH_EQUIP_SN = "3E5AEFC0819CBDA9";

	//==========6.13============
	/*public static String SEARCH_KEY_FACTOR = "CF0798F7CE1611B8";
	
	public static String SEARCH_EQUIP_SN = "3D2CEFC5DF4CBD69";*/


	//=====================通信type类型==============================
	//身份认证
	public static String VERIFY_IDENTIFY = "0";
	//人员同步
	public static String SYN_PERSON_TYPE = "1";
	//车辆同步
	public static String SYN_CAR_TYPE = "2";
	//排班计划
	public static String SYN_TASK_CLASS = "3";

	public static String CONNECT_EVENT_TYPE = "4";

	public static String ALARM_EVENT_TYPE = "5";

	public static String CLOCK_CHECK_TYPE = "7";

	public static String BIG_DATA_TYPE = "8";

	public static String REQUEST_SYN_TYPE = "9";

	public static String CASHBOX_INFO_TYPE= "10";

	public static String RESPONSE_TYPE = "255";

	public static String CASHBOX_ALLOT_TYPE = "11";

	public static String CAR_ARRIVE_EVENT_TYPE = "21";

	public static String SUPERGO_EVENT_TYPE = "22";

	public static String WORKPERSON_EVENT_TYPE = "23";

	public static String CASHBOX_EVENT_TYPE = "24";

	public static String CASHBOX_ORDER_TYPE = "25";

	public static String CASHBOX_RETURN_TYPE = "26";

	public static String SAFEGUARD_EVENT_TYPE = "27";

	public static String ACCESS_EVENT_TYPE = "28";

	public static String ACCESS_ALARM_TYPE = "29";

	public static String INFORMATION_RELEASE_TYPE="30";

	public static String ACCESS_PARAM_TYPE = "31";

	public static String ACCESS_TIMEZONE_TYPE = "32";

	public static String SEARCH_ALL_TYPE = "70";

	public static String SEARCH_RESPONSE_TYPE = "70";

	public static String MODIFY_EQUIP_TYPE = "71";

	public static String RESET_PASSWORD_TYPE = "72";

	public static String MODIFY_PASSWORD_TYPE = "90";

	public static String RESTART_TYPE = "91";

	public static String CONTROL_DOOR_TYPE = "92";

	public static String PARAM_SETTING_TYPE = "6";

	public static String READ_EQUIPSN_TYPE = "50";

	public static String READ_EQUIPSN_RESPONSE_TYPE = "50";

	public static String SETTING_EQUIPSN_TYPE = "51";

	public static String SETTING_EQUIPSN_RESPONSE_TYPE = "51";

	public static String READ_ACTIVE_FACTOR_TYPE = "52";

	public static String READ_ACTIVE_FACTOR_RESPONSE_TYPE = "52";

	public static String SETTING_ACTIVE_FACTOR_TYPE = "53";

	public static String SETTING_ACTIVE_FACTOR_RESPONSE_TYPE = "53";


	public static String SYN_SUCCESS = "0";

	public static String OPTIONTYPE_SERVER = "0";

	public static String OPTIONTYPE_CLIENT = "1";

	public static String INFOTYPE_DOWN_PERSON_PHOTO = "0";

	public static String INFOTYPE_DOWN_FINGER_MODE = "1";

	public static String INFOTYPE_DOWN_CAR_PHOTO = "2";

	public static String INFOTYPE_UPLOAD_CONNECT_PERSON_PHOTO = "3";

	public static String INFOTYPE_UPLOAD_ALARM_PHOTO = "4";

	public static String INFOTYPE_UPLOAD_COMMISSION_FINGER_MODE = "5";

	public static String INFOTYPE_UPLOAD_SUPERGO_PHOTO = "6";

	public static String INFOTYPE_UPLOAD_SAFEGUARD_PHOTO = "7";

	public static String INFOTYPE_UPLOAD_COMMISSION_ACCESS_PHOTO = "8";

	public static String INFOTYPE_UPLOAD_ACCESS_ALARM_PHOTO = "9";

	public static String INFOTYPE_UPLOAD_STAFF_ONE_CARD_PHOTO = "10";//第一张证件照

	public static String INFOTYPE_UPLOAD_STAFF_TWO_CARD_PHOTO = "11";//第二张证件照

	//=====================人员照片类型============================
	public static String STAFF_IMAGE_TYPE_PERSON = "0";//人员头像

	public static String STAFF_IMAGE_TYPE_CARD = "1";//人员证件

}
