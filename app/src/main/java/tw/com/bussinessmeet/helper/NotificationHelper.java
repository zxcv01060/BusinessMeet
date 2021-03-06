package tw.com.bussinessmeet.helper;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import retrofit2.Call;
import tw.com.bussinessmeet.FriendsIntroductionActivity;
import tw.com.bussinessmeet.R;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;
import tw.com.bussinessmeet.adapter.MatchedDeviceRecyclerViewAdapter;

public class NotificationHelper {
    private Activity activity;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private AvatarHelper avatarHelper = new AvatarHelper();
    private MatchedBean matchedBean;
    private static int NOTIFICATION_ID = 0;
    public static final String CHANNEL_1_ID = "channel1";
    private int position;
    //private static int NOTIFICATION_ID = 0x30001; //196610
    private static int SUMMARY_ID = 1;
    
    public NotificationHelper(Activity activity) {
        this.activity = activity;
    }



    //public static final String CHANNEL_2_ID = "channel2";

     public void sendMessage(UserInformationBean userInformationBean, String memo) {
         String userName = userInformationBean.getUserName();
         String company = userInformationBean.getCompany();
         String email = userInformationBean.getEmail();
         String tel = userInformationBean.getTel();

         //String avatar = userInformationBean.getAvatar();


         String title1 = userName;
         String message1 = company;

                    /*String title2 = "李赫宰";
                    String message2 = " SM娛樂公司";*/
         /**/


         NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
         bigStyle.bigText(memo);
         NotificationCompat.Builder notification1 = new NotificationCompat.Builder(
                 activity, CHANNEL_1_ID
         )
                 .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                 .setContentTitle(title1 + " "  + message1)
                 .setContentText(memo)
                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                 .setDefaults(NotificationCompat.DEFAULT_ALL)
                 .setAutoCancel(true)
                 .setColor(Color.rgb(4,42,88))
                 .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                 .setLargeIcon(avatarHelper.getImageResource(userInformationBean.getAvatar()))
                 //.setStyle(bigStyle)
                 .setStyle(new NotificationCompat.BigTextStyle()
                         .bigText(memo))
                 .setGroup("group");



                    /*NotificationCompat.Builder notification2 = new NotificationCompat.Builder(
                            activity, CHANNEL_1_ID
                    )
                            .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                            .setContentTitle(title2)
                            .setContentText(message2)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setColor(Color.rgb(4,42,88))
                            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                            .setLargeIcon(avatarHelper.getImageResource(userInformationBean.getAvatar()))
                            .setGroup("group");*/

                    /*NotificationCompat.Builder summaryNotification = new NotificationCompat.Builder(
                            activity, CHANNEL_1_ID
                    )
                            .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .addLine(title1 + " " + message1)
                                    //.addLine(title2 + " " + message2)
                                    .setBigContentTitle("2 new messages"))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(NotificationCompat.DEFAULT_ALL) //設置鈴聲和振動 Android 7.1（API級別25）及更低版本上
                            //.setPriority(NotificationManager.IMPORTANCE_HIGH)
                            .setColor(Color.rgb(4,42,88))
                            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                            .setGroup("group")
                            .setColor(Color.BLUE)
                            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                            .setGroupSummary(true);*/


         //宣告Intent物件 跳至friends_introduction
/*                    Intent intent = new Intent(activity,
                            FriendsIntroductionActivity.class);*/

         Intent intent = new Intent();
         intent.setClass(activity,FriendsIntroductionActivity.class);
         Bundle bundle = new Bundle();
         bundle.putString("blueToothAddress",userInformationBean.getBlueTooth());
         intent.putExtras(bundle);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         //大頭貼---------------------
         AvatarHelper avatarHelper = new AvatarHelper();
         Bitmap profilePhoto = avatarHelper.getImageResource(userInformationBean.getAvatar());
         ByteArrayOutputStream bs = new ByteArrayOutputStream();
         profilePhoto.compress(Bitmap.CompressFormat.PNG, 100, bs);
         intent.putExtra("avatar", bs.toByteArray());
         //.大頭貼---------------------

         // Since android Oreo notification channel is needed. 添加通道分配
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             NotificationChannel channel1 =
                     new NotificationChannel(
                             CHANNEL_1_ID,
                             "channel1",
                             NotificationManager.IMPORTANCE_HIGH
                     );
             channel1.setDescription("This is channel 1");
             channel1.enableLights(true);
             channel1.enableVibration(true);

             NotificationManager manager = activity.getSystemService(NotificationManager.class);
             manager.createNotificationChannel(channel1);

         }
         //.添加通道分配

         // 宣告一個 PendingIntent 的物件(執行完並不會馬上啟動,點訊息的時候才會跳到別的 Activity)
         PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                 NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         notification1.setContentIntent(pendingIntent);
                    /*notification2.setContentIntent(pendingIntent);
                    summaryNotification.setContentIntent(pendingIntent);*/

         //定義一個訊息管理者 和系統要 取得訊息管理者的物件
         NotificationManager notificationManager = (NotificationManager) activity.getSystemService(
                 Context.NOTIFICATION_SERVICE
         );
//                    NotificationManagerCompat.from(activity)
                    /*手機端有摘要通知，又有一般的通知的話改寫
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);*/


         //要求傳送一個訊息
         //id若一樣，則為更新通知，之前的通知會不見
         notificationManager.notify(NOTIFICATION_ID++,notification1.build());


         //notificationManager.notify(SUMMARY_ID, summaryNotification.build());
         if(NOTIFICATION_ID >= 10) notificationManager.cancel(NOTIFICATION_ID - 10);// 取消之前的通知消息;
         Log.d("intomatchID",String.valueOf(NOTIFICATION_ID));
/*                    SystemClock.sleep(1000);
                    notificationManager.notify(2, notification1.build());
                    SystemClock.sleep(1000);
                    notificationManager.notify(3, notification2.build());
                    SystemClock.sleep(1000);
                    notificationManager.notify(4, summaryNotification.build());*/

         Log.d("taskHelper","sucess");
    }



}
