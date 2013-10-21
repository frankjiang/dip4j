/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Moments.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Moments thresholding.
 * <p>
 * Tsai's method attempts to preserve the moments of the original image in the
 * thresholded result.
 * </p>
 * <p>
 * See the source paper at: <br>
 * <a href="http://portal.acm.org/citation.cfm?id=201578"> Tsai, W (1985),
 * "Moment-preserving thresholding: a new approach", Computer Vision, Graphics,
 * and Image Processing 29: 377-393</a>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Moments implements ThresholdFinder
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
		double total = 0;
		double m0 = 1.0, m1 = 0.0, m2 = 0.0, m3 = 0.0, sum = 0.0, p0 = 0.0;
		double cd, c0, c1, z0, z1; /* auxiliary variables */
		int threshold = -1;
		double[] histo = new double[256];
		for (int i = 0; i < 256; i++)
			total += data[i];
		for (int i = 0; i < 256; i++)
			histo[i] = (double) (data[i] / total); // normalised histogram
		/* Calculate the first, second, and third order moments */
		for (int i = 0; i < 256; i++)
		{
			m1 += i * histo[i];
			m2 += i * i * histo[i];
			m3 += i * i * i * histo[i];
		}
		/*
		 * First 4 moments of the gray-level image should match the first 4
		 * moments
		 * of the target binary image. This leads to 4 equalities whose
		 * solutions
		 * are given in the Appendix of Ref. 1
		 */
		cd = m0 * m2 - m1 * m1;
		c0 = (-m2 * m2 + m1 * m3) / cd;
		c1 = (m0 * -m3 + m2 * m1) / cd;
		z0 = 0.5 * (-c1 - Math.sqrt(c1 * c1 - 4.0 * c0));
		z1 = 0.5 * (-c1 + Math.sqrt(c1 * c1 - 4.0 * c0));
		p0 = (z1 - m1) / (z1 - z0);
		/*
		 * Fraction of the object pixels in the
		 * target binary image
		 */
		// The threshold is the gray-level closest
		// to the p0-tile of the normalized histogram
		sum = 0;
		for (int i = 0; i < 256; i++)
		{
			sum += histo[i];
			if (sum > p0)
			{
				threshold = i;
				break;
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
		return "moments";
	}
}
