/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. FilterKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * The single kernel is kernel structure with only one matrix inside.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SingleKernel implements Kernel
{
	/**
	 * The kernel array.
	 */
	public float[]	kernel;
	/**
	 * The width of the kernel.
	 */
	protected int	width;
	/**
	 * The height of the kernel.
	 */
	protected int	height;

	/**
	 * Construct an empty instance of <tt>SingleKernel</tt>.
	 */
	protected SingleKernel()
	{
		// do nothing
	}

	/**
	 * Construct an instance of <tt>FilterKernel</tt>.
	 * 
	 * @param width
	 *            the width of the kernel
	 * @param height
	 *            the height of the kernel
	 * @param values
	 *            the kernel values
	 */
	public SingleKernel(int width, int height, float... values)
	{
		initialize(width, height, values);
	}

	/**
	 * Initialize the kernel.
	 * 
	 * @param width
	 *            the width of the kernel
	 * @param height
	 *            the height of the kernel
	 * @param values
	 *            the kernel values
	 */
	protected void initialize(int width, int height, float... values)
	{
		this.width = width;
		this.height = height;
		kernel = new float[width * height];
		for (int i = 0; i < kernel.length && i < values.length; i++)
			kernel[i] = values[i];
	}

	/**
	 * @see com.frank.dip.enhance.convolver.Kernel#width()
	 */
	public int width()
	{
		return width;
	}

	/**
	 * @see com.frank.dip.enhance.convolver.Kernel#height()
	 */
	public int height()
	{
		return height;
	}

	/**
	 * Returns the value in the specified kernel position.
	 * 
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 * @return the value of the kernel
	 */
	public double value(int x, int y)
	{
		if (x < 0 || y < 0 || x >= width || y >= height)
			return 0.0;
		else
			return kernel[y * width + x];
	}

	/**
	 * @see com.frank.dip.enhance.convolver.Kernel#perform(float[])
	 */
	public float perform(float... pixels)
	{
		float k = 0.0f;
		for (int i = 0; i < pixels.length && i < kernel.length; i++)
			k += pixels[i] * kernel[i];
		return k;
	}

	/**
	 * Returns the equals instance of {@linkplain java.awt.image.Kernel}.
	 * 
	 * @return the {@linkplain java.awt.image.Kernel} instance
	 */
	public java.awt.image.Kernel toKernel()
	{
		return new java.awt.image.Kernel(width, height, kernel);
	}
}
