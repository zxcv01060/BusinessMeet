package tw.com.bussinessmeet;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import tw.com.bussinessmeet.helper.BlueToothHelper;

//https://codertw.com/android-%E9%96%8B%E7%99%BC/332688/
public class MainActivity extends AppCompatActivity  {
    private BlueToothHelper blueToothHelper;
    private boolean permission = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.startBuleTooth();
        blueToothHelper.openGPS(this);
        Thread checkPermission = new Thread(){
            @Override
            public void run() {
                super.run();
                while(!permission) {

                    permission = blueToothHelper.checkPermission();
                    if(permission){
                        Thread.interrupted();
                    }
                }
                Log.d("resultthread",String.valueOf(permission));
            }
        };
        checkPermission.start();





    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        blueToothHelper.requestPermissionsResult(requestCode,grantResults);
    }
}
