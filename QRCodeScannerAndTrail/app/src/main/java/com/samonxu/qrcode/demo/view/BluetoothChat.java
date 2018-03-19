package com.samonxu.qrcode.demo.view;

/**
 * Created by yinlili on 2017/5/20.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.samonxu.qrcode.demo.Service.BluetoothChatService;

public class BluetoothChat extends Activity
{
    private static final boolean D = true;
    public static final String DEVICE_NAME = "device_name";
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String TAG = "BluetoothChat";
    public static final String TOAST = "toast";
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private ListView mConversationView;
    private final Handler mHandler = new Handler()
    {
        public void handleMessage(Message paramAnonymousMessage)
        {
            switch (paramAnonymousMessage.what)
            {
                default:
                    return;
                case 1:
                    Log.i("BluetoothChat", "MESSAGE_STATE_CHANGE: " + paramAnonymousMessage.arg1);
                    switch (paramAnonymousMessage.arg1)
                    {
                        default:
                            return;
                        case 0:
                        case 1:
                            BluetoothChat.this.mTitle.setText("2130968585");
                            return;
                        case 3:
                            BluetoothChat.this.mTitle.setText("2130968584");
                            BluetoothChat.this.mTitle.append(BluetoothChat.this.mConnectedDeviceName);
                            BluetoothChat.this.mConversationArrayAdapter.clear();
                            return;
                        case 2:
                    }
                    BluetoothChat.this.mTitle.setText("2130968583");
                    return;
                case 3:
                    String str2 = new String((byte[])paramAnonymousMessage.obj);
                    BluetoothChat.this.mConversationArrayAdapter.add("Me:  " + str2);
                    return;
                case 2:
                    String str1 = new String((byte[])paramAnonymousMessage.obj, 0, paramAnonymousMessage.arg1);
                    BluetoothChat.this.mConversationArrayAdapter.add(BluetoothChat.this.mConnectedDeviceName + ":  " + str1);
                    return;
                case 4:
                    BluetoothChat.this.mConnectedDeviceName = paramAnonymousMessage.getData().getString("device_name");
                    //Toast.makeText(BluetoothChat.this.getApplicationContext(), "Connected to " + BluetoothChat.this.mConnectedDeviceName, 0).show();
                    return;
                case 5:
            }
            //Toast.makeText(BluetoothChat.this.getApplicationContext(), paramAnonymousMessage.getData().getString("toast"), ).show();
        }
    };
    private EditText mOutEditText;
    private StringBuffer mOutStringBuffer;
    private Button mSendButton;
    private Button mSendButtonA;
    private Button mSendButtonB;
    private TextView mTitle;
    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener()
    {
        public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
        {
            if ((paramAnonymousInt == 0) && (paramAnonymousKeyEvent.getAction() == 1))
            {
                String str = paramAnonymousTextView.getText().toString();
                //BluetoothChat.this.sendMessage(str);
            }
            Log.i("BluetoothChat", "END onEditorAction");
            return true;
        }
    };

    private void ensureDiscoverable()
    {
        Log.d("BluetoothChat", "ensure discoverable");
        if (this.mBluetoothAdapter.getScanMode() != 23)
        {
            Intent localIntent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
            localIntent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", 300);
            startActivity(localIntent);
        }
    }

   /* private void sendMessage(String paramString)
    {
        if (this.mChatService.getState() != 3)
            Toast.makeText(this, "2130968581",Toast.LENGTH_SHORT).show();
        while (paramString.length() <= 0)
            return;
        byte[] arrayOfByte = paramString.getBytes();
        this.mChatService.write(arrayOfByte);
        this.mOutStringBuffer.setLength(0);
        this.mOutEditText.setText(this.mOutStringBuffer);
    }

    private void setupChat()
    {
        Log.d("BluetoothChat", "setupChat()");
        this.mConversationArrayAdapter = new ArrayAdapter(this, 2130903044);
        this.mConversationView = ((ListView)findViewById(2131165191));
        this.mConversationView.setAdapter(this.mConversationArrayAdapter);
        this.mOutEditText = ((EditText)findViewById(2131165194));
        this.mOutEditText.setOnEditorActionListener(this.mWriteListener);
        this.mSendButton = ((Button)findViewById(R.id.bt_connect));
        this.mSendButtonA = ((Button)findViewById(2131165192));
        this.mSendButtonB = ((Button)findViewById(2131165193));
        this.mSendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                String str = ((TextView)BluetoothChat.this.findViewById(2131165194)).getText().toString();
                BluetoothChat.this.sendMessage(str);
            }
        });
        this.mSendButtonA.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                BluetoothChat.this.sendMessage("A");
            }
        });
        this.mSendButtonB.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                //((TextView)BluetoothChat.this.findViewById(2131165194));
                BluetoothChat.this.sendMessage("a");
            }
        });
        this.mChatService = new BluetoothChatService(this, this.mHandler);
        this.mOutStringBuffer = new StringBuffer("");
    }

    public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        Log.d("BluetoothChat", "onActivityResult " + paramInt2);
        switch (paramInt1)
        {
            default:
            case 1:
                do
                    return;
                while (paramInt2 != -1);
                String str = paramIntent.getExtras().getString(MainActivity.macAddress);
                Log.d("BluetoothChat", "address " + str);
                BluetoothDevice localBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(str);
                Log.d("BluetoothChat", "device " + localBluetoothDevice);
                this.mChatService.connect(localBluetoothDevice);
                return;
            case 2:
        }
        if (paramInt2 == -1)
        {
            setupChat();
            return;
        }
        Log.d("BluetoothChat", "BT not enabled");
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        requestWindowFeature(7);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(7, 2130903040);
        *//*this.mTitle = ((TextView)findViewById(2131165184));
        this.mTitle.setText(2130968576);
        this.mTitle = ((TextView)findViewById(2131165185));*//*
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter == null)
        {
            Toast.makeText(this, "Bluetooth is not available", 1).show();
            finish();
        }
    }

    *//*public boolean onCreateOptionsMenu(Menu paramMenu)
    {
        getMenuInflater().inflate(2131099649, paramMenu);
        return true;
    }*//*

    public void onDestroy()
    {
        super.onDestroy();
        if (this.mChatService != null)
            this.mChatService.stop();
        Log.e("BluetoothChat", "--- ON DESTROY ---");
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
        switch (paramMenuItem.getItemId())
        {
            default:
                return false;
            case 2131165197:
                startActivityForResult(new Intent(this, MainActivity.class), 1);
                return true;
            case 2131165198:
        }
        ensureDiscoverable();
        return true;
    }

    public void onPause()
    {
            super.onPause();
            Log.e("BluetoothChat", "- ON PAUSE -");
            return;

    }

    public void onResume() {
            super.onResume();
            Log.e("BluetoothChat", "+ ON RESUME +");
            if ((this.mChatService != null) && (this.mChatService.getState() == 0))
                this.mChatService.start();
            return;
    }
    public void onStart()
    {
        super.onStart();
        Log.e("BluetoothChat", "++ ON START ++");
        if (!this.mBluetoothAdapter.isEnabled())
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 2);
        while (this.mChatService != null)
            return;
        setupChat();
    }

    public void onStop()
    {
        super.onStop();
        Log.e("BluetoothChat", "-- ON STOP --");
    }*/
}
