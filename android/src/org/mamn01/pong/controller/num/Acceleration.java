package org.mamn01.pong.controller.num;

import org.mamn01.pong.controller.num.filters.Filter;
import org.mamn01.pong.controller.num.filters.LowPass;
import org.mamn01.pong.controller.num.filters.SimplestFilter;

/**
 * Created by hampusballdin on 2016-04-12.
 */
public class Acceleration implements Sampler.SampleGatheredCallback{
		private double value;
		private Filter filter;
		private Filter rateOfChange;
		private Sampler sampler;

		private static final int ACCELERATION_NUMBER_INPUT = 55;
		private static final int ACCELERATION_ROC_NUMBER_INPUT = 30;

		public Acceleration(Sampler sampler) {
				// 20 = XN, ALPHA = 0.025f
				this.sampler = sampler;
				filter = new LowPass(ACCELERATION_NUMBER_INPUT, ACCELERATION_NUMBER_INPUT, 0.025f);
				rateOfChange = new SimplestFilter(ACCELERATION_ROC_NUMBER_INPUT, 0);

		}

		public double getValue() {
				return filter.getValue();
		}

		public double getRateOfChange() {
				return rateOfChange.getValue();
		}

		public double getAbsoluteAverageOutputRateOfChange() {
				return rateOfChange.getAbsoluteAverageOutput();
		}

		public double getAbsoluteAverageInputRateOfChange() {
				return rateOfChange.getAbsoluteAverageInput();
		}

		@Override
		public void onSampleGathered(double dt, double sample) {
				this.value = sample;

				filter.update(sample);
				filter.update(filter.getValue());

				if(dt > 0)
						rateOfChange.update( ((getInputHistory(1) - getInputHistory(2)) / dt) / 50);

				if(isDeaccelerated()) {
						resetAcceleration();
				}
				//System.out.println(filter.getAbsoluteAverageOutput());
		}

		public double getInputHistory(int k) {
				return filter.getInputHistory(k);
		}

		public double getOutputHistory(int k) {
				return filter.getOutputHistory(k);
		}

		public boolean isDeaccelerated() {
				boolean deaccelerated = true;
				double threshold = 1.2;

				for(int i = 2; i < 15 + 2; i++) {

						if(Math.abs(rateOfChange.getInputHistory(i)) > threshold) {
								deaccelerated = false;
								break;
						}

				}
				return deaccelerated;
		}

		public void resetAcceleration() {
				filter.resetValues();
		}

}
