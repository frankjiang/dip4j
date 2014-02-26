/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
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
public abstract class AbstractImage implements java.io.Serializable
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
	 * The description of this image.
	 */
	protected String			description;

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

	/**
	 * Returns the specified sub-image of the current image according the
	 * specified coordinates.
	 * 
	 * @param x0
	 *            the X-coordinate of the left-top anchor in the current image
	 * @param y0
	 *            the Y-coordinate of the left-top anchor in the current image
	 * @param xt
	 *            the X-coordinate of the right-bottom anchor in the current
	 *            image
	 * @param yt
	 *            the Y-coordinate of the right-bottom anchor in the current
	 *            image
	 * @return the sub-image
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the coordinates are out of the image pixels range
	 */
	public abstract AbstractImage subImage(int x0, int y0, int xt, int yt)
			throws ArrayIndexOutOfBoundsException;

	/**
	 * Returns the specified sub-image of the current image according the
	 * specified coordinates.
	 * 
	 * @param x0
	 *            the X-coordinate of the left-top anchor in the current image
	 * @param y0
	 *            the Y-coordinate of the left-top anchor in the current image
	 * @param xt
	 *            the X-coordinate of the right-bottom anchor in the current
	 *            image
	 * @param yt
	 *            the Y-coordinate of the right-bottom anchor in the current
	 *            image
	 * @return the sub-image
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the anchors are out of the image pixels range
	 */
	public AbstractImage subImage(java.awt.Point leftTop,
			java.awt.Point rightBottom) throws ArrayIndexOutOfBoundsException
	{
		return subImage(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y);
	}

	/**
	 * Returns a comment string describe this image.
	 * 
	 * @return the comment string
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set the description of this image.
	 * 
	 * @param description
	 *            the description string
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
}
