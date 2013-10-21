/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * HomomorphicFilter.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.frequency;

import java.awt.Point;

import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.math.Function;
import com.frank.math.Complex;
import com.frank.math.FFT2D;

/**
 * Homomorphic filter.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class HomomorphicFilter extends AbstractFourierFilter
{
	/**
	 * The distance filter function.
	 */
	protected Function	function;

	/**
	 * Construct an instance of <tt>HomomorphicFilter</tt>.
	 * 
	 * @param function
	 *            the distance filter function
	 */
	public HomomorphicFilter(Function function)
	{
		this.function = function;
	}

	/**
	 * @see com.frank.dip.analyze.FourierTransformation#complex(com.frank.dip.Image)
	 */
	@Override
	public void complex(Image source)
	{
		super.complex(source);
		double cx = width / 2.0;
		double cy = height / 2.0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				complex[y][x] = complex[y][x].multiply(function.function(Point
						.distance(cx, cy, x, y)));
	}

	/**
	 * @see com.frank.dip.analyze.FourierTransformation#createComplex(com.frank.dip.Image)
	 */
	@Override
	protected Complex[][] createComplex(Image source)
	{
		Complex[][] c = new Complex[height][width];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				c[y][x] = new Complex(((x + y) % 2 == 0 ? 1 : -1)
						* Math.log1p(source.getPixel(x, y)));
		return c;
	}

	/**
	 * @see com.frank.dip.analyze.FourierTransformation#backward()
	 */
	@Override
	public GrayImage backward()
	{
		GrayImage gi = new GrayImage(width, height);
		Complex[][] c = FFT2D.ifft(complex);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				gi.setPixel(
						x,
						y,
						(int) Math.round(Math.exp(((x + y) % 2 == 0 ? 1 : -1)
								* c[y][x].real) - 1));
		return gi;
	}
}
