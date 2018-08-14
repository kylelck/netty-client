package com.lick.nettyclient;

import io.netty.buffer.Unpooled;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

@SpringBootApplication
public class NettyClientApplication {

	public static void main(String[] args) throws Exception{
//		SpringApplication.run(NettyClientApplication.class, args);
//        niowithNetty();
    }
    private static void nettyConnect() {
//        NioEventLoopGroup boss = new NioEventLoopGroup(1);
//        NioEventLoopGroup worker = new NioEventLoopGroup();
//
//        try {
//            //定义netty  server
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            InetSocketAddress address = new InetSocketAddress(8090);
//            bootstrap.group(boss,worker)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(new EchoChlientHandler());
//                        }
//                    });
//            ChannelFuture f = bootstrap.bind(address).sync();
//            f.channel().closeFuture().sync();
//        }finally {
//            boss.shutdownGracefully();
//            worker.shutdownGracefully();
//        }
    }
    private static void niowithNetty() {
        /**
         * 不使用netty实现nio
         */
        try {
        //获取socket channel
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        ServerSocket socket = socketChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8090);
        //scoket 绑定接口
        socket.bind(inetSocketAddress);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        for(;;) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            final ByteBuffer msg = ByteBuffer.wrap("hello world".getBytes());
            while (iterator.hasNext()) {
                SelectionKey selectionKey =  iterator.next();
                iterator.remove();
                //新创建连接
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel client = channel.accept();
                    client.configureBlocking(false);
                    client.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE,msg.duplicate());
                    System.out.println("Accepted connection from "+client);
                }
                //可读
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(6 * 1024);
                    StringBuilder content = new StringBuilder();
                    while (channel.read(buffer) > 0) {
                        buffer.flip();
                        Charset charset = Charset.forName("UTF-8");
                        CharsetDecoder decoder = charset.newDecoder();
                        CharBuffer charBuffer =  decoder.decode(buffer.asReadOnlyBuffer());
                        content.append(charBuffer.toString());
                        ByteBuffer en = ByteBuffer.wrap("received".getBytes());
                        channel.write(en);
                        System.out.println("read info:"+content.toString());
                    }
                }
            }
            selectionKeys.clear();
        }
        }catch (IOException e) {

        }
    }
}
