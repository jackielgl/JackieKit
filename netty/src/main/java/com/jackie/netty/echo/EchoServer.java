package com.jackie.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }

        // 设置端口值
        int port = Integer.parseInt(args[0]);
        // 呼叫服务器的start方法
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        // 创建EvenLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class) // 指定使用NIO的传输Channel
                    .localAddress(new InetSocketAddress(port)) // 设置socket地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 添加EchoServerHandler到Channel的ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync(); // 绑定的服务器sysnc等待服务关闭
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();// 关闭channel和块直到它被关闭
        } finally {
            group.shutdownGracefully().sync(); // 关闭EventLoopGroup，释放所有资源
        }
    }
}
