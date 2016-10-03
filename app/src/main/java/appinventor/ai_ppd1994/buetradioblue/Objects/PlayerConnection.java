package appinventor.ai_ppd1994.buetradioblue.Objects;

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

    //remotes
    private static String Channel1=null;
    private static String Channel2=null;
    private static String ContactNumber=null;
    private static String CallNumber=null;

    public static String getSmsNumber() {
        return SmsNumber;
    }

    public static void setSmsNumber(String smsNumber) {
        SmsNumber = smsNumber;
    }

    public static String getChannel1() {
        return Channel1;
    }

    public static void setChannel1(String channel1) {
        Channel1 = channel1;
    }

    public static String getChannel2() {
        return Channel2;
    }

    public static void setChannel2(String channel2) {
        Channel2 = channel2;
    }

    public static String getContactNumber() {
        return ContactNumber;
    }

    public static void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public static String getCallNumber() {
        return CallNumber;
    }

    public static void setCallNumber(String callNumber) {
        CallNumber = callNumber;
    }

    private static String SmsNumber=null;

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
