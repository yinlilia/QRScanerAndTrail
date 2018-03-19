package com.samonxu.qrcode.demo.Service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by yinlili on 2017/3/25.
 */

public class BluetoothService extends Service {

    private static final boolean D = true;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final String NAME = "BluetoothChat";
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;;
    private static final String TAG = "BluetoothChatService";
    //private AcceptThread mAcceptThread;
    private final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    //private BluetoothChatService.ConnectThread mConnectThread;
    //private ConnectedThread mConnectedThread;
    private int mState = 0;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final String OPEN="B";
    private static final String CLOSE="b";
    public boolean isConnect=false;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;

    @Override
    public void onCreate() {
        super.onCreate();
        initBluetooth();
    }

    private void initBluetooth() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()) {
            case "CONNECT":
                //final String macAddress = intent.getStringExtra("macAddress");
                final String macAddress="00:0D:18:00:00:3E";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connect(macAddress);
                    }
                }).start();
                /*BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice("00:0D:18:00:00:1E");
                 Thread thread=new ConnectThread(bluetoothDevice);
                thread.start();*/
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void connect(String address) {
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        if (bluetoothDevice != null) {
            try {
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord (MY_UUID_INSECURE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.connect();
                isConnect=true;
                sendBroadcast("CONNECTED_SUCCESS");
                outputStream=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("",e.getMessage());
                try {
                    Log.e("","trying fallback...");

                    bluetoothSocket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
                    bluetoothSocket.connect();

                    Log.e("","Connected");
                }
                catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }
        }
    }
    private void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }
    public void open(){
        if(bluetoothSocket!=null&&bluetoothSocket.isConnected()){
            try{
                outputStream.write(OPEN.getBytes());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void close(){
        if(bluetoothSocket!=null&&bluetoothSocket.isConnected()){
            try{
                outputStream.write(CLOSE.getBytes());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BluBinder();
    }
    public class BluBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    public class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice arg2) {
            Object localObject;
            this.mmDevice = arg2;
            try {
                BluetoothSocket localBluetoothSocket2 = arg2.createRfcommSocketToServiceRecord(BluetoothService.MY_UUID);
                BluetoothSocket localBluetoothSocket1 = localBluetoothSocket2;
                this.mmSocket = localBluetoothSocket1;
                return;
            } catch (IOException localIOException) {
                while (true) {
                    Log.e("BluetoothChatService", "create() failed", localIOException);
                    BluetoothSocket localBluetoothSocket1 = null;
                }
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
                return;
            } catch (IOException localIOException) {
                Log.e("BluetoothChatService", "close() of connect socket failed", localIOException);
            }
        }

        public void run() {
            Log.i("BluetoothChatService", "BEGIN mConnectThread");
            setName("ConnectThread");
            BluetoothService.this.mAdapter.cancelDiscovery();
            try {
                Log.i("BluetoothChatService", "mmSocket.connect() start");
                this.mmSocket.connect();
                Log.i("BluetoothChatService", "mmSocket.connect() end");
            } catch (IOException localIOException1) {
                synchronized (BluetoothService.this) {
                   // BluetoothService.this.mConnectThread = null;
                    //BluetoothChatService.this.connected(this.mmSocket, this.mmDevice);
                    return;
                    //localIOException1.printStackTrace();
                    //BluetoothService.this.connectionFailed();
                   /* try
                    {
                        this.mmSocket.close();
                        BluetoothService.start();
                        return;
                    }
                    catch (IOException localIOException2)
                    {
                        while (true)
                            Log.e("BluetoothChatService", "unable to close() socket during connection failure", localIOException2);
                    }*/
                }
            }
        }
    }
        public static void start()
        {
            try
            {
                Log.d("BluetoothChatService", "start");
            }
            finally
            {
            }
        }
    }


