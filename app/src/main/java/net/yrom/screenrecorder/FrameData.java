package net.yrom.screenrecorder;

/**
 * Created by gengj on 7/8/2016.
 */
public class FrameData {

    public static final long HEADER = 0xFFFFFFFFL;
    public static final byte TYPE_SCREEN = 0x01;
    public static final byte CODEC_TYPE = 0x01;

    public static final int headerSize = 22;
    String name = "samsung";

    public byte[] data;
    public int length;
    public int refrenceCount = 0;

    public FrameData(byte[] video_data, int size) {
        data = new byte[size + 22];
        length = size + 22;

        byte[] head = Utility.uint32ToByteArray(HEADER);
        System.arraycopy(head, 0, data, 0, 4);

        data[4] = TYPE_SCREEN;
        data[5] = CODEC_TYPE;

        byte[] video_size = Utility.uint32ToByteArray(size);
        System.arraycopy(video_size, 0, data, 18, 4);

        System.arraycopy(video_data, 0, data, 0, size);
    }
}
