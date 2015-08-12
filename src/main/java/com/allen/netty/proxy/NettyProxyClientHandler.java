package com.allen.netty.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyProxyClientHandler extends ChannelInboundHandlerAdapter {
	private Object msg;
	private Channel inBoundChannel;
	public NettyProxyClientHandler(Channel outBoundChannel, Object msg) {
		this.msg = msg;
		this.inBoundChannel = outBoundChannel;
		
	}
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//    	System.out.println("[client]------channelActive------");
////        ctx.writeAndFlush(msg);
//    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	if ( inBoundChannel.isActive() ) {
    		System.out.println("[client]------channelRead---alive---");
    		inBoundChannel.writeAndFlush(msg);
    	} else {
    		System.out.println("[client]------channelRead---close---");
    		ctx.close();
    	}
    	
    }
	
}
