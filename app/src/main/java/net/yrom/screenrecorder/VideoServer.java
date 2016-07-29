package net.yrom.screenrecorder;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by gengj on 7/8/2016.
 */
public class VideoServer extends  Thread{

    private static final  int MAX_CONNECTIONS = 10;
    public static final int PORT = 9090;
    private ServerSocket mServersocket;
    private boolean isStop = false;
    private Queue<ClientInstance> clientQ =
            new ConcurrentLinkedQueue<ClientInstance>();

    public void init()
    {
        try{
            mServersocket = new ServerSocket(PORT);
            mServersocket.setReuseAddress(true);

            isStop = false;
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void Stop() {
       isStop = true;
        try {
            if (mServersocket != null) {
                mServersocket.close();
                mServersocket = null;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Queue<ClientInstance> getClientInstances() {
        return clientQ;
    }

    public void run() {
        init();
        while(!isStop) {
            try {
                Socket aSocket = mServersocket.accept();
                ClientInstance client = new ClientInstance(aSocket);

                if (clientQ.size() < MAX_CONNECTIONS) {
                    clientQ.add(client);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (mServersocket != null) {
                mServersocket.close();
                mServersocket = null;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
