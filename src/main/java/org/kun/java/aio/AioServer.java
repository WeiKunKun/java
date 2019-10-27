package org.kun.java.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kun
 * @date 2019/10/06
 */
public class AioServer {

    private static final int PORT = 30002;
    static final String UTF_8 = "utf-8";
    static List<AsynchronousSocketChannel> channelList = new ArrayList<>();

    public void startListen() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        AsynchronousServerSocketChannel serverChannel =
            AsynchronousServerSocketChannel.open(channelGroup).bind(new InetSocketAddress(PORT));
        while (true) {
            serverChannel.accept(null, new AcceptHandler(serverChannel));
        }
    }

    public static void main(String[] args) throws IOException {
        AioServer server = new AioServer();
        server.startListen();
    }
}

class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

    private AsynchronousServerSocketChannel serverChannel;

    public AcceptHandler(AsynchronousServerSocketChannel serverChannel) {
        this.serverChannel = serverChannel;
    }

    @Override
    public void completed(final AsynchronousSocketChannel sc, Object attachment) {
        AioServer.channelList.add(sc);
        serverChannel.accept(null, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        sc.read(buffer, null, new CompletionHandler<Integer, Object>() {

            @Override
            public void completed(Integer result, Object attachment) {
                buffer.flip();
                String content = StandardCharsets.UTF_8.decode(buffer).toString();
                for (AsynchronousSocketChannel asc : AioServer.channelList) {
                    try {
                        asc.write(ByteBuffer.wrap(content.getBytes(AioServer.UTF_8)));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                buffer.clear();
                sc.read(buffer, null, this);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("读取数据失败：" + exc);
                AioServer.channelList.remove(sc);
            }
        });
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        System.out.println("连接失败：" + exc);
        exc.printStackTrace();
    }
}
