package radio.buetian.org.buetradio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        message = (TextView) findViewById(R.id.tMessage);

        if(getIntent().getExtras()!=null)
        {
            message.setText(getIntent().getExtras().getString("Message"));
        }

    }
}
