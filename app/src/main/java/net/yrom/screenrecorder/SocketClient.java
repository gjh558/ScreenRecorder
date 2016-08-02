package net.yrom.screenrecorder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by gengj on 7/29/2016.
 */
public class SocketClient {
    private Socket mSocket;

    private OutputStream outputStream;


    public SocketClient(String ip)
    {
        try {
            mSocket = new Socket(ip, 12345);
            outputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int sendData(byte[] buffer, int length)
    {
        if (mSocket != null && outputStream != null) {

            try {
                outputStream.write(buffer, 0, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public void Stop()
    {
        try {
            outputStream.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
