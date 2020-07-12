package cn.jeefast.rest.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jeefast.common.exception.RRException;
import cn.jeefast.rest.entity.Equipment;
import cn.jeefast.rest.entity.ReadRecord;
import cn.jeefast.rest.entity.ReadStatusEntity;

/**
 * 硬件消息返回处理类
 * 
 * @author zgx
 *
 */
public class ReturnUtil {
	
	private static Logger log = LoggerFactory.getLogger(ReturnUtil.class);
	/**
	 * 得到实际长度的通信数据 去掉了贞头贞尾
	 * 
	 * @param data
	 * @return
	 */
	
	public static Map<String, Object> getCommunicationData(byte[] dealData) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result", "success");
		
		byte[] data = new byte[] {};
		
		for(int i=0;i<dealData.length;i++) {
			Byte b = dealData[i];
			if(i==0) {
				data = ArrayUtils.add(data, b);
			}else {
				if(b.equals((byte)0x7d)) {
					if(((Byte)dealData[i+1]).equals((byte)0x5e)) {
						data = ArrayUtils.add(data, (byte)0x7e);
						++i;
					}else if(((Byte)dealData[i+1]).equals((byte)0x5d)) {
						data = ArrayUtils.add(data, (byte)0x7d);
						++i;
					}else {
						data = ArrayUtils.add(data, b);
					}
				}else if(b.equals((byte)0x7e)){
					data = ArrayUtils.add(data, b);
					log.error("接受到的数据："+ByteUtil.bytesToHexString(Arrays.copyOf(dealData, i)));
					break;
				}else {
					data = ArrayUtils.add(data, b);
				}
			}
		}
		
