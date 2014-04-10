/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ButternworthLowPassFilter.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.frequency;

import java.awt.Point;

import com.frank.dip.Image;
import com.frank.dip.math.Function;

/**
 * The function based Fourier Filter.
 * <p>
 * H(u,v) = f(D(u,v))<br>
 * f(x): the distance base filter function
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class FourierDistanceFunctionFilter extends AbstractFourierFilter
{
	/**
	 * The distance base filter function.
	 */
	protected Function	function;

	/**
	 * Construct an instance of <tt>FourierDistanceFunctionFilter</tt>.
	 * 
	 * @param function
	 *            the frequency domain filter function
	 */
	public FourierDistanceFunctionFilter(Function function)
	{
		this.function = function;
	}

	/**
	 * @see com.frank.dip.analyze.FourierTransformation#complex(com.frank.dip.Image)
	 */
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
}
