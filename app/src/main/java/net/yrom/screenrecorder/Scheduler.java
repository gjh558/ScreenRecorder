package net.yrom.screenrecorder;

import java.util.Iterator;
import java.util.Queue;

/**
 * Created by gengj on 7/8/2016.
 */
public class Scheduler extends Thread{

    private Queue<FrameData> frameDatas;
    private ScreenRecorder mScreenRecoder;
    private SocketClient mSocketClient;

    private boolean isStop = false;
    private String ip;

    public Scheduler(ScreenRecorder screenRecorder, String ip) {
        mScreenRecoder = screenRecorder;
        this.ip = ip;

        frameDatas = mScreenRecoder.getFrameDatas();
        isStop = false;
    }

    private void closeClients() {

    }

    public void Stop(){
        isStop = true;
    }
    public void run() {

        mSocketClient = new SocketClient(ip);

        //send id ... to tcp server


        mScreenRecoder.start();

        while (!isStop) {
            if (!frameDatas.isEmpty()) {

                FrameData frameData = frameDatas.poll();
                int res = mSocketClient.sendData(frameData.data, frameData.length);
            }
        }
        mSocketClient.Stop();
        mScreenRecoder.quit();
    }

}
