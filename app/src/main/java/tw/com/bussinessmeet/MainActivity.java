package tw.com.bussinessmeet;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.JsonHelper;
import tw.com.bussinessmeet.network.RetrofitConfig;

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
