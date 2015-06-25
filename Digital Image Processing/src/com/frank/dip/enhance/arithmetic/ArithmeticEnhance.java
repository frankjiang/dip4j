/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Arithmetic.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.arithmetic;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;

/**
 * The arithmetic enhance algorithm processor.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ArithmeticEnhance
{
	/**
	 * The arithmetic performance.
	 */
	protected Arithmetic	arithmetic;

	/**
	 * Construct an instance of <tt>Arithmetic</tt>.
	 */
	public ArithmeticEnhance(Arithmetic arithmetic)
	{
		this.arithmetic = arithmetic;
	}

	/**
	 * Perform the arithmetic enhance to the specified image.
	 * 
	 * @param left
	 *            the left image to process
	 * @param right
	 *            the right image to process
	 * @return the new image after arithmetic performance
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public <T extends Image> T operate(T left, T right)
			throws IllegalImageTypeException
	{
		int width = left.getWidth();
		int height = right.getHeight();
		if (width > right.getWidth())
			width = right.getWidth();
		if (height > right.getHeight())
			height = right.getHeight();
		if (left instanceof GrayImage)
		{
			GrayImage gi = new GrayImage(width, height);
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					gi.setPixel(
							x,
							y,
							arithmetic.calculate(left.getPixel(x, y),
									right.getPixel(x, y)));
			return (T) gi;
		}
		if (left instanceof ColorImage)
		{
			ColorImage _left = (ColorImage) left;
			ColorImage _right = (ColorImage) right;
			ColorImage ci = new ColorImage(width, height);
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
				{
					ci.setAlpha(x, y, 255);
					ci.setRed(
							x,
							y,
							arithmetic.calculate(_left.getRed(x, y),
									_right.getRed(x, y)));
					ci.setGreen(
							x,
							y,
							arithmetic.calculate(_left.getGreen(x, y),
									_right.getGreen(x, y)));
					ci.setBlue(
							x,
							y,
							arithmetic.calculate(_left.getBlue(x, y),
									_right.getBlue(x, y)));
				}
			return (T) ci;
		}
		if (left instanceof BinaryImage)
		{
			BinaryImage bi = new BinaryImage(width, height);
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					bi.setPixel(
							x,
							y,
							arithmetic.calculate(left.getPixel(x, y),
									right.getPixel(x, y)));
			return (T) bi;
		}
		throw new IllegalImageTypeException(getClass(), left.getClass());
	}
}
