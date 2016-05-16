package org.mamn01.pong.controller.num.filters;

/**
 * Created by hampusballdin on 2016-04-13.
 */
public class SimplestFilter extends Filter {

	public SimplestFilter(final int XN, final int YN) {
		super(XN, YN);

		if(super.XN > 0) {

			for (int i = 0; i < XN; i++) {
				a[i] = 1.0 / (double)XN;
			}
		}

		if(super.YN > 0) {
			for (int i = 0; i < YN; i++) {
				b[i] = 1 / (double)YN;
			}
		}
	}
}
