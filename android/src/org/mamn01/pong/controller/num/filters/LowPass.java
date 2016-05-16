package org.mamn01.pong.controller.num.filters;

/**
 * Created by hampusballdin on 2016-04-13.
 */
public class LowPass extends Filter {

	public LowPass(final int XN, final int YN, final float ALPHA) {
		super(XN, YN);

		for (int i = 0; i < XN; i++) {
			a[i] = ALPHA / (double)XN;
		}

		for (int i = 0; i < YN; i++) {
			b[i] = (1 - ALPHA) / (double) YN;
		}
	}

}
