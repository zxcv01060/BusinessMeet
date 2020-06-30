package tw.com.bussinessmeet.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.helper.BlueToothHelper;

public class NotificationService  extends Service {
    private Map<String, UserInformationBean> userInformationBeanMap = new HashMap<>();
    public class LocalBinder extends Binder{
        NotificationService getService(){
            return NotificationService.this;
        }
    }
    private LocalBinder mLocBin = new LocalBinder();
    private BlueToothHelper blueToothHelper = null;
    public void sendNotifitation(){
        Log.e("test","success");
    }
    public void searchBlueTooth(){

    }
    @Override
    public IBinder onBind(Intent intent) {
        return mLocBin;
    }

    @Override
    public void onCreate() {
        Log.e("service ","serviceStart");
        blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.searchBlueToothInBackground();
        blueToothHelper.startBuleTooth();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
