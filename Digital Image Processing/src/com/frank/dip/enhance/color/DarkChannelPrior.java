/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * DarkChannelPrior.java is PROPRIETARY/CONFIDENTIAL built in 下午8:47:32,
 * 2015年12月22日.
 * Use is subject to license terms.
 */

package com.frank.dip.enhance.color;

import java.util.LinkedList;

import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.ImageOperate;
import com.frank.math.MathUtils;

/**
 * The dark channel prior.
 * 
 * <pre>
 * 1. Like convolution, use a pattern to mask the image locality;
 * 2. Find the minimum value of the locality and replace the original.
 * </pre>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class DarkChannelPrior<T extends Image> implements ImageOperate<T, GrayImage>
{
	/**
	 * The radius of the DCP pattern.
	 */
	protected int radius = 7;

	/**
	 * @see com.frank.dip.ImageOperate#operate(com.frank.dip.Image)
	 */
	@Override
	public GrayImage operate(T source)
	{
		int width = source.getWidth();
		int height = source.getHeight();
		int y0, yt;
		int value;
		GrayImage result = new GrayImage(width, height, 0);
		for (int y = 0; y < height; y++)
		{
			y0 = Math.max(0, y - radius);
			yt = Math.min(height, y + radius);

			// initialize the data
			LinkedList<Integer> list = new LinkedList<>();
			for (int i = 0; i < width && i < radius; i++)
			{
				value = Integer.MAX_VALUE;
				for (int j = y0; j < yt; j++)
					value = Math.min(value, getPixel(source, i, j));
				list.add(value);
			}

			// iterate the image
			for (int x = 0; x < width; x++)
			{
				// set pixel value of point(x,y)
				value = (int) MathUtils.minimum(list);
				if (value != 0)
					result.setPixel(x, y, value);

				// remove out of range data
				if (x - radius >= 0)
					list.removeFirst();

				// add new data
				if (x + radius < width)
				{
					value = Integer.MAX_VALUE;
					for (int j = y0; j < yt; j++)
						value = Math.min(value, getPixel(source, x, j));
					list.addLast(value);
				}
			}
		}
		return result;
	}

	/**
	 * Get the dark channel value from the specified image by the specified
	 * position.
	 * <p>
	 * If the pixels are in multiple channels, use the minimum one of them.
	 * </p>
	 * 
	 * @param source the source image
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @return the pixel value in dark channel
	 */
	protected abstract int getPixel(T source, int x, int y);

	/**
	 * The dark channel prior for color image.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static class ColorDarkChannelPrior extends DarkChannelPrior<ColorImage>
	{

		/**
		 * @see com.frank.dip.enhance.color.DarkChannelPrior#getPixel(com.frank.dip.Image,
		 *      int, int)
		 */
		@Override
		protected int getPixel(ColorImage source, int x, int y)
		{
			return Math.min(source.getRed(x, y),
					Math.min(source.getGreen(x, y), source.getBlue(x, y)));
		}

	}

	/**
	 * The dark channel prior for gray image.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static class GrayDarkChannelPrior extends DarkChannelPrior<GrayImage>
	{

		/**
		 * @see com.frank.dip.enhance.color.DarkChannelPrior#getPixel(com.frank.dip.Image, int, int)
		 */
		@Override
		protected int getPixel(GrayImage source, int x, int y)
		{
			return source.getPixel(x, y);
		}

	}

	/**
	 * Returns radius.
	 * 
	 * @return the radius
	 */
	public int getRadius()
	{
		return radius;
	}

	/**
	 * Set radius.
	 * 
	 * @param radius the value of radius
	 */
	public void setRadius(int radius)
	{
		this.radius = radius;
	}

	/**
	 * Returns the dark channel prior extractor.
	 * 
	 * @param imageType the specified image type
	 * @return the dark channel prior extractor
	 */
	public static <T extends Image> DarkChannelPrior<T> getExtractor(Class<T> imageType)
	{
		if (ColorImage.class.isAssignableFrom(imageType))
			return (DarkChannelPrior<T>) new ColorDarkChannelPrior();
		else if (GrayImage.class.isAssignableFrom(imageType))
			return (DarkChannelPrior<T>) new GrayDarkChannelPrior();
		throw new IllegalArgumentException(String.format(
				"The image type \"%s\" is not supported by dark channel prior extractor.",
				imageType.getName()));
	}
}
