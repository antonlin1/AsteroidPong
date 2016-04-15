package com.mygdx.game;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.view.MyGdxGame;

import org.mamn01.pong.controller.num.AccelerationConverter;
import org.mamn01.pong.controller.num.Converter;

import java.util.Arrays;


public class AndroidLauncher extends AndroidApplication implements SensorEventListener {
		private SensorManager mSensorManager;

		private float[] mGData = new float[3];

		private Converter accelerationConverter;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

				//hide android nav-buttons
				config.useImmersiveMode = true;

				mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

				mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(
						Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);


				MyGdxGame game = new MyGdxGame();
				initialize(game, config);


				accelerationConverter = new Converter(game);

		}


		@Override
		public void onSensorChanged(SensorEvent event) {
				float[] data = event.values;
				// Perform LP-Filtering

				//final float alpha = 0.025f;

				//mGData = lowPass(data, mGData);

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

					//	System.out.println("mGData: " + Arrays.toString(mGData));

						final float ALPHA = 1.0f;
						mGData = lowPass(data, mGData, ALPHA);

						//	if (!accelerationConverter.getBiasedDetermined()) {
						//			accelerationConverter.calibrateErrors(mGData, event.timestamp);
						//	} else {
						accelerationConverter.convert(mGData, event.timestamp);
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

				mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(
						Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

		}

		@Override
		protected void onPause() {
				super.onPause();
				Log.i(AndroidLauncher.class.getName(), "onPause");

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
