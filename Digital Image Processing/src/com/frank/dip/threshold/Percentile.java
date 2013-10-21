/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Percentile.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Percentile threshold selection.
 * <p>
 * Assumes the fraction of foreground pixels to be 0.5.
 * </p>
 * <p>
 * See the source paper at: <br>
 * <a href="http://portal.acm.org/citation.cfm?id=321119.321123"> Doyle, W
 * (1962), "Operation useful for similarity-invariant pattern recognition",
 * Journal of the Association for Computing Machinery 9: 259-267,
 * doi:10.1145/321119.321123 </a> <br>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Percentile implements ThresholdFinder
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
		double ptile = 0.5; // default fraction of foreground pixels
		double[] avec = new double[256];
		for (int i = 0; i < 256; i++)
			avec[i] = 0.0;
		double total = partialSum(data, 255);
		double temp = 1.0;
		for (int i = 0; i < 256; i++)
		{
			avec[i] = Math.abs((partialSum(data, i) / total) - ptile);
			// IJ.log("Ptile["+i+"]:"+ avec[i]);
			if (avec[i] < temp)
			{
				temp = avec[i];
				threshold = i;
			}
		}
		return threshold;
	}

	private double partialSum(int[] y, int j)
	{
		double x = 0;
		for (int i = 0; i <= j; i++)
			x += y[i];
		return x;
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "percentile";
	}
}
