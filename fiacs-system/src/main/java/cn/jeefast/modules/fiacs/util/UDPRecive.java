package cn.jeefast.modules.fiacs.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fiacs.common.util.DecodeToMapUtil;

import cn.jeefast.modules.fiacs.entity.EquipEntity;

public class UDPRecive extends Thread {

	private List<EquipEntity> list = new ArrayList<EquipEntity>();
	
	private EquipEntity map=null;
	
	private DatagramSocket socket;
	
	public UDPRecive(DatagramSocket socket) {
		this.socket=socket;
	}
	
	public List<EquipEntity> get(){
		return this.list;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				byte[] buf = new byte[2048];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				System.out.println(packet.getData());
				System.out.println(new String(packet.getData(),"UTF-8").trim());
				 map = XStreamUtils.xmlToObject(new String(packet.getData(),"UTF-8").trim(), EquipEntity.class);
				 System.out.println(map.toString());
				 list.add(map);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}

}

