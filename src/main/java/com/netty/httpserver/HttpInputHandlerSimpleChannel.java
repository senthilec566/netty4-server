package com.netty.httpserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Simple Channel Handler API 
 * @author senthilec566
 */

public class HttpInputHandlerSimpleChannel extends SimpleChannelInboundHandler<FullHttpRequest> {
	    
		private final String RESP="SUCCESS";

	     @Override
	    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	    // TODO Auto-generated method stub
	    super.channelActive(ctx);
	    }
	     
	     @Override
	    public boolean acceptInboundMessage(Object msg) throws Exception {
	    // TODO Auto-generated method stub
	    return super.acceptInboundMessage(msg);
	    }
	     @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	    	 ctx.flush();
	    	 System.out.println("Read Complete");
	    }

	     
	    @Override
	    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
	    	
	    	
	        if (msg instanceof FullHttpRequest) {
	        	FullHttpRequest req = (FullHttpRequest) msg;
	        	
	        	if (req.getMethod() != HttpMethod.POST) {
	        		throw new IllegalArgumentException(HttpResponseStatus.METHOD_NOT_ALLOWED.reasonPhrase());
	            }
	        	
	            if (HttpHeaders.is100ContinueExpected(req)) {
	                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
	            }
	            
	            boolean keepAlive = HttpHeaders.isKeepAlive(req);
	            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
	                                                                    Unpooled.wrappedBuffer(RESP.getBytes()));
	            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
	            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
	            
	            
	            if (!keepAlive) {
	                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	                System.out.println("Flushed Response KeepAlive");
	            } else {
	                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
	                ctx.writeAndFlush(response);
	                System.out.println("Flushed Response");
	            }
	            System.out.println("done!");
	        }
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        cause.printStackTrace();
	        ctx.close();
	    }
}
