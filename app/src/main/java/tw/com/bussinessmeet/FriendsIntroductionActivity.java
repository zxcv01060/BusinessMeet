package tw.com.bussinessmeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class FriendsIntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_introduction);

        TextView textView = findViewById(R.id.friends_name);
        String message = getIntent().getStringExtra("message");
        textView.setText(message);

    }
}
