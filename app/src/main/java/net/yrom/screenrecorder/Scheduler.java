package net.yrom.screenrecorder;

import java.util.Iterator;
import java.util.Queue;

/**
 * Created by gengj on 7/8/2016.
 */
public class Scheduler extends Thread{

    private Queue<FrameData> frameDatas;
    private Queue<ClientInstance> clientInstances;

    private VideoServer mVideoServer;
    private ScreenRecorder mScreenRecoder;

    private boolean isStop = false;

    public Scheduler(VideoServer videoServer, ScreenRecorder screenRecorder) {
        mVideoServer = videoServer;
        mScreenRecoder = screenRecorder;

        clientInstances = mVideoServer.getClientInstances();
        frameDatas = mScreenRecoder.getFrameDatas();
        isStop = false;
    }

    private void closeClients() {
        while(!clientInstances.isEmpty()) {
            clientInstances.poll().Close();
        }
    }

    public void Stop(){
        isStop = true;
    }
    public void run() {

        mVideoServer.init();

        mVideoServer.start();
        mScreenRecoder.start();

        while (!isStop) {
            if (!clientInstances.isEmpty() && !frameDatas.isEmpty()) {

                FrameData frameData = frameDatas.poll();

                Iterator<ClientInstance> it = clientInstances.iterator();

                while(it.hasNext()) {

                    ClientInstance c = it.next();
                    int res = c.Write(frameData.data, frameData.length);
                    if (res == -1) {
                        c.Close();
                        it.remove();
                    }
                }
            }
        }

        closeClients();
        mVideoServer.Stop();
        mScreenRecoder.quit();
    }

}
