package cn.jeefast.modules.fiacs.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cn.jeefast.modules.fiacs.entity.EquipEntity;

/**
 * @author ceshi
 * @Title: XStreamUtils
 * @ProjectName ceshi
 * @Description: TODO
 * @date 2018/7/1122:10
 */
public class XStreamUtils{
    /**
     * 将Object转换为xml
     * @param obj 转换的bean
     * @return bean转换为xml
     */
    public static String objectToXml(Object obj) {
        XStream xStream = new XStream();
        //xstream使用注解转换
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);
    }

    /**
     * 将xml转换为T
     * @param <T> 泛型
     * @param xml 要转换为T的xml
     * @param cls T对应的Class
     * @return xml转换为T
     */
    public static <T> T xmlToObject(String xml, Class<T> cls){
        XStream xstream = new XStream(new DomDriver());
        //xstream使用注解转换
        xstream.processAnnotations(cls);
        return (T) xstream.fromXML(xml);
    }
    
    public static void main(String[] args) {
		String s = "<ProtocolRoot><EquipIP>123213</EquipIP></ProtocolRoot>";
		EquipEntity e = xmlToObject(s, EquipEntity.class);
		System.out.println(e.getEquipIP());
	}
}
