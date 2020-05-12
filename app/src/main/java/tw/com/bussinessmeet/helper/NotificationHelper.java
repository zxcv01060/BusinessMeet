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

import java.io.ByteArrayOutputStream;
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
    private static int NOTIFICATION_ID = 0;
    //private static int NOTIFICATION_ID = 0x30001; //196610
    private static int SUMMARY_ID = 1;
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
                    String position = userInformationBean.getPosition();
                    String email = userInformationBean.getEmail();
                    String tel = userInformationBean.getTel();
                    //String memo = userInformationBean.getMemo();
                    //String avatar = userInformationBean.getAvatar();


                    String title1 = userName;
                    String message1 = company;
                    String memo = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
                    //String memo = memo;
                    /*String title2 = "李赫宰";
                    String message2 = " SM娛樂公司";*/
                    /**/


                    NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
                    bigStyle.bigText(memo);
                    NotificationCompat.Builder notification1 = new NotificationCompat.Builder(
                            activity, CHANNEL_1_ID
                    )
                            .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                            .setContentTitle(title1 + " " +company)
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
                    Intent intent = new Intent(activity,
                            FriendsIntroductionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title", userName);
                    intent.putExtra("company", company);
                    intent.putExtra("position", position);
                    intent.putExtra("email", email);
                    intent.putExtra("tel", tel);
                    //大頭貼---------------------
                    AvatarHelper avatarHelper = new AvatarHelper();
                    Bitmap profilePhoto = avatarHelper.getImageResource(userInformationBean.getAvatar());
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    profilePhoto.compress(Bitmap.CompressFormat.PNG, 100, bs);
                    intent.putExtra("avatar", bs.toByteArray());
                    //.大頭貼---------------------


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
                    /*手機端有摘要通知，又有一般的通知的話改寫
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);*/


                    //要求傳送一個訊息
                    //id若一樣，則為更新通知，之前的通知會不見
                    notificationManager.notify(NOTIFICATION_ID++,notification1.build());


                    //notificationManager.notify(SUMMARY_ID, summaryNotification.build());
                    if(NOTIFICATION_ID == 10) notificationManager.cancel(NOTIFICATION_ID - 10);// 取消之前的通知消息;
                    Log.d("intomatchID",String.valueOf(NOTIFICATION_ID));
/*                    SystemClock.sleep(1000);
                    notificationManager.notify(2, notification1.build());
                    SystemClock.sleep(1000);
                    notificationManager.notify(3, notification2.build());
                    SystemClock.sleep(1000);
                    notificationManager.notify(4, summaryNotification.build());*/


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


/*    public void createNotificationChannel() {
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
    }*/


     public void sendMessage(String matchedBlueTooth) {

         Log.d("seedmess",matchedBlueTooth);
         UserInformationBean ufb = new UserInformationBean();
         ufb.setBlueTooth(matchedBlueTooth);
         AsyncTasKHelper.execute(searchResponseListener, ufb);
        Log.d("taskHelper","sucess");
         //Bitmap profilePhoto = avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));

    }
}
