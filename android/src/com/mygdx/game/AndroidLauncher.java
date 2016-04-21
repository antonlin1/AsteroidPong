package com.mygdx.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Network.PeerHelper;
import com.mygdx.game.Network.WifiDirectBroadcastReceiver;
import com.mygdx.game.view.MyGdxGame;

import org.mamn01.pong.controller.num.AccelerationConverter;
import org.mamn01.pong.controller.num.Converter;

import java.util.Arrays;


public class AndroidLauncher extends AndroidApplication implements SensorEventListener {
    private SensorManager mSensorManager;


    private float[] mGData = new float[3];
    private MyGdxGame game;

    //Network stuff
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private PeerHelper peerHelper;

    private Converter accelerationConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        peerHelper = new PeerHelper(mManager,mChannel);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this, peerHelper, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //disable screen timeout
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //hide android nav-buttons
        config.useImmersiveMode = true;
        // First:
        //      7a:f8:82:ed:21:73
        // Second:
        //      7a:f8:82:ed:2a:88
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(
                Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);

        accelerationConverter = new Converter();

        game = new MyGdxGame(accelerationConverter, peerHelper);
        initialize(game, config);

        System.out.println("DEVICE MAC: "+ getMacAddress(this));
    }

    private String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] data = event.values;

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            //		TextView[] tmp = {xView, yView, zView};
            //		for (int i = 0; i < 3; i++) {
            //				setText(tmp[i], getNbrs(mGData[i], 10));
            //		}

            //		System.out.println("data: " + Arrays.toString(data));
            //		System.out.println("mGData: " + Arrays.toString(mGData));

				/*		if(!accelerationConverter.getBiasedAccelerationDetermined()) {
                                accelerationConverter.determineBiasAcceleration(data, event.timestamp);
						}else if(!accelerationConverter.getBiasVelocityDetermined()) {
								accelerationConverter.determineBiasVelocity(data, event.timestamp);
						}else {
								accelerationConverter.receiveData(data, event.timestamp);
						}*/

            final float ALPHA = 1.0f;
            mGData = lowPass(data, mGData, ALPHA);

            //	if (!accelerationConverter.getBiasedDetermined()) {
            //			accelerationConverter.calibrateErrors(mGData, event.timestamp);
            //	} else {
            accelerationConverter.convert(mGData, event.timestamp, game);
            //	}
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(AndroidLauncher.class.getName(), "onAccuracyChanged");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(AndroidLauncher.class.getName(), "onResume");
        registerReceiver(mReceiver, mIntentFilter);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(AndroidLauncher.class.getName(), "onPause");
        unregisterReceiver(mReceiver);

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);

    }

    protected float[] lowPass(float[] input, float[] output, final float ALPHA) {
        if (output == null)
            return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

}
