package com.xk.netty.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiacs.common.util.Constants;
import com.fiacs.common.util.DecodeToMapUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class LonglinkXmlDecodeHandler extends ByteToMessageDecoder{
	
	private static Logger log = LoggerFactory.getLogger(LonglinkXmlDecodeHandler.class);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int readableLength = in.readableBytes();//计算可读长度
		//如果小于报文最小长度，则放弃*********
		if(readableLength<Constants.MIN_LENGTH) {
			return;
		}
		
		int readIndex = in.readerIndex();
		byte[] bytes = new byte[readableLength];
        in.getBytes(readIndex, bytes);
        
        String temp = new String(bytes,"UTF-8");
        int index = temp.indexOf("</ProtocolRoot>");
        
        if(index==-1) {
        	return;
        }
        in.skipBytes(temp.substring(0, index+15).getBytes("UTF-8").length);
        
        //防止中间穿插了脏数据
        int startIndex = temp.indexOf("<ProtocolRoot");
        log.info("===接受到的数据："+temp.substring(startIndex, index+15));
        out.add(DecodeToMapUtil.xmlToMap(temp.substring(startIndex, index+15)));
	}
}
