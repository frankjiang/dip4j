/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * MaxEntropy1D.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Maximum entropy thresholding method.
 * <p>
 * Implements Kapur-Sahoo-Wong (Maximum Entropy) thresholding method
 * </p>
 * <p>
 * See source paper at:<br>
 * Kapur, JN; Sahoo, PK & Wong, ACK (1985),
 * "A New Method for Gray-Level Picture Thresholding Using the Entropy of the Histogram"
 * , Graphical Models and Image Processing 29(3): 273-285
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MaxEntropy implements ThresholdFinder
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
		int threshold = -1;
		int ih, it;
		int first_bin;
		int last_bin;
		double tot_ent; /* total entropy */
		double max_ent; /* max entropy */
		double ent_back; /* entropy of the background pixels at a given threshold */
		double ent_obj; /* entropy of the object pixels at a given threshold */
		double[] norm_histo = new double[256]; /* normalized histogram */
		double[] P1 = new double[256]; /* cumulative normalized histogram */
		double[] P2 = new double[256];
		int total = 0;
		for (ih = 0; ih < 256; ih++)
			total += data[ih];
		for (ih = 0; ih < 256; ih++)
			norm_histo[ih] = (double) data[ih] / total;
		P1[0] = norm_histo[0];
		P2[0] = 1.0 - P1[0];
		for (ih = 1; ih < 256; ih++)
		{
			P1[ih] = P1[ih - 1] + norm_histo[ih];
			P2[ih] = 1.0 - P1[ih];
		}
		/* Determine the first non-zero bin */
		first_bin = 0;
		for (ih = 0; ih < 256; ih++)
		{
			if (!(Math.abs(P1[ih]) < 2.220446049250313E-16))
			{
				first_bin = ih;
				break;
			}
		}
		/* Determine the last non-zero bin */
		last_bin = 255;
		for (ih = 255; ih >= first_bin; ih--)
		{
			if (!(Math.abs(P2[ih]) < 2.220446049250313E-16))
			{
				last_bin = ih;
				break;
			}
		}
		// Calculate the total entropy each gray-level
		// and find the threshold that maximizes it
		max_ent = Double.MIN_VALUE;
		for (it = first_bin; it <= last_bin; it++)
		{
			/* Entropy of the background pixels */
			ent_back = 0.0;
			for (ih = 0; ih <= it; ih++)
			{
				if (data[ih] != 0)
				{
					ent_back -= (norm_histo[ih] / P1[it])
							* Math.log(norm_histo[ih] / P1[it]);
				}
			}
			/* Entropy of the object pixels */
			ent_obj = 0.0;
			for (ih = it + 1; ih < 256; ih++)
			{
				if (data[ih] != 0)
				{
					ent_obj -= (norm_histo[ih] / P2[it])
							* Math.log(norm_histo[ih] / P2[it]);
				}
			}
			/* Total entropy */
			tot_ent = ent_back + ent_obj;
			if (max_ent < tot_ent)
			{
				max_ent = tot_ent;
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
		return "maximum entropy";
	}
}
