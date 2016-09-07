package radio.buetian.org.buetradio;

/**
 * Created by ajoy on 9/6/16.
 */
public class ChatMessage {
    private String user;
    private String message;
    private String photoUrl;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
