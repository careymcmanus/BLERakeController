package com.southsalt.blerakecontroller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String              EXTRA_MESSAGE = "com.southsalt.blerakecontroller.MESSAGE";
    private final static String             TAG = MainActivity.class.getSimpleName();

    private ArrayList<BluetoothDevice>      mBleDevices;
    private LeDeviceListAdapter             mBleDeviceAdapter;
    private BluetoothAdapter                mBluetoothAdapter;
    private BluetoothLeScanner              mBleScanner;
    private BluetoothDevice                 mDevice;
    private Handler                         mHandler = new Handler();
    private boolean                         mScanning = false;
    private boolean                         mConnected = false;


    private ProgramViewModel mProgramViewModel;
    public static final int NEW_PROGRAM_ACTIVITY_REQUEST_CODE = 1;
    public static final int DEVICE_ADDRESS_REQUEST_CODE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final long SCAN_PERIOD = 10000;

    private int programCount = 0;
    private int currentProgramNo = 0;
    private int stateCount = 0;
    private int currentStateNo = 0;
    private List<ProgramData> progData;
    private TextView connVal, progVal, stateVal, speedVal, timeVal, gateVal, dirVal;

    private Buttons buttons;
    private RecyclerView deviceView;
    private RecyclerView.LayoutManager layoutManager;
    private AlertDialog deviceDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBleDevices = new ArrayList<BluetoothDevice>();
        mBleDeviceAdapter = new LeDeviceListAdapter(mBleDevices);

        setContentView(R.layout.controller_layout);
        initProgramData();
        initInfoText();
        updateInfoText();
        bluetoothInit();
        buttons = new Buttons(this);
    }

    public void updateInfoText(){
          connVal.setText(mConnected ? getText(R.string.text_true) : getText(R.string.text_false));
          progVal.setText(Integer.toString(currentProgramNo));
          stateVal.setText(Integer.toString(currentStateNo));
          if (progData != null) {
              MotorState currentState = progData.get(currentProgramNo).states.get(currentStateNo);
              int speed = currentState.getSpeed();
              int time = currentState.getTime();
              int gate = currentState.getGate();
              int dir = currentState.getDir();
              speedVal.setText(Integer.toString(speed));
              timeVal.setText(Integer.toString(time));
              gateVal.setText(Integer.toString(gate));
              dirVal.setText(Integer.toString(dir));
          }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                scanLeDevice(true);

                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_PROGRAM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Program program = new Program(programCount+1, data.getStringExtra(NewProgramActivity.EXTRA_REPLY));
            mProgramViewModel.insert(program);
        }
    }

    private void bluetoothInit(){
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
        mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (mBleScanner == null){
            Toast.makeText(this, "Could not get BLE Scanner", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void scanLeDevice(final boolean enable) {
        if (mBleScanner == null){
            mBleScanner =mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (enable) {
            deviceView.setVisibility(View.VISIBLE);
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBleScanner.stopScan(deviceCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBleScanner.startScan(deviceCallback);
        } else {
            mScanning = false;
            mBleScanner.stopScan(deviceCallback);
        }
        invalidateOptionsMenu();
    }

    private final ScanCallback deviceCallback = new ScanCallback(){
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            synchronized (this) {
                if (!mBleDevices.contains(result.getDevice())) {
                    mBleDevices.add(result.getDevice());
                    mBleDeviceAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void initProgramData(){
        mProgramViewModel = new ViewModelProvider(this).get(ProgramViewModel.class);
        mProgramViewModel.insert(new Program(0, "Main"));
        MotorState[] states = {new MotorState(0,0,100,100,0,0), new MotorState(1,0,100,100, 1,0)};
        mProgramViewModel.insert(states);
        mProgramViewModel.getProgramData().observe(this, new Observer<List<ProgramData>>() {
            @Override
            public void onChanged(@Nullable List<ProgramData> programData) {
                programCount = programData.size();
                stateCount = programData.get(currentProgramNo).states.size();
                progData = programData;
                updateInfoText();
            }
        });
        progData = mProgramViewModel.getProgramData().getValue();
    }

    public void initInfoText(){
        connVal = findViewById(R.id.connected_val_text);
        progVal = findViewById(R.id.program_text_val);
        stateVal = findViewById(R.id.current_state_val);
        speedVal = findViewById(R.id.speed_val_text);
        timeVal = findViewById(R.id.time_val_text);
        gateVal = findViewById(R.id.gate_val_text);
        dirVal = findViewById(R.id.direction_val_text);

        deviceView = findViewById(R.id.device_view);

        layoutManager = new LinearLayoutManager(this);
        deviceView.setLayoutManager(layoutManager);
        deviceView.setHasFixedSize(true);
        deviceView.setAdapter(mBleDeviceAdapter);
        deviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = deviceView.indexOfChild(v);
                mDevice = mBleDevices.get(pos);
                return;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }


}
