package net.yrom.screenrecorder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by gengj on 7/29/2016.
 */
public class SocketClient {
    private Socket mSocket;

    private OutputStream outputStream;
    private InputStream inputStream;


    public SocketClient(String ip)
    {
        try {
            mSocket = new Socket("10.112.116.181", 12345);
            outputStream = mSocket.getOutputStream();
            inputStream = mSocket.getInputStream();
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

    public int recvData(byte[] buffer, int length)
    {
        if (mSocket != null && inputStream != null) {
            try{
                inputStream.read(buffer);
            }catch (IOException e){
                e.printStackTrace();
                return -1;
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
