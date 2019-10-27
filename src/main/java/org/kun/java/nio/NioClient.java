package org.kun.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author kun
 * @date 2019/10/06
 */
public class NioClient {
    private Selector selector = null;
    private static final int PORT = 30000;
    private Charset charset = Charset.forName("UTF-8");
    private SocketChannel sc = null;

    public void init() throws IOException {
        selector = Selector.open();
        InetSocketAddress isa = new InetSocketAddress("127.0.0.1", PORT);
        sc = SocketChannel.open(isa);
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        new ClientThread().start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            sc.write(charset.encode(line));
        }

    }

    private class ClientThread extends Thread {
        @Override
        public void run() {
            try {
                while (selector.select() > 0) {
                    for (SelectionKey sk : selector.selectedKeys()) {
                        selector.selectedKeys().remove(sk);
                        if (sk.isReadable()) {
                            SocketChannel sc = (SocketChannel)sk.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            String content = "";
                            while (sc.read(buffer) > 0) {
                                // sc.read(buffer);
                                buffer.flip();
                                content += charset.decode(buffer);
                            }
                            System.out.println("聊天信息：" + content);
                            sk.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NioClient().init();
    }
}
