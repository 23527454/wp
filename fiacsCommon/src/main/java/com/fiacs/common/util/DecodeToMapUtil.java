package com.fiacs.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;


public class DecodeToMapUtil {
	
	/**
	 * xml转map
	 * @param xmlString
	 * @return
	 */
	public static void main(String[] a ){
		Map<String,Object> returnMap = xmlToMap("<ProtocolRoot type=\"21\" TotalCount=\"1\" CurrIndex=\"1\" SeqNum=\"241859639\" ><AuthorCode>7B95CA32CB6E640E</AuthorCode><CarEventTable><CarEventInfo><CarID>2</CarID><CardNum>16777525</CardNum><EquipSN>1001200102100001</EquipSN><EventDate>2019-12-09 15:58:47</EventDate><EventID>106</EventID><GroupID>0</GroupID><TaskID>0</TaskID></CarEventInfo></CarEventTable></ProtocolRoot>");

		System.out.println(returnMap);
	}
	public static Map<String,Object> xmlToMap(String xmlString){
		Document doc = null;
		Map<String,Object> returnMap= new HashMap<>();
		try {
			doc = DocumentHelper.parseText(xmlString);
			 // 获取根节点
            Element rootElt = doc.getRootElement();
			String type = rootElt.attributeValue("type");
			returnMap.put("type", type);
			returnMap.put("SeqNum",  rootElt.attributeValue("SeqNum"));
			returnMap.put("TotalCount",  rootElt.attributeValue("TotalCount"));
			returnMap.put("CurrIndex",  rootElt.attributeValue("CurrIndex"));

			Element authorCode = rootElt.element("AuthorCode");
			returnMap.put("AuthorCode", authorCode.getText());
			if(type.equals(Constants.CONNECT_EVENT_TYPE)) {
				connectEvent(rootElt,returnMap);
			}else if(type.equals(Constants.ALARM_EVENT_TYPE)) {
				alarmInfo(rootElt, returnMap);
			}else if(type.equals(Constants.CASHBOX_EVENT_TYPE)) {
				cashBoxEvent(rootElt, returnMap);
			}else if(type.equals(Constants.WORKPERSON_EVENT_TYPE)) {
				workPersonEvent(rootElt, returnMap);
			}else if(type.equals(Constants.SUPERGO_EVENT_TYPE)) {
				superCarGoEvent(rootElt, returnMap);
			}else if(type.equals(Constants.CAR_ARRIVE_EVENT_TYPE)) {
				carEvent(rootElt, returnMap);
			}else if(type.equals(Constants.CASHBOX_ORDER_TYPE)) {
				cashBoxOrderEvent(rootElt,returnMap);
			}else if(type.equals(Constants.CASHBOX_RETURN_TYPE)) {
				cashBoxReturnEvent(rootElt,returnMap);
			}else if(type.equals(Constants.SAFEGUARD_EVENT_TYPE)) {
				safeGuardEvent(rootElt,returnMap);
			}else if(type.equals(Constants.ACCESS_EVENT_TYPE)) {
				accessEvent(rootElt,returnMap);
			}else if(type.equals(Constants.ACCESS_ALARM_TYPE)) {
				accessAlarmEvent(rootElt,returnMap);
			}else {
				commonTran(rootElt,returnMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * 通用转换
	 * @param rootElt
	 * @param returnMap
	 */
	private static void commonTran(Element rootElt,Map<String,Object> returnMap) {
		Iterator intor = rootElt.elementIterator();
		while(intor.hasNext()) {
			Element ele = (Element)intor.next();
			if(!ele.getName().equals("AuthorCode")) {
				returnMap.put(ele.getName(), ele.getText());
			}
		}
	}
	
	/**
	 * 排班信息
	 * @param rootElt
	 * @param returnMap
	 *//*
	private static void taskClass(Element rootElt,Map<String,Object> returnMap) {
		Element taskTable = rootElt.element("TaskTable");
		Iterator tableIntor = taskTable.elementIterator();
		List<Map<String,Object>> taskClassInfoList = new ArrayList<>();
		while(tableIntor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element connectEvent = (Element)tableIntor.next();
			if(connectEvent.getName().equals("TaskInfo")) {
				
			}
		}
	}*/
	
	/**
	 * 交接事件上传转换
	 * @param rootElt
	 * @param returnMap
	 */
	private static void connectEvent(Element rootElt,Map<String,Object> returnMap) {
		Element connectEventTable = rootElt.element("ConnectEventTable");
		Iterator tableIntor = connectEventTable.elementIterator();
		List<Map<String,Object>> connectEventInfoList = new ArrayList<>();
		while(tableIntor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element connectEvent = (Element)tableIntor.next();
			if(connectEvent.getName().equals("ConnectEventInfo")) {
				Iterator intor = connectEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element)intor.next();
					if(ele.getName().equals("SuperCarGo")) {
						Iterator superCarInterator = ele.elementIterator();
						List<Map<String,String>> superCarList = new ArrayList<Map<String,String>>();
						while(superCarInterator.hasNext()) {
							Element superCarGoID = (Element) superCarInterator.next();
							Map<String,String> superMap = new HashMap<>();
							superMap.put("FingerID", superCarGoID.attributeValue("FingerID"));
							superMap.put("PersonID", superCarGoID.attributeValue("PersonID"));
							superMap.put("PhotoSize", superCarGoID.attributeValue("PhotoSize"));
							superMap.put("AuthorType", superCarGoID.attributeValue("AuthorType"));
							//superCarGoID.
							superCarList.add(superMap);
						}
						mmap.put("superCarGoList", superCarList);
					}else if(ele.getName().equals("WorkPerson")) {
						Iterator workPersonInterator = ele.elementIterator();
						List<Map<String,String>> workPersonList = new ArrayList<Map<String,String>>();
						while(workPersonInterator.hasNext()) {
							Element workPersonID = (Element) workPersonInterator.next();
							Map<String,String> superMap = new HashMap<>();
							superMap.put("FingerID", workPersonID.attributeValue("FingerID"));
							superMap.put("PersonID", workPersonID.attributeValue("PersonID"));
							superMap.put("PhotoSize", workPersonID.attributeValue("PhotoSize"));
							superMap.put("AuthorType", workPersonID.attributeValue("AuthorType"));
							workPersonList.add(superMap);
						}
						mmap.put("workPersonList", workPersonList);
					}else {
						if(ele.getName().equals("EquipSN")) {
							returnMap.put("EquipSN", ele.getText());
						}
						mmap.put(ele.getName()	, ele.getText());
					}
				}
			}
			connectEventInfoList.add(mmap);
		}
		returnMap.put("ConnectEventTable", connectEventInfoList);
	}
	
	/**
	 * 异常报警数据转换
	 * @param rootElt
	 * @param returnMap
	 */
	private static void alarmInfo(Element rootElt,Map<String,Object> returnMap) {
		Element alarmEventTable = rootElt.element("AlarmTable");
		Iterator tableIntor = alarmEventTable.elementIterator();
		List<Map<String,Object>> alarmEventInfoList = new ArrayList<>();
		while(tableIntor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element alarmEvent = (Element)tableIntor.next();
			if(alarmEvent.getName().equals("AlarmInfo")) {
				Iterator intor = alarmEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("Cashbox")) {
						Iterator cashBoxInterator = ele.elementIterator();
						List<Map<String,String>> cashBoxList = new ArrayList<Map<String,String>>();
						while(cashBoxInterator.hasNext()) {
							Element cashBoxID = (Element) cashBoxInterator.next();
							Map<String,String> superMap = new HashMap<>();
							superMap.put("ID", cashBoxID.attributeValue("ID"));
							superMap.put("CardNum", cashBoxID.attributeValue("CardNum"));
							cashBoxList.add(superMap);
						}
						mmap.put("cashBoxList", cashBoxList);
					}else {
						if(ele.getName().equals("EquipSN")) {
							returnMap.put("EquipSN", ele.getText());
						}
						mmap.put(ele.getName(), ele.getText());
					}
				}
			}
			alarmEventInfoList.add(mmap);
		}
		returnMap.put("AlarmTable", alarmEventInfoList);
	}
	
	
	/**
	 * 车辆事件到达
	 * @param rootElt
	 * @param returnMap
	 */
	private static void carEvent(Element rootElt,Map<String,Object> returnMap) {
		Element carEventTable = rootElt.element("CarEventTable");
		Iterator tableItor = carEventTable.elementIterator();
		List<Map<String,String>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,String> mmap = new HashMap<>();
			Element carEvent = (Element)tableItor.next();
			if(carEvent.getName().equals("CarEventInfo")) {
				Iterator intor = carEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("EquipSN")) {
						returnMap.put("EquipSN", ele.getText());
					}
					mmap.put(ele.getName(), ele.getText());
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("CarEventTable", mapList);
	}
	

