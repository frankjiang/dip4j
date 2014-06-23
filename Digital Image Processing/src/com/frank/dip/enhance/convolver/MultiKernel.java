/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. BiKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * The multiple kernel contains two or more single kernels.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MultiKernel implements Kernel
{
	/**
	 * The two single kernel inside.
	 */
	protected SingleKernel[]	kernels;
	/**
	 * The width of the kernel.
	 */
	protected int				width;
	/**
	 * The height of the kernel.
	 */
	protected int				height;

	/**
	 * Construct an empty instance of <tt>MultiKernel</tt>.
	 */
	protected MultiKernel()
	{
		// do nothing
	}

	/**
	 * Construct an instance of <tt>BiKernel</tt>.
	 * 
	 * @param kernels
	 *            single kernels list
	 */
	public MultiKernel(SingleKernel... kernels)
	{
		initialize(kernels);
	}

	/**
	 * Initialize the multiple kernel.
	 * 
	 * @param kernels
	 *            single kernels list
	 */
	protected void initialize(SingleKernel... kernels)
	{
		if (kernels == null || kernels.length == 0)
			throw new NullPointerException("The kernels are empty.");
		width = kernels[0].width;
		height = kernels[0].height;
		for (int i = 1; i < kernels.length; i++)
			if (width != kernels[i].width || height != kernels[i].height)
				throw new NullPointerException(
						"The kernels dimension do not match.");
		this.kernels = kernels;
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
	 * @see com.frank.dip.enhance.convolver.Kernel#perform(float[])
	 */
	@Override
	public float perform(float... pixels)
	{
		float max = Float.NEGATIVE_INFINITY;
		float t;
		for (Kernel kernel : kernels)
		{
			t = kernel.perform(pixels);
			if (max < t)
				max = t;
		}
		return max;
	}
}
