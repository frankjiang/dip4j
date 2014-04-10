/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * FakeColorTransform.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.color;

import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Operator;
import com.frank.dip.math.Function;

/**
 * The fake color transform translates a gray image to a color image, according
 * the tree transform functions in each channel - red, green and blue.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class FakeColorTransform extends Operator<GrayImage, ColorImage>
{
	/**
	 * The transform functions for color channel mapping.
	 */
	protected Function	red, green, blue;

	/**
	 * Construct an instance of <tt>FakeColorTransform</tt>.
	 * 
	 * @param red
	 *            the transform function for red channel
	 * @param green
	 *            the transform function for green channel
	 * @param blue
	 *            the transform function for blue channel
	 */
	public FakeColorTransform(Function red, Function green, Function blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public ColorImage operate(GrayImage source)
	{
		int width = source.width();
		int height = source.height();
		ColorImage ci = new ColorImage(width, height);
		int p;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
			{
				p = source.getPixel(x, y);
				ci.setPixel(x, y, 255, (int) red.function(p),
						(int) green.function(p), (int) blue.function(p));
			}
		return ci;
	}

	/**
	 * Returns a <tt>FakeColorTransform</tt> instance whose color transformation
	 * is based on sine function and the function divide colors into different
	 * terms.
	 * 
	 * @param termCount
	 *            the terms amount in the scale [0,256)
	 * @param ratio
	 *            the delays of signal in each color channel, ratio is the delay
	 *            time divides the whole term
	 * @return the transform instance
	 */
	public static FakeColorTransform getTermBasedInstance(double termCount,
			double ratio)
	{
		double phi = -ratio * Math.PI;
		double omega = 2 * Math.PI / 256;
		double a = 255.0;
		double b = 0.0;
		return new FakeColorTransform(new AbsoluteSineFunction(a, omega, 0, b),
				new AbsoluteSineFunction(a, omega, phi, b),
				new AbsoluteSineFunction(a, omega, phi * 2, b));
	}
}
