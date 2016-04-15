package org.mamn01.pong.controller.num;

import org.mamn01.pong.controller.time.MeasureTime;

import java.util.ArrayList;

/**
 * Created by hampusballdin on 2016-03-31.
 */
public class Sampler {
		private MeasureTime measureTime;
		private int N = 1; // Number of samples to gather
		private int n = 0; // Current entry
		private int nbrSamplesGathered = 0; // Total number of samples gathered
		private float[] samples = new float[N]; // All Collected samples
		private boolean IS_INITIALIZED = false;

		private ArrayList<SampleGatheredCallback> callbacks =
				new ArrayList<SampleGatheredCallback>();


		public Sampler() {
				measureTime = new MeasureTime();
		}

		/**
		 * Must be called first
		 */
		public void init() {
				measureTime.onInvocation(0); // Start
				nbrSamplesGathered = 0;
				IS_INITIALIZED = true;
				samples = new float[N];
		}

		/**
		 * @param sample
		 * sample to gather
		 */
		public double gatherSample(double sample, long time) {
				if(!IS_INITIALIZED)
						throw new IllegalStateException("Must initialize before use!");
				double dt = measureTime.onInvocation((double)time);
				samples[n] = (float)sample;

				performCallBacks(dt, sample);
				n = (n + 1) % N;
				return dt;
		}

		public void performCallBacks(double dt, double sample) {
				for(SampleGatheredCallback c : callbacks)
						c.onSampleGathered(dt,sample);
		}
		public void registerSampleGatheredCallback(SampleGatheredCallback callback){
				callbacks.add(callback);
		}

		public boolean unRegisterSampleGatheredCallback(SampleGatheredCallback callback){
				return callbacks.remove(callback);
		}

		public static interface SampleGatheredCallback {
				/**
				 * @param dt, the time between this and the previous gathered sample
				 * @param sample, the value of the gathered sample
				 */
				void onSampleGathered(double dt, double sample);
		}

		public float[] getSamples() {
				return samples;
		}

		public double getElapsedTime() {
				return measureTime.getElapsedTime();
		}

		public double getSampleHistory(int k) {
				int si = (n - (N + k)) % N;
				if(si < 0)
						si += N;
				return samples[si];
		}
}
