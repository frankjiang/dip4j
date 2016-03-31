/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Dehaze.java is PROPRIETARY/CONFIDENTIAL built in 下午6:51:19, 2015年12月23日.
 * Use is subject to license terms.
 */

package com.frank.dip.enhance.time;

import java.util.LinkedList;
import java.util.PriorityQueue;

import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.ImageOperate;
import com.frank.math.MathUtils;

/**
 * The dehaze implementation use dark channel prior to estimate the original
 * image.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class Dehaze<T extends Image> implements ImageOperate<T, T>
{
	/**
	 * The radius of the DCP pattern.
	 */
	protected int radius = 7;

	/**
	 * The rate of using the airglow.
	 * <p>
	 * The airglow is set as the average of the first {@linkplain #rateAirglow}
	 * of largest values.
	 * </p>
	 */
	protected float rateAirglow = 0.01f;

	/**
	 * The intensity of the airglow, ordered in R(0), G(1), B(2).
	 */
	protected float[] airglow;

	/**
	 * The priority queues.
	 */
	protected PriorityQueue<Integer>[] pq;

	/**
	 * Calculate the dark channel prior and the airglow.
	 * 
	 * @param image the source image
	 * @return the DCP image
	 */
	protected GrayImage calculateDarkChannelPrior(T image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int y0, yt;
		int value;
		GrayImage result = new GrayImage(width, height, 0);
		airglow[0] = width * height * rateAirglow;
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
					value = Math.min(value, getPixel(image, i, j));
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
						value = Math.min(value, getPixel(image, x, j));
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
	protected abstract int getPixel(T image, int x, int y);

	/**
	 * Dehaze the source image according to the DCP image.
	 * <p>
	 * The airglow is calculated in function
	 * {@linkplain #calculateDarkChannelPrior(Image)} and be restored in
	 * {@linkplain #airglow}.
	 * </p>
	 * 
	 * @param source the source image
	 * @param dcp the dark channel prior image
	 * @return the dehazed the image
	 */
	protected T dehaze(T source, GrayImage dcp)
	{
		// calculate the airglow
		for (int i = 0; i < airglow.length; i++)
		{
			airglow[i] = (float) MathUtils.average(pq[i]);
			pq[i].clear();
		}

		// iterate the image and restore the orginal
		int width = source.getWidth();
		int height = source.getHeight();
		T result = (T) source.recreate();
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				dehaze(source, result, dcp, x, y);
		return result;
	}

	/**
	 * @see com.frank.dip.ImageOperate#operate(com.frank.dip.Image)
	 */
	@Override
	public synchronized T operate(T source)
	{
		GrayImage image = calculateDarkChannelPrior(source);
		return dehaze(source, image);
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
	 * The dehaze implementation for color image.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class DehazeColor extends Dehaze<ColorImage>
	{

		public DehazeColor()
		{
			airglow = new float[3];
			pq = new PriorityQueue[3];
			for (int i = 0; i < 3; i++)
				pq[i] = new PriorityQueue<>();
		}

		/**
		 * @see com.frank.dip.enhance.time.Dehaze#getPixel(com.frank.dip.Image,
		 *      int, int)
		 */
		@Override
		protected int getPixel(ColorImage image, int x, int y)
		{
			int r = image.getRed(x, y);
			int g = image.getGreen(x, y);
			int b = image.getBlue(x, y);

			pq[0].add(r);
			pq[1].add(g);
			pq[2].add(b);

			// save space
			if (pq[0].size() > airglow[0])
				for (int i = 0; i < airglow.length; i++)
					pq[i].poll();

			return Math.min(r, Math.min(g, b));
		}

		/**
		 * @see com.frank.dip.enhance.time.Dehaze#dehaze(com.frank.dip.Image,
		 *      com.frank.dip.Image, com.frank.dip.GrayImage, int, int)
		 */
		@Override
		protected void dehaze(ColorImage source, ColorImage result, GrayImage dcp, int x, int y)
		{
			int r, g, b, dark = Math.round(dcp.getPixel(x, y) * 0.85f);
			r = dehaze(source.getRed(x, y), dark, airglow[0]);
			g = dehaze(source.getGreen(x, y), dark, airglow[1]);
			b = dehaze(source.getBlue(x, y), dark, airglow[2]);
			result.setPixel(x, y, 255, r, g, b);
		}

	}

	/**
	 * The dehaze implementation for gray image.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class DehazeGray extends Dehaze<GrayImage>
	{

		public DehazeGray()
		{
			airglow = new float[1];
			pq = new PriorityQueue[1];
			pq[0] = new PriorityQueue<>();
		}

		/**
		 * @see com.frank.dip.enhance.time.Dehaze#getPixel(com.frank.dip.Image,
		 *      int, int)
		 */
		@Override
		protected int getPixel(GrayImage image, int x, int y)
		{
			int value = image.getPixel(x, y);
			pq[0].add(value);
			if (pq[0].size() > airglow[0])
				pq[0].poll();
			return value;
		}

		/**
		 * @see com.frank.dip.enhance.time.Dehaze#dehaze(com.frank.dip.Image,
		 *      com.frank.dip.Image, com.frank.dip.GrayImage, int, int)
		 */
		@Override
		protected void dehaze(GrayImage source, GrayImage result, GrayImage dcp, int x, int y)
		{
			result.setPixel(x, y, dehaze(source.getPixel(x, y), dcp.getPixel(x, y), airglow[0]));
		}

	}

	/**
	 * Returns the airglow rate.
	 * <p>
	 * The airglow is set as the average of the first {@linkplain #rateAirglow}
	 * of largest values.
	 * </p>
	 * 
	 * @return the rateAirglow
	 */
	public float getRateAirglow()
	{
		return rateAirglow;
	}

	/**
	 * Set the airglow rate.
	 * <p>
	 * The airglow is set as the average of the first {@linkplain #rateAirglow}
	 * of largest values.
	 * </p>
	 * 
	 * @param rateAirglow the value of rateAirglow
	 */
	public void setRateAirglow(float rateAirglow)
	{
		if (rateAirglow < 0 || rateAirglow > 1)
			throw new IllegalArgumentException("The airglow rate should be between 0 and 1.");
		this.rateAirglow = rateAirglow;
	}

	/**
	 * Dehaze one pixel according to the formula.
	 * 
	 * @param source the source image
	 * @param result the result image
	 * @param dcp the dark channel prior image
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	abstract protected void dehaze(T source, T result, GrayImage dcp, int x, int y);

	/**
	 * The dehaze function.
	 * 
	 * @param image the real image value in the color channel
	 * @param dark the dark channel value
	 * @param airglow the airglow value
	 * @return the dehazed value to estimate orginal image
	 */
	protected int dehaze(float image, float dark, float airglow)
	{
		// return Math.round(airglow * (1 + (image - airglow) / (airglow - dark)));
		// return Math.round(airglow + (image - airglow) / (1 - dark / airglow));
		int original = Math.round(airglow + airglow * (image - airglow) / (airglow - dark));
		if (original < 0)
			return 0;
		else if (original > 0xff)
			return 0xff;
		else
			return original;
	}

	/**
	 * Returns the dark channel prior extractor.
	 * 
	 * @param imageType the specified image type
	 * @return the dark channel prior extractor
	 */
	public static <T extends Image> Dehaze<T> getDehazer(Class<T> imageType)
	{
		if (ColorImage.class.isAssignableFrom(imageType))
			return (Dehaze<T>) new DehazeColor();
		else if (GrayImage.class.isAssignableFrom(imageType))
			return (Dehaze<T>) new DehazeGray();
		throw new IllegalArgumentException(String
				.format("The image type \"%s\" is not supported by dehazer.", imageType.getName()));
	}
}
