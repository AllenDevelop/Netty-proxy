package com.allen.netty.proxy;

import org.junit.Test;

public class NettyProxyServerTest {
	@Test
	public void start() throws Exception {
		NettyProxyServer server = new NettyProxyServer(8080);
		server.start();
	}
}
