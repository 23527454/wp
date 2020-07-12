package cn.jeefast.rest.util;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jeefast.rest.entity.Equipment;


public class OperateUtil {
	
	private static Logger log = LoggerFactory.getLogger(OperateUtil.class);
	/**
	 * 添加贞头贞尾
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] bulidData(byte[] dcmd, byte[] data, byte operateType) {
		byte[] head = new byte[] { OperateConstants.headField };
		byte[] tail = new byte[] { OperateConstants.tailField };

		byte[] res = {};

		res = ArrayUtils.add(res, OperateConstants.equipmentType);
		res = ArrayUtils.add(res, OperateConstants.address);
		res = ArrayUtils.addAll(res, getCcont());
		res = ArrayUtils.add(res, operateType);
		res = ArrayUtils.addAll(res, ByteUtil.shortToBytes((short) (getPassword().length + dcmd.length + data.length)));
		res = ArrayUtils.addAll(res, getPassword());
		res = ArrayUtils.addAll(res, dcmd);
		res = ArrayUtils.addAll(res, data);
		res = ArrayUtils.addAll(res, getCrcByte(res));
		
		for(int i=0;i<res.length;i++) {
			Byte b = res[i];
			if(b.equals((byte)0x7E)) {
				head = ArrayUtils.addAll(head, new byte[] {0x7d,0x5e});
			}else if(b.equals((byte)0x7D)) {
				head = ArrayUtils.addAll(head, new byte[] {0x7d,0x5d});
			}else {
				head = ArrayUtils.add(head, b);
			}
		}
		head = ArrayUtils.addAll(head, tail);
		log.error("发送数据："+ByteUtil.bytesToHexString(head));
		return head;
	}

	/**
	 * 得到传输控制字
	 * 
	 * @return
	 */
	public static byte[] getCcont() {
		StringBuffer sb = new StringBuffer();
		sb.append("1");// bit15
		sb.append("00000");// bit14-bit10 预留
		sb.append(SerialNumber.getNewAutoNum());// bit9-bit0 流水号
		return ByteUtil.strToBytes(sb.toString());
	}

	/**
	 * 通讯密码
	 * 
	 * @return
	 */
	public static byte[] getPassword() {
		return new byte[] { 0x11, 00, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00 };
	}

