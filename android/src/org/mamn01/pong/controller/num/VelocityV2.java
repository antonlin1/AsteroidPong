package org.mamn01.pong.controller.num;

/**
 * Created by hampusballdin on 2016-04-12.
 */
public class VelocityV2 {
		/**
		 * AccelerationV2 which drives velocity
		 */
		private AccelerationV2 acceleration;

		/**
		 * Current velocity
		 */
		double value = 0.0;

		/**
		 * Allow crossing the zero if acceleration is large enough.
		 * For example if phone is rapidly moving back and forth.
		 */
		private static final double RAPID_REVERSE_ACCELERATION_THRESHOLD = 1.5;

		/**
		 * The latest value of acceleration as seen by velocity (filtered and trimmed)
		 */
		double accelerationValue = 0.0;

		public VelocityV2(AccelerationV2 acceleration) {
				this.acceleration = acceleration;
		}

		double zeroTrim = 0.0;

		public void onAccelerationUpdate(double dt) {
				accelerationValue = acceleration.getValue();
				double dv = accelerationValue * dt;
				zeroTrim = (dv == 0) ? 0 : zeroTrim + dv;
				trim(dt);
		}

		private boolean isLastZero = true;
		private boolean isLastZero2 = true;

		private boolean isLastPositive = false;
		private boolean isLastPositive2 = false;

		/**
		 * Limit for possible peak value
		 */
		private static final double PEAK_THRESHOLD = 1.0;

		/**
		 * S
		 */
		private static final double PEAK_DOMINANCE_SCALE = 1.7;
		private double minVal = 0.0;
		private double maxVal = 0.0;
		private double maxPeak = 0.0;
		private double minPeak = 0.0;

		private double sumMin = 0.0;
		private double sumMax = 0.0;
		private double minPeakSum = 0.0;
		private double maxPeakSum = 0.0;

		private boolean isPeakDominated = false;

		public void trim(double dt) {
				/**
				 * LOCATE PEAKS AND SUM AREA UNDER PEAKS
				 */
				if (accelerationValue != 0) {
						//sum = 0;
						if (accelerationValue > maxVal) {
								maxVal = accelerationValue;
						} else if (accelerationValue < minVal) {
								minVal = accelerationValue;
						}
				}

				if (accelerationValue <= 0 && isLastPositive2 && !isLastZero2) {
						if (maxVal > PEAK_THRESHOLD) {
								maxPeak = maxVal;
								maxPeakSum = sumMax;
						}
						maxVal = 0.0;
						sumMax = 0.0;
				} else if (accelerationValue >= 0 && !isLastPositive2 && !isLastZero2) {
						if (Math.abs(minVal) > PEAK_THRESHOLD) {
								minPeak = minVal;
								minPeakSum = sumMin;
						}
						minVal = 0.0;
						sumMin = 0.0;
				}

				if (accelerationValue < 0) {
						isLastPositive2 = false;
						isLastZero2 = false;
						sumMin += accelerationValue * dt;
				} else if (accelerationValue > 0) {
						isLastPositive2 = true;
						isLastZero2 = false;
						sumMax +=  accelerationValue * dt;
				}else {
						isLastZero2 = true;
				}

				/**
				 * DO ACTUAL TRIMMING
				 *
				 *
				 */
				if (zeroTrim == 0) { // Zero VelocityV2
						isLastZero = true;
						isPeakDominated = false;
						value = 0.0;
				} else if (zeroTrim > 0) { // Positive VelocityV2

						if (isLastZero) { // Last time it was zero velocity
								value = zeroTrim;
								isLastPositive = true;
						} else if(isLastPositive) { // Last time it was positive velocity
								value = zeroTrim;
						} else  { // Last time it was negative velocity
								value = 0.0;
						}


						/**
						 *  Back and forth motion should produce
						 *
						 *  1 small peak ( up / down ) ,
						 *  1 large peak ( twice area down / up),
						 *  1 small peak ( up / down ) ,
						 *
						 *  Thus, if an area is sufficiently larger (ideally 2.0)
						 *  in opposite direction we conclude that we have back
						 *  and forth motion.
						 */
						if (Math.abs(sumMax) > Math.abs(minPeakSum) * PEAK_DOMINANCE_SCALE || isPeakDominated) {
								isLastPositive = true;
								isPeakDominated = true;
								value = zeroTrim;
						}
						isLastZero = false;
				} else { // Negative VelocityV2
						if (isLastZero) {
								value = zeroTrim;
								isPeakDominated = false;
								isLastPositive = false;
						} else if (!isLastPositive) {
								value = zeroTrim;
						} else {
								value = 0.0;
						}

						if (Math.abs(sumMin) > maxPeakSum * PEAK_DOMINANCE_SCALE || isPeakDominated) {
								isLastPositive = false;
								isPeakDominated = true;
								value = zeroTrim;
						}
						isLastZero = false;
				}
		}

		public double getValue() {
				return 5 * value;
		}

		public void reset() {
				value = 0;
				zeroTrim = 0;

				isLastZero = true;
				isLastZero2 = true;

				isLastPositive = false;
				isLastPositive2 = false;
		}
}
