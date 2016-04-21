package org.mamn01.pong.controller.num;

import org.mamn01.pong.controller.num.filters.Filter;
import org.mamn01.pong.controller.num.filters.SimplestFilter;

/**
 * Created by hampusballdin on 2016-04-12.
 */
public class Velocity {
		private Acceleration acceleration;

		double v;
		double prev;
		public Velocity(Acceleration acceleration) {
				this.acceleration = acceleration;
		}


		public void onAccelerationUpdate(double dt) {
				double a = acceleration.getOutputHistory(1);
				double dv = a * dt;

				v += dv;
				v = (acceleration.isDeaccelerated())? 0 : v;

				if(Math.abs(v) < 0.00000000001) {
						v = 0.0;
				}
				prev = v;
		}
		public double getDisplayValue() {
				return getValue();//filter.getValue();
		}

		public double getValue() {
				//double display = Math.exp(20 * Math.abs(v)) - 1;
				//display = v < 0 ? display * -1 : display;
				return 10 * v;//display / 10;//filter.getValue();
		}

		public void reset() {
				v = 0;
		}
}