	/**
	 * 维保员事件
	 * @param rootElt
	 * @param returnMap
	 */
	private static void safeGuardEvent(Element rootElt,Map<String,Object> returnMap) {
		Element carEventTable = rootElt.element("SafeguardEventTable");
		Iterator tableItor = carEventTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element carEvent = (Element)tableItor.next();
			if(carEvent.getName().equals("SafeguardEventInfo")) {
				Iterator intor = carEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("EquipSN")) {
						returnMap.put("EquipSN", ele.getText());
					}
					mmap.put(ele.getName(), ele.getText());
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("SafeguardEventTable", mapList);
	}
	
	
	/**
	 * 门禁出入事件
	 * @param rootElt
	 * @param returnMap
	 */
	private static void accessEvent(Element rootElt,Map<String,Object> returnMap) {
		Element carEventTable = rootElt.element("AccessEventTable");
		Iterator tableItor = carEventTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element carEvent = (Element)tableItor.next();
			if(carEvent.getName().equals("AccessEventInfo")) {
				Iterator intor = carEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("EquipSN")) {
						returnMap.put("EquipSN", ele.getText());
					}
					mmap.put(ele.getName(), ele.getText());
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("AccessEventTable", mapList);
	}
	
	/**
	 * 门禁报警事件
	 * @param rootElt
	 * @param returnMap
	 */
	private static void accessAlarmEvent(Element rootElt,Map<String,Object> returnMap) {
		Element carEventTable = rootElt.element("AccessAlarmEventTable");
		Iterator tableItor = carEventTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element carEvent = (Element)tableItor.next();
			if(carEvent.getName().equals("AccessAlarmEventInfo")) {
				Iterator intor = carEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("EquipSN")) {
						returnMap.put("EquipSN", ele.getText());
					}
					mmap.put(ele.getName(), ele.getText());
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("AccessAlarmEventTable", mapList);
	}
	
	
	/**
	 * 押款员 事件信息（ 事件信息（ 事件信息（ 中心金库专用 中心金库专用 中心金库专用 ）
	 * @param rootElt
	 * @param returnMap
	 */
	private static void superCarGoEvent(Element rootElt,Map<String,Object> returnMap) {
		Element superGoTable = rootElt.element("SuperCarGoEventTable");
		Iterator tableItor = superGoTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element superGoEvent = (Element)tableItor.next();
			if(superGoEvent.getName().equals("SuperCarGoEventInfo")) {
				Iterator intor = superGoEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("EquipSN")) {
						returnMap.put("EquipSN", ele.getText());
					}
					mmap.put(ele.getName(), ele.getText());
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("SuperCarGoEventTable", mapList);
	}
	
