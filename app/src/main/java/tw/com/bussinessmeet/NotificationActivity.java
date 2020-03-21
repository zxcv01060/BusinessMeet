package tw.com.bussinessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 0;
    Button btNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        btNotification = (Button)findViewById(R.id.bt_notification);

        btNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "This is a.";
                //定義訊息
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        NotificationActivity.this
                )
                        .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)  //icon
                        .setContentText("New Notification") //標題
                        .setContentText(message)  //內容
                        .setAutoCancel(true);
                //宣告Intent物件 跳至friends_introduction
                Intent intent = new Intent(NotificationActivity.this,
                        FriendsIntroductionActivity.class);


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",message);


                // 宣告一個 PendingIntent 的物件(執行完並不會馬上啟動,點訊息的時候才會跳到別的 Activity)
                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this,
                        NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                //定義一個訊息管理者 和系統要 取得訊息管理者的物件
                NotificationManager notificationManager = (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );

                //要求傳送一個訊息
                notificationManager.notify(0,builder.build());
            }
        });
    }
}
