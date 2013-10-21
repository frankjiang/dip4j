/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Mean.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Mean of gray levels as the threshold.
 * <p>
 * Uses the mean of gray levels as the threshold. It is used by some other
 * methods as a first guess threshold.
 * </p>
 * <p>
 * See source paper at:<br>
 * Glasbey, CA (1993), "An analysis of histogram-based thresholding algorithms",
 * CVGIP: Graphical Models and Image Processing 55: 532-537
 * <p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Mean implements ThresholdFinder
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
		return threshold(data);
	}

	/**
	 * Returns the selected threshold according to the pixels histogram.
	 * 
	 * @param data
	 *            the pixels histogram
	 * @return the selected threshold
	 */
	public int threshold(int[] data)
	{
		int threshold = -1;
		double tot = 0, sum = 0;
		for (int i = 0; i < 256; i++)
		{
			tot += data[i];
			sum += (i * data[i]);
		}
		threshold = (int) Math.floor(sum / tot);
		return threshold;
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "mean of gray levels";
	}
}
