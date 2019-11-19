package edu.scsa.android.testbleapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextView infoTv;
    EditText macEt;
    BluetoothAdapter blueAdapter;
    String devMac;
    BluetoothDevice blueDev;
    BluetoothGatt blueGatt;
    UUID UUID_KEY_SERV = UUID
            .fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    UUID UUID_KEY_DATA = UUID
            .fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = findViewById(R.id.infoTv);
        macEt = findViewById(R.id.macEt);
        blueAdapter = BluetoothAdapter.getDefaultAdapter();

        if (blueAdapter == null) {
            Toast.makeText(this, "No Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 화면이 뜨는 순간에 블루투스 기능 활성화 여부 확인
        if(!blueAdapter.isEnabled()) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, 0);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        blueAdapter.stopLeScan(leScanCallback);
        if (blueGatt != null) {
            blueGatt.disconnect();
            blueGatt.close();
        }
    }

    public void register(View view) {
        devMac = macEt.getText().toString().trim();
        showStatus("장비등록 : " + devMac);

    }

    public void scanStart(View view) {
        blueAdapter.startLeScan(leScanCallback); // BLE는 모든 메커니즘이 콜백.
        showStatus("start Scan...");

    }

    public void scanStop(View view) {
        blueAdapter.stopLeScan(leScanCallback);
        showStatus("stop Scan...");
    }



    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.e("INFO", "-----------" + device.getAddress());
            if (devMac.equals(device.getAddress())){
                blueDev = device;
                blueAdapter.stopLeScan(leScanCallback);
                showStatus("연결한 센서 발견 " + device.getAddress());

            }
        }
    };

    public void connectSensor(View view) {
        if(blueGatt == null) Toast.makeText(this, "Null!", Toast.LENGTH_SHORT).show();
       blueGatt = blueDev.connectGatt(this, false, gattCallback); // 첫번째 것을 가장 많이 쓴다. autoconnect는 만약 연결이 끊기면 자동으로 재연결. 보통 false

    }

    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("INFO", status + "connected!");

                Log.i("INFO", "현재 돌아가는 스레드 : " + Thread.currentThread().getName());
                showStatus("센서와 연결이 되었습니다...");
                blueGatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("INFO", "현재 돌아가는 스레드 : " + Thread.currentThread().getName());
                showStatus("센서와 연결이 끊겼습니다...");

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            enableSensor();
//            List<BluetoothGattService> sevList = gatt.getServices();
//            for(BluetoothGattService sev : sevList) {
//            showStatus("======" + sev.getUuid());
//            for(BluetoothGattCharacteristic ch : sev.getCharacteristics()) {
//                showStatus("ch : " + ch.getUuid());
//            }
//        }
    }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic ch) {
            super.onCharacteristicChanged(gatt, ch);
            Log.e("INFO", "onCharateristicChanged call...");
            changeData(ch);

        }
    };

    private void changeData(BluetoothGattCharacteristic ch) {
        // 넘어온 ch를 이용하여 value값을 추출하고 변환한 후
        // 값이 01이면 Mediaplayer 서비스를 스타트하고 02이면 서비스를 스탑한다.
        byte[] rawValue =ch.getValue();
        final String value =Conversion.BytetohexString(rawValue, rawValue.length);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int v = Integer.parseInt(value);
                Intent i =new Intent(MainActivity.this, MyService.class);
                Log.i("INFO", v+"value");
                if(v == 2) {
                    stopService(i);
                    Log.i("INFO", v+"in");
                }
                else if (v == 1) {
                    startService(i);
                    Log.i("INFO", v+"in");
                }

            }
        });




    }

    private void enableSensor() {
        // key event 관련 기능을 활성화시키는 코드 구현
        BluetoothGattCharacteristic c = null;
        c = blueGatt.getService(UUID_KEY_SERV).getCharacteristic(UUID_KEY_DATA);
        blueGatt.setCharacteristicNotification(c, true);

        BluetoothGattDescriptor descriptor = c.getDescriptor(UUID
                .fromString("00002902-0000-1000-8000-00805f9b34fb")); // cccd의 키값은 항상 동일하다.

        if (descriptor != null) {
            byte[] val = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
            descriptor.setValue(val);
            blueGatt.writeDescriptor(descriptor);
        }
    }

    private void showStatus(String data) {
        final String temp = data;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoTv.append("\n" + temp);
            }
        });
    }
}
