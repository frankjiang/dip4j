/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. GrayScaleCoefficient.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.enhance;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;

/**
 * Transform color image to gray image using coefficient algorithm:
 * 
 * <pre>
 * g = 0.3 * r + 0.59 * g + 0.11 * b
 * </pre>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public final class GrayScaleCoefficient extends GrayScale
{
	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public GrayImage operate(Image image)
	{
		if (image instanceof GrayImage)
			return (GrayImage) image.clone();
		int width = image.getWidth();
		int height = image.getHeight();
		GrayImage gi = new GrayImage(width, height);
		if (image instanceof BinaryImage)
		{
			BinaryImage bi = (BinaryImage) image;
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					gi.setPixel(x, y, bi.getPixel(x, y));
			return gi;
		}
		if (image instanceof ColorImage)
		{
			ColorImage ci = (ColorImage) image;
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					gi.setPixel(x, y, ci.getGray(x, y));
			return gi;
		}
		throw new IllegalArgumentException(String.format(
				"Current gray image transform cannot support image type: %s",
				image.getClass().toString()));
	}
}
