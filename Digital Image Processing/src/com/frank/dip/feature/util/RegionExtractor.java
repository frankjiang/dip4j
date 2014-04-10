/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * RegionExtractor.java is PROPRIETARY/CONFIDENTIAL built in 4:55:59 PM, Apr 10,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.util;

import java.awt.Color;
import java.awt.Dimension;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;
import com.frank.dip.Operator;
import com.frank.dip.geom.Geometry;

/**
 * Region extractor.
 * <p>
 * Extract the region of interest and remove the edges of no data.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class RegionExtractor<T extends Image> extends Operator<T, T>
{
	/**
	 * Color of background (the part can be remove).
	 */
	protected Color		background;
	/**
	 * The scale size for normalizing the extracted image. Set <code>null</code>
	 * if scale is not needed.
	 */
	protected Dimension	scaleSize;

	/**
	 * Construct an instance of <tt>RegionExtractor</tt>.
	 */
	public RegionExtractor()
	{
		this(Color.BLACK, null);
	}

	/**
	 * Construct an instance of <tt>RegionExtractor</tt>.
	 * 
	 * @param background
	 *            the background color
	 * @param scaleSize
	 *            the size to scale the extracted image
	 */
	public RegionExtractor(Color background, Dimension scaleSize)
	{
		this.background = background;
		this.scaleSize = scaleSize;
	}

	/**
	 * Construct an instance of <tt>RegionExtractor</tt>.
	 * 
	 * @param background
	 *            the background color
	 * @param width
	 *            the scaled image width
	 * @param height
	 *            the scaled image height
	 */
	public RegionExtractor(Color background, int width, int height)
	{
		this.background = background;
		scaleSize = new Dimension(width, height);
	}

	/**
	 * Returns the scale size for normalizing the extracted image. Set
	 * <code>null</code> if scale is not needed.
	 * 
	 * @return the scale size
	 */
	public Dimension getScaleSize()
	{
		return scaleSize;
	}

	/**
	 * The scale size for normalizing the extracted image. Set <code>null</code>
	 * if scale is not needed.
	 * 
	 * @param scaleSize
	 *            the value of scale size
	 */
	public void setScaleSize(Dimension scaleSize)
	{
		this.scaleSize = scaleSize;
	}

	/**
	 * The scale size for normalizing the extracted image. Set <code>null</code>
	 * if scale is not needed.
	 * 
	 * @param width
	 *            the scaled image width
	 * @param height
	 *            the scaled image height
	 */
	public void setScaleSize(int width, int height)
	{
		if (scaleSize == null)
			scaleSize = new Dimension(width, height);
		else
		{
			scaleSize.width = width;
			scaleSize.height = height;
		}
	}

	/**
	 * Set background color.
	 * 
	 * @param background
	 *            the background color
	 */
	public void setBackground(Color background)
	{
		this.background = background;
	}

	/**
	 * Returns the background color.
	 * 
	 * @return the background color
	 */
	public Color getBackground()
	{
		return background;
	}

	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public T operate(T source)
	{
		// generate color
		int rgb = 0;
		if (source instanceof ColorImage)
			rgb = background.getRGB();
		else if (source instanceof GrayImage)
			rgb = (background.getRed() + background.getGreen() + background
					.getBlue()) / 3;
		else if (source instanceof BinaryImage)
			rgb = ((background.getRed() + background.getGreen() + background
					.getBlue()) / 3 > 127) ? 0 : 255;
		else
			throw new IllegalImageTypeException(getClass(), source.getClass());
		// prepare parameters
		int width = source.width(), height = source.height();
		int top = 0, bottom = height - 1, left = 0, right = width - 1;
		int x, y;
		boolean flag;
		// seeking top
		flag = true;
		for (y = 0; flag && y < bottom; y++)
			for (x = 0; flag && x < width; x++)
				if (source.getPixel(x, y) != rgb)
				{
					top = y;
					flag = false;
				}
		// seeking bottom
		flag = true;
		for (y = height - 1; flag && y >= top; y--)
			for (x = 0; flag && x < width; x++)
				if (source.getPixel(x, y) != rgb)
				{
					bottom = y;
					flag = false;
				}
		// seeking left
		flag = true;
		for (x = 0; flag && x < right; x++)
			for (y = 0; flag && y < height; y++)
				if (source.getPixel(x, y) != rgb)
				{
					left = x;
					flag = false;
				}
		// seeking right
		flag = true;
		for (x = width - 1; flag && x >= left; x--)
			for (y = 0; flag && y < height; y++)
				if (source.getPixel(x, y) != rgb)
				{
					right = x;
					flag = false;
				}
		if (left > right || top > bottom)
			return source;
		// scale sub-image if needed
		if (scaleSize == null)
			return (T) source.subImage(left, top, right, bottom);
		else
			return (T) Geometry.getGeometry(source, Geometry.TYPE_BILINEAR,
					Geometry.FILL_WITH_BLANK).scale(
					(T) source.subImage(left, top, right, bottom),
					scaleSize.width, scaleSize.height);
	}
}
