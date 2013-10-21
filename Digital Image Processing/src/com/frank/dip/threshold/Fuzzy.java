/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * FuzzyThresholding.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Huang's fuzzy thresholding method.
 * <p>
 * Implements Huang's fuzzy thresholding method. This uses Shannon's entropy
 * function (one can also use Yager's entropy function).
 * </p>
 * <p>
 * See source paper at:<br>
 * <a href=
 * "http://www.ktl.elf.stuba.sk/study/vacso/Zadania-Cvicenia/Cvicenie_3/TimA2/
 * Huang_E016529624.pdf"> Huang, L-K & Wang, M-J J (1995),
 * "Image thresholding by minimizing the measure of fuzziness", Pattern
 * Recognition 28(1): 41-51</a>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Fuzzy implements ThresholdFinder
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
		int sum_pix;
		int num_pix;
		double term;
		double ent; // entropy
		double min_ent; // min entropy
		double mu_x;
		// Determine the first non-zero bin
		first_bin = 0;
		for (ih = 0; ih < 256; ih++)
		{
			if (data[ih] != 0)
			{
				first_bin = ih;
				break;
			}
		}
		// Determine the last non-zero bin
		last_bin = 255;
		for (ih = 255; ih >= first_bin; ih--)
		{
			if (data[ih] != 0)
			{
				last_bin = ih;
				break;
			}
		}
		term = 1.0 / (double) (last_bin - first_bin);
		double[] mu_0 = new double[256];
		sum_pix = num_pix = 0;
		for (ih = first_bin; ih < 256; ih++)
		{
			sum_pix += ih * data[ih];
			num_pix += data[ih];
			// NUM_PIX cannot be zero !
			mu_0[ih] = sum_pix / (double) num_pix;
		}
		double[] mu_1 = new double[256];
		sum_pix = num_pix = 0;
		for (ih = last_bin; ih > 0; ih--)
		{
			sum_pix += ih * data[ih];
			num_pix += data[ih];
			// NUM_PIX cannot be zero !
			mu_1[ih - 1] = sum_pix / (double) num_pix;
		}
		// Determine the threshold that minimizes the fuzzy entropy
		threshold = -1;
		min_ent = Double.MAX_VALUE;
		for (it = 0; it < 256; it++)
		{
			ent = 0.0;
			for (ih = 0; ih <= it; ih++)
			{
				// Equation (4) in Ref. 1
				mu_x = 1.0 / (1.0 + term * Math.abs(ih - mu_0[it]));
				if (!((mu_x < 1e-06) || (mu_x > 0.999999)))
					// Equation (6) & (8) in Ref. 1
					ent += data[ih]
							* (-mu_x * Math.log(mu_x) - (1.0 - mu_x)
									* Math.log(1.0 - mu_x));
			}
			for (ih = it + 1; ih < 256; ih++)
			{
				// Equation (4) in Ref. 1
				mu_x = 1.0 / (1.0 + term * Math.abs(ih - mu_1[it]));
				if (!((mu_x < 1e-06) || (mu_x > 0.999999)))
					// Equation (6) & (8) in Ref. 1
					ent += data[ih]
							* (-mu_x * Math.log(mu_x) - (1.0 - mu_x)
									* Math.log(1.0 - mu_x));
			}
			// No need to divide by NUM_ROWS * NUM_COLS * LOG(2) !
			if (ent < min_ent)
			{
				min_ent = ent;
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
		return "Huang's fuzzy thresholding";
	}
}
