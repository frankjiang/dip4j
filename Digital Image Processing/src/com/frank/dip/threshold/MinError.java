/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * MinError.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Minimum error thresholding.
 * <p>
 * An iterative implementation of Kittler and Illingworth's Minimum Error
 * thresholding. This implementation seems to converge more often than the
 * original. Nevertheless, sometimes the algorithm does not converge to a
 * solution. In that case a warning is reported to the log window and the result
 * defaults to the initial estimate of the threshold which is computed using the
 * Mean method. The Ignore black or Ignore white options might help to avoid
 * this problem.
 * </p>
 * <p>
 * See the source paper at: <br>
 * Kittler, J & Illingworth, J (1986), "Minimum error thresholding", Pattern
 * Recognition 19: 41-47
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MinError implements ThresholdFinder
{
	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#threshold(com.frank.dip.GrayImage)
	 */
	@Override
	public int threshold(GrayImage image)
	{
		int[] data = new int[256];
		for (int pixel : image.getPixelsArray())
			data[pixel]++;
		// Initial estimate for the threshold is found with the MEAN algorithm.
		int threshold = new Mean().threshold(data);
		//
		int Tprev = -2;
		double mu, nu, p, q, sigma2, tau2, w0, w1, w2, sqterm, temp;
		// int counter=1;
		while (threshold != Tprev)
		{
			// Calculate some statistics.
			mu = B(data, threshold) / A(data, threshold);
			nu = (B(data, 255) - B(data, threshold))
					/ (A(data, 255) - A(data, threshold));
			p = A(data, threshold) / A(data, 255);
			q = (A(data, 255) - A(data, threshold)) / A(data, 255);
			sigma2 = C(data, threshold) / A(data, threshold) - (mu * mu);
			tau2 = (C(data, 255) - C(data, threshold))
					/ (A(data, 255) - A(data, threshold)) - (nu * nu);
			// The terms of the quadratic equation to be solved.
			w0 = 1.0 / sigma2 - 1.0 / tau2;
			w1 = mu / sigma2 - nu / tau2;
			w2 = (mu * mu) / sigma2 - (nu * nu) / tau2
					+ log10((sigma2 * (q * q)) / (tau2 * (p * p)));
			// If the next threshold would be imaginary, return with the current
			// one.
			sqterm = (w1 * w1) - w0 * w2;
			if (sqterm < 0)
			{
				System.out
						.println("MinError(I): not converging. Try \'Ignore black/white\' options");
				return threshold;
			}
			// The updated threshold is the integer part of the solution of the
			// quadratic equation.
			Tprev = threshold;
			temp = (w1 + Math.sqrt(sqterm)) / w0;
			if (Double.isNaN(temp))
			{
				System.out
						.println("MinError(I): NaN, not converging. Try \'Ignore black/white\' options");
				threshold = Tprev;
			}
			else
				threshold = (int) Math.floor(temp);
			// IJ.log("Iter: "+ counter+++"  t:"+threshold);
		}
		return threshold;
	}

	private double A(int[] y, int j)
	{
		double x = 0;
		for (int i = 0; i <= j; i++)
			x += y[i];
		return x;
	}

	private double B(int[] y, int j)
	{
		double x = 0;
		for (int i = 0; i <= j; i++)
			x += i * y[i];
		return x;
	}

	private double C(int[] y, int j)
	{
		double x = 0;
		for (int i = 0; i <= j; i++)
			x += i * i * y[i];
		return x;
	}

	private final double log10(double x)
	{
		return Math.log(x) / Math.log(10);
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "minimum error";
	}
}
