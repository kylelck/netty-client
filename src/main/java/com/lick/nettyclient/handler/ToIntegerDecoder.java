package com.lick.nettyclient.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**ReplayingDecoder 包含了对读取内容长度的判断
 * @author lichengkai
 * 2018-08-09: 下午7:49
 * Copyright(c) for dianwoda
 */
public class ToIntegerDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}
