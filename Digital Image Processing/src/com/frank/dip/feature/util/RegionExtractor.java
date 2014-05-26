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
import java.awt.Rectangle;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageContentException;
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
	 * The size of the short edge.
	 */
	protected Integer	size;

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
		size = null;
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
		size = null;
	}

	/**
	 * Construct an instance of <tt>RegionExtractor</tt>.
	 * 
	 * @param background
	 *            the background color
	 * @param size
	 *            the size of the short edge
	 */
	public RegionExtractor(Color background, int size)
	{
		this.background = background;
		scaleSize = null;
		this.size = size;
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
	 * @throws IllegalImageContentException
	 *             if no bound found in the specified image
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public T operate(T source) throws RuntimeException
	{
		Rectangle bound = findBound(source, background);
		if (bound == null)
			throw new IllegalImageContentException(
					"No bound found in the specified image.");
		// scale sub-image if needed
		if (scaleSize == null)
		{
			if (size == null)
				return (T) source.subImage(bound.x, bound.y, bound.x
						+ bound.width, bound.y + bound.height);
			else
				return (T) Geometry.getGeometry(source, Geometry.TYPE_BILINEAR,
						Geometry.FILL_WITH_BLANK).scale(
						(T) source.subImage(bound.x, bound.y, bound.x
								+ bound.width, bound.y + bound.height), size,
						false);
		}
		else
			return (T) Geometry.getGeometry(source, Geometry.TYPE_BILINEAR,
					Geometry.FILL_WITH_BLANK).scale(
					(T) source.subImage(bound.x, bound.y,
							bound.x + bound.width, bound.y + bound.height),
					scaleSize.width, scaleSize.height);
	}

	/**
	 * Returns the outer bound of the specified image.
	 * <p>
	 * The outer bound of a image is the rectangle which matches the top, left,
	 * right and bottom edges of the foreground in the image.
	 * </p>
	 * 
	 * @param source
	 *            the specified image to find
	 * @param background
	 *            the background color
	 * @return the outer bound or <code>null</code> if there is not foreground
	 *         found
	 */
	public static Rectangle findBound(Image source, Color background)
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
					.getBlue()) / 3 < 128) ? 0 : 255;
		else
			throw new IllegalImageTypeException(RegionExtractor.class,
					source.getClass());
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
			return null;
		return new Rectangle(left, top, right - left, bottom - top);
	}
}
