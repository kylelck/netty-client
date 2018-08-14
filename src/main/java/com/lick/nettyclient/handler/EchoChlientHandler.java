package com.lick.nettyclient.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @author lichengkai
 * 2018-08-07: 下午8:24
 * Copyright(c) for dianwoda
 */
public class EchoChlientHandler extends SimpleChannelInboundHandler<ByteBuf>{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("connect-------------："+ctx.name());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello world",Charset.defaultCharset()));
    }
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf s) throws Exception {
        System.out.printf("received message:"+s.toString(Charset.defaultCharset()));
    }
}
