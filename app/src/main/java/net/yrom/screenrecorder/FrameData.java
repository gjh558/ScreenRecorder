package net.yrom.screenrecorder;

/**
 * Created by gengj on 7/8/2016.
 */
public class FrameData {
    public byte[] data;
    public int length;
    public int refrenceCount = 0;

    public FrameData(byte[] video_data, int size, long pts) {
        data = new byte[size];
        length = size;

//        byte[] video_size = Utility.uint32ToByteArray(size);
//        System.arraycopy(video_size, 0, data, 0, 4);

        System.arraycopy(video_data, 0, data, 0, size);
    }
}
