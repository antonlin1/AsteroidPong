package org.mamn01.pong.controller.num;

import android.util.Log;

import org.mamn01.pong.controller.num.filters.FFTLowpass;

import java.util.Arrays;

/**
 * Created by hampusballdin on 2016-04-12.
 */
public class AccelerationV2 implements SamplerV2.SampleGatheredCallback{

		/**
		 * The current acceleration value ...
		 */
		private double value = 0.0;

		/**
		 * The previous acceleration value ...
		 */
		private double prevValue = 0.0;

		/**
		 * Number derivative samples to save for history
		 */
		private static final int N_DERIVATIVE = 8;

		/**
		 * Current index of the derivative
		 */
		private int nDerivative = 0;

		/**
		 * The N_DERIVATIVE last samples gathered of the rate of change of acceleration.
		 */
		private double[] derivatives = new double[N_DERIVATIVE];

		/**
		 * From Matlab:
		 * 	max(a) / max(da) over relatively long intervall with impulses ...
		 */
		private static final double MAGIC_SCALE = 0.024633466692420;

		/*
		 * Limit for checking deacceleration ...
		 */
		private static final double AVERAGE_LIMIT = 0.05;

		private SamplerV2 sampler;
		private FFTLowpass fftLowpass;

		public AccelerationV2(SamplerV2 sampler) {
				this.sampler = sampler;
				fftLowpass = new FFTLowpass(sampler.getNbrSamples());
				Arrays.fill(derivatives, 0.0);
		}

		@Override
		public void onSampleGathered(double dt, double sample) {
				fftLowpass.filter(sampler.getSamples());
				value = fftLowpass.average(fftLowpass.getResult());

				if(dt > 0 && prevValue != 0.0) {
						derivatives[nDerivative] = MAGIC_SCALE * (value - prevValue) / dt;
						nDerivative = (nDerivative + 1) % N_DERIVATIVE;
				}
				prevValue = value;
				value = isZero() ? 0.0 : value;
				//Log.i("Accleration", dt + ":" + value);
		}

		public double getValue() {
				return value;
		}

		public boolean isZero() {
				double sum = 0.0;
				for(int i = 0; i < N_DERIVATIVE; i++) {
						sum += Math.abs(derivatives[i]);
				}
				return sum < N_DERIVATIVE * AVERAGE_LIMIT;
		}

}
