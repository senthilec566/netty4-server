package com.netty.httpserver;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Values;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

import io.netty.buffer.Unpooled;

/**
 * Simple Http HelloWorld Server Handler - Handle all Requests 
 * @author senthilec566
 */
public class HttpHelloWorldServerHandler extends ChannelInboundHandlerAdapter {
	 
	private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
	 
	 @Override
	 public void channelReadComplete(ChannelHandlerContext ctx) {
		 ctx.flush();
	 }

	 	@Override
	 	public void channelRead(ChannelHandlerContext ctx, Object msg) {
	 		if (msg instanceof HttpRequest) {
	 			HttpRequest req = (HttpRequest) msg;
	 			if (HttpHeaders.is100ContinueExpected(req)) {
	 				ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
	 			}
	 			boolean keepAlive = HttpHeaders.isKeepAlive(req);
	 			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
	 			response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
	 			response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
	 			if (!keepAlive) {
	 				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	 			} else {
	 				response.headers().set(CONNECTION, Values.KEEP_ALIVE);
	 				ctx.writeAndFlush(response);
	 			}
	 		}
	 	}
	 	
	 	@Override
	 	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	 		cause.printStackTrace();
	 		ctx.close();
	 	}
}
