package org.mamn01.pong.controller.num;


import com.badlogic.gdx.Gdx;
import com.mygdx.game.view.MyGdxGame;

import org.mamn01.pong.controller.time.Units;
import java.util.Arrays;

/**
 * Created by hampusballdin on 2016-04-01.
 */
 public class AccelerationConverter {
		double prevTime = 0.0;

		private double Rs = 0;

		private int vN = 500; // MA Velocity Size
		private int aN = 75; // MA Acceleration Size
		private int daN = 30; // MA Acceleration Size

		//	private int rsN = 20; // MA Acceleration Rate Of Change Size

		//private double[] Rs_MA = new double[rsN];


		private int vn = 0; // Current entries
		private int an = 0; // Current entries
		private int dan = 0;

		//private int rsn = 0; // Current entries

		double[][] da_ma = new double[daN][3]; // Total current velocity MA
		double[] da = new double[3]; // Total current velocity MA

		double[][] a_ma = new double[aN][3]; // Total current velocity MA
		double[][] aWeights = new double[aN][3]; // Total current velocity MA

		double[] a = new double[3]; // Total current velocity MA

		double[] vdraw = new double[3]; // Total current velocity MA
		double[] v = new double[3]; // Total current velocity

		double[] dv = new double[3]; // Last change in current velocity
		double[] p = new double[3]; // Position relative to start, i.e. total movement
		double[] dp = new double[3]; // Last change in space

		double[] unbiasedAcceleration = new double[3];

		private ErrorCalibrator errorCalibrator = new ErrorCalibrator();

		double[] avgErrorSecondAcceleration = new double[3];
		double[] avgErrorSecondVelocity = new double[3];

		private boolean IS_INITIALIZED = false;

		private MyGdxGame game;
		public AccelerationConverter(MyGdxGame game) {
				this.game = game;
		}

		public void convert(float[] acceleration, long timestamp) {
				double nowTime = (double)timestamp;
				double nowTimeSec = nowTime * Units.NANO;

				if(!IS_INITIALIZED) {
						System.out.println("Initializing ... ");
						prevTime = nowTimeSec;

						vdraw = new double[3]; // Total current velocity
						a_ma = new double[aN][3]; // Total current velocity
						aWeights = new double[aN][3];

						v = new double[3];
						a = new double[3];
						p = new double[3];

						dv = new double[3]; // Last change in current velocity
						dp = new double[3];

						an = 0;
						vn = 0;
						IS_INITIALIZED = true;
						return;
				}

				double dt = nowTimeSec - prevTime;

				unbiasedAcceleration = new double[] {
						acceleration[0] - avgErrorSecondAcceleration[0] * dt,
						acceleration[1] - avgErrorSecondAcceleration[1] * dt,
						acceleration[2] - avgErrorSecondAcceleration[2] * dt
				};

				for(int j = 0; j < 3; j++) {
						double sum = 0;
						for (int i = 0; i < aN; i++) {
								int si = (an - (aN + i)) % aN;
								if(si < 0)
										si += aN;
								aWeights[si][j] = 1 / (double)aN;
								sum += aWeights[si][j];
						}

						for (int i = 0; i < aN; i++) {
								aWeights[i][j] /= sum;
						}
				}

				for(int i = 0; i < 3; i++){
						a_ma[an][i] = unbiasedAcceleration[i]; // - bias_velocity[i];
				}

				for (int j = 0; j < 3; j++) {
						a[j] = 0;
						for(int i = 0; i < aN; i++) {
								int si = (an - (aN + i)) % aN;
								if(si < 0)
										si += aN;
								a[j] += a_ma[i][j] * aWeights[si][j];
						}

						//	if(Math.abs(a[j]) < 0.15)
						//			a[j] = 0;
				}

				for(int i = 0; i < 3; i++){
						// an = 0, aN = 50
						// -51 % 50 = -1 => 49
						int si = (an - (aN + 1)) % aN;
						if(si < 0)
								si += aN;

						if(dt > 0)
								da_ma[dan][i] = (a_ma[an][i] -  a_ma[si][i])/ dt; // - bias_velocity[i];
				}

				double scale = 100;
				for (int j = 0; j < 3; j++) {
						da[j] = 0;
						for (int i = 0; i < daN; i++) {
								da[j] += da_ma[i][j] / (double)(daN * scale);
						}
				}

				double[] dvData = {
						dt * a[0],
						dt * a[1],
						dt * a[2]
				};

				boolean isDeaccelerated = true;
				int deAccN = 15;
				for(int i = 0; i < deAccN; i++) {
						int si = (dan - (daN + i)) % daN;
						if(si < 0)
								si += daN;

						if(Math.abs(da_ma[si][0]) > 150) {
								isDeaccelerated = false;
								break;
						}

						//	System.out.println("v[0]: " + v[0]);
				}

			/*	int deAccN = 30;
				for(int i = 0; i < deAccN; i++) {
						int si = (an - (aN + i)) % aN;
						if(si < 0)
								si += aN;

						if(Math.abs(a_ma[si][0]) > 0.15) {
								isDeaccelerated = false;
								break;
						}
				}*/

				dv = lowPass(dvData, dv, 1.0);

				double K = 0.005;
				double Ti = 5;
				double Td = 1 / K;

				for(int i = 0; i < 3; i++){
						v[i] += dv[i] - K * v[i];//(dv[i] - K * (v[i] + a[i]*Td)); //+ (1/Ti) * p[i];
						v[i] = isDeaccelerated ? 0 : v[i];//a[i] != 0 ? v[i] : 0;
				}


				double[] dpData = new double[3];

				for (int j = 0; j < 3; j++) {
						dpData[j] = (v[j] - avgErrorSecondVelocity[j] * dt ) * dt;
				}

				dp = lowPass(dpData, dp, 1);

				for (int j = 0; j < 3; j++) {
						p[j] += dp[j];
				}

				//
			/*	System.out.println("dt: " + dt);
				System.out.println("Acceleration: " + Arrays.toString(unbiasedAcceleration));
				System.out.println("Velocity: " + Arrays.toString(v));
				System.out.println("avgErrorSecondAcceleration[0] * dt: " + avgErrorSecondAcceleration[0] * dt);
				System.out.println("p: " + Arrays.toString(p));
			*/

				float WIDTH = Gdx.graphics.getWidth();
				float HEIGHT = Gdx.graphics.getHeight();

				//TODO: if X pos less than 0. Sett p[0] to zero. If X pos greater than WIDTH set p[0] to WIDTH.
				// this is already done in InputController, but the logical X pos in the Accelerator is unchanged.
				// This must be fixed.
				float xPos = (WIDTH / 2 + (float) p[0] * 20000 / 2 );

//				p[0] = (xPos > WIDTH - GameState.PaddleConstant.LENGTH.value) ?
//					WIDTH - GameState.PaddleConstant.LENGTH.value : xPos;
//
//				p[0] = (xPos < 0) ? 0 : xPos;

				game.getInput().movePaddleToAbsPos(xPos/*% WIDTH*/);
				//paddles[0].setX((WIDTH / 2 + (float) p[0] * 10000 / 2 )% WIDTH);

				vn = (vn + 1) % vN;
				an = (an + 1) % aN;
				dan = (dan + 1) % daN;
				//		rsn = (rsn + 1) % rsN;
				prevTime = nowTimeSec;
		}

		public void calibrateErrors(float[] acceleration, long timestamp) {
				errorCalibrator.calibrateErrors(acceleration, timestamp);
		}

		private class ErrorCalibrator {
				private double startCalibrate;
				private double endCalibrate;

				private double[] accelerationAccumulator = new double[3];
				private double[] velocityAccumulator = new double[3];

				private boolean IS_BIAS_DETERMINED = false;

				private static final int BIAS_COUNT_LIMIT = 1000;
				private long invocationCount = 0;

				private boolean IS_CALIBRATING = false;

				public void calibrateErrors(float[] acceleration, long timestamp) {
						float nowTime = (float)timestamp;
						float nowTimeSec = nowTime * (float) Units.NANO;


						if(!IS_CALIBRATING && !IS_BIAS_DETERMINED) {
								System.out.println("Start Calibrate");

								invocationCount = 0;
								prevTime = 0.0;
								startCalibrate = nowTimeSec;

								velocityAccumulator = new double[3];
								accelerationAccumulator = new double[3];
								IS_CALIBRATING = true;
								return;
						}

						convert(acceleration, timestamp);

						for(int i = 0; i < 3; i++) {
								velocityAccumulator[i] += v[i];
								accelerationAccumulator[i] += a[i];
						}

						endCalibrate = nowTimeSec;
						double totalTime = endCalibrate - startCalibrate; // Time Calib. Seconds.

						for(int i = 0; i < 3; i++){
								avgErrorSecondVelocity[i] = velocityAccumulator[i] / totalTime;
								avgErrorSecondAcceleration[i] = accelerationAccumulator[i]/ totalTime;
						}

						invocationCount++;

						if (invocationCount >= BIAS_COUNT_LIMIT || totalTime >= 5) {
								System.out.println("totalTime = " + totalTime);
								System.out.println("avgErrorSecondVelocity: " + Arrays.toString(avgErrorSecondVelocity));
								System.out.println("avgErrorSecondAcceleration: " + Arrays.toString(avgErrorSecondAcceleration));

								invocationCount = 0;
								IS_BIAS_DETERMINED = true;
								IS_CALIBRATING = false;
								IS_INITIALIZED = false;
								prevTime = 0.0;
						}
				}

				public boolean getBiasedDetermined(){
						return IS_BIAS_DETERMINED;
				}

				public void setBiasedDetermined(boolean biasDetermined){
						this.IS_BIAS_DETERMINED = biasDetermined;
				}
		}

		public double[] getAccelerationRateOfChange() {
				return da;
		}

		public double[] getAcceleration() {
				return a;
		}

		public double getLastTime() {
				return prevTime;
		}

		public double[] getVelocity() {
				return v;
		}

		public double[] getPosition() {
				return p;
		}

		public boolean getBiasedDetermined(){
				return errorCalibrator.getBiasedDetermined();
		}

		public void setBiasedDetermined(boolean biasDetermined){
				errorCalibrator.setBiasedDetermined(biasDetermined);
				IS_INITIALIZED = false;
		}

		protected double[] lowPass(double[] input, double[] output, final double ALPHA) {
				if (output == null)
						return input;
				for (int i = 0; i < input.length; i++) {
						output[i] = output[i] + ALPHA * (input[i] - output[i]);
				}
				return output;
		}


}
