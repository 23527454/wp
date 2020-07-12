package cn.jeefast.modules.equipment.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketUtil {
	
	public static byte[] sendMsgForEquipment(String ip,int port,byte[] msg) throws Exception {
		Socket socket = new Socket(ip,port);
		OutputStream socketWriter=null;
		InputStream socketReader=null ;
		try {
			socketWriter = socket.getOutputStream();

			socketWriter.write(msg);

			socketWriter.flush();

			socketReader = socket.getInputStream();

			byte[] temp = new byte[2048];

			socketReader.read(temp);
			
			return temp;
		}finally {
			if(socketReader!=null) {
				socketReader.close();
			}
			if(socketWriter!=null) {
				socketWriter.close();
			}
			if(socket!=null) {
				socket.close();
			}
		}
	}
}
