package cn.jeefast.modules.equipment.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jeefast.modules.equipment.entity.Equipment;

public class UDPReceiveClient extends Thread {

	private List<Equipment> list = new ArrayList<Equipment>();
	
	private Map<String,Object> map=null;
	
	private DatagramSocket socket;
	
	public UDPReceiveClient(DatagramSocket socket) {
		this.socket=socket;
	}
	
	public List<Equipment> get(){
		return this.list;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				 map = ReturnUtil.returnSearchList(packet.getData());
				 if(!"fail".equals(map.get("result"))) {
					 list.add((Equipment)map.get("data"));
				 }
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}

}
