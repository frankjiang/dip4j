/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Yen.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Yen's threshold selection.
 * <p>
 * Implements Yen's thresholding method from:
 * </p>
 * <p>
 * <a href="http://ieeexplore.ieee.org/xpl/freeabs_all.jsp?arnumber=366472">Yen
 * JC, Chang FJ, Chang S (1995),
 * "A New Criterion for Automatic Multilevel Thresholding", IEEE Trans. on Image
 * Processing 4 (3): 370-378, ISSN 1057-7149, doi:10.1109/83.366472</a>
 * </p>
 * <p>
 * <a href="http://citeseer.ist.psu.edu/sezgin04survey.html"> Sezgin, M &
 * Sankur, B (2004),
 * "Survey over Image Thresholding Techniques and Quantitative Performance Evaluation"
 * , Journal of Electronic Imaging 13(1): 146-165</a>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Yen implements ThresholdFinder
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
		int threshold;
		int ih, it;
		double crit;
		double max_crit;
		double[] norm_histo = new double[256]; /* normalized histogram */
		double[] P1 = new double[256]; /* cumulative normalized histogram */
		double[] P1_sq = new double[256];
		double[] P2_sq = new double[256];
		int total = 0;
		for (ih = 0; ih < 256; ih++)
			total += data[ih];
		for (ih = 0; ih < 256; ih++)
			norm_histo[ih] = (double) data[ih] / total;
		P1[0] = norm_histo[0];
		for (ih = 1; ih < 256; ih++)
			P1[ih] = P1[ih - 1] + norm_histo[ih];
		P1_sq[0] = norm_histo[0] * norm_histo[0];
		for (ih = 1; ih < 256; ih++)
			P1_sq[ih] = P1_sq[ih - 1] + norm_histo[ih] * norm_histo[ih];
		P2_sq[255] = 0.0;
		for (ih = 254; ih >= 0; ih--)
			P2_sq[ih] = P2_sq[ih + 1] + norm_histo[ih + 1] * norm_histo[ih + 1];
		/* Find the threshold that maximizes the criterion */
		threshold = -1;
		max_crit = Double.MIN_VALUE;
		for (it = 0; it < 256; it++)
		{
			crit = -1.0
					* ((P1_sq[it] * P2_sq[it]) > 0.0 ? Math.log(P1_sq[it]
							* P2_sq[it]) : 0.0)
					+ 2
					* ((P1[it] * (1.0 - P1[it])) > 0.0 ? Math.log(P1[it]
							* (1.0 - P1[it])) : 0.0);
			if (crit > max_crit)
			{
				max_crit = crit;
				threshold = it;
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
		return "Yen's threshold";
	}
}
