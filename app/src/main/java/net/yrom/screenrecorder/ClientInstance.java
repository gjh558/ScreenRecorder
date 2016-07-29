package net.yrom.screenrecorder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by gengj on 7/8/2016.
 */
public class ClientInstance{

    private Socket mSocket;
    private OutputStream outputStream;

    public ClientInstance(Socket aSock) {

        try {
            mSocket = aSock;
            mSocket.setKeepAlive(true);
            outputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Close() {
        try {
            outputStream.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int Read(byte[] buffer, int length) {
        return 0;
    }

    public int Write(byte[] buffer, int length) {
        int res = 0;
        try {
            outputStream.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
            res = -1;
        }
        return res;
    }
}