	/**
	 * 得到设置时间的info time格式 yyyyMMddHHssii
	 * 
	 * @return
	 */
	public static byte[] settingTime(String time) {

		byte[] dcmd = new byte[] { (byte) 0xD0, 0X00, 0x08 };
		byte[] data = ByteUtil.hexStringToBytes(time);

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 得到crc
	 * 
	 * @param data
	 * @return
	 */

	public static String getCrc(byte[] data) {
		int crc = CRC16.crc16_ccitt_xmodem(data);
		return ByteUtil.bytesToHexString(ByteUtil.intToBytes(crc, 2)).toUpperCase();
	}

	public static byte[] getCrcByte(byte[] data) {
		int crc = CRC16.crc16_ccitt_xmodem(data);
		//String hex = Integer.toHexString(crc).toUpperCase();
		return ByteUtil.intToBytes(crc, 2);
	}

	/**
	 * 校验crc码
	 * 
	 * @param data
	 * @return
	 */
	public static boolean validCrc(byte[] data) {
		byte[] info = Arrays.copyOf(data, data.length - 2);
		byte[] crc = Arrays.copyOfRange(data, data.length - 2, data.length);

		String crcInfo = getCrc(info);
		String validInfo = ByteUtil.bytesToHexString(crc).toUpperCase();

		return crcInfo.equals(validInfo);
	}

	/**
	 * 设置出厂设置
	 * 
	 * @return
	 */
	public static byte[] settingFactory() {

		byte[] dcmd = new byte[] { (byte) 0xFE, 0x00, 0x01 };
		byte[] data = new byte[] { 0x00 };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 闸机类型 type 0 翼闸 1 摆闸
	 */
	public static byte[] settingMachineType(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x00, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 闸机模式 type 0 常闭 1 常开
	 */
	public static byte[] settingMachinePatten(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x02, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 电机类型 type 0 直流 1 司机
	 */
	public static byte[] settingElectricalMachineType(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x01, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 左通行模式 type 0 刷卡 1 自由 2 禁止
	 */
	public static byte[] settingLeftPatten(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x03, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 右通行模式 type 0 刷卡 1 自由 2 禁止
	 */
	public static byte[] settingRightPatten(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x04, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 设置记忆功能 type 0 不启用 1 启用
	 */
	public static byte[] settingRemember(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x05, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 反向物品穿行
	 * 
	 * @return
	 */
	public static byte[] settingFxwpcx(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x06, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 儿童刷卡通行
	 * 
	 * @param type
	 *            0 关闭 1 开启
	 * @return
	 */
	public static byte[] settingBabyGo(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x07, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 闸机运行模式 0 正常模式 1 老化模式 2 紧急通行 3 指令模式 4 停机维护
	 * 
	 * @param type
	 * @return
	 */
	public static byte[] settingMachineOperatePatten(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x08, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 设置主马达运行速度
	 * 
	 * @param speed
	 * @return
	 */
	public static byte[] settingMainMDSpeed(int speed) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x09, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(speed) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 设置副马达运行速度
	 * 
	 * @param speed
	 * @return
	 */
	public static byte[] settingMDSpeed(int speed) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x0A, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(speed) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 翼闸尾随关闸
	 * 
	 * @param type
	 *            0 关闭 1 开启
	 * @return
	 */
	public static byte[] settingYzWsGz(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC0, 0x0B, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 设置马达最大运行时间(秒)
	 * 
	 * @param second（4-20）
	 * @return
	 */
	public static byte[] settingMdMaxTime(int second) {
		byte[] dcmd = new byte[] { (byte) 0xC2, 0x00, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(second) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 设置红外线检查时间
	 * 
	 * @param second
	 *            1-10 单位0.01秒
	 * @return
	 */
	public static byte[] settingRedLineTime(int second) {
		byte[] dcmd = new byte[] { (byte) 0xC2, 0x01, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(second) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 通信间隔时间
	 * 
	 * @param second
	 *            0~255 单位0.01秒
	 * @return
	 */
	public static byte[] settingGoIntervalTime(int second) {
		byte[] dcmd = new byte[] { (byte) 0xC2, 0x02, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(second) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 等待人员进入时间
	 * 
	 * @param second
	 *            5~255 单位秒
	 * @return
	 */
	public static byte[] settingWaitPepoleGo(int second) {
		byte[] dcmd = new byte[] { (byte) 0xC2, 0x03, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(second) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 人员滞留时间
	 * 
	 * @param second
	 * @return
	 */
	public static byte[] settingPepoleRetetion(int second) {
		byte[] dcmd = new byte[] { (byte) 0xC2, 0x04, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(second) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 延时关闸时间
	 * 
	 * @param second
	 *            0~255 单位 0.01秒
	 * @return
	 */
	public static byte[] settingDelayCloseTime(int second) {
		byte[] dcmd = new byte[] { (byte) 0xC2, 0x05, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(second) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 自由通行间隔时间
	 * 
	 * @param second
	 *            0~255 单位0.01秒
	 * @return
	 */
	public static byte[] settingFreeGoIntervalTime(int second) {
		byte[] dcmd = new byte[] { (byte) 0xC2, 0x06, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(second) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 闯入警报
	 * 
	 * @param type
	 *            0 开启 1 关闭
	 * @return
	 */
	public static byte[] settingIntrudeAlarm(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC4, 0x00, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 尾随警报
	 * 
	 * @param type
	 *            0 开启 1 关闭
	 * @return
	 */
	public static byte[] settingWsAlarm(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC4, 0x01, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 滞留警报
	 * 
	 * @param type
	 *            0 开启 1 关闭
	 * @return
	 */
	public static byte[] settingRetetionAlarm(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC4, 0x02, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 自检警报
	 * 
	 * @param type
	 *            0 开启 1 关闭
	 * @return
	 */
	public static byte[] settingSelfInspectionAlarm(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC4, 0x03, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 潜回警报
	 * 
	 * @param type
	 *            0 开启 1 关闭
	 * @return
	 */
	public static byte[] settingBackAlarm(int type) {
		byte[] dcmd = new byte[] { (byte) 0xC4, 0x04, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(type) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数挡板参数
	 * 
	 * @param type
	 * @return
	 */
	public static byte[] settingSfdjParamDbcz(short type) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x01, 0x02 };
		byte[] data = ByteUtil.getBytes(type, true);

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数开闸速度
	 * 
	 * @param one
	 *            主闸 1-5000 two 副闸
	 * @return
	 */
	public static byte[] settingSfdjParamKzsd(short one, short two) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x02, 0x04 };
		byte[] data = ArrayUtils.addAll(ByteUtil.getBytes(one, true), ByteUtil.getBytes(two, true));

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数关闸速度
	 * 
	 * @param one
	 *            主闸 1-5000 two 副闸
	 * @return
	 */
	public static byte[] settingSfdjParamGzsd(short one, short two) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x03, 0x04 };
		byte[] data = ArrayUtils.addAll(ByteUtil.getBytes(one, true), ByteUtil.getBytes(two, true));

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数主从阻挡电流
	 * 
	 * @param one
	 *            主闸 0-1000 two 副闸
	 * @return
	 */
	public static byte[] settingSfdjParamZddl(short one, short two) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x04, 0x04 };
		byte[] data = ArrayUtils.addAll(ByteUtil.getBytes(one, true), ByteUtil.getBytes(two, true));

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数主从开闸时间
	 * 
	 * @param one
	 *            主闸 1-5000 two 副闸
	 * @return
	 */
	public static byte[] settingSfdjParamKzsj(short one, short two) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x05, 0x04 };
		byte[] data = ArrayUtils.addAll(ByteUtil.getBytes(one, true), ByteUtil.getBytes(two, true));

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数主从关闸时间
	 * 
	 * @param one
	 *            主闸 1-5000 two 副闸
	 * @return
	 */
	public static byte[] settingSfdjParamGzsj(short one, short two) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x06, 0x04 };
		byte[] data = ArrayUtils.addAll(ByteUtil.getBytes(one, true), ByteUtil.getBytes(two, true));

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数主从开闸角度
	 * 
	 * @param one
	 *            主闸 1-1800 two 副闸
	 * @return
	 */
	public static byte[] settingSfdjParamKzjd(short one, short two) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x07, 0x04 };
		byte[] data = ArrayUtils.addAll(ByteUtil.getBytes(one, true), ByteUtil.getBytes(two, true));

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数强推脉冲数
	 * 
	 * @param one
	 *            脉冲数 0~65535
	 * @return
	 */
	public static byte[] settingSfdjParamQtmcs(short one) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x08, 0x02 };
		byte[] data = ByteUtil.getBytes(one, true);

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机参数强推恢复时间
	 * 
	 * @param one
	 *            脉冲数 100~65535
	 * @return
	 */
	public static byte[] settingSfdjParamQthfsj(short second) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x09, 0x02 };
		byte[] data = ByteUtil.getBytes(second, true);

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机阻挡反弹角度
	 * 
	 * @param angle
	 *            脉冲数 0~900
	 * @return
	 */
	public static byte[] settingSfdjParamZdftjd(short angle) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x0A, 0x02 };
		byte[] data = ByteUtil.getBytes(angle, true);

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 司机电机阻挡模式选择
	 * 
	 * @param type
	 *            1 反弹一个角度 2 速度力矩减小
	 * @return
	 */
	public static byte[] settingSfdjParamZdmsxz(short type) {
		byte[] dcmd = new byte[] { (byte) 0xE0, 0x0B, 0x02 };
		byte[] data = ByteUtil.getBytes(type, true);

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 老模式开关闸间隔时间
	 * 
	 * @param one
	 *            0~255
	 * @param two
	 * @return
	 */
	public static byte[] settingOldModeKgIntervalTime(int one, int two) {

		byte[] dcmd = new byte[] { (byte) 0xCA, 0x00, 0x02 };
		byte[] data = new byte[] { ByteUtil.intToByte(one), ByteUtil.intToByte(two) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	public static byte[] settingBaseInfo(Equipment equip) {

		byte[] dcmd = new byte[] { (byte) 0xA9, 0x01, 0x24 };
		byte[] data = {};
		data = ArrayUtils.add(data, ByteUtil.intToByte(Integer.valueOf(equip.getAddress())));// 控制器地址

		data = ArrayUtils.addAll(data, ByteUtil.SnToBytes(equip.getEquipSn()));
		data = ArrayUtils.addAll(data, ByteUtil.ipv4Address2BinaryArray(equip.getIp()));
		data = ArrayUtils.addAll(data, ByteUtil.shortToBytes( Short.valueOf(String.valueOf(equip.getPort()))));
		data = ArrayUtils.addAll(data, ByteUtil.ipv4Address2BinaryArray(equip.getSubnetMask()));
		data = ArrayUtils.addAll(data, ByteUtil.ipv4Address2BinaryArray(equip.getGateWay()));
		data = ArrayUtils.addAll(data, ByteUtil.ipv4Address2BinaryArray(equip.getIpCenter()));
		data = ArrayUtils.addAll(data, ByteUtil.shortToBytes( Short.valueOf(String.valueOf(equip.getPortCenter()))));
		data = ArrayUtils.addAll(data, ByteUtil.reqToBytes(equip.getEquipReq()));
		return bulidData(dcmd, data, OperateConstants.operateSeq);
	}

	/**
	 * 操作 0 左开门 1 右开门 2 关闸 3急停 4 取消急停 5 锁离合 6 开离合 7 手动社零 8 自动找零
	 * 
	 * @param operate
	 * @return
	 */
	public static byte[] controllEquipment(int operate) {

		byte[] dcmd = new byte[] { (byte) 0xB0, 0x00, 0x01 };
		byte[] data = new byte[] { ByteUtil.intToByte(operate) };

		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}

	/**
	 * 读取时间
	 * 
	 * @return
	 */
	public static byte[] readTime() {
		byte[] dcmd = new byte[] { (byte) 0xD0, 0x00, 0x01 };
		byte[] data = new byte[] { 0x00 };
		return bulidData(dcmd, data, OperateConstants.operateRead);
	}

	/**
	 * 读取有效期
	 * 
	 * @return
	 */
	public static byte[] readValidTime() {
		byte[] dcmd = new byte[] { (byte) 0xD0, 0x01, 0x01 };
		byte[] data = new byte[] { 0x00 };
		return bulidData(dcmd, data, OperateConstants.operateRead);
	}

	/**
	 * 读取工作模式
	 */
	public static byte[] readWorkMode() {
		byte[] data = {};
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x00, 0x01, 0x00 });// 闸机类型
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x01, 0x01, 0x00 });// 电机类型
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x02, 0x01, 0x00 });// 闸机模式
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x03, 0x01, 0x00 });// 左通信模式
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x04, 0x01, 0x00 });// 右通行模式
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x05, 0x01, 0x00 });// 读取记忆功能
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x06, 0x01, 0x00 });// 反向物品穿行
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x07, 0x01, 0x00 });// 儿童刷卡通行
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x08, 0x01, 0x00 });// 闸机运行模式
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x0B, 0x01, 0x00 });// 翼闸尾随关闸

		return bulidData(new byte[] {}, data, OperateConstants.operateRead);
	}

	/**
	 * 读取时间参数
	 */
	public static byte[] readParamsTime() {
		byte[] data = {};
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x09, 0x01, 0x00 });// 主马达运行速率
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC0, 0x0A, 0x01, 0x00 });// 副马达运行速率
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC2, 0x00, 0x01, 0x00 });// 马达最大运行时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC2, 0x01, 0x01, 0x00 });// 红外检测时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC2, 0x02, 0x01, 0x00 });// 通行间隔时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC2, 0x03, 0x01, 0x00 });// 等待人员进入时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC2, 0x04, 0x01, 0x00 });// 人员滞留时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC2, 0x05, 0x01, 0x00 });// 延时关闸时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC2, 0x06, 0x01, 0x00 });// 自由通信间隔时间

