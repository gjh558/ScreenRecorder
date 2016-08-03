package net.yrom.screenrecorder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

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

    private String teacherId;
    private String roomId;

    private Context context;

    public Scheduler(Context context, ScreenRecorder screenRecorder, String ip, String id, String cid) {
        mScreenRecoder = screenRecorder;
        this.ip = ip;
        this.teacherId = id;
        this.roomId = cid;
        this.context = context;

        frameDatas = mScreenRecoder.getFrameDatas();
        isStop = false;
    }

    private void closeClients() {

    }

    public void Stop(){
        isStop = true;
    }
    public void run() {

        int type_side = 1;
        int id = Integer.valueOf(teacherId);
        int cid = Integer.valueOf(roomId);

        mSocketClient = new SocketClient(ip);

        //send id ... to tcp server
        byte[] type_side_bytes = Utility.uint32ToByteArray(type_side);
        byte[] id_bytes = Utility.uint32ToByteArray(id);
        byte[] cid_bytes = Utility.uint32ToByteArray(cid);

        byte[] settings = new byte[12];
        System.arraycopy(type_side_bytes, 0, settings, 0, 4);
        System.arraycopy(id_bytes, 0, settings, 4, 4);
        System.arraycopy(cid_bytes, 0, settings, 8, 4);

        mSocketClient.sendData(settings, 12);

        int ret = mSocketClient.recvData(settings, 4);
        Message msg = new Message();
        if (ret != 0) {

            msg.what = 0;
            mHandler.sendMessage(msg);
            return;
        } else {
            if (Utility.byteArrayToUint32(settings, 0) == 0) {
                msg.what = 1;
                mHandler.sendMessage(msg);
            }else {
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
        }
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

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(context, "create succeed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "create failed", Toast.LENGTH_SHORT).show();
            }
        }
    };
}