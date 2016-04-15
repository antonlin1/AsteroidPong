package org.mamn01.pong.controller.time;

import org.mamn01.pong.controller.num.Units;

/**
 * Created by hampusballdin on 2016-03-31.
 */
public class MeasureTime {
		private double prevTime; // Time on last invocation
		private long invocationCount; // Number of invocations
		private double totalTime; // The accumulated time


		public void init() {
				prevTime = 0; // Time on last invocation
				invocationCount = 0; // Number of invocations
				totalTime = 0; //
		}

		/**
		 *	This method is to be invoced in each cycle
		 */
		public double onInvocation(double time) {
				invocationCount++;
				double dt = 0;
				if(invocationCount > 1) {
						dt = (time - prevTime) * Units.NANO;
						totalTime += dt;
				}

				prevTime = time;
				return dt;
		}


		/**
		 *	Get average time between invocation calls, in seconds
		 */
		public double getAverageDeltaT() {
				double avg = invocationCount > 0 ? totalTime / invocationCount : 0.0;
				return avg;
		}

		/**
		 *	Get average number of invocations per second.
		 */
		public double getAvgInvokePerSec() {
				double avg = getAverageDeltaT();
				if(avg > 0) {
						return 1.0 / avg;
				}
				return 0.0;
		}

		public double getElapsedTime() {
				return totalTime;
		}
}