		log.error("数据格式转换后："+ByteUtil.bytesToHexString(data));
		byte headField = data[0];
		// 校验消息贞头
		if (headField != 0x7E) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "返回消息帧头错误");
			return returnMap;
		}
		
		byte crtn = data[5];
		if (crtn == OperateConstants.crtnReq01) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "校验失败");
		} else if (crtn == OperateConstants.crtnReq02) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "不能识别的CCMD指令");
		}
		
		short length = ByteUtil.bytesToShort(Arrays.copyOfRange(data, 6, 8));
		// 校验尾部
		byte tailField = data[length + 10];
		if (tailField != 0x7E) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "返回消息帧尾错误");
			
			return returnMap;
		}

		// 校验crc
		if (!OperateUtil.validCrc(Arrays.copyOfRange(data, 1, length + 10))) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "返回消息CRC校验失败");
			
			return returnMap;
		}
		
		returnMap.put("data", Arrays.copyOfRange(data, 8, length + 8));// 返回的是包数据
		return returnMap;
	}
	
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String settingReturn(byte[] data) {
		
		Map<String,Object> returnMap = getCommunicationData(data);
		
		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			throw new RRException(String.valueOf(returnMap.get("msg")));
		}
		
		log.error("==========开始处理数据==========");

		data = (byte[]) returnMap.get("data");
		
		byte[] dcmd = Arrays.copyOfRange(data, 0, 2);
		if (Arrays.equals(dcmd, new byte[] { 0x00, 0x00 })) {
			try {
				byte returnCode = data[3];
				if (returnCode == OperateConstants.returnReq01) {
					log.error("权限校验失败:returnData====="+ByteUtil.bytesToHexString(data));
					throw new RRException("权限校验失败");
				} else {
					return "指令执行成功";
				}
			}catch (Exception e) {
				log.error("上传失败:returnData====="+ByteUtil.bytesToHexString(data));
				throw new RRException("上传失败");
			}
		}
		log.error("无法识别DCMD:returnData====="+ByteUtil.bytesToHexString(data));
		throw new RRException("无法识别DCMD");
	}

	/**
	 * 搜索设备消息处理
	 */
	public static Map<String, Object> returnSearchList(byte[] data) {
		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		System.out.println("==========开始处理数据==========");

		data = (byte[]) returnMap.get("data");

		byte[] dcmd = Arrays.copyOfRange(data, 0, 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xA9, 0x00 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "DCMD类型错误");
			return returnMap;
		}

		// 校验数据返回码
		byte returnCode = data[3];
		if (returnCode == 0x01) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "无法识别命令");
		} else if (returnCode == 0x02) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "数据参数错误");
		} else if (returnCode == 0x03) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "存储空间已满");
		} else if (returnCode == 0x04) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "数据存储空间已空");
		} else {
			byte[] equipmentBytes = Arrays.copyOfRange(data, 4, data.length);

			List<Equipment> equList = new ArrayList<Equipment>();
			// 30个字节为一个设备
			for (int i = 0; i < equipmentBytes.length / 36; i++) {
				Equipment eq = ByteUtil.bytesToEquipment(Arrays.copyOfRange(equipmentBytes, i * 36, (i + 1) * 36));
				equList.add(eq);
			}
			returnMap.put("data", equList);
		}
		return returnMap;
	}

	public static Map<String, Object> returnWorkMode(byte[] data, Equipment equip) {
		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		System.out.println("==========开始处理数据==========");

		int nowIndex = 0;

		data = (byte[]) returnMap.get("data");

		// ===================闸机类型==================================
		byte[] dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x00 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "闸机类型DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		byte returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取闸机类型返回码错误");
			}
			returnMap.put("result", "fail");
		}

		equip.setZjType(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ===============电机类型========================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x01 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取电机类型DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取电机类型数据返回码错误");
			}
			returnMap.put("result", "fail");
		}

		equip.setDjType(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// =============闸机模式=====================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x02 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取闸机模式DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取闸机模式数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setZjModel(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ===================左通行模式=================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x03 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取左通行模式DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取左通行模式数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setLeftCrossModel(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ==========右通行模式======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x04 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "右通行模式DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取右通行数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setRightCrossModel(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ==============记忆模式=====================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x05 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "记忆模式DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取记忆模式数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setRemember(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ==================反向穿物通行====================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x06 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "反向穿行通行DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取反向穿行通行数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setFxwpCross(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============儿童刷卡通行==================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x07 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "儿童刷卡DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取儿童刷卡数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setBabyCross(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ===============闸机运行模式==================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x08 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "闸机运行模式DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取闸机运行模式数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setZjWorkModel(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============翼闸尾随关闸=====================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x0B })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "尾随关闸DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取尾随关闸数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setYzwsgzModel(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		returnMap.put("data", equip);
		return returnMap;
	}

	/**
	 * 时间参数返回处理
	 * 
	 * @param data
	 * @param equip
	 * @return
	 */
	public static Map<String, Object> returnTimeParam(byte[] data, Equipment equip) {
		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		System.out.println("==========开始处理数据==========");

		int nowIndex = 0;

		data = (byte[]) returnMap.get("data");

		// =============主马达运行速度=============
		byte[] dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x09 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "主马达运行速度DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		byte returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取主马达运行速度数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setZmdWorkSpeed(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============副马达运行速度======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC0, 0x0A })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "副马达运行速度DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取副马达运行速度数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setFmdWorkSpeed(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============马达最大运行时间======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC2, 0x00 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "马达最大运行时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "马达最大运行时间数据返回码错误");
			return returnMap;
		}

		equip.setMdMaxWorkTime(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============红外检测时间======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC2, 0x01 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "红外检测行时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取红外检测数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setHwjcTime(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============通行间隔时间======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC2, 0x02 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "通行间隔时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取通行间隔时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setTxjgTime(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============等待人员进入时间======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC2, 0x03 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "等待人员进入时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取等待人员进入时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setDdryjrTime(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============人员滞留时间======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC2, 0x04 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "人员滞留时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取人员滞留时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setRyzlTime(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============延时关闸时间======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC2, 0x05 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "延时关闸时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取延时关闸时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setYsgzTime(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============自由通行间隔时间======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC2, 0x06 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "自由通行间隔时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取自由通行间隔时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setZytxjgTime(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		returnMap.put("data", equip);
		return returnMap;
	}
	
	
	public static Map<String,Object> returnAlarmParam(byte[] data,Equipment equip){

		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		System.out.println("==========开始处理数据==========");

		int nowIndex = 0;

		data = (byte[]) returnMap.get("data");

		// ===================闯入报警==================================
		byte[] dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC4, 0x00 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "闯入报警DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		byte returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取闯入报警数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setCrAlarm(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ===============尾随类型========================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC4, 0x01 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "尾随报警DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取尾随报警数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setWsAlarm(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ============滞留报警=====================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC4, 0x02 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "滞留报警DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取滞留报警数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setZlAlarm(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ===================自检报警=================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC4, 0x03 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "自检报警DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取自检报警数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setZjAlarm(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		// ==========潜回报警======================
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xC4, 0x04 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "潜回报警DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取潜回报警数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		equip.setQhAlarm(String.valueOf(ByteUtil.bytesToInt(new byte[] { data[++nowIndex] })));

		
		returnMap.put("data", equip);
		return returnMap;
	
	}
	
	/**
	 * 电机参数返回处理
	 * @param data
	 * @param equip
	 * @return
	 */
	public static Map<String,Object> returnDjParam(byte[] data,Equipment equip){

		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		System.out.println("==========开始处理数据==========");

		int nowIndex = 0;

		data = (byte[]) returnMap.get("data");

		// ===================伺机电机挡板材料==================================
		byte[] dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x01 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "伺机电机挡板材料DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		byte returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取挡板材料数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}
		
		nowIndex+=1;
		equip.setDbcl(String.valueOf(ByteUtil.getShort(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));

		// ===============主从开闸速度========================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x02 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "主从开闸速度DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取主从开闸速度数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZkzSpeed(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		nowIndex+=2;
		equip.setCkzSpeed(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		// ============主从关闸速度=====================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x03 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "主从关闸速度DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取主从关闸速度数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZgzSpeed(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		nowIndex+=2;
		equip.setCgzSpeed(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));

		// ===================主从阻挡电流=================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x04 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "主从阻挡电流DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取阻挡电流数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZzdElectric(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		nowIndex+=2;
		equip.setCzdElectric(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		// ==========主从开闸时间======================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x05 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "主从开闸时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取主从开闸时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZkzTime(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		nowIndex+=2;
		equip.setCkzTime(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		
		//===========主从关闸时间=============
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x06 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "主从关闸时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取主从关闸时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZgzTime(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		nowIndex+=2;
		equip.setCgzTime(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		
		//==================主从开闸角度======================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x07 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "主从开闸角度DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取主开关闸角度数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZkzAngle(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		nowIndex+=2;
		equip.setCkzAngle(String.valueOf(ByteUtil.getInt(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		
		//=============强推脉冲数==========================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x08 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "强推脉冲数DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取强推脉冲数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setQtmcs(String.valueOf(ByteUtil.getShort(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		
		//============强推脉冲恢复时间=======================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x09 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "强推脉冲恢复时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取强推脉冲恢复时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setQthfTime(String.valueOf(ByteUtil.getShort(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		
		//===========阻挡反弹角度=======================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x0A })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "阻挡反弹角度DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取阻挡反弹角度数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZdftAngle(String.valueOf(ByteUtil.getShort(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		
		//==============阻挡模式选择=======================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xE0, 0x0B })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "阻挡模式选择DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取闸阻挡模式选择数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}

		nowIndex+=1;
		equip.setZdModel(String.valueOf(ByteUtil.getShort(Arrays.copyOfRange(data, nowIndex, nowIndex+2), false)));
		
		//===========开关闸间隔时间===================
		nowIndex += 2;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xCA, 0x00 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "开关闸间隔时间DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;

		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取开关闸间隔时间数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}
		
		

		nowIndex+=1;
		equip.setKzjgTime(String.valueOf(ByteUtil.bytesToInt(Arrays.copyOfRange(data, nowIndex, nowIndex+1))));
		nowIndex+=1;
		equip.setGzjgTime(String.valueOf(ByteUtil.bytesToInt(Arrays.copyOfRange(data, nowIndex, nowIndex+1))));
		
		returnMap.put("data", equip);
		return returnMap;
	
	}
	
	/**
	 * 版本号
	 * @param data
	 * @param equip
	 * @return
	 */
	public static Map<String,Object> returnVersion(byte[] data,Equipment equip){

		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		log.error("==========开始处理数据==========");

		int nowIndex = 0;

		data = (byte[]) returnMap.get("data");
		
		byte[] dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xA2, 0x02 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取版本号DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;
		
		byte length = data[nowIndex];

		byte returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取版本号数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}
		
		equip.setVersion(new String(Arrays.copyOfRange(data,nowIndex+1, nowIndex+length)));
		
		nowIndex += length;
		
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xD0, 0x00 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取时钟DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;
		
		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取时钟数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}
		
		equip.setYear(Integer.valueOf(ByteUtil.bytesToHexString(Arrays.copyOfRange(data,nowIndex+1, nowIndex+3))));
		nowIndex +=2;
		try {
			equip.setMonth(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setMonth(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		try {
			equip.setDay(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setDay(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		try {
			equip.setWeek(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setWeek(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		try {
			equip.setHours(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setHours(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		try {
			equip.setMinute(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setMinute(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		try {
			equip.setSecond(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setSecond(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		
		nowIndex += 1;
		dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xD0, 0x01 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取有效期DCMD类型错误");
			return returnMap;
		}

		nowIndex += 2;
		
		returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取有效期数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}
		
		equip.setValidYear(Integer.valueOf(ByteUtil.bytesToHexString(Arrays.copyOfRange(data,nowIndex+1, nowIndex+3))));
		nowIndex +=2;
		try {
			equip.setValidMonth(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setValidMonth(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		try {
			equip.setValidDay(Integer.valueOf(ByteUtil.bytesToHexString(new byte[] {data[++nowIndex]})));
		}catch (Exception e) {
			equip.setValidDay(Integer.valueOf(ByteUtil.bytesToInt(new byte[] {data[++nowIndex]})));
		}
		
		
		returnMap.put("data", equip);
		return returnMap;
	}
	
	/**
	 * 读取记录返回消息处理
	 * @param data
	 * @return
	 */
	public static Map<String,Object> returnReadRecord(byte[] data){
		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		
		System.out.println("==========开始处理数据==========");
		
		int nowIndex = 0;

		data = (byte[]) returnMap.get("data");
		
		byte[] dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xA2, 0x00 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取记录DCMD类型错误");
			return returnMap;
		}
		
		nowIndex += 2;
		
		byte length = data[nowIndex];

		byte returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取版本号数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}
		
		nowIndex+=1;
		
		byte[] mac = Arrays.copyOfRange(data, nowIndex, nowIndex+6);
		
		nowIndex+=6;
		
		byte[] recordBytes = Arrays.copyOfRange(data, nowIndex, nowIndex+length-1);
		
		List<ReadRecord> records = new ArrayList<ReadRecord>();
		
		//每一条记录  都是12个字节   
		for(int i=0;i<recordBytes.length/12;i++) {
			ReadRecord r = new ReadRecord();
			r.setMac(ByteUtil.bytesToSn(mac));
			byte[] ddata = Arrays.copyOfRange(recordBytes, i*12, (i+1)*12);
			try {
				r.setDate(DateUtil.stringToDate(ByteUtil.bytesToTime(Arrays.copyOfRange(ddata, 0, 8)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ddata[8]==0x02) {
				r.setRemark("通行记录");
				if(ddata[10]==0x01) {
					r.setResource("左向刷卡");
				}else if(ddata[10]==0x02) {
					r.setResource("右向刷卡");
				}else if(ddata[10]==0x03) {
					r.setResource("左向通行");
				}else if(ddata[10]==0x04) {
					r.setResource("右向通行");
				}else if(ddata[10]==0x05) {
					r.setResource("左向等待超时");
				}else if(ddata[10]==0x06) {
					r.setResource("右向等待超时");
				}
			}else if(ddata[8]==0x03) {
				r.setRemark("异常事件记录");
				if(ddata[10]==0x01) {
					if(ddata[11]==0x00) {
						r.setResource("主电机运行异常(左到位检测异常)");
					}else if(ddata[11]==0x01) {
						r.setResource("主电机运行异常(右到位检测异常)");
					}else if(ddata[11]==0x02) {
						r.setResource("主电机运行异常(零   位检测异常)");
					}else if(ddata[11]==0x03) {
						r.setResource("主电机运行异常(HALL检测异常)");
					}
				}else if(ddata[10]==0x02) {
					if(ddata[11]==0x00) {
						r.setResource("副电机运行异常(左到位检测异常)");
					}else if(ddata[11]==0x01) {
						r.setResource("副电机运行异常(右到位检测异常)");
					}else if(ddata[11]==0x02) {
						r.setResource("副电机运行异常(零   位检测异常)");
					}else if(ddata[11]==0x03) {
						r.setResource("副电机运行异常(HALL检测异常)");
					}
				}else if(ddata[10]==0x03) {
					r.setResource("红外检测异常("+ByteUtil.bytesToInt(new byte[] {ddata[11]})+"号红外异常");
				}else if(ddata[10]==0x04) {
					if(ddata[11]==0x00) {
						r.setResource("通讯异常(主副控制器通讯异常)");
					}else if(ddata[11]==0x01) {
						r.setResource("通讯异常(控制器与驱动器通讯异常)");
					}else if(ddata[11]==0x02) {
						r.setResource("通讯异常(主副驱动器通讯异常)");
					}
				}else if(ddata[10]==0x05) {
					if(ddata[11]==0x00) {
						r.setResource("驱动异常(主驱动器运行异常)");
					}else if(ddata[11]==0x01) {
						r.setResource("驱动异常(从驱动器运行异常)");
					}
				}
				records.add(r);
			}
		}
		
		returnMap.put("data", records);
		
		return returnMap;
	}
    
	/**
	 * 读取状态返回消息处理
	 * @param data
	 * @return
	 */
	public static Map<String,Object> returnReadStatus(byte[] data){
		Map<String, Object> returnMap = getCommunicationData(data);

		if (String.valueOf(returnMap.get("result")).equals("fail")) {
			return returnMap;
		}
		
		System.out.println("==========开始处理数据==========");
		
		int nowIndex = 0;

		data = (byte[]) returnMap.get("data");
		
		byte[] dcmd = Arrays.copyOfRange(data, nowIndex, nowIndex + 2);
		if (!Arrays.equals(dcmd, new byte[] { (byte) 0xA2, 0x01 })) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "读取状态DCMD类型错误");
			return returnMap;
		}
		
		nowIndex += 2;
		
		byte length = data[nowIndex];

		byte returnCode = data[++nowIndex];// +1是因为跳过长度位
		if (returnCode != 0x00) {
			if(returnCode==0x04) {
				returnMap.put("msg", "数据存储空间已空");
			}else if(returnCode==0x03) {
				returnMap.put("msg", "存储空间已满");	
			}else {
				returnMap.put("msg", "读取状态数据返回码错误");
			}
			returnMap.put("result", "fail");
			return returnMap;
		}
		
		
		ReadStatusEntity entity = new ReadStatusEntity();
		
		
		byte workModel = data[++nowIndex];
		
		String hhStatus = ByteUtil.byteArrayToString(new byte[] {data[nowIndex+4]});
		byte zzjStatus = data[nowIndex+5];
		byte fzjStatus = data[nowIndex+6];
		
		byte zDriverStatus = data[nowIndex+7];
		byte cDriverStatus = data[nowIndex+8];
		
		byte zjCrossStatus = data[nowIndex+9];
		
		byte kzqStatus = data[nowIndex+10];
		
		byte[] personTotal = Arrays.copyOfRange(data, nowIndex+11, nowIndex+13);
		
		StringBuffer sb = new StringBuffer();
		
		String statusBit = ByteUtil.byteArrayToString(Arrays.copyOfRange(data, nowIndex+1, nowIndex+4));
	
		entity.setRedLine(statusBit);
		
		if(workModel==0x00) {
			entity.setZjWorkModel("正常模式");
		}else if(workModel==0x01){
			entity.setZjWorkModel("老化模式");
		}else if(workModel==0x02){
			entity.setZjWorkModel("紧急模式");
		}else if(workModel==0x03){
			entity.setZjWorkModel("指令模式");
		}else if(workModel==0x04){
			entity.setZjWorkModel("停机维护");
		}
		
		if(zzjStatus==0x00) {
			entity.setZzjStatus("闸机失能状态");
		}else if(zzjStatus==0x01) {
			entity.setZzjStatus("驱动器找零点中");
		}else if(zzjStatus==0x02) {
			entity.setZzjStatus("正向开门中");
		}else if(zzjStatus==0x03) {
			entity.setZzjStatus("反向开门中");
		}else if(zzjStatus==0x04) {
			entity.setZzjStatus("正向关门中");
		}else if(zzjStatus==0x05) {
			entity.setZzjStatus("反向关门中");
		}else if(zzjStatus==0x06) {
			entity.setZzjStatus("正向开门到位");
		}else if(zzjStatus==0x07) {
			entity.setZzjStatus("反向开门到位");
		}else if(zzjStatus==0x08) {
			entity.setZzjStatus("关门到位");
		}else if(zzjStatus==0x09) {
			entity.setZzjStatus("运行阻挡");
		}else if(zzjStatus==0x0A) {
			entity.setZzjStatus("停机强推");
		}else if(zzjStatus==0x0B) {
			entity.setZzjStatus("暴力闯闸");
		}else if(zzjStatus==0x0C) {
			entity.setZzjStatus("闸机急停");
		}else if(zzjStatus==0x0D) {
			entity.setZzjStatus("CAN通讯超时");
		}else if(zzjStatus==0x0E) {
			entity.setZzjStatus("对轴运行阻挡");
		}else if(zzjStatus==0x0F) {
			entity.setZzjStatus("对轴停机强推");
		}else if(zzjStatus==0x10) {
			entity.setZzjStatus("对轴暴力闯闸");
		}
		
		if(fzjStatus==0x00) {
			entity.setCzjStatus("闸机失能状态");
		}else if(fzjStatus==0x01) {
			entity.setCzjStatus("驱动器找零点中");
		}else if(fzjStatus==0x02) {
			entity.setCzjStatus("正向开门中");
		}else if(fzjStatus==0x03) {
			entity.setCzjStatus("反向开门中");
		}else if(fzjStatus==0x04) {
			entity.setCzjStatus("正向关门中");
		}else if(fzjStatus==0x05) {
			entity.setCzjStatus("反向关门中");
		}else if(fzjStatus==0x06) {
			entity.setCzjStatus("正向开门到位");
		}else if(fzjStatus==0x07) {
			entity.setCzjStatus("反向开门到位");
		}else if(fzjStatus==0x08) {
			entity.setCzjStatus("关门到位");
		}else if(fzjStatus==0x09) {
			entity.setCzjStatus("运行阻挡");
		}else if(fzjStatus==0x0A) {
			entity.setCzjStatus("停机强推");
		}else if(fzjStatus==0x0B) {
			entity.setCzjStatus("暴力闯闸");
		}else if(fzjStatus==0x0C) {
			entity.setCzjStatus("闸机急停");
		}else if(fzjStatus==0x0D) {
			entity.setCzjStatus("CAN通讯超时");
		}else if(fzjStatus==0x0E) {
			entity.setCzjStatus("对轴运行阻挡");
		}else if(fzjStatus==0x0F) {
			entity.setCzjStatus("对轴停机强推");
		}else if(fzjStatus==0x10) {
			entity.setCzjStatus("对轴暴力闯闸");
		}
		
		if(zDriverStatus==0x00) {
			entity.setzDriverStatus("无异常");
		}else if(zDriverStatus==0x01) {
			entity.setzDriverStatus("上电HALL出错");
		}else if(zDriverStatus==0x02) {
			entity.setzDriverStatus("EEPROM出错");
		}else if(zDriverStatus==0x03) {
			entity.setzDriverStatus("转堵");
		}else if(zDriverStatus==0x04) {
			entity.setzDriverStatus("位置偏差过大");
		}else if(zDriverStatus==0x05) {
			entity.setzDriverStatus("识别缺相");
		}else if(zDriverStatus==0x06) {
			entity.setzDriverStatus("识别反向");
		}else if(zDriverStatus==0x07) {
			entity.setzDriverStatus("识别Z丢失");
		}else if(zDriverStatus==0x08) {
			entity.setzDriverStatus("识别Hall丢失");
		}else if(zDriverStatus==0x09) {
			entity.setzDriverStatus("Z丢失");
		}else if(zDriverStatus==0x0A) {
			entity.setzDriverStatus("V相电流校零出错");
		}else if(zDriverStatus==0x0B) {
			entity.setzDriverStatus("U相电流校零出错");
		}else if(zDriverStatus==0x0C) {
			entity.setzDriverStatus("欠压");
		}else if(zDriverStatus==0x0D) {
			entity.setzDriverStatus("过压");
		}else if(zDriverStatus==0x0E) {
			entity.setzDriverStatus("过温");
		}else if(zDriverStatus==0x0F) {
			entity.setzDriverStatus("过载");
		}else if(zDriverStatus==0x10) {
			entity.setzDriverStatus("过流");
		}
		
		if(cDriverStatus==0x00) {
			entity.setcDriverStatus("无异常");
		}else if(cDriverStatus==0x01) {
			entity.setcDriverStatus("上电HALL出错");
		}else if(cDriverStatus==0x02) {
			entity.setcDriverStatus("EEPROM出错");
		}else if(cDriverStatus==0x03) {
			entity.setcDriverStatus("转堵");
		}else if(cDriverStatus==0x04) {
			entity.setcDriverStatus("位置偏差过大");
		}else if(cDriverStatus==0x05) {
			entity.setcDriverStatus("识别缺相");
		}else if(cDriverStatus==0x06) {
			entity.setcDriverStatus("识别反向");
		}else if(cDriverStatus==0x07) {
			entity.setcDriverStatus("识别Z丢失");
		}else if(cDriverStatus==0x08) {
			entity.setcDriverStatus("识别Hall丢失");
		}else if(cDriverStatus==0x09) {
			entity.setcDriverStatus("Z丢失");
		}else if(cDriverStatus==0x0A) {
			entity.setcDriverStatus("V相电流校零出错");
		}else if(cDriverStatus==0x0B) {
			entity.setcDriverStatus("U相电流校零出错");
		}else if(cDriverStatus==0x0C) {
			entity.setcDriverStatus("欠压");
		}else if(cDriverStatus==0x0D) {
			entity.setcDriverStatus("过压");
		}else if(cDriverStatus==0x0E) {
			entity.setcDriverStatus("过温");
		}else if(cDriverStatus==0x0F) {
			entity.setcDriverStatus("过载");
		}else if(cDriverStatus==0x10) {
			entity.setcDriverStatus("过流");
		}
		
		if(zjCrossStatus==0x00) {
			entity.setZjCrossStatus("空闲状态");
		}else if(zjCrossStatus==0x01) {
			entity.setZjCrossStatus("左向刷卡");
		}else if(zjCrossStatus==0x02) {
			entity.setZjCrossStatus("右向刷卡");
		}else if(zjCrossStatus==0x03) {
			entity.setZjCrossStatus("左向通行");
		}else if(zjCrossStatus==0x04) {
			entity.setZjCrossStatus("右向通行");
		}else if(zjCrossStatus==0x05) {
			entity.setZjCrossStatus("左向闯入");
		}else if(zjCrossStatus==0x06) {
			entity.setZjCrossStatus("右向闯入");
		}else if(zjCrossStatus==0x07) {
			entity.setZjCrossStatus("左向尾随");
		}else if(zjCrossStatus==0x08) {
			entity.setZjCrossStatus("右向尾随");
		}else if(zjCrossStatus==0x09) {
			entity.setZjCrossStatus("左向滞留");
		}else if(zjCrossStatus==0x0A) {
			entity.setZjCrossStatus("右向滞留");
		}else if(zjCrossStatus==0x0B) {
			entity.setZjCrossStatus("左向潜回");
		}else if(zjCrossStatus==0x0C) {
			entity.setZjCrossStatus("右向潜回");
		}
		
		if(kzqStatus==0x00) {
			entity.setKzqStatus("正常");
		}else if(kzqStatus==0x01) {
			entity.setKzqStatus("主副控制器通讯异常");
		}else if(kzqStatus==0x02) {
			entity.setKzqStatus("控制器与驱动器通讯异常");
		}else if(kzqStatus==0x03) {
			entity.setKzqStatus("主从驱动器通讯异常");
		}else if(kzqStatus==0x04) {
			entity.setKzqStatus("上电红外检测异常");
		}else if(kzqStatus==0x05) {
			entity.setKzqStatus("HALL检测异常");
		}else if(kzqStatus==0x06) {
			entity.setKzqStatus("马达运行异常");
		}
		entity.setPersonTotal(String.valueOf(ByteUtil.bytesToInt(personTotal)));
		
		entity.setHeStatus(hhStatus);
		
		returnMap.put("data", entity);
		
		return returnMap;
	}
	
	public static void main(String[] args) {
		System.out.println(ByteUtil.bytesToInt(new byte[] {10}));
	}
}
