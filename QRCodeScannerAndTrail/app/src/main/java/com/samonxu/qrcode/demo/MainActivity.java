package com.samonxu.qrcode.demo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.samonxu.qrcode.demo.Service.BluetoothService;
import com.samonxu.qrcode.demo.util.GpsData;
import com.samonxu.qrcode.demo.util.SlideSwitchView;
import com.samonxu.qrcode.demo.view.BaseActivity;
import com.samonxu.qrcode.demo.view.HistoryActivity;
import com.samonxu.qrcode.demo.view.SettingActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by Samon Xu on 2016/03/22 0022.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final int PRINTTIME = 1;
    public static final int FAILED_AUTH=1;
    private int timeSpace = 1000;

    private TextView resultTv;
    private ImageView resultIv;
    private SlideSwitchView mSlideSwitchView;
    private String resultData;
    private MyHandler handler = new MyHandler();
    private Handler messagehandler;
    public Intent lastIntent;
    public String result;
    public String id;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;
    private AutoThread autoThread=new AutoThread();
    private Location loc;
    private TextView gps;
    private int gps_lat;
    private int gps_lon;
    public static final int SUCCESS=1;
    public static final int FAILED=2;
    private Message message=new Message();
    private TextView verCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = (TextView) findViewById(R.id.tv_result);
        gps=(TextView)findViewById(R.id.tv_gps);
        verCode=(TextView)findViewById(R.id.tv_verCode);
        lastIntent = getIntent();
        id = lastIntent.getStringExtra("ID");
        message.what=SUCCESS;
        verCode.setText("当前的软件版本号为："+getVerCode(MainActivity.this));
        initLocation();
    }
    private void initLocation(){
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            criteria=new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(false);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            provider=locationManager.getBestProvider(criteria,true);
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(provider,1000,0, locationListener);
            }
        }
    }
    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
        new Thread(runnable1).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            resultData=data.getStringExtra(CaptureActivity.EXTRA_RESULT);
            resultTv.setText("扫描结果为："+resultData);
            handler.sendMessage(message);
            //resultIv.setImageBitmap((Bitmap) data.getParcelableExtra(CaptureActivity.EXTRA_BITMAP));//TODO
        } else {
            resultTv.setText("扫描结果为空...");
            //resultIv.setImageDrawable(null);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            String packageName = context.getPackageName();
            verCode = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    Runnable runnable1=new Runnable() {
            @Override
            public void run() {
                try {
                    int verCode = getVerCode(MainActivity.this);
                    String spec = "http://114.55.252.22:8989/auth?user_id=" + verCode + "&result=" + resultData + "&gps_lat" + gps_lat + "&gps_lon" + gps_lon;
                    URL url = new URL(spec);
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");// 设置请求的方式
                    urlConnection.setReadTimeout(5000);// 设置超时的时间
                    urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
                    // 设置请求的头
                    urlConnection
                            .setRequestProperty("User-Agent",
                                    "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    // 获取响应的状态码 404 200 505 302
                    if (urlConnection.getResponseCode() == 200) {
                        // 获取响应的输入流对象
                        InputStream is = urlConnection.getInputStream();

                    }
                    handler.sendMessage(message);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(myReceiver);
    }
    private final LocationListener locationListener = new LocationListener()
    {

        public void onLocationChanged(Location arg0)
        {
            //isLocation = true;
            if (!autoThread.isAlive())
                autoThread.start();
        }

        public void onProviderDisabled(String arg0)
        {
            showInfo(null);
        }

        public void onProviderEnabled(String arg0)
        {
        }

        public void onStatusChanged(String arg0, int arg1, Bundle arg2)
        {
            //isLocation = true;
            if (!autoThread.isAlive())
                autoThread.start();
        }
    };
    private GpsData getLastPosition()
    {
        GpsData result = new GpsData();
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            loc = locationManager.getLastKnownLocation(provider);
        }
        if (loc != null) {
            result.Latitude = (int) (loc.getLatitude() * 1E6);
            result.Longitude = (int) (loc.getLongitude() * 1E6);
        }
        return result;
    }
    private class AutoThread extends Thread
    {
        private boolean running = true;
        private Handler h = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {

                showInfo(getLastPosition());
            }
        };

        @Override
        public void run()
        {
            while (running)
            {
                try
                {
                    h.sendEmptyMessage(0);
                    Thread.sleep(timeSpace);
                } catch (Exception e)
                {

                }
            }
        }

    }
    public class MyHandler extends Handler{
        public void handleMessage(Message msg){
            if(msg.what==SUCCESS){
                Toast.makeText(MainActivity.this,"数据上传成功",Toast.LENGTH_SHORT);
            }else{
                Toast.makeText(MainActivity.this,"数据上传失败",Toast.LENGTH_SHORT);
            }

        }
    }
    private void showInfo(GpsData cdata) {
        if (cdata == null)
        {
            gps.setText("GPS未打开");

        } else {
            gps_lat=cdata.Latitude;
            gps_lon=cdata.Longitude;
            gps.setText("当前位置的纬度为："+String.format("%d",gps_lat)+"    "+"当前位置的经度为："+String.format("%d", gps_lon));

        }

    }
}


