package radio.buetian.org.buetradio;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ajoy on 9/4/16.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recent_token = FirebaseInstanceId.getInstance().getToken();

    }
}
