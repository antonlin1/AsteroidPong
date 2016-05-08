package com.mygdx.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.mygdx.game.SpeechRecognizer.ListenerActivity;
import com.mygdx.game.SpeechRecognizer.SpeechListener;
import com.mygdx.game.view.MyGdxGame;

import org.mamn01.pong.controller.num.AccelerationConverter;
import org.mamn01.pong.controller.num.Converter;
import org.mamn01.pong.controller.num.ConverterV2;

import java.util.Arrays;


public class AndroidLauncher extends ListenerActivity implements SensorEventListener{
    private SensorManager mSensorManager;
   // private SpeechListener sl;

    private float[] mGData = new float[3];
    private MyGdxGame game;

    //Network stuff
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiDirectBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private PeerHelper peerHelper;

    protected static boolean pause;

    private ConverterV2 accelerationConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        peerHelper = new PeerHelper(mManager, mChannel);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, peerHelper);

        mIntentFilter = new IntentFilter();

        // Launches speech recognition
        context = getApplicationContext();
        //SpeechListener.getInstance().setListener(this);
       // startListening();
        pause = false;


        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mIntentFilter.addAction(Intent.ACTION_SHUTDOWN);

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
                Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME);

        accelerationConverter = new ConverterV2();

        game = new MyGdxGame(accelerationConverter, peerHelper, mReceiver);
        initialize(game, config);

        System.out.println("DEVICE MAC: "+ getMacAddress(this));
        mReceiver.resetNetwork();

        peerHelper.discover();
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
            final float ALPHA = 1.0f;
            mGData = lowPass(data, mGData, ALPHA);
            accelerationConverter.convert(mGData, event.timestamp, game);
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
                Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        startListening();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(AndroidLauncher.class.getName(), "onPause");
        unregisterReceiver(mReceiver);

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);

        //Stops speech recognition
        stopListening();

    }

    @Override
    public void processVoiceCommands(String... voiceCommands) {

        for (String c : voiceCommands) {
            if (c.contains("start") || c.contains("play")) {
                pause = false;
                c = "";

            } else if (c.contains("stop") || c.contains("pause") || c.contains("paws")) {
                pause = true;
                c = "";

            }
            startListening();
        }
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
