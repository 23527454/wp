package com.xk.netty.handler;

import java.util.Map;

import javax.annotation.Resource;

import com.xk.netty.util.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.fiacs.common.util.AuthorCodeUtil;
import com.fiacs.common.util.Constants;
import com.fiacs.common.util.EncodeToXmlUtil;
import com.fiacs.common.util.SocketUtil;
import com.xk.netty.service.CarService;
import com.xk.netty.service.CashBoxService;
import com.xk.netty.service.PersonService;
import com.xk.netty.service.UploadInfoService;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

@Component
@Sharable
public class ShortLinksServerHandler extends ChannelHandlerAdapter {

	private static Logger log = LoggerFactory.getLogger(ShortLinksServerHandler.class);

	@Resource
	private PersonService personServiceImpl;

	@Resource
	private CashBoxService cashBoxServiceImpl;

	@Resource
	private CarService carServiceImpl;

	@Resource
	private UploadInfoService uploadInfoServiceImpl;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			if (msg instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) msg;
				// 首先校验认证码
				String requestType = String.valueOf(map.get("type"));
				String authcode = String.valueOf(map.get("AuthorCode"));
				int seqNum = Integer.valueOf(String.valueOf(map.get("SeqNum")));
				if (Constants.VERIFY_IDENTIFY.equals(requestType)) {
					String equipSn = String.valueOf(map.get("EquipSN"));
					if (AuthorCodeUtil.checkBySn(authcode, equipSn, seqNum)) {
						Map<String, Object> equip = uploadInfoServiceImpl.queryEquipBySn(equipSn);
						if (equip == null) {
							log.error("数据库中不存在序列号为" + equipSn + "的数据");
							ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
							ctx.close();
							return;
						} else {
							ShortLinkChannelMap.addChannel(equipSn, String.valueOf(equip.get("id")), ctx.channel());
							log.info("设备序列号：【" + equipSn + "】短链接认证成功！");
							ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 0, equipSn)));
						}
					} else {
						log.error("校验错误：设备号【" + equipSn + "】");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
					}
					//转发修改设备基本信息
				}else if(Constants.MODIFY_EQUIP_TYPE.equals(requestType)) {
					String equipSn = String.valueOf(map.get("EquipSN"));
					if (AuthorCodeUtil.checkBySnOfSearch(authcode, equipSn, seqNum)) {
						String ip = String.valueOf(map.get("SocketIP"));
						System.out.println("name::::"+String.valueOf(map.get("EquipName")));
						int port = Integer.valueOf(String.valueOf(map.get("SocketPort")));
						String requestXml = EncodeToXmlUtil.modifyEquip(map, seqNum);
						byte[] resultBytes = SocketUtil.sendMsg(ip, port, requestXml.getBytes("UTF-8"));
						System.out.println(new String(resultBytes));
						ctx.writeAndFlush(Unpooled.copiedBuffer(resultBytes));
					} else {
						log.error("校验错误：设备号【" + equipSn + "】");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, Integer.parseInt("E4", 16), equipSn)));
					}
					//重启
				}else if(Constants.RESTART_TYPE.equals(requestType)) {
					String equipSn = String.valueOf(map.get("EquipSN"));
					if (AuthorCodeUtil.checkBySnOfSearch(authcode, equipSn, seqNum)) {
						String ip = String.valueOf(map.get("SocketIP"));
						int port = Integer.valueOf(String.valueOf(map.get("SocketPort")));
						String requestXml = EncodeToXmlUtil.resetEquip(Integer.valueOf(String.valueOf(map.get("ResetType"))), seqNum, equipSn, null, null);
						byte[] resultBytes = SocketUtil.sendMsg(ip, port, requestXml.getBytes());
						ctx.writeAndFlush(Unpooled.copiedBuffer(resultBytes));
					} else {
						log.error("校验错误：设备号【" + equipSn + "】");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, Integer.parseInt("E4", 16), equipSn)));
					}
					//初始化
				}else if(Constants.MODIFY_PASSWORD_TYPE.equals(requestType)) {
					String equipSn = String.valueOf(map.get("EquipSN"));
					if (AuthorCodeUtil.checkByOldKey(authcode, equipSn, seqNum)) {
						String ip = String.valueOf(map.get("SocketIP"));
						int port = Integer.valueOf(String.valueOf(map.get("SocketPort")));
						String requestXml = EncodeToXmlUtil.modifyCommPwd(map, seqNum);
						byte[] resultBytes = SocketUtil.sendMsg(ip, port, requestXml.getBytes("UTF-8"));
						ctx.writeAndFlush(Unpooled.copiedBuffer(resultBytes));
					} else {
						log.error("校验错误：设备号【" + equipSn + "】");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, Integer.parseInt("E4", 16), equipSn)));
					}

				} else if(Constants.CONTROL_DOOR_TYPE.equals(requestType)){
					String equipSn = String.valueOf(map.get("EquipSN"));
					if (AuthorCodeUtil.checkByOldKey(authcode, equipSn, seqNum)) {
						String ip = String.valueOf(map.get("SocketIP"));
						int port = Integer.valueOf(String.valueOf(map.get("SocketPort")));
						String requestXml = EncodeToXmlUtil.controlDoor(map, seqNum);
						byte[] resultBytes = SocketUtil.sendMsg(ip, port, requestXml.getBytes("UTF-8"));
						log.info("控门回应："+ ByteUtil.bytesToHexString(resultBytes));
						ctx.writeAndFlush(Unpooled.copiedBuffer(resultBytes));
					} else {
						log.error("校验错误：设备号【" + equipSn + "】");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, Integer.parseInt("E4", 16), equipSn)));
					}
				}else {
					String equipSn = ShortLinkChannelMap.getEquipSn(ctx.channel());
					if (StringUtils.isEmpty(equipSn)) {
						log.error("==========通道对应设备序列号不存在================");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
						return;
					}
					log.error("==========" + equipSn + "进入 short read 业务处理 ======================");
					if (AuthorCodeUtil.checkBySn(authcode, equipSn, seqNum)) {
						String equipId = ShortLinkChannelMap.getEquipId(ctx.channel());
						if (StringUtils.isEmpty(equipId)) {
							log.error("==========通道对应设备主键不存在================");
							ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
							return;
						}
						// 交接事件处理
						int result=0;
						if (Constants.CONNECT_EVENT_TYPE.equals(requestType)) {
							result = uploadInfoServiceImpl.uploadConnectEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.ALARM_EVENT_TYPE.equals(requestType)) {
							result = uploadInfoServiceImpl.uploadAlarmEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.CAR_ARRIVE_EVENT_TYPE.equals(requestType)) {
							result = carServiceImpl.insertCarArriveEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.SUPERGO_EVENT_TYPE.equals(requestType)) {
							result = personServiceImpl.uploadSuperGoEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.WORKPERSON_EVENT_TYPE.equals(requestType)) {
							result = personServiceImpl.uploadCommissionerEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.CASHBOX_EVENT_TYPE.equals(requestType)) {
							result = cashBoxServiceImpl.uploadCashBoxEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.CASHBOX_ORDER_TYPE.equals(requestType)) {
							result = cashBoxServiceImpl.uploadCashBoxOrderEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.CASHBOX_RETURN_TYPE.equals(requestType)) {
							result = cashBoxServiceImpl.uploadCashBoxReturnEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.SAFEGUARD_EVENT_TYPE.equals(requestType)) {
							result = personServiceImpl.uploadSafeGuardEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));

						} else if (Constants.ACCESS_EVENT_TYPE.equals(requestType)) {
							result = uploadInfoServiceImpl.uploadAccessEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));
						} else if (Constants.ACCESS_ALARM_TYPE.equals(requestType)) {
							result = uploadInfoServiceImpl.uploadAccessAlarmEvent(map, equipId);
							ctx.writeAndFlush(
									Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, result, equipSn)));
							// 下载照片
						} else if (Constants.BIG_DATA_TYPE.equals(requestType)) {
							String infoType = String.valueOf(map.get("InfoType"));
							String data = String.valueOf(map.get("data"));
							int currIndex = Integer.valueOf(String.valueOf(map.get("CurrIndex")));
							int totalcount = Integer.valueOf(String.valueOf(map.get("TotalCount")));
							if (totalcount != currIndex) {
								if (currIndex == 1) {
									Snippet.bigDataMap.put(equipSn, data);
								} else {
									Snippet.addBigData(equipSn, data);
								}
								return;
							} else {
								if (totalcount > 1) {
									String hasData = Snippet.bigDataMap.get(equipSn);
									String requestData = String.valueOf(map.get("data"));
									map.put("data", hasData + requestData);
								}
							}
							// 交接人员照片||押解员照片
							if (Constants.INFOTYPE_UPLOAD_CONNECT_PERSON_PHOTO.equals(infoType)
									|| Constants.INFOTYPE_UPLOAD_SUPERGO_PHOTO.equals(infoType)) {
								personServiceImpl.uploadSuperGoEventImag(map, equipId);
							} else if (Constants.INFOTYPE_UPLOAD_ALARM_PHOTO.equals(infoType)) {
								uploadInfoServiceImpl.uploadAlarmEventImag(map, equipId);
							} else if (Constants.INFOTYPE_UPLOAD_COMMISSION_FINGER_MODE.equals(infoType)) {
								personServiceImpl.updateFingerModeImag(map, equipId);
							} else if (Constants.INFOTYPE_UPLOAD_SAFEGUARD_PHOTO.equals(infoType)) {
								personServiceImpl.uploadSafeGuardEventImag(map, equipId);
							} else if (Constants.INFOTYPE_UPLOAD_COMMISSION_ACCESS_PHOTO.equals(infoType)) {
								uploadInfoServiceImpl.uploadAccessEventImag(map, equipId);
							} else if (Constants.INFOTYPE_UPLOAD_ACCESS_ALARM_PHOTO.equals(infoType)) {
								uploadInfoServiceImpl.uploadAccessAlarmEventImag(map, equipId);
							}
							ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 0, equipSn)));
						}
						//上传成功  通知大数据界面
						if(result==0){
							FiacsServerWebSocketChannelMap.tellMessage(map,equipId);
						}
					} else {
						// 认证失败
						log.error("校验错误：设备号【" + equipSn + "】");
						ctx.writeAndFlush(EncodeToXmlUtil.returnResult(seqNum, Integer.parseInt("E4", 16), equipSn));
					}
				}
			}else {
				log.error("==========xml decode结果类型不符合，跳过===============");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 出现异常就关闭
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//log.error("=================断开连接========================");
		ShortLinkChannelMap.removeChannel(ctx.channel());
	}
}
