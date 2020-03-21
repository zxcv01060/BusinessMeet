package tw.com.bussinessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity {

    Button btNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications","MyNotifications",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        //指定通知的UI和操作
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"MyNotifications")
                .setContentTitle("This is my title")
                .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                .setAutoCancel(true)
                .setContentText("This is my text");

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        //創建通知
        manager.notify(999, builder.build());
    }
}


//要指定通知的UI和操作，請使用NotificationCompat.Builder。
//要創建通知，請使用NotificationCompat.Builder.build()。
//要發出通知，請使用NotificationManager.notify()將該通知對像傳遞給Android運行時系統。