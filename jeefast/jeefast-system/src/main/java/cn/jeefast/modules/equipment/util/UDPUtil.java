package cn.jeefast.modules.equipment.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.jeefast.modules.equipment.entity.Equipment;

@Component
public class UDPUtil {

	private static Integer timeout;

	@Value("${search.timeOut}")
	public void setTimeout(Integer timeOut) {
		timeout = timeOut;
	}
	
	private static String netCard;

	@Value("${search.netCard}")
	public void setNetCard(String netcard) {
		netCard = netcard;
	}

	public static Integer port;

	@Value("${server.port}")
	public void setPort(Integer port) {
		UDPUtil.port = port;
	}

	/**
	 * 设备过多 等待时间需要设置为30秒
	 * 
	 * @param hostName
	 * @param port
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static List<Equipment> sendMsgForSearchEquipment(String hostName, int port, byte[] msg) throws Exception {
		DatagramSocket socket=null;
		if(StringUtils.isEmpty(netCard)) {
			 socket = new DatagramSocket(0);

		}else {
			InetAddress host1 = InetAddress.getByName(netCard);
			 socket = new DatagramSocket(0, host1);
			
		}
		try {
			UDPReceiveClient client = new UDPReceiveClient(socket);
			client.start();
			socket.setSoTimeout(timeout);
			InetAddress host = InetAddress.getByName(hostName);
			// 指定包要发送的目的地
			DatagramPacket request = new DatagramPacket(msg, msg.length, host, port);
			// 为接受的数据包创建空间
			socket.send(request);
			Thread.sleep(timeout - 100);
			client.stop();
			return client.get();
		} finally {
			if(socket!=null) {
				socket.close();
			}
		}
	}
}