package org.mamn01.pong.controller.num.filters;


import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Created by hampusballdin on 2016-04-28.
 */
public class FFTLowpass {
		private FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

		private int N = 1;
		private static final double SIGMA = 8.0;
		private double[] gaussian;
		private Complex[] result;
		/**
		 * Number samples
		 */

		public FFTLowpass(int N) {
				this.N = N;
				result = new Complex[N];
				gaussian = FFTLowpass.getGaussian(N, SIGMA, 0.0, -10, 10);
		}



		public void filter(double[] y) {
				Complex[] Y = fft.transform(y, TransformType.FORWARD);

				for(int i = 0; i < N; i++) {
						Y[i].multiply(gaussian[i]);
				}

				result = fft.transform(Y , TransformType.INVERSE);
		}

		public Complex[] getResult() {
				return result;
		}

		/**
		 * Get a sample set of a gaussian distribution
		 * @return
		 */
		private static double[] getGaussian(int N, double s, double mu, double xmin, double xmax) {
				double[] g = new double[N];
				double C = 1 / (s * Math.sqrt(2 * Math.PI));
				double C2 = -1 / (2 * s * s);
				double dx = (xmax - xmin) / (double)(N - 1);

				for(int i = 0; i < N; i++) {
						double x = xmin + i * dx;
						double C3 = (x - mu);
						g[i] = C * Math.exp( C2 * C3 * C3 );
 				}
				return g;
		}


		private int n = 0;
		public double nextGaussian() {
				double tmp =  gaussian[n];
				n = (n + 1) % N;
				return tmp;
		}

		public double[] getGaussianValues() {
				return gaussian;
		}

		public double average(Complex[] d) {
				double sum = 0.0;
				for(int i = 0; i < d.length; i++) {
						sum += d[i].getReal() / d.length;
				}
				return sum;
		}

}
