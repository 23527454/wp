package com.fiacs.common.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SocketUtil {
	
	
	public static byte[] sendMsg(String ip,int port,byte[] msg) throws Exception {
		Socket socket = new Socket(ip,port);
		socket.setSoTimeout(15000);
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
