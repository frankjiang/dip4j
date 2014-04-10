/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * HarmonicMeanKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * Harmonic mean filter kernel.
 * <p>
 * <strong>Pattern</strong>:<br>
 * s(x,y) = w*h / (&sum;1/g(x,y))
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class HarmonicMeanKernel extends SingleKernel
{
	/**
	 * Harmonic mean kernel.
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
	 * Harmonic mean kernel.
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
	 * The summary of the harmonic mean kernel.
	 * <p>
	 * {@code summary} = &sum;coef/g(x,y)
	 * </p>
	 */
	protected double		summary;

	/**
	 * Construct an instance of <tt>HarmonicMeanKernel</tt>.
	 * 
	 * @param width
	 *            the kernel pattern width
	 * @param height
	 *            the kernel pattern height
	 */
	public HarmonicMeanKernel(int width, int height)
	{
		float[] values = new float[width * height];
		for (int i = 0; i < values.length; i++)
			values[i] = 1;
		initialize(width, height, values);
		summary = values.length;
	}

	/**
	 * Construct an instance of <tt>HarmonicMeanKernel</tt>.
	 * 
	 * @param width
	 *            the kernel pattern width
	 * @param height
	 *            the kernel pattern height
	 * @param values
	 *            the kernel pattern values
	 */
	public HarmonicMeanKernel(int width, int height, float... values)
	{
		initialize(width, height, values);
		summary = 0;
		for (int i = 0; i < values.length; i++)
			summary += 1.0 / values[i];
		// summary = 1 / summary;
	}

	/**
	 * Construct an instance of <tt>HarmonicMeanKernel</tt>.
	 * 
	 * @param type
	 *            the harmonic mean kernel type
	 * @see #KERNEL_9
	 * @see #KERNEL_16
	 */
	public HarmonicMeanKernel(int type)
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
						"Unknown harmonic smooth convolution type: %s", type));
		}
	}

	/**
	 * @see com.frank.dip.enhance.convolver.SingleKernel#perform(int[])
	 */
	public float perform(int... pixels)
	{
		if (summary <= 0)
			return super.perform(pixels);
		else
		{
			double sum = 0.0;
			for (int i = 0; i < pixels.length && i < kernel.length; i++)
				if (pixels[i] != 0)
				{
					if (kernel[i] == 1)
						sum += 1.0 / pixels[i];
					else
						sum += kernel[i] / pixels[i];
				}
			long v = Math.round(summary / sum);
			if (v < 0)
				return 0;
			else if (v < COLOR_SCALE_LEVEL)
				return (int) v;
			else
				return COLOR_SCALE_LEVEL - 1;
		}
	}

	/**
	 * Set the summary of the kernel.
	 * 
	 * @param summary
	 *            the product value
	 */
	public void setSummary(double summary)
	{
		if (summary <= 0)
			throw new IllegalArgumentException(String.format(
					"Summary(%f) must be positive.", summary));
		this.summary = summary;
	}
}
