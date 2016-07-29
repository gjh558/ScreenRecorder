package net.yrom.screenrecorder;

/**
 * Created by gengj on 7/8/2016.
 */
public class FrameData {

    public byte[] data;
    public int length;
    public int refrenceCount = 0;

    public FrameData(byte[] video_data, int size) {
        data = new byte[size + 4];
        length = size + 4;

        byte[] header = Utility.uint32ToByteArray(size);
        System.arraycopy(header, 0, data, 0, 4);
        System.arraycopy(video_data, 0, data, 4, size);
    }
}
