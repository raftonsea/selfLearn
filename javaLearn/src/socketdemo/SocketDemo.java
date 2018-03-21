package socketdemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: raftonsea
 * @createTime:2018/3/6 15:33
 * @description:
 */
public class SocketDemo {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9999);

        ServerSocket ss = new ServerSocket();

        while (true) {
            InputStream is = socket.getInputStream();
            byte[] d = new byte[2048];
            is.read(d);
            String data = new String(d);
            System.err.println(data);
        }
    }

}
