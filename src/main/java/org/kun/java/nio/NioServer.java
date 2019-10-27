package org.kun.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author kun
 * @date 2019/10/06
 */
public class NioServer {
    private Selector selector = null;
    private static final int PORT = 30000;
    private Charset charset = Charset.forName("UTF-8");

    public void init() {
        try {
            selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            InetSocketAddress isa = new InetSocketAddress("127.0.0.1", PORT);
            server.bind(isa);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (selector.select() > 0) {
                for (SelectionKey sk : selector.selectedKeys()) {
                    selector.selectedKeys().remove(sk);
                    if (sk.isAcceptable()) {
                        SocketChannel sc = server.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        sk.interestOps(SelectionKey.OP_ACCEPT);
                    }
                    if (sk.isReadable()) {
                        SocketChannel sc = (SocketChannel)sk.channel();
                        ByteBuffer buff = ByteBuffer.allocate(1024);
                        String content = "";
                        try {
                            while (sc.read(buff) > 0) {
                                buff.flip();
                                content += charset.decode(buff);
                            }
                            System.out.println("读取的数据：" + content);
                            sk.interestOps(SelectionKey.OP_READ);
                        } catch (IOException e) {
                            sk.cancel();
                            if (sk.channel() != null) {
                                sk.channel().close();
                            }
                        }
                        if (content.length() > 0) {
                            for (SelectionKey key : selector.keys()) {
                                Channel targetChannel = sk.channel();
                                if (targetChannel instanceof SocketChannel) {
                                    SocketChannel destChannel = (SocketChannel)targetChannel;
                                    destChannel.write(charset.encode(content));
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new NioServer().init();
    }
}
