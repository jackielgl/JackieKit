package com.jackie.netty.client;


import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.bootstrap.Bootstrap;

public class NettyClient {

    static final String HOST = System.getProperty("host", "192.168.1.100");
    static final int PORT = Integer.parseInt(System.getProperty("port", "11230"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {
        String msgBody = "00000936<?xml version='1.0' encoding='GBK' ?><root><head><TranCode>40099</TranCode></head><body><yhth></yhth><gydm>40704</gydm><fqqd>5</fqqd><czgydm>999999</czgydm><czzd>00187dbab642</czzd><gyflmc></gyflmc><mac>00187dbab642</mac><answer1>1</answer1><date>2019-10-11 15:54:15</date><answer3>1</answer3><answer2>1</answer2><answer5>1</answer5><answer4>2</answer4><blms>3</blms><fqr>40704</fqr><user_token>000000000004EB9C0B285610EEC807854A0FBB6BC49EA794308B1B7B3DABA24311CA79B19D712B4E63F871DC5930693C2F445E22A7ACEAA1E1FBA4E5A0EE0D2D9B5C50C8B33210B1</user_token><gt_iwm_id>201910111529080000100003E140032246000000                                                     1001903        01679802</gt_iwm_id><custId>03000061006736</custId><yyb>8888</yyb><gt_usercode>1001903</gt_usercode><op_password></op_password><ip>10.176.65.222</ip><fsyyb>3115</fsyyb><source>V</source><czgy>40704</czgy><method>4</method><hardwareInfo></hardwareInfo></body></root>";

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new Client01Handler());
                            p.addLast(new Client02Handler());
                        }
                    });

            ChannelFuture future = b.connect(HOST, PORT).sync();
            future.channel().writeAndFlush(msgBody);
            future.channel().closeFuture().sync();
        } finally {
            group.spliterator();
        }
    }
}