		return bulidData(new byte[] {}, data, OperateConstants.operateRead);
	}

	/**
	 * 读取报警参数
	 */
	public static byte[] readParamsAlarm() {
		byte[] data = {};
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC4, 0x00, 0x01, 0x00 });// 闯入报警
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC4, 0x01, 0x01, 0x00 });// 尾随报警
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC4, 0x02, 0x01, 0x00 });// 滞留报警
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC4, 0x03, 0x01, 0x00 });// 自检报警
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xC4, 0x04, 0x01, 0x00 });// 潜回报警

		return bulidData(new byte[] {}, data, OperateConstants.operateRead);
	}

	/**
	 * 读取记录
	 */
	public static byte[] readRecord() {
		byte[] dcmd = new byte[] { (byte) 0xA2, 0x00, 0x01 };
		byte[] data = new byte[] { 0x00 };
		return bulidData(dcmd, data, OperateConstants.operateRead);
	}

	/**
	 * 读取状态
	 */
	public static byte[] readStatus() {
		byte[] dcmd = new byte[] { (byte) 0xA2, 0x01, 0x01 };
		byte[] data = new byte[] { 0x00 };
		return bulidData(dcmd, data, OperateConstants.operateRead);
	}

	/**
	 * 读取版本号
	 */
	public static byte[] readVersion() {
		byte[] data = {};
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xA2, 0x02, 0x01, 0x00 });// 版本号
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xD0, 0x00, 0x01, 0x00 });// 读取时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xD0, 0x01, 0x01, 0x00 });// 读取有效期

		return bulidData(new byte[] {}, data, OperateConstants.operateRead);
	}

	/**
	 * 读取控制板
	 */
	public static byte[] snReadEquipment() {
		byte[] dcmd = new byte[] { (byte) 0xA9, 0x00, 0x01 };
		byte[] data = new byte[] { 0x00 };
		return bulidData(dcmd, data, OperateConstants.operateSeq);
	}

	/**
	 * 设置控制板
	 */

	public static byte[] snSettingEquipment(byte[] data) {
		byte[] dcmd = new byte[] { (byte) 0xA9, 0x01, ByteUtil.intToByte(data.length) };
		return bulidData(dcmd, data, OperateConstants.operateSeq);
	}

	/**
	 * 读取电机参数
	 */
	public static byte[] readDjParams() {
		byte[] data = {};
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x01, 0x01, 0x00 });// 挡板材料
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x02, 0x01, 0x00 });// 主从开闸速度
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x03, 0x01, 0x00 });// 主从关闸速度
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x04, 0x01, 0x00 });// 主从阻挡电流
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x05, 0x01, 0x00 });// 主从开闸时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x06, 0x01, 0x00 });// 主从关闸时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x07, 0x01, 0x00 });// 主从开闸角度
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x08, 0x01, 0x00 });// 强推脉冲数
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x09, 0x01, 0x00 });// 强推恢复时间
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x0A, 0x01, 0x00 });// 阻挡反弹角度
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xE0, 0x0B, 0x01, 0x00 });// 阻挡模式选择
		data = ArrayUtils.addAll(data, new byte[] { (byte) 0xCA, 0x00, 0x01, 0x00 });// 开关闸间隔时间

		return bulidData(new byte[] {}, data, OperateConstants.operateRead);
	}
	
	/**
	 * 拼接有效期设置信息
	 * @param equip
	 * @return
	 * @throws ParseException 
	 */
	public static byte[] settingEquipTime(String masterValue) throws ParseException {
		byte[] dcmd = new byte[] { (byte) 0xD0, 0x00, 0x08};

		byte[] data = {};
		
		data = ArrayUtils.addAll(data, ByteUtil.hexStringToBytes(masterValue.substring(0, 4)));
		data = ArrayUtils.addAll(data, ByteUtil.hexStringToBytes(masterValue.substring(5, 7)));
		data = ArrayUtils.addAll(data, ByteUtil.hexStringToBytes(masterValue.substring(8, 10)));
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(DateUtil.stringToDate(masterValue, "yyyy-MM-dd HH:mm:ss"));
	    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	    if (w < 0)
	        w = 0;
		
		data = ArrayUtils.add(data, ByteUtil.intToByte(w));
		data = ArrayUtils.addAll(data, ByteUtil.hexStringToBytes(masterValue.substring(11,13)));
		data = ArrayUtils.addAll(data, ByteUtil.hexStringToBytes(masterValue.substring(14,16)));
		data = ArrayUtils.addAll(data, ByteUtil.hexStringToBytes(masterValue.substring(17,19)));
		
		return bulidData(dcmd, data, OperateConstants.operateSetting);
	}
}
