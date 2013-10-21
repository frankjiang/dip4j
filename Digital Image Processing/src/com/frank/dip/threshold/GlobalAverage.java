/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * GlobalAverage.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * The global average threshold finder.
 * <p>
 * In this threshold finder, the final global threshold is the average pixel
 * value of the whole pixels.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GlobalAverage implements ThresholdFinder
{
	/**
	 * Construct an instance of <tt>GlobalAverage</tt>.
	 */
	public GlobalAverage()
	{
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#threshold(com.frank.dip.GrayImage)
	 */
	@Override
	public int threshold(GrayImage image)
	{
		int sum = 0;
		int height = image.height();
		int width = image.width();
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				sum += image.getPixel(x, y);
		return (int) (sum / (double) width / height);
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "Global Average";
	}
}
