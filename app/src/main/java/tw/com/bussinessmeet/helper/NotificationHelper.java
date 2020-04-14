package tw.com.bussinessmeet.helper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import tw.com.bussinessmeet.FriendsIntroductionActivity;
import tw.com.bussinessmeet.R;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;
import tw.com.bussinessmeet.service.UserInformationService;

public class NotificationHelper {
    private Activity activity;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private AvatarHelper avatarHelper = new AvatarHelper();
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>> searchResponseListener =
            new AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>>() {
                @Override
                public Call<ResponseBody<List<UserInformationBean>>> request(UserInformationBean... userInformationBeans) {

                    return userInformationService.search(userInformationBeans[0]);
                }

                @Override
                public void onSuccess(List<UserInformationBean> userInformationBeanList) {
                    Log.d("nameeee", userInformationBeanList.get(0).getUserName());
                    UserInformationBean userInformationBean = userInformationBeanList.get(0);
                    String userName = userInformationBean.getUserName();
                    String company = userInformationBean.getCompany();

                    String title1 = userName;
                    String message1 = company;
                    String title2 = "李赫宰";
                    String message2 = " SM娛樂公司";



                    NotificationCompat.Builder notification1 = new NotificationCompat.Builder(
                            activity, CHANNEL_1_ID
                    )
                            .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                            .setContentTitle(title1)
                            .setContentText(message1)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setColor(Color.rgb(4,42,88))
                            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                            .setLargeIcon(avatarHelper.getImageResource(userInformationBean.getAvatar()))
                            .setGroup("group");


                    NotificationCompat.Builder notification2 = new NotificationCompat.Builder(
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
                            .setGroup("group");
                    NotificationCompat.Builder summaryNotification = new NotificationCompat.Builder(
                            activity, CHANNEL_1_ID
                    )
                            .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .addLine(title2 + " " + message2)
                                    .addLine(title1 + " " + message1)
                                    .setBigContentTitle("2 new messages"))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(NotificationCompat.DEFAULT_ALL) //設置鈴聲和振動 Android 7.1（API級別25）及更低版本上
                            //.setPriority(NotificationManager.IMPORTANCE_HIGH)
                            .setColor(Color.rgb(4,42,88))
                            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                            .setGroup("group")
                            .setColor(Color.BLUE)
                            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                            .setGroupSummary(true);


                    //宣告Intent物件 跳至friends_introduction
                    Intent intent = new Intent(activity,
                            FriendsIntroductionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title", title1);

                    // 宣告一個 PendingIntent 的物件(執行完並不會馬上啟動,點訊息的時候才會跳到別的 Activity)
                    PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification1.setContentIntent(pendingIntent);
                    notification2.setContentIntent(pendingIntent);
                    summaryNotification.setContentIntent(pendingIntent);

                    //定義一個訊息管理者 和系統要 取得訊息管理者的物件
                    NotificationManager notificationManager = (NotificationManager) activity.getSystemService(
                            Context.NOTIFICATION_SERVICE
                    );

                    //要求傳送一個訊息
                    //id若一樣，則為更新通知，之前的通知會不見
                    SystemClock.sleep(1000);
                    notificationManager.notify(2, notification1.build());
                    SystemClock.sleep(1000);
                    notificationManager.notify(3, notification2.build());
                    SystemClock.sleep(1000);
                    notificationManager.notify(4, summaryNotification.build());
                }

                @Override
                public void onFail(int status) {

                }
            };
    public NotificationHelper(Activity activity) {
        this.activity = activity;
    }

    public static final String CHANNEL_1_ID = "channel1";


    //public static final String CHANNEL_2_ID = "channel2";


    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =
                    new NotificationChannel(
                            CHANNEL_1_ID,
                            "channel1",
                            NotificationManager.IMPORTANCE_HIGH
                    );
            channel1.setDescription("This is channel 1");

            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }


     public void sendMessage(String matchedBlueTooth) {

         Log.d("seedmess","ness");
         UserInformationBean ufb = new UserInformationBean();
         ufb.setBlueTooth(matchedBlueTooth);
         AsyncTasKHelper.execute(searchResponseListener, ufb);
        Log.d("taskHelper","sucess");
         //Bitmap profilePhoto = avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));

    }
}
