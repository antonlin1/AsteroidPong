package org.mamn01.pong.controller.num.filters;

/**
 * Created by hampusballdin on 2016-04-12.
 */
public abstract class Filter {

	/*
	 *			XN				      YN
	 * y[n] = SIGMA( a_k * x[k] ) - SIGMA( b_k * y[k] )
	 *          k                     k
	 *
	 */
	protected int XN;
	protected int YN;
	// Current x index
	protected int xn;

	protected double[] x;
	protected double[] y;

	protected double[] a;
	protected double[] b;

	// Current index
	protected int n;

	public Filter(final int XN, final int YN) {
		this.XN = XN;
		this.YN = YN <= 0 ? 1 : YN;

		if(XN > 0) {
			x = new double[XN];
			a = new double[XN];
		}
		y = new double[this.YN];
		b = new double[this.YN];

	}

	/**
	 * @param xval, value to be added to filter
	 */
	public void update(double xval) {
		double sum = 0.0;

		if(XN > 0) {
			x[xn] = xval;


			for (int i = 0; i < XN; i++) {
				int aindex = (i + xn) % XN;
				if (aindex < 0)
					aindex += XN;
				sum += a[aindex] * x[i];
			}

			for (int i = 0; i < YN; i++) {
				int aindex = (i + n) % YN;
				if (aindex < 0)
					aindex += YN;
				sum += b[aindex] * y[i];
			}
			xn = (xn + 1) % XN;

		}

		y[n] = sum;
		n = (n + 1) % YN;
	}

	/*
	 * Returns y[n]
	 */
	public double getValue() {
		return y[n];
	}

	public double getPreviousInput() {
		int si = (xn - (XN + 1)) % XN;
		if(si < 0)
			si += XN;
		return x[si];
	}

	public double getAverageOutput() {
		double sum = 0;
		for(int i = 0; i < YN; i++){
			sum += y[i] / (double)YN;
		}
		return sum;
	}

	public double getAbsoluteAverageOutput() {
		double sum = 0;
		for(int i = 0; i < YN; i++){
			sum += (double)Math.abs(y[i]) / (double)YN;
		}
		return sum;
	}

	public double getAverageInput() {
		double sum = 0;
		for(int i = 0; i < XN; i++){
			sum += x[i] / (double)XN;
		}
		return sum;
	}

	public double getAbsoluteAverageInput() {
		double sum = 0;
		for(int i = 0; i < XN; i++) {
			sum += (double)Math.abs(x[i]) / (double)XN;
		}
		return sum;
	}

	public void resetValues() {
		x = new double[XN];
		y = new double[YN];
		xn = 0;
		n = 0;
	}

	public double getInputHistory(int k) {
		int si = (xn - (XN + k)) % XN;
		if(si < 0)
			si += XN;
		return x[si];
	}

	public double getOutputHistory(int k) {
		int si = (n - (YN + k)) % YN;
		if(si < 0)
			si += YN;
		return y[si];
	}

}
