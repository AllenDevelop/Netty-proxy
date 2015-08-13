package com.allen.netty.proxy;

public class Main {
	public static void main (String args[]) {
		/* Start your server */
		NettyProxyServer server  = new NettyProxyServer(8080);
		
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
