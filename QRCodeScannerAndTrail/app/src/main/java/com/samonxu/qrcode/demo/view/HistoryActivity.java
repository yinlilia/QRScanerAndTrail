package com.samonxu.qrcode.demo.view;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import com.samonxu.qrcode.demo.R;
import com.samonxu.qrcode.demo.util.DBManager;
import com.samonxu.qrcode.demo.util.History;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yinlili on 2017/5/18.
 */

public class HistoryActivity extends AppCompatActivity {
    private List<String> groupList;
    private List<List<String>> childList;
    private ImageView status;
    private DBManager dbManager;
    private ListView listView;
    public static final String USER_NAME = "user_name";
    //private MyReceiver myReceiver = new MyReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);

            listView = (ListView) findViewById(R.id.listView);
            // 初始化DBManager
            dbManager = new DBManager(this);
            //initReceiver();
        }
    /*private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        registerReceiver(myReceiver, intentFilter);
    }*/

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            //getMenuInflater().inflate(R.menu.hello_db, menu);
            return true;
        }

        @Override
        protected void onDestroy()
        {
            super.onDestroy();
            dbManager.closeDB();// 释放数据库资源
        }

        public void add(View view)
        {
            ArrayList<History> histories = new ArrayList<>();
            String number=null;
            String user_name=getIntent().getExtras().getString(USER_NAME);
            String macAddress=getIntent().getExtras().getString("macAddress");
            String time=getIntent().getExtras().getString("time");
            String time1=getIntent().getExtras().getString("time1");
            History history = new History(number,user_name, macAddress,time,time1 );

            histories.add(history);


            dbManager.add(histories);
        }

        public void update(View view)
        {
            // 把Jane的年龄改为30（注意更改的是数据库中的值，要查询才能刷新ListView中显示的结果）
            History person = new History();
            person.name = "Jane";
            person.time = "30";
            dbManager.updateAge(person);
        }

        public void delete(View view)
        {
            // 删除所有三十岁以上的人（此操作在update之后进行，Jane会被删除（因为她的年龄被改为30））
            // 同样是查询才能查看更改结果
            History person = new History();
            person.time = "30";
            dbManager.deleteOldPerson(person);
        }

        public void query(View view)
        {
            List<History> histories = dbManager.query();
            ArrayList<Map<String, String>> list = new ArrayList<>();
            for (History history : histories)
            {
                HashMap<String, String> map = new HashMap<>();
                map.put("name","user_name:"+history.name );
                map.put("info","macAddress:"+history.macAddress + "      "+"recommand_time:"+history.time+"    "+"actucal_time:"+history.time1);
                list.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, list,
                    android.R.layout.simple_list_item_2, new String[] { "name",
                    "info" }, new int[] { android.R.id.text1,
                    android.R.id.text2 });
            listView.setAdapter(adapter);
        }

        @SuppressWarnings("deprecation")
        public void queryTheCursor(View view)
        {
            Cursor c = dbManager.queryTheCursor();
            startManagingCursor(c); // 托付给activity根据自己的生命周期去管理Cursor的生命周期
            CursorWrapper cursorWrapper = new CursorWrapper(c)
            {
                @Override
                public String getString(int columnIndex)
                {
                    // 将简介前加上年龄
                    if (getColumnName(columnIndex).equals("info"))
                    {
                        int age = getInt(getColumnIndex("age"));
                        return age + " years old, " + super.getString(columnIndex);
                    }
                    return super.getString(columnIndex);
                }
            };
            // 确保查询结果中有"_id"列
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2, cursorWrapper,
                    new String[] { "name", "info" }, new int[] {
                    android.R.id.text1, android.R.id.text2 });
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }
    /*class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //user_id = intent.getIntExtra(USER_ID);
            user_name=intent.getStringExtra(USER_NAME);
            macAddress=intent.getStringExtra("macAddress");
            time=intent.getStringExtra("time");
            time1=intent.getStringExtra("time1");
        }
    }*/


    }
