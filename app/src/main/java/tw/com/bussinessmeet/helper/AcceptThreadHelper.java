package tw.com.bussinessmeet.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.UUID;

public class AcceptThreadHelper extends Thread {
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    // 为其链接创建一个名称
    private final String NAME = "Bluetooth_Socket";
    private BluetoothAdapter mBlueToothAdapter;
    private Activity activity;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 通过msg传递过来的信息，吐司一下收到的信息
            Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    };
    public AcceptThreadHelper(BluetoothAdapter mBlueToothAdapter, UUID UUID,Activity activity) {
        this.mBlueToothAdapter = mBlueToothAdapter;
        this.activity = activity;
        try{
            serverSocket =  this.mBlueToothAdapter.listenUsingRfcommWithServiceRecord(NAME,UUID);
            Log.d("output",outputStream.toString());
        }catch(Exception e){

        }
    }

    @Override
    public void run() {
        super.run();
        try{
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            Log.d("output",outputStream.toString());
            while(true){
                Log.e("output","here");
                byte[] buffer = new byte[128];
                int count = inputStream.read(buffer);
                Log.e("output","message");
                Message message = new Message();
                message.obj = new String(buffer,0,count,"utf-8");
                Log.e("output",message.toString());
                handler.sendMessage(message);
            }

        }catch(Exception e){

        }
    }
}