	/**
	 * 专员 事件信息 
	 * @param rootElt
	 * @param returnMap
	 */
	private static void workPersonEvent(Element rootElt,Map<String,Object> returnMap) {
		Element superGoTable = rootElt.element("WorkPersonEventTable");
		Iterator tableItor = superGoTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element superGoEvent = (Element)tableItor.next();
			if(superGoEvent.getName().equals("WorkPersonEventInfo")) {
				Iterator intor = superGoEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("EquipSN")) {
						returnMap.put("EquipSN", ele.getText());
					}
					mmap.put(ele.getName(), ele.getText());
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("WorkPersonEventTable", mapList);
	}
	
	/**
	 * 款箱 事件信息 
	 * @param rootElt
	 * @param returnMap
	 */
	private static void cashBoxEvent(Element rootElt,Map<String,Object> returnMap) {
		Element cashBoxTable = rootElt.element("CashboxEventTable");
		Iterator tableItor = cashBoxTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element cashBoxEvent = (Element)tableItor.next();
			if(cashBoxEvent.getName().equals("CashboxEventInfo")) {
				Iterator intor = cashBoxEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("Cashbox")) {
						Iterator cashBoxInterator = ele.elementIterator();
						List<Map<String,String>> cashBoxList = new ArrayList<Map<String,String>>();
						while(cashBoxInterator.hasNext()) {
							Element cashBoxID = (Element) cashBoxInterator.next();
							Map<String,String> superMap = new HashMap<>();
							superMap.put("ID", cashBoxID.attributeValue("ID"));
							superMap.put("CardNum", cashBoxID.attributeValue("CardNum"));
							cashBoxList.add(superMap);
						}
						mmap.put("cashBoxList", cashBoxList);
					}else {
						if(ele.getName().equals("EquipSN")) {
							returnMap.put("EquipSN", ele.getText());
						}
						mmap.put(ele.getName(), ele.getText());
					}
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("CashboxEventTable", mapList);
	}
	
	
	/**
	 * 款箱 预约事件信息 
	 * @param rootElt
	 * @param returnMap
	 */
	private static void cashBoxOrderEvent(Element rootElt,Map<String,Object> returnMap) {
		Element cashBoxTable = rootElt.element("CashboxOrderTable");
		Iterator tableItor = cashBoxTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element cashBoxEvent = (Element)tableItor.next();
			if(cashBoxEvent.getName().equals("CashboxOrderInfo")) {
				Iterator intor = cashBoxEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("CashboxPacket")) {
						Iterator cashBoxInterator = ele.elementIterator();
						List<Map<String,String>> cashBoxList = new ArrayList<Map<String,String>>();
						while(cashBoxInterator.hasNext()) {
							Element cashBox = (Element) cashBoxInterator.next();
							if(cashBox.getName().equals("CashBox")) {
								Iterator cashItor = cashBox.elementIterator();
								Map<String,String> cboxMap = new HashMap<>();
								while(cashItor.hasNext()) {
									Element cbox = (Element)cashItor.next();
									cboxMap.put(cbox.getName(), cbox.getText());
								}
								cashBoxList.add(cboxMap);
							}
						}
						mmap.put("CashboxPacket", cashBoxList);
					}else {
						if(ele.getName().equals("EquipSN")) {
							returnMap.put("EquipSN", ele.getText());
						}
						mmap.put(ele.getName(), ele.getText());
					}
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("CashboxOrderTable", mapList);
	}
	
	
	/**
	 * 款箱 上缴事件信息 
	 * @param rootElt
	 * @param returnMap
	 */
	private static void cashBoxReturnEvent(Element rootElt,Map<String,Object> returnMap) {
		Element cashBoxTable = rootElt.element("CashboxReturnTable");
		Iterator tableItor = cashBoxTable.elementIterator();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while(tableItor.hasNext()) {
			Map<String,Object> mmap = new HashMap<>();
			Element cashBoxEvent = (Element)tableItor.next();
			if(cashBoxEvent.getName().equals("CashboxReturnInfo")) {
				Iterator intor = cashBoxEvent.elementIterator();
				while(intor.hasNext()) {
					Element ele = (Element) intor.next();
					if(ele.getName().equals("CashboxPacket")) {
						Iterator cashBoxInterator = ele.elementIterator();
						List<Map<String,String>> cashBoxList = new ArrayList<Map<String,String>>();
						while(cashBoxInterator.hasNext()) {
							Element cashBox = (Element) cashBoxInterator.next();
							if(cashBox.getName().equals("CashBox")) {
								Iterator cashItor = cashBox.elementIterator();
								Map<String,String> cboxMap = new HashMap<>();
								while(cashItor.hasNext()) {
									Element cbox = (Element)cashItor.next();
									cboxMap.put(cbox.getName(), cbox.getText());
								}
								cashBoxList.add(cboxMap);
							}
						}
						mmap.put("CashboxPacket", cashBoxList);
					}else {
						if(ele.getName().equals("EquipSN")) {
							returnMap.put("EquipSN", ele.getText());
						}
						mmap.put(ele.getName(), ele.getText());
					}
				}
			}
			mapList.add(mmap);
		}
		returnMap.put("CashboxReturnTable", mapList);
	}
	
	/**
	 * byte数组转map
	 * @param resultBytes
	 * @return
	 */
	public static Map<String,Object> bytesToMap(byte[] resultBytes){
		String result = new String(resultBytes);
		int indexBegin = result.indexOf("<ProtocolRoot");
		int end = result.indexOf("</ProtocolRoot>");
		if(end==-1) {
			return null;
		}
		return DecodeToMapUtil.xmlToMap(result.substring(indexBegin, end+15));
	}
}
