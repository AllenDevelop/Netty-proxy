package com.allen.netty.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

/**
 * Netty Server
 * @author Allen
 */
public class NettyProxyServer {
	/* Default port : 80 */
	private int port = 80;
	
	public NettyProxyServer(){}
	
	public NettyProxyServer(int port){
		this.port = port;
	}
	public void start () throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
			         .channel(NioServerSocketChannel.class)
			         .localAddress(new InetSocketAddress(port))
			         .childHandler(new ChannelInitializer<SocketChannel>(){
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("codec", new HttpServerCodec());
							pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
							pipeline.addLast("handler", new NettyProxyServerHandler());
						}});
			ChannelFuture future = bootstrap.bind().sync(); 
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully().sync();
			workerGroup.shutdownGracefully().sync();
		}
	}
}
