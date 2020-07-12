package cn.jeefast.modules.fiacs.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fiacs.common.util.EncodeToXmlUtil;

import cn.jeefast.modules.fiacs.entity.EquipEntity;

@Component
public class UDPSend {

	private static Integer timeout;

	private static String netCard;

	@Value("${search.netCard}")
	public void setNetCard(String gateway) {
		netCard = gateway;
	}

	@Value("${search.timeOut}") 
	public void setTimeout(Integer timeOut) {
		timeout = timeOut;
	}
	
	public static Integer port; 

	@Value("${server.port}")
	public void setPort(Integer port) {
		UDPSend.port = port;
	}
	
	public static List<EquipEntity> searchEquip() throws Exception {
		DatagramSocket socket = null;
		if(StringUtils.isEmpty(netCard)) {
			socket = new DatagramSocket(0);
		}else {
			InetAddress host1 = InetAddress.getByName(netCard);
			socket = new DatagramSocket(0,host1);
		}
		try {
			UDPRecive client = new UDPRecive(socket);
			client.start();
			socket.setSoTimeout(timeout);
			InetAddress host = InetAddress.getByName("255.255.255.255");
			// 指定包要发送的目的地
			byte[] msg = EncodeToXmlUtil.searchEquip().getBytes();
			DatagramPacket request = new DatagramPacket(msg, msg.length, host, 8001);
			// 为接受的数据包创建空间
			socket.send(request);
			 Thread.sleep(timeout-100);
			 client.stop();
			return client.get();
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
}
