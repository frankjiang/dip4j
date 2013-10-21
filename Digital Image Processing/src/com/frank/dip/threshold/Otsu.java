/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Otsu1D.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Otsu's threshold clustering algorithm (maximum between class distance
 * threshold clustering).
 * <p>
 * Otsu thresholding selection is a widely used method for global thresholding
 * selection. Otsu's threshold clustering algorithm searches for the threshold
 * that minimizes the intra-class variance, defined as a weighted sum of
 * variances of the two classes.
 * </p>
 * <p>
 * See the source paper at: <br>
 * <a href="http://web-ext.u-aizu.ac.jp/course/bmclass/documents/otsu1979.pdf">
 * Otsu N. A threshold selection method from gray-level histograms[J].
 * Automatica, 1975, 11(285-296): 23-27. </a>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Otsu implements ThresholdFinder
{
	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#threshold(com.frank.dip.GrayImage)
	 */
	@Override
	public int threshold(GrayImage image)
	{
		int k; // counter
		int threshold; // k = the current threshold; kStar = optimal threshold
		int N1, N; // N1 = # points with intensity <=k; N = total number of
					// points
		double bcv, // The current Between Class Variance
		bcvMax; // maximum BCV
		double num, denom; // temporary bookeeping
		int Sk; // The total intensity for all histogram points <=k
		int S, L = 256; // The total intensity of the image
		int[] data = new int[L];
		for (int pixel : image.getPixelsArray())
			data[pixel]++;
		// Initialize values:
		S = N = 0;
		for (k = 0; k < L; k++)
		{
			S += k * data[k]; // Total histogram intensity
			N += data[k]; // Total number of data points
		}
		Sk = 0;
		N1 = data[0]; // The entry for zero intensity
		bcv = 0;
		bcvMax = 0;
		threshold = 0;
		// Look at each possible threshold value,
		// calculate the between-class variance, and decide if it's a max
		for (k = 1; k < L - 1; k++)
		{ // No need to check end points k = 0 or k = L-1
			Sk += k * data[k];
			N1 += data[k];
			/*
			 * The float casting here is to avoid compiler warning about loss of
			 * precision and will prevent overflow in the case of large
			 * saturated images.
			 */
			// Maximum value of denom is (N^2)/4 = approx. 3E10
			denom = (double) (N1) * (N - N1);
			if (denom != 0)
			{
				// Float here is to avoid loss of precision when dividing
				// Maximum value of num = 255*N = approx 8E7
				num = ((double) N1 / N) * S - Sk;
				bcv = (num * num) / denom;
			}
			else
				bcv = 0;
			if (bcv >= bcvMax)
			{ // Assign the best threshold found so far
				bcvMax = bcv;
				threshold = k;
			}
		}
		return threshold;
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "maximum between class distance";
	}
}
