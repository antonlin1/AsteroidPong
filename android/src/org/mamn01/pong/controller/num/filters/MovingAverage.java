package org.mamn01.pong.controller.num.filters;

/**
 * Created by hampusballdin on 2016-04-12.
 */
public class MovingAverage extends Filter {
		/*			XN				      YN
		 * y[n] = SIGMA( a_k * x[k] ) - SIGMA( b_k * y[k] )
		 *          k                     k
		 */
		public MovingAverage(final int XN, final int YN){
				super(XN, YN);

				if(XN > 0) {
						double sum = 0;
						for (int i = 0; i < XN; i++) {
								a[i] = XN - i;
								sum += a[i];
						}
						for (int i = 0; i < XN; i++) {
								a[i] /= sum;
						}
				}

				if(YN > 0) {
						for (int i = 0; i < YN; i++) {
								b[i] = 1.0/(double)YN;
						}
				}
		}

}
