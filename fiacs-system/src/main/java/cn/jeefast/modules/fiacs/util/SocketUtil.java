package cn.jeefast.modules.fiacs.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SocketUtil {
	
	private static String ip;

	@Value("${netty.ip}")
	public void setPort(String nettyIp) {
		ip = nettyIp;
	}
	
	private static Integer port;

	@Value("${netty.port}")
	public void setPort(Integer nettyPort) {
		port = nettyPort;
	}
	
	public static byte[] sendMsg(byte[] msg) throws Exception {
		Socket socket = new Socket(ip,port);
		OutputStream socketWriter=null;
		InputStream socketReader=null ;
		try {
			socketWriter = socket.getOutputStream();

			socketWriter.write(msg);

			socketWriter.flush();

			//socket.shutdownOutput();

			socketReader = socket.getInputStream();

			byte[] temp = new byte[288];

			while(socketReader.read(temp)!=-1) {
				return temp;
			}
			return null;
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
