package org.kun.java.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kun
 * @date 2019/10/06
 */
public class Server {
    public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9000);
        while (true) {
            Socket socket = server.accept();
            socketList.add(socket);
            new Thread(new ServerThread(socket)).start();
        }
    }

}
