/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. ArithmeticMeanKernel.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * Arithmetic mean filter kernel.
 * <p>
 * <strong>Pattern</strong>:<br>
 * s(x,y) = 1/(w*h) * &sum;g(x,y)
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ArithmeticMeanKernel extends SingleKernel
{
	/**
	 * Arithmetic mean kernel.
	 * <table>
	 * <tr align="right">
	 * <td></td>
	 * <td width="16%">|</td>
	 * <td width="16%">1</td>
	 * <td width="16%">1</td>
	 * <td width="16%">1</td>
	 * <td width="16%">|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>1/9&times;</td>
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right" >
	 * <td></td>
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * </table>
	 */
	public static final int	KERNEL_9	= 9;
	/**
	 * Arithmetic mean kernel.
	 * <table>
	 * <tr align="right">
	 * <td></td>
	 * <td width="16%">|</td>
	 * <td width="16%">1</td>
	 * <td width="16%">2</td>
	 * <td width="16%">1</td>
	 * <td width="16%">|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>1/16&times;</td>
	 * <td>|</td>
	 * <td>2</td>
	 * <td>4</td>
	 * <td>2</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right" >
	 * <td></td>
	 * <td>|</td>
	 * <td>1</td>
	 * <td>2</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * </table>
	 */
	public static final int	KERNEL_16	= 16;
	/**
	 * The summary of the kernel values.
	 */
	protected float			summary		= 0.0f;

	/**
	 * Construct an instance of whole 1 <tt>ArithmeticMeanKernel</tt> with
	 * specified pattern dimension.
	 * 
	 * @param width
	 *            the kernel pattern width
	 * @param height
	 *            the kernel pattern height
	 */
	public ArithmeticMeanKernel(int width, int height)
	{
		float[] values = new float[width * height];
		for (int i = 0; i < values.length; i++)
			values[i] = 1;
		initialize(width, height, values);
		summary = width * height;
	}

	/**
	 * Construct an instance of <tt>ArithmeticMeanKernel</tt> with specified
	 * kernel.
	 * 
	 * @param width
	 *            the kernel pattern width
	 * @param height
	 *            the kernel pattern height
	 * @param values
	 *            the kernel pattern values
	 */
	public ArithmeticMeanKernel(int width, int height, float... values)
	{
		initialize(width, height, values);
		summary = 0;
		for (int i = 0; i < values.length; i++)
			summary += values[i];
	}

	/**
	 * Construct an instance of <tt>ArithmeticMeanKernel</tt>.
	 * 
	 * @param type
	 *            the arithmetic mean kernel type
	 * @see #KERNEL_9
	 * @see #KERNEL_16
	 */
	public ArithmeticMeanKernel(int type)
	{
		switch (type)
		{
			case KERNEL_9:
				initialize(3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1);
				summary = 9;
				break;
			case KERNEL_16:
				initialize(3, 3, 1, 2, 1, 2, 4, 2, 1, 2, 1);
				summary = 16;
				break;
			default:
				throw new IllegalArgumentException(String.format(
						"Unknown Linear smooth convolution type: %s", type));
		}
	}

	/**
	 * @see com.frank.dip.enhance.convolver.SingleKernel#perform(float[])
	 */
	public float perform(float... pixels)
	{
		if (summary <= 0)
			return super.perform(pixels);
		else
		{
			float k = 0.0f;
			for (int i = 0; i < pixels.length && i < kernel.length; i++)
				k += pixels[i] * kernel[i];
			int v = Math.round(k / summary);
			if (v < 0)
				return 0;
			else if (v < COLOR_SCALE_LEVEL)
				return v;
			else
				return COLOR_SCALE_LEVEL - 1;
		}
	}

	/**
	 * Set the summary of the mean kernel.
	 * 
	 * @param summary
	 *            the summary value
	 */
	public void setSummary(float summary)
	{
		if (summary <= 0)
			throw new IllegalArgumentException(String.format(
					"Summary(%f) must be positive.", summary));
		this.summary = summary;
	}
}
