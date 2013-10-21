/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * GeometricMeanKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * Geometric mean filter kernel.
 * <p>
 * <strong>Pattern</strong>:<br>
 * s(x,y) = (&prod;g(x,y))<sup>1/(w*h)</sup>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GeometricMeanKernel extends SingleKernel
{
	/**
	 * The production of the geometric mean kernel.
	 */
	protected double	product;

	/**
	 * Construct an instance of <tt>GeometricMeanKernel</tt>.
	 * 
	 * @param width
	 *            the kernel pattern width
	 * @param height
	 *            the kernel pattern height
	 */
	public GeometricMeanKernel(int width, int height)
	{
		float[] values = new float[width * height];
		for (int i = 0; i < values.length; i++)
			values[i] = 1;
		initialize(width, height, values);
		product = values.length;
	}

	/**
	 * Construct an instance of <tt>GeometricMeanKernel</tt>.
	 * 
	 * @param width
	 *            the kernel pattern width
	 * @param height
	 *            the kernel pattern height
	 * @param values
	 *            the kernel pattern values
	 */
	public GeometricMeanKernel(int width, int height, float... values)
	{
		initialize(width, height, values);
		product = 0;
		for (int i = 0; i < values.length; i++)
			product += values[i];
	}

	/**
	 * @see com.frank.dip.enhance.convolver.SingleKernel#perform(int[])
	 */
	public float perform(int... pixels)
	{
		if (product <= 0)
			return super.perform(pixels);
		else
		{
			double prod = 1.0;
			for (int i = 0; i < pixels.length && i < kernel.length; i++)
				if (pixels[i] != 0)
				{
					if (kernel[i] == 1)
						prod *= pixels[i];
					else
						prod *= Math.pow(pixels[i], kernel[i]);
				}
			long v = Math.round(Math.pow(prod, 1.0 / product));
			if (v < 0)
				return 0;
			else if (v < COLOR_SCALE_LEVEL)
				return (int) v;
			else
				return COLOR_SCALE_LEVEL - 1;
		}
	}

	/**
	 * Set the production of the kernel.
	 * 
	 * @param product
	 *            the product value
	 */
	public void setProduct(double product)
	{
		if (product <= 0)
			throw new IllegalArgumentException(String.format(
					"Production(%f) must be positive.", product));
		this.product = product;
	}
}
