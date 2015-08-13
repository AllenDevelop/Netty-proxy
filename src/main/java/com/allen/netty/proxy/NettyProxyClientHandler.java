package com.allen.netty.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 代理 Client channel 处理函数
 * @author sky_han
 *
 */
public class NettyProxyClientHandler extends ChannelInboundHandlerAdapter {
	private Channel inBoundChannel;
	
	public NettyProxyClientHandler(Channel outBoundChannel) {
		this.inBoundChannel = outBoundChannel;
	}
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	if ( inBoundChannel.isActive() ) {
    		inBoundChannel.writeAndFlush(msg);
    	} else {
    		ctx.close();
    	}
    	
    }
	
}
