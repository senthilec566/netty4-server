package com.netty.httpserver;


import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;


/**
 * Channel Input Handler
 * @author senthilec566
 *
 */
public class HttpInputHandler extends ChannelInboundHandlerAdapter {
	   
		private final String RESP="SUCCESS";
	    
	    @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	    	ctx.flush();
	    }
	    
	    @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	        
	    	if (msg instanceof HttpRequest) 
	        {
	        	HttpRequest req = (HttpRequest) msg;
	        	FullHttpRequest fReq = (FullHttpRequest) req;
	        	Charset utf8 = CharsetUtil.UTF_8;
	        	ByteBuf buf = fReq.content();
                String in = buf.toString( utf8 );
                buf.clear();
                if( in == null || in.isEmpty() ) {
                	in = "{\"application_nm\":\"netty4server\",\"type\":\"http\"}";
                }
                System.out.println(" Request ==> "+in);
	            if (HttpHeaders.is100ContinueExpected(req)) {
	                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
	            }
	            boolean keepAlive = HttpHeaders.isKeepAlive(req);
	            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
	                                                                    Unpooled.wrappedBuffer(RESP.getBytes()));
	            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
	            response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
	            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
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
