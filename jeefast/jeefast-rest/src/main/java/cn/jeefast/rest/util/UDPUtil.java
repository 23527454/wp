package cn.jeefast.rest.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UDPUtil {
	
	public static Integer port;

	@Value("${server.port}")
	public void setPort(Integer port) {
		UDPUtil.port = port;
	}
	/**
	 * 设备过多 消息过长
	 * @param hostName
	 * @param port
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static byte[] sendMsgForSearchEquipment(String hostName,int port,byte[] msg) throws Exception {
		DatagramSocket socket = new DatagramSocket(0);
        socket.setSoTimeout(3000);
        InetAddress host = InetAddress.getByName(hostName);
        //指定包要发送的目的地
        DatagramPacket request = new DatagramPacket(msg, msg.length, host, port);
        //为接受的数据包创建空间
        socket.send(request);
        
        Thread.sleep(50);
        
        DatagramPacket response = new DatagramPacket(new byte[40960], 40960);
        socket.receive(response);
        return response.getData();
	}
	
	/**
	 * 单个设备信息查询  信息短
	 * @param hostName
	 * @param port
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static byte[] sendMsgForEquipment(String hostName,int port,byte[] msg) throws Exception {
		DatagramSocket socket = new DatagramSocket(0);
        socket.setSoTimeout(10000);
        InetAddress host = InetAddress.getByName(hostName);
        //指定包要发送的目的地
        DatagramPacket request = new DatagramPacket(msg, msg.length, host, port);
        //为接受的数据包创建空间
        socket.send(request);
        
        Thread.sleep(5);
        
        DatagramPacket response = new DatagramPacket(new byte[2048], 2048);
        socket.receive(response);
        return response.getData();
	}
}
