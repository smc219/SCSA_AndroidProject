package edu.scsa.android.reviewbleapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Button scan;
    Button stop;
    Button conn;
    BluetoothAdapter ba;
    BluetoothDevice bd;
    BluetoothGatt bg;
    String etText;
    public final int BT_CHECK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.editText);
        scan = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        conn = findViewById(R.id.connect);
        ba = BluetoothAdapter.getDefaultAdapter();

        if (ba == null) {

            Toast.makeText(this, "블루투스 기기가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!ba.isEnabled()) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, BT_CHECK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BT_CHECK && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "블루투스 기능을 끄고는 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ba.stopLeScan(leScanCallback);
        if (bg != null) {
            bg.disconnect();
            bg.close();
        }
    }

    public void scanStart(View view) {
        etText = et.getText().toString().trim();
        ba.startLeScan(leScanCallback);
        Toast.makeText(this, etText, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "탐색을 시작합니다.", Toast.LENGTH_SHORT).show();

    }

    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("INFO","test");
            Log.e("INFO", "address : " + device.getAddress());
            if (etText.equals(device.getAddress())) {
                bd = device;
                Toast.makeText(MainActivity.this, "Find device", Toast.LENGTH_SHORT).show();
                ba.stopLeScan(leScanCallback);
            }

        }
    };

    public void scanStop(View view) {
        ba.stopLeScan(leScanCallback);
        Toast.makeText(this, "탐색을 중지합니다.", Toast.LENGTH_SHORT).show();
    }

    public void connect(View view) {
       bg = bd.connectGatt(this, false, bluetoothGattCallback);
    }
    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Toast.makeText(MainActivity.this, "연결 성공", Toast.LENGTH_SHORT).show();
                bg.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                Toast.makeText(MainActivity.this, "연결해제", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            gatt.getService()
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };
}
