package com.jackie.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 *
 * channelRead() - 每个信息入站都会调用
 * channelReadComplete() - 通知处理器最后的 channelread() 是当前批处理中的最后一条消息时调用
 * exceptionCaught()- 读操作时捕获到异常时调用
 *
 */
//Sharable 标志这类的实力之间可以在channel里面共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        // 消息打印
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        // 将苏接收的消息返回给发送者。注意：这还没有冲刷数据
        ctx.write(in);
        //super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) // 冲刷所有待审消息到远程节点。关闭通道后，操作完成
                .addListener(ChannelFutureListener.CLOSE);
        //super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //关闭通道
        ctx.close();
        //super.exceptionCaught(ctx, cause);
    }
}
