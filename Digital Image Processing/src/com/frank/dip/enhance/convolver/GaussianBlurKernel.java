/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * GaussianBlurKernel.java is PROPRIETARY/CONFIDENTIAL built in 10:14:27 AM, Mar
 * 5, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * The Gaussian blur kernel.
 * <p>
 * Use G(u,v) = exp(&minus;(u<sup>2</sup>+v<sup>2</sup>)/2&sigma;<sup>2</sup>)
 * /&radic;(2&pi;&sigma;<sup>2</sup>) as kernel.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GaussianBlurKernel extends SingleKernel
{
	/**
	 * Construct an instance of <tt>GaussianBlurKernel</tt>.
	 * <p>
	 * In this constructor, the Gaussian blur patch will be a circle.
	 * </p>
	 * 
	 * @param radius
	 *            the radius of the patch
	 * @param sigma
	 *            the standard deviation
	 */
	public GaussianBlurKernel(float radius, float sigma)
	{
		super((int) radius, (int) radius);
		if (sigma <= 0)
			throw new IllegalArgumentException("Sigma value must be possitive.");
		kernel = new float[width * height];
		float centerX = (width % 2 != 0 ? (width - 1) : width) / 2.0f;
		float centerY = (height % 2 != 0 ? (height - 1) : height) / 2.0f;
		float u, v, distance = radius * radius, sum, summary = 0.0f;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				u = centerX - x;
				v = centerY - y;
				sum = u * u + v * v;
				kernel[y * width + x] = (sum < distance) ? gaussian(sum, sigma)
						: 0;
				summary += kernel[y * width + x];
			}
		for (int i = 0; i < kernel.length; i++)
			kernel[i] /= summary;
	}

	/**
	 * Construct an instance of <tt>GaussianBlurKernel</tt>.
	 * <p>
	 * In this constructor, the Gaussian blur patch will be a square.
	 * </p>
	 * 
	 * @param width
	 *            the width of the patch
	 * @param height
	 *            the height of the patch
	 * @param sigma
	 *            the standard deviation
	 */
	public GaussianBlurKernel(int width, int height, float sigma)
	{
		super(width, height);
		if (sigma <= 0)
			throw new IllegalArgumentException("Sigma value must be possitive.");
		kernel = new float[width * height];
		float centerX = (width % 2 != 0 ? (width - 1) : width) / 2.0f;
		float centerY = (height % 2 != 0 ? (height - 1) : height) / 2.0f;
		float u, v, summary = 0.0f;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				u = centerX - x;
				v = centerY - y;
				kernel[y * width + x] = gaussian(u * u + v * v, sigma);
				summary += kernel[y * width + x];
			}
		for (int i = 0; i < kernel.length; i++)
			kernel[i] /= summary;
	}

	/**
	 * The guassian function.
	 * 
	 * @param sum
	 *            value of <code>u<sup>2</sup>+v<sup>2</sup></code>
	 * @param sigma
	 *            the standard deviation
	 * @return the guassian value
	 */
	public static final float gaussian(float sum, float sigma)
	{
		sigma = sigma * sigma;
		return (float) (Math.exp(-sum / 2.0 / sigma) / Math.sqrt(2 * Math.PI
				* sigma));
	}
}
