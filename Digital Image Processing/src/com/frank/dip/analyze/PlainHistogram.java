/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * PlainHistogram.java is PROPRIETARY/CONFIDENTIAL built in 7:48:39 PM, May 16,
 * 2016.
 * Use is subject to license terms.
 */

package com.frank.dip.analyze;

import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;

/**
 * TODO
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class PlainHistogram extends Histogram
{

	/**
	 * Construct an instance of <tt>PlainHistogram</tt>.
	 * 
	 * @param image
	 * @throws IllegalImageTypeException
	 */
	public PlainHistogram(GrayImage image, boolean isVertical)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int edge = isVertical ? width : height;
		area = edge;
		data = new int[edge];
		minimum = Integer.MAX_VALUE;
		maximum = Integer.MIN_VALUE;

		if (isVertical)
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
					data[x] += image.getPixel(x, y);
		else
			for (int y = 0; y < height; y++)
				for (int x = width / 2; x < width; x++)
					data[y] += 255 - image.getPixel(x, y);
		
		for (int i = 0; i<data.length; i++)
		{
			if(data[i] < minimum)
				minimum = data[i];
			if(data[i] > maximum)
				maximum = data[i];
		}
	}

}
