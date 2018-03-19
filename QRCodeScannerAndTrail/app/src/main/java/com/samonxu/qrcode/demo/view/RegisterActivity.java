package com.samonxu.qrcode.demo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samonxu.qrcode.demo.MainActivity;
import com.samonxu.qrcode.demo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.PortUnreachableException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;


/**
 * Created by SHIYONG on 2017/5/9.
 */

public class RegisterActivity extends Activity implements View.OnClickListener{
    public static final int FAILED_CONNECT=1;
    public static final int WRONG_PASS=2;
    public static final int WRONG_USER=3;
    public static final int LOGIN_SUCCESS=4;
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    private EditText newuser;
    private EditText newpass;
    private EditText confirm;
    private TextView register;
    private String result;
    private SharedPreferences setting;
    private Handler handler;
    public SharedPreferences sp;
    public SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        newuser=(EditText)findViewById(R.id.newuser);
        newpass=(EditText)findViewById(R.id.newpass);
        confirm=(EditText)findViewById(R.id.confirm);
        register=(TextView)findViewById(R.id.register);
        register.setOnClickListener(this);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sp.edit();
        setting= PreferenceManager.getDefaultSharedPreferences(this);
        handler=new Handler(){
            public void handleMessage(Message message){
                switch(message.what){
                    case FAILED_CONNECT: {
                        Toast.makeText(RegisterActivity.this, "链接失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case WRONG_PASS: {
                        Toast.makeText(RegisterActivity.this, "请再次确认您的密码！"
                                , Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case WRONG_USER:{
                        Toast.makeText(RegisterActivity.this, "用户名格式错误！"
                                , Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case LOGIN_SUCCESS:{
                        int user_id=setting.getInt("ID",1);
                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                        intent.putExtra(USER_NAME,newuser.getText().toString());
                        intent.putExtra(USER_ID,user_id);
                        startActivity(intent);
                    }
                    default:
                        break;
                }
            }
        };
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:{
                final String new_user_name=newuser.getText().toString();
                final String new_pass_word=newpass.getText().toString();
                final String confirm_word=confirm.getText() .toString();
                editor.putString("username",new_user_name);
                editor.commit();
                if(confirm_word!=new_pass_word){
                    Message msg=new Message();
                    msg.what=WRONG_PASS;
                    handler.sendMessage(msg);
                }
                if(new_user_name.length()!=11){
                    Message msg=new Message();
                    msg.what=WRONG_USER;
                    handler.sendMessage(msg);
                }
                String test_user=new_user_name;
                if(test_user!=new_user_name){
                    Message msg=new Message();
                    msg.what=WRONG_USER;
                    handler.sendMessage(msg);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //HttpURLConnection connection=null;
                        try {
                            Random random = new Random();
                            int i=random.nextInt(2)+1;
                            String auth=String.valueOf(i);
                        String spec = "http://114.55.252.22:8989/register?phone_num="
                                + URLEncoder.encode(new_user_name, "UTF-8") + "&auth="
                                + URLEncoder.encode(auth, "UTF-8");
                        // 根据地址创建URL对象(网络访问的url)
                        URL url = new URL(spec);
                        // url.openConnection()打开网络链接
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

                            // 创建字节输出流对象
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            // 定义读取的长度
                            int len;
                            // 定义缓冲区
                            byte buffer[] = new byte[1024];
                            // 按照缓冲区的大小，循环读取
                            while ((len = is.read(buffer)) != -1) {
                                // 根据读取的长度写入到os对象中
                                os.write(buffer, 0, len);
                            }
                            // 释放资源
                            is.close();
                            os.close();
                            // 返回字符串
                            result = new String(os.toByteArray());
                            int id = Integer.parseInt(result);
                            setting.edit().putInt("ID", id).commit();
                            System.out.println("***************" + result
                                    + "******************");
                            Message msg=new Message();
                            msg.what=LOGIN_SUCCESS;
                            handler.sendMessage(msg);

                        } else {
                            System.out.println("------------------链接失败-----------------");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                }).start();
                finish();
            }

        }
    }
}
