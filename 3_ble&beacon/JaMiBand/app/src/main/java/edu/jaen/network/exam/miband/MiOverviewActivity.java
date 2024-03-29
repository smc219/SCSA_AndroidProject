package edu.jaen.network.exam.miband;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.jaen.network.exam.miband.R;
import edu.jaen.network.exam.miband.debugging.L;
import edu.jaen.network.exam.miband.model.Battery;
import edu.jaen.network.exam.miband.model.LeParams;
import edu.jaen.network.exam.miband.model.MiBand;
import edu.jaen.network.exam.miband.view.ColorPickerDialog;

public class MiOverviewActivity extends Activity implements Observer {

	private static final UUID UUID_MILI_SERVICE = UUID
			.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CHAR_pair = UUID
			.fromString("0000ff0f-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CHAR_CONTROL_POINT = UUID
			.fromString("0000ff05-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CHAR_REALTIME_STEPS = UUID
			.fromString("0000ff06-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CHAR_ACTIVITY = UUID
			.fromString("0000ff07-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CHAR_LE_PARAMS = UUID
			.fromString("0000ff09-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CHAR_DEVICE_NAME = UUID
			.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CHAR_BATTERY = UUID
			.fromString("0000ff0c-0000-1000-8000-00805f9b34fb");

	// BLUETOOTH
	private String mDeviceAddress;
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mBluetoothMi;
	private BluetoothGatt mGatt;


	private MiBand mMiBand = new MiBand();

	// UI
	private TextView mTVSteps;
	private TextView mTVBatteryLevel;
	private ProgressBar mLoading;
	private TextView mTvDevInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDeviceAddress = getIntent().getStringExtra("address");

		mMiBand.addObserver(this);
		mMiBand.mBTAddress = mDeviceAddress;

		setContentView(R.layout.activity_mi_overview);

		mTVSteps = (TextView) findViewById(R.id.text_steps);
		mTVBatteryLevel = (TextView) findViewById(R.id.text_battery_level);
		mLoading = (ProgressBar) findViewById(R.id.laoding);
		mTvDevInfo = (TextView) findViewById(R.id.text_band_info);

		mBluetoothManager = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE));
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		mBluetoothMi = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
		
		mTvDevInfo.setText("장비정보 : "+mBluetoothMi.getName()+" Mac : "+mBluetoothMi.toString());
		

	}

	@Override
	public void onResume() {
		super.onResume();
		mGatt = mBluetoothMi.connectGatt(this, false, mGattCallback);
		mGatt.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		mGatt.disconnect();
		mGatt.close();
		mGatt = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_overview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_leparams:
			if (mMiBand.mLeParams == null) {
				L.toast(this, "No LE params received yet");
				return true;
			}
			Intent intent = new Intent(getApplicationContext(),
					MiLeParamsActivity.class);
			intent.putExtra("params", mMiBand.mLeParams);
			startActivity(intent);
			break;
		case R.id.action_ledcolor:
			new ColorPickerDialog(this, null, "", Color.BLUE, Color.RED).show();
			break;
		}
		return true;
	}

	private void pair() {

		BluetoothGattCharacteristic chrt = getMiliService().getCharacteristic(
				UUID_CHAR_pair);

		chrt.setValue(new byte[] { 2 });

		mGatt.writeCharacteristic(chrt);
		System.out.println("pair sent");
		
	}

	private void request(UUID what) {
		mGatt.readCharacteristic(getMiliService().getCharacteristic(what));
	}

	private void setColor(byte r, byte g, byte b) {
		BluetoothGattCharacteristic theme = getMiliService().getCharacteristic(
				UUID_CHAR_CONTROL_POINT);
		theme.setValue(new byte[] { 14, r, g, b, 0 });
		mGatt.writeCharacteristic(theme);
	}

	private BluetoothGattService getMiliService() {
		return mGatt.getService(UUID_MILI_SERVICE);

	}

	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		int state = 0;

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				pair();
			}

		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				gatt.discoverServices();
			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// this is called tight after pair()
			// setColor((byte)127, (byte)0, (byte)0);
			request(UUID_CHAR_REALTIME_STEPS); // start with steps
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			
			byte[] b = characteristic.getValue();
			Log.i(characteristic.getUuid().toString(), "state: " + state
					+ " value:" + Arrays.toString(b));

			// handle value
			if (characteristic.getUuid().equals(UUID_CHAR_REALTIME_STEPS))
				mMiBand.setSteps(0xff & b[0] | (0xff & b[1]) << 8);
			else if (characteristic.getUuid().equals(UUID_CHAR_BATTERY)) {
				Battery battery = Battery.fromByte(b);
				mMiBand.setBattery(battery);
			} else if (characteristic.getUuid().equals(UUID_CHAR_DEVICE_NAME)) {
				mMiBand.setName(new String(b));
			} else if (characteristic.getUuid().equals(UUID_CHAR_LE_PARAMS)) {
				LeParams params = LeParams.fromByte(b);
				mMiBand.setLeParams(params);
			}

			// proceed with state machine (called in the beginning)
			state++;
			switch (state) {
			case 0:
				request(UUID_CHAR_REALTIME_STEPS);
				break;
			case 1:
				request(UUID_CHAR_BATTERY);
				break;
			case 2:
				request(UUID_CHAR_DEVICE_NAME);
				break;
			case 3:
				request(UUID_CHAR_LE_PARAMS);
				break;
			}
		}
	};

	@Override
	public void update(Observable observable, Object data) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mLoading.setVisibility(View.GONE);
				((LinearLayout) findViewById(R.id.textHolder))
						.setVisibility(View.VISIBLE);
				mTVSteps.setText(mMiBand.mSteps + "");
				if (mMiBand.mBattery != null)
					mTVBatteryLevel.setText(mMiBand.mBattery.mBatteryLevel
							+ "%");
			}
		});
	}

}
