/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * AbstractImage.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip;

import java.awt.Dimension;

/**
 * Define a matrix which stores image data.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class AbstractImage implements java.io.Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 8958486967160784959L;
	/**
	 * The image width.
	 */
	protected int				width;
	/**
	 * The image height.
	 */
	protected int				height;

	/**
	 * Returns the width of the current image.
	 * 
	 * @return the image width
	 */
	public int width()
	{
		return width;
	}

	/**
	 * Returns the height of the current image.
	 * 
	 * @return the image height
	 */
	public int height()
	{
		return height;
	}

	/**
	 * Returns the size of the current image, described by
	 * {@linkplain Dimension}.
	 * 
	 * @return the image size
	 */
	public Dimension size()
	{
		return new Dimension(width(), height());
	}

	/**
	 * Check whether the retrieving position is legal.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the position is illegal.
	 */
	protected void checkBounds(int x, int y)
			throws ArrayIndexOutOfBoundsException
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new ArrayIndexOutOfBoundsException(
					String.format(
							"The retrieved position of (%d,%d) is out of the image dimension(%d, %d).",
							x, y, width, height));
	}
}
