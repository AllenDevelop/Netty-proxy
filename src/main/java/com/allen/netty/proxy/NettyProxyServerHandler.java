package com.allen.netty.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyProxyServerHandler extends ChannelInboundHandlerAdapter {
	private Channel outBoundChannel;
	
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
    	System.out.println("[server]------channelRead------");
    	if(outBoundChannel==null || !ctx.channel().isActive()) {
    		System.out.println("[create]------------");
        	Bootstrap bootstrap = new Bootstrap();
        	bootstrap.group(ctx.channel().eventLoop())
        	         .channel(ctx.channel().getClass())
        	         .handler(new ChannelInitializer<SocketChannel>(){
 						@Override
 						protected void initChannel(SocketChannel ch)
 								throws Exception {
 							ChannelPipeline pipeline = ch.pipeline();
 							pipeline.addLast("codec", new HttpClientCodec());
 							pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
 							pipeline.addLast(new NettyProxyClientHandler(ctx.channel(),msg));
 						}});
        	ChannelFuture future = bootstrap.connect("www.9ikaka.com",80);
        	outBoundChannel = future.channel();
        	future.addListener(new ChannelFutureListener(){
    			@Override
    			public void operationComplete(ChannelFuture future)
    					throws Exception {
    				if (future.isSuccess()) {
    					ctx.read();
		                future.channel().writeAndFlush(msg);
    				} else {
    					future.channel().close();
    				}
    			}
        		
        	});
    	} else {
    		System.out.println("[write]------------");
    		outBoundChannel.writeAndFlush(msg);
    	}
    }
}
