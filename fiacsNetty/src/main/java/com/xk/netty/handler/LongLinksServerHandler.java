package com.xk.netty.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.xk.netty.entity.*;
import com.xk.netty.service.*;
import com.xk.netty.util.LongLinkChannelNoDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.fiacs.common.util.AuthorCodeUtil;
import com.fiacs.common.util.Constants;
import com.fiacs.common.util.EncodeToXmlUtil;
import com.fiacs.common.util.Encodes;
import com.fiacs.common.util.ResultEnum;
import com.xk.netty.util.LongLinkChannelMap;
import com.xk.netty.util.Snippet;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

@Component
@Sharable
public class LongLinksServerHandler extends ChannelHandlerAdapter {

	private static Logger log = LoggerFactory.getLogger(LongLinksServerHandler.class);

	@Resource
	private PersonService personServiceImpl;

	@Resource
	private CashBoxService cashBoxServiceImpl;

	@Resource
	private CarService carServiceImpl;

	@Resource
	private UploadInfoService uploadInfoServiceImpl;

	@Resource
	private DoorService doorServiceImpl;

	@Resource
	private TaskClassService taskClassServiceImpl;

	@Resource
	private InformationReleaseService informationReleaseServiceImpl;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			log.info("========="+ctx.channel().remoteAddress()+"join longlink channelRead============"+(msg instanceof Map));
			if (msg instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) msg;
				String requestType = String.valueOf(map.get("type"));
				int seqNum = Integer.valueOf(String.valueOf(map.get("SeqNum")));
				String authorCode = String.valueOf(map.get("AuthorCode"));
				if (Constants.VERIFY_IDENTIFY.equals(requestType)) {
					String equipSn = String.valueOf(map.get("EquipSN"));
					if (AuthorCodeUtil.checkBySn(authorCode, equipSn, seqNum)) {
						// 避免重复处理认证请求
						if (LongLinkChannelMap.getEquipSn(ctx.channel()) != null) {
							log.info("=================="+ctx.channel().remoteAddress()+"重复认证==================");
							ctx.channel()
									.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 0, equipSn)));
							return;
						}
						Map<String, Object> equip = uploadInfoServiceImpl.queryEquipBySn(equipSn);
						if (equip == null) {
							log.error("========数据库中不存在序列号为" + equipSn + "的数据========");
							boolean addResult = LongLinkChannelNoDataMap.addChannel(equipSn,ctx.channel(),map);
							if(addResult){
								log.info("long link add LongLinkChannelNoDataMap");
								ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 0, equipSn)));
							}else{
								log.error("===========【"+equipSn+"】链接失败，已达到最大连接数=============");
								ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
							}
							return;
						} else {
							if(LongLinkChannelMap.equipList.get(equipSn)!=null) {
								log.error("=========存在不同端口的通道,关掉前者============");

								//ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
								//return;
								LongLinkChannelMap.getChannel(equipSn).close();
							}
							boolean addResult = LongLinkChannelMap.addChannel(equipSn, String.valueOf(equip.get("id")), ctx.channel(),map);
							if(addResult) {
								log.info("long link add LongLinkChannelMap");
								/*String equipName = String.valueOf(map.get("EquipName"));
								String equipVersion = String.valueOf(map.get("HardVersion"));
								LongLinkChannelMap.addEquip(equipSn, equipName, equipVersion, ctx.channel());*/
								ctx.channel()
										.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 0, equipSn)));

								// 更新数据库设备状态
								uploadInfoServiceImpl.updateEquipStatusBySn(equipSn, 1);
								log.info("设备序列号：【" + equipSn + "】长链接成功！");
							}else {
								log.error("===========【"+equipSn+"】链接失败，已达到最大连接数=============");
								ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
							}
						}
					} else {
						log.error("校验错误：设备号【" + equipSn + "】");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
					}
				}else {
					String validequipId = LongLinkChannelMap.getEquipId(ctx.channel());
					log.error("====equipSn:"+validequipId+"===channelId:"+ctx.channel().id());
					if (StringUtils.isEmpty(validequipId)) {
						log.error("==========通道对应已注册设备序列号不存在================");
						String equipSnNoData = LongLinkChannelNoDataMap.getEquipSn(ctx.channel());
						if(!StringUtils.isEmpty(equipSnNoData)){
							Map<String,Object> mmap = LongLinkChannelNoDataMap.getInfomationMap(ctx.channel());
							log.error("==========通道对应未注册设备序列号================");
							Map<String, Object> validEquip = uploadInfoServiceImpl.queryEquipBySn(equipSnNoData);
							if(validEquip!=null){
								log.error("==========【"+equipSnNoData+"】从未注册转移到已注册设备列表================");
								LongLinkChannelNoDataMap.removeEquipSn(ctx.channel());
								LongLinkChannelMap.addChannel(equipSnNoData, String.valueOf(validEquip.get("id")),ctx.channel(),mmap);
								ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSnNoData)));
							}else{
								log.error("==========设备仍然未注册================");
								ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSnNoData)));
							}
						}else{
							log.error("==========通道未对应未注册设备序列号================");
							ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSnNoData)));
						}
						return;
					}
					String equipSn = LongLinkChannelMap.getEquipSn(ctx.channel());
					if (!AuthorCodeUtil.checkBySn(authorCode, equipSn, seqNum)) {
						log.error("==========校验错误：设备号【" + equipSn + "】==========");
						ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
						return;
					} else {
						String equipId = LongLinkChannelMap.getEquipId(ctx.channel());
						if (StringUtils.isEmpty(equipId)) {
							log.error("==========通道对应设备主键不存在================");
							ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 1, equipSn)));
							return;
						}
						if (Constants.REQUEST_SYN_TYPE.equals(requestType)) {
							log.info("=========设备号：【"+equipSn+"】==请求同步数据==============");
							String newAuthCode = AuthorCodeUtil.generateCode(equipSn, seqNum + 1,
									Constants.COMMUNICATION_KEY_FACTOR);
							// 同步人员
							PersonEntity person = personServiceImpl.queryOne(equipId);
							if (person != null) {
								String personXml = EncodeToXmlUtil.personMapToXml(person.toMap(), newAuthCode, seqNum);
								ChannelFuture personTure;

								personTure = ctx.channel().writeAndFlush(Unpooled.copiedBuffer(personXml.getBytes("UTF-8")));
								personTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("人员数据发送成功，人员工号：【" + person.getWorkNum() + "】" + "downId:【"
													+ person.getDownLoadId() + "】");
											Snippet.addDownId(Snippet.PERSON, person.getDownLoadId(), equipSn);
										}
									}
								});

								return;
							}

							// 同步车辆信息
							CarEntity car = carServiceImpl.queryOne(equipId);
							if (car != null) {
								String carXml = EncodeToXmlUtil.carMapToXml(car.toMap(), newAuthCode, seqNum);
								ChannelFuture personTure = ctx.writeAndFlush(Unpooled.copiedBuffer(carXml.getBytes("UTF-8")));
								personTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("车辆数据发送成功，车牌号：【" + car.getCarNum() + "】" + "downId:【"
													+ car.getDownLoadId() + "】");
											Snippet.addDownId(Snippet.CAR, car.getDownLoadId(), equipSn);
										}
									}
								});
								return;
							}

							//排班信息
							TaskClassEntity taskClass = taskClassServiceImpl.queryOne(equipId);
							if(taskClass!=null) {
								String taskClassXml = EncodeToXmlUtil.taskClassMapToXml(taskClass.toMap(), newAuthCode,
										seqNum);
								ChannelFuture personTure = ctx
										.writeAndFlush(Unpooled.copiedBuffer(taskClassXml.getBytes("UTF-8")));
								personTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("排班数据发送成功，排班编号：【" + taskClass.getTaskId() + "】" + "downId:【"
													+ taskClass.getDownLoadId() + "】");
											Snippet.addDownId(Snippet.TASK_CLASS, taskClass.getDownLoadId(), equipSn);
										}
									}
								});
								return;
							}
							// 款箱同步
							CashBoxEntity cashBox = cashBoxServiceImpl.queryOne(equipId);
							if (cashBox != null) {
								String cashBoxXml = EncodeToXmlUtil.cashBoxMapToXml(cashBox.toMap(), newAuthCode,
										seqNum);
								ChannelFuture personTure = ctx
										.writeAndFlush(Unpooled.copiedBuffer(cashBoxXml.getBytes("UTF-8")));
								personTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("款箱数据发送成功，款箱编号：【" + cashBox.getBoxCode() + "】" + "downId:【"
													+ cashBox.getDownLoadId() + "】");
											Snippet.addDownId(Snippet.CASH_BOX, cashBox.getDownLoadId(), equipSn);
										}
									}
								});
								return;
							}

							// 款箱调拨
							Map<String, Object> cashBoxAllot = cashBoxServiceImpl.queryCashBoxAllot(equipId);
							if (cashBoxAllot != null) {
								String cashBoxAllotXml = EncodeToXmlUtil.cashBoxAllotMapToXml(cashBoxAllot, newAuthCode,
										seqNum);
								ChannelFuture personTure = ctx
										.writeAndFlush(Unpooled.copiedBuffer(cashBoxAllotXml.getBytes("UTF-8")));
								personTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("款箱调拨数据发送成功，款箱编号：【" + cashBoxAllot.get("Cashbox").toString() + "】"
													+ "downId:【" + cashBoxAllot.get("downLoadIdList").toString() + "】");
											Snippet.addDownId(Snippet.CASH_BOX_ALLOT,
													(List<String>) cashBoxAllot.get("downLoadIdList"), equipSn);
										}
									}
								});
								return;
							}

							// 门禁参数
							DoorParamEntity doorParam = doorServiceImpl.queryDoorParam(equipId);
							if (doorParam != null) {
								String doorParamXml = EncodeToXmlUtil.EncodeAccessParametersPacket(doorParam.toMap(),
										newAuthCode, seqNum);
								log.info("门禁参数："+doorParamXml);
								ChannelFuture personTure = ctx
										.writeAndFlush(Unpooled.copiedBuffer(doorParamXml.getBytes()));
								personTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("门禁参数数据发送成功，设备号：【" + equipSn + "】门号：【" + doorParam.getDoorPos()
													+ "】" + "downId:【" + doorParam.getDownLoadId() + "】");
											Snippet.addDownId(Snippet.DOOR_PARAM,
													Integer.valueOf(doorParam.getDownLoadId()),
													equipSn);
										}
									}
								});
								return;
							}

							// 门禁时区
							Map<String, Object> doorTimezone = doorServiceImpl.queryDoorTimeZone(equipId);
							if (doorTimezone != null) {
								String doorTimeXml = EncodeToXmlUtil.EncodeDoorTimeZonePacket(doorTimezone, newAuthCode,
										seqNum);
								ChannelFuture personTure = ctx
										.writeAndFlush(Unpooled.copiedBuffer(doorTimeXml.getBytes()));
								personTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("门禁参数数据发送成功，设备号：【" + equipSn + "】门号：【"
													+ doorTimezone.get("nDoor_pos") + "】" + "downId:【"
													+ doorTimezone.get("DownloadID") + "】");
											Snippet.addDownId(Snippet.DOOR_TIME_ZONE,
													Integer.valueOf(String.valueOf(doorTimezone.get("DownloadID"))),
													equipSn);
										}
									}
								});
								return;
							}

							//信息发布同步
							InformationRelease informationRelease1 = informationReleaseServiceImpl.queryOne(equipId);
							if(informationRelease1!=null){
								String informationReleaseXml = EncodeToXmlUtil.informationReleaseMapToXml(informationRelease1.toMap(),newAuthCode,seqNum);
								ChannelFuture informationReleaseTure = ctx
										.writeAndFlush(Unpooled.copiedBuffer(informationReleaseXml.getBytes("UTF-8")));
								informationReleaseTure.addListener(new ChannelFutureListener() {
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										if (future.isSuccess()) {
											log.info("信息发布发送成功，设备号：【" + equipSn + "】downId:【"
													+ informationRelease1.getDownloadID() + "】");
											Snippet.addDownId(Snippet.INFORMATION_RELEASE,
													Integer.valueOf(String.valueOf(informationRelease1.getDownloadID())),
													equipSn);
										}
									}
								});
								return;
							}
							log.info("=======没有数据可同步================");
							ctx.writeAndFlush(Unpooled.copiedBuffer(EncodeToXmlUtil.returnResult(seqNum, 3, equipSn)));
							// 应答是否同步成功
						} else if (Constants.RESPONSE_TYPE.equals(requestType)) {
							log.info("=========设备号：【"+equipSn+"】==返回响应==============");
							String resultCode = String.valueOf(map.get("ResultCode"));
							String synType = Snippet.seqNumMap.get(equipSn);
							if (Constants.SYN_SUCCESS.equals(resultCode)) {
								if (Snippet.PERSON.equals(synType)) {
									// 修改人员同步成功
									int downId = (int) Snippet.getDownId(equipSn);
									personServiceImpl.updateSynStatus(downId);
									Snippet.removeDownId(equipSn);
									log.info("=====修改人员同步状态成功downId:【" + downId + "】");
									return;
								}

								if (Snippet.CAR.equals(synType)) {
									int downId = (int) Snippet.getDownId(equipSn);
									carServiceImpl.updateSynStatus(downId);
									Snippet.removeDownId(equipSn);
									log.info("=====修改车辆同步状态成功downId:【" + downId + "】");
									return;
								}

								if (Snippet.CASH_BOX.equals(synType)) {
									int downId = (int) Snippet.getDownId(equipSn);
									cashBoxServiceImpl.updateSynStatus(downId);
									Snippet.removeDownId(equipSn);
									log.info("=====修改款箱同步状态成功downId:【" + downId + "】");
									return;
								}

								if (Snippet.TASK_CLASS.equals(synType)) {
									int downId = (int) Snippet.getDownId(equipSn);
									taskClassServiceImpl.updateSynStatus(downId);
									Snippet.removeDownId(equipSn);
									log.info("=====修改排班信息同步状态成功downId:【" + downId + "】");
									return;
								}

								if (Snippet.CASH_BOX_ALLOT.equals(synType)) {
									List<String> downIds = (List<String>) Snippet.getDownId(equipSn);
									cashBoxServiceImpl.updateSynAllotStatus(downIds);
									Snippet.removeDownId(equipSn);
									log.info("=====修改款箱调拨同步状态成功downId:【" + downIds.toString() + "】");
									return;
								}

								if (Snippet.DOOR_PARAM.equals(synType)) {
									int downId = (int) Snippet.getDownId(equipSn);
									doorServiceImpl.updateParamSynsStatus(downId);
									Snippet.removeDownId(equipSn);
									log.info("=====修改门禁参数同步状态成功downId:【" + downId + "】");
									return;
								}

								if (Snippet.DOOR_TIME_ZONE.equals(synType)) {
									int downId = (int) Snippet.getDownId(equipSn);
									doorServiceImpl.updateTimeZoneSynsStatus(downId);
									Snippet.removeDownId(equipSn);
									log.info("=====修改门禁时区同步状态成功downId:【" + downId + "】");
									return;
								}

								if (Snippet.INFORMATION_RELEASE.equals(synType)) {
									int downId = (int) Snippet.getDownId(equipSn);
									informationReleaseServiceImpl.updateSynStatus(downId);
									Snippet.removeDownId(equipSn);
									log.info("=====修改信息发布同步状态成功downId:【" + downId + "】");
									return;
								}
							} else {
								log.error("====响应错误信息：" + ResultEnum.getMsgByCode(Integer.valueOf(resultCode)));
							}
						} else if (Constants.BIG_DATA_TYPE.equals(requestType)) {
							String infoType = String.valueOf(map.get("InfoType"));
							String id = String.valueOf(map.get("Id"));
							int fingerId = Integer.valueOf(String.valueOf(map.get("FingerID")));
							map.put("OptionType", 1);
							// 人员图片下载
							if (Constants.INFOTYPE_DOWN_PERSON_PHOTO.equals(infoType)) {
								log.info("=========设备号：【"+equipSn+"】==请求下载人员图片数据==============");
								// 默认改为1

								PersonEntity person = personServiceImpl.queryPersonPhoto(id,Constants.STAFF_IMAGE_TYPE_PERSON,null);
								byte[] photoBytes = person.getPhoto();
								String encodePhoto = Encodes.encodeBase64(photoBytes);
								int times = getPhotoNumber(encodePhoto);
								if(times==0) {
									log.info("===========此用户无人员数据信息================");
									map.put("data", "");
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
											1);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
									return;
								}
								for (int i = 0; i < times; i++) {
									String pho;
									if (i == times - 1) {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												encodePhoto.length());
									} else {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												Constants.MAX_IMAGE_SEND_SIZE * (i + 1));
									}
									map.put("data", pho);
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, times,
											i + 1);
									log.info("===人员照片发送 总包数：" + times + "，当前包数：" + (i + 1) + ",数据：" + phtoParam);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
								}
								// 人员证件照片下载
							}else if(Constants.INFOTYPE_UPLOAD_STAFF_ONE_CARD_PHOTO.equals(infoType)){
								log.info("=========设备号：【"+equipSn+"】==请求下载人员证件照片图片one数据==============");

								PersonEntity person = personServiceImpl.queryPersonPhoto(id,Constants.STAFF_IMAGE_TYPE_CARD,null);
								byte[] photoBytes = person.getPhoto();
								String encodePhoto = Encodes.encodeBase64(photoBytes);
								int times = getPhotoNumber(encodePhoto);
								if(times==0) {
									log.info("===========此用户无人员证件照片one数据信息================");
									map.put("data", "");
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
											1);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
									return;
								}
								for (int i = 0; i < times; i++) {
									String pho;
									if (i == times - 1) {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												encodePhoto.length());
									} else {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												Constants.MAX_IMAGE_SEND_SIZE * (i + 1));
									}
									map.put("data", pho);
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, times,
											i + 1);
									log.info("===人员证件照片one发送 总包数：" + times + "，当前包数：" + (i + 1) + ",数据：" + phtoParam);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
								}
								// 人员证件照片下载
							}else if(Constants.INFOTYPE_UPLOAD_STAFF_TWO_CARD_PHOTO.equals(infoType)){
								log.info("=========设备号：【"+equipSn+"】==请求下载人员证件照片图片two数据==============");

								PersonEntity person = personServiceImpl.queryPersonPhoto(id,Constants.STAFF_IMAGE_TYPE_CARD,"orderBy");
								byte[] photoBytes = person.getPhoto();
								String encodePhoto = Encodes.encodeBase64(photoBytes);
								int times = getPhotoNumber(encodePhoto);
								if(times==0) {
									log.info("===========此用户无人员证件照片two数据信息================");
									map.put("data", "");
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
											1);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
									return;
								}
								for (int i = 0; i < times; i++) {
									String pho;
									if (i == times - 1) {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												encodePhoto.length());
									} else {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												Constants.MAX_IMAGE_SEND_SIZE * (i + 1));
									}
									map.put("data", pho);
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, times,
											i + 1);
									log.info("===人员证件照片two发送 总包数：" + times + "，当前包数：" + (i + 1) + ",数据：" + phtoParam);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
								}
								// 人员指纹模板下载
							} else if (Constants.INFOTYPE_DOWN_FINGER_MODE.equals(infoType)) {
								log.info("=========设备号：【"+equipSn+"】==请求下载人员【"+id+"】指纹【"+fingerId+"】图片数据==============");
								PersonEntity person = personServiceImpl.queryFingerMode(id);
								if(person==null) {
									log.info("===========此用户无指纹膜版数据信息================");
									map.put("data", "");
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
											1);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
									return;
								}
								byte[] photoBytes;
								//胁迫指纹
								if(fingerId % 100000000 > 30000000){
									photoBytes = person.getCoerceTemplate();
								}
								// 备份指纹	210010001
								else if (fingerId % 100000000 > 20000000) {
									photoBytes = person.getBackUpf();
								} else {
									photoBytes = person.getFingerTemplate();
								}
								if(photoBytes==null) {
									log.info("===========此用户无指纹膜版图片同步================");
									map.put("data", "");
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
											1);
									log.info("人员指纹图片数据"+phtoParam);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
								}else {
									String encodePhoto = Encodes.encodeBase64(photoBytes);
									int times = getPhotoNumber(encodePhoto);
									if(times==0) {
										log.info("===========此用户无指纹膜版数据信息times=0================");
										map.put("data", "");
										String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
												1);
										ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
										return;
									}
									for (int i = 0; i < times; i++) {
										String pho;
										if (i == times - 1) {
											pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
													encodePhoto.length());
										} else {
											pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
													Constants.MAX_IMAGE_SEND_SIZE * (i + 1));
										}
										map.put("data", pho);
										String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, times,
												i + 1);
										log.info("===指纹模板照片发送 总包数：" + times + "，当前包数：" + (i + 1) + ",数据：" + phtoParam);
										ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
									}
								}
								// 下载车辆照片
							} else if (Constants.INFOTYPE_DOWN_CAR_PHOTO.equals(infoType)) {
								log.info("=========设备号：【"+equipSn+"】==请求下载车辆图片数据==============");
								CarEntity car = carServiceImpl.queryCarPhoto(id);
								byte[] photoBytes = car.getPhoto();
								String encodePhoto = Encodes.encodeBase64(photoBytes);
								int times = getPhotoNumber(encodePhoto);
								if(times==0) {
									log.info("===========此车辆无图片数据信息================");
									map.put("data", "");
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
											1);
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
									return;
								}
								for (int i = 0; i < times; i++) {
									String pho;
									if (i == times - 1) {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												encodePhoto.length());
									} else {
										pho = encodePhoto.substring(Constants.MAX_IMAGE_SEND_SIZE * i,
												Constants.MAX_IMAGE_SEND_SIZE * (i + 1));
									}
									map.put("data", pho);
									String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, times,
											i + 1);
									log.info("===车辆照片发送 总包数：" + times + "，当前包数：" + (i + 1) );
									ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
								}
								// 上传交接人员抓拍照片
								//不知道的类型 直接返回空数据 add 2019-10-19
							}else{
								map.put("data", "");
								String phtoParam = EncodeToXmlUtil.EncodeBinaryPacket(map, equipSn, seqNum, 1,
										1);
								ctx.writeAndFlush(Unpooled.copiedBuffer(phtoParam.getBytes()));
								return;
							}
						} else if (Constants.CLOCK_CHECK_TYPE.equals(requestType)) {
							log.info("=========设备号：【"+equipSn+"】==请求时钟校对==============");
							String responseXml = EncodeToXmlUtil.EncodeTimePacket(equipSn, seqNum);
							ctx.channel().writeAndFlush(Unpooled.copiedBuffer(responseXml.getBytes()));
						}
					}
				}
				ctx.flush();
			}else {
				log.error("==========xml decode结果类型不符合，跳过======"+JSON.toJSON(msg));
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
		log.error("=================异常断开连接========================");
		cause.printStackTrace();
		log.error(cause.getMessage(),cause);
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.error("=================断开连接========================");
		//认证多次  删除前者  所以设备依然是链接状态  无需更新状态
		Channel nChannel = LongLinkChannelMap.getChannel(LongLinkChannelMap.getEquipSn(ctx.channel()));
		if (nChannel==null) {
			uploadInfoServiceImpl.updateEquipStatusBySn(LongLinkChannelMap.getEquipSn(ctx.channel()), 0);
			LongLinkChannelMap.removeEquipSn(ctx.channel());
		}
		//未注册列表
		LongLinkChannelNoDataMap.removeEquipSn(ctx.channel());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;

			String eventType = null;
			switch (event.state()) {
				case READER_IDLE: //客户端在一定时间内未向服务端发送任何的数据
					eventType = "10秒内为收到客户端任何数据，可能网络异常，主动断开";
					break;
				case WRITER_IDLE: //接收到数据，但在一定时间内未写入数据
					eventType = "写空闲";
					break;
				case ALL_IDLE: //未发送数据，也未写入数据
					eventType = "10秒内,读写空闲";
					break;
			}

			log.error(ctx.channel().remoteAddress() + "超时事件：" + eventType);
			ctx.channel().close();
		}
	}
	/**
	 * 照片分包次数
	 *
	 * @param photo
	 * @return
	 */
	private int getPhotoNumber(String photo) {
		return photo.length() / Constants.MAX_IMAGE_SEND_SIZE
				+ (photo.length() % Constants.MAX_IMAGE_SEND_SIZE == 0 ? 0 : 1);
	}
}
