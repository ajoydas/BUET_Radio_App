package radio.buetian.org.buetradio.Objects;

import android.media.MediaPlayer;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by ajoy on 9/10/16.
 */
public class PlayerConnection {
    private static MediaPlayer mediaPlayer=new MediaPlayer();
    private static InputStream inputStream=null;
    private static FileOutputStream fileOutputStream=null;
    private static boolean isRecording=false;
    private static String channel="Channel 1";
    private static boolean ui=false;

    public static String getChannel() {
        return channel;
    }

    public static void setChannel(String channel) {
        PlayerConnection.channel = channel;
    }


    public static boolean isUi() {
        return ui;
    }

    public static void setUi(boolean ui) {
        PlayerConnection.ui = ui;
    }


    public static boolean isRecording() {
        return isRecording;
    }

    public static void setIsRecording(boolean isRecording) {
        PlayerConnection.isRecording = isRecording;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        PlayerConnection.mediaPlayer = mediaPlayer;
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static void setInputStream(InputStream inputStream) {
        PlayerConnection.inputStream = inputStream;
    }

    public static FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public static void setFileOutputStream(FileOutputStream fileOutputStream) {
        PlayerConnection.fileOutputStream = fileOutputStream;
    }
}
