/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * IsoData.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Iterative procedure based on the isolate data algorithm.
 * <p>
 * The procedure divides the image into object and background by taking an
 * initial threshold, then the averages of the pixels at or below the threshold
 * and pixels above are computed. The averages of those two values are computed,
 * the threshold is incremented and the process is repeated until the threshold
 * is larger than the composite average. That is,
 * 
 * <pre>
 * threshold = (average background + average objects)/2
 * </pre>
 * 
 * Several implementations of this method exist. See the source code for further
 * comments.
 * </p>
 * <p>
 * See the source paper at: <br>
 * <a href="http://ieeexplore.ieee.org/xpls/abs_all.jsp?arnumber=4310039">
 * Ridler, TW & Calvard, S (1978),
 * "Picture thresholding using an iterative selection method", IEEE Transactions
 * on Systems, Man and Cybernetics 8: 630-632</a>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class IsoData implements ThresholdFinder
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
		int level;
		int maxValue = data.length - 1;
		double result, sum1, sum2, sum3, sum4;
		int min = 0;
		while ((data[min] == 0) && (min < maxValue))
			min++;
		int max = maxValue;
		while ((data[max] == 0) && (max > 0))
			max--;
		if (min >= max)
		{
			level = data.length / 2;
			return level;
		}
		int movingIndex = min;
		do
		{
			sum1 = sum2 = sum3 = sum4 = 0.0;
			for (int i = min; i <= movingIndex; i++)
			{
				sum1 += i * data[i];
				sum2 += data[i];
			}
			for (int i = (movingIndex + 1); i <= max; i++)
			{
				sum3 += i * data[i];
				sum4 += data[i];
			}
			result = (sum1 / sum2 + sum3 / sum4) / 2.0;
			movingIndex++;
		}
		while ((movingIndex + 1) <= result && movingIndex < max - 1);
		level = (int) Math.round(result);
		return level;
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "isolate data";
	}
}
