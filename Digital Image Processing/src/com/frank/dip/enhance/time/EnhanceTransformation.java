/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. GrayTransformation.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.time;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.ColorScaleLevel;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.Operator;
import com.frank.dip.math.Function;

/**
 * The abstract class of image transform.
 * <p>
 * In this class, the procedure of image transform is defined. The procedure is
 * performing formula:
 * 
 * <pre>
 * {@code s} = T({@code r})
 * {@code r}: the pixel input
 * {@code s}: the pixel output
 * T: the transform function
 * </pre>
 * 
 * to all the pixels in the image.
 * </p>
 * 
 * @see Transform
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class EnhanceTransformation<T extends Image> extends
		Operator<T, T> implements Function, ColorScaleLevel
{
	/**
	 * The matrixes for accuracy calculating.
	 */
	protected float[][]		r, g, b;
	/**
	 * The max values for accuracy calculating.
	 */
	protected float			r_min, r_max, g_min, g_max, b_min, b_max;
	/**
	 * The scale level minus {@code 1} of the transform image.
	 */
	public static final int	SCALE_LEVEL	= 256;
	/**
	 * The flag for whether the high accuracy scheme is used. If this scheme is
	 * used, the pixels values will be stored in matrixes, they will be
	 * normalized before set to the image.
	 */
	protected boolean		useHighAccuracyScheme;

	/**
	 * Perform transformation to the pixel value.
	 * 
	 * @param r
	 *            the input pixel value
	 * @return the output pixel value
	 */
	protected int perform(double r)
	{
		return (int) function(r);
	}

	protected void doStatistic(T source)
	{
		int height = source.getHeight();
		int width = source.getWidth();
		if (source instanceof GrayImage)
		{
			r = new float[height][width];
			r_max = Float.NEGATIVE_INFINITY;
			r_min = Float.POSITIVE_INFINITY;
			float p;
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
				{
					p = (float) function(source.getPixel(x, y));
					r[y][x] = p;
					if (p < r_min)
						r_min = p;
					if (p > r_max)
						r_max = p;
				}
			return;
		}
		if (source instanceof ColorImage)
		{
			r = new float[height][width];
			g = new float[height][width];
			b = new float[height][width];
			r_max = Float.NEGATIVE_INFINITY;
			g_max = Float.NEGATIVE_INFINITY;
			b_max = Float.NEGATIVE_INFINITY;
			r_min = Float.POSITIVE_INFINITY;
			g_min = Float.POSITIVE_INFINITY;
			b_min = Float.POSITIVE_INFINITY;
			float p;
			ColorImage ci = (ColorImage) source;
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
				{
					// red
					p = (float) function(ci.getRed(x, y));
					if (p < r_min)
						r_min = p;
					if (p > r_max)
						r_max = p;
					r[y][x] = p;
					// green
					p = (float) function(ci.getGreen(x, y));
					if (p < g_min)
						g_min = p;
					if (p > g_max)
						g_max = p;
					g[y][x] = p;
					// blue
					p = (float) function(ci.getBlue(x, y));
					if (p < b_min)
						b_min = p;
					if (p > b_max)
						b_max = p;
					b[y][x] = p;
				}
			return;
		}
	}

	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public T operate(T source)
	{
		int height = source.getHeight();
		int width = source.getWidth();
		if (!(source instanceof BinaryImage) && useHighAccuracyScheme)
		{
			doStatistic(source);
			int csl = COLOR_SCALE_LEVEL - 1;
			if (source instanceof GrayImage)
			{
				GrayImage result = new GrayImage(width, height);
				float base = r_max - r_min;
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
						result.setPixel(x, y,
								Math.round((r[y][x] - r_min) * csl / base));
				return (T) result;
			}
			if (source instanceof ColorImage)
			{
				ColorImage ci = (ColorImage) source;
				ColorImage result = new ColorImage(width, height);
				float r_base = r_max - r_min;
				float g_base = g_max - g_min;
				float b_base = b_max - b_min;
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
						result.setPixel(x, y, ci.getAlpha(x, y),
								Math.round((r[y][x] - r_min) * csl / r_base),
								Math.round((r[y][x] - r_min) * csl / g_base),
								Math.round((r[y][x] - r_min) * csl / b_base));
				return (T) result;
			}
			return (T) source.clone();
		}
		else
		{
			T result = (T) source.recreate();
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					result.setPixel(x, y, (int) perform(source.getPixel(x, y)));
			return result;
		}
	}

	/**
	 * Getter for useHighAccuracyScheme.
	 * 
	 * @return the useHighAccuracyScheme
	 */
	public boolean isUseHighAccuracyScheme()
	{
		return useHighAccuracyScheme;
	}

	/**
	 * Setter for useHighAccuracyScheme.
	 * 
	 * @param useHighAccuracyScheme
	 *            the value of useHighAccuracyScheme
	 */
	public void setUseHighAccuracyScheme(boolean useHighAccuracyScheme)
	{
		this.useHighAccuracyScheme = useHighAccuracyScheme;
	}
}
