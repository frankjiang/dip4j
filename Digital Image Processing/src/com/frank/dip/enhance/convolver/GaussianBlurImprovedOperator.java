/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * GaussianBlurImprovedOperator.java is PROPRIETARY/CONFIDENTIAL built in
 * 3:42:48 PM, Mar 5, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.Operator;
import com.frank.dip.geom.Geometry;
import com.frank.dip.geom.GeometryColor;
import com.frank.dip.geom.GeometryGray;

/**
 * The improved Gaussian blur operator.
 * <p>
 * In this operator use two 1D convolution to solve the 2D Gaussian blur
 * problem.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GaussianBlurImprovedOperator<T extends Image> extends
		Operator<T, T>
{
	/**
	 * The standard deviant value.
	 */
	protected double			sigma;
	/**
	 * The dimension of the &sigma; value.
	 */
	private static final int	DIMENSION	= 3;

	/**
	 * Construct an instance of <tt>GaussianBlurImprovedOperator</tt>.
	 * 
	 * @param sigma
	 *            the standard deviant value.
	 */
	public GaussianBlurImprovedOperator(double sigma)
	{
		setSigma(sigma);
	}

	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public T operate(T source)
	{
		if (source instanceof BinaryImage)
			return (T) source.clone();
		if (source instanceof GrayImage)
			return (T) convolveGray((GrayImage) source);
		if (source instanceof ColorImage)
			return (T) convolveColor((ColorImage) source);
		throw new IllegalArgumentException();
	}

	/**
	 * @param src
	 * @return
	 */
	private GrayImage convolveGray(GrayImage source)
	{
		int width = source.width() / 2;
		int height = source.height() / 2;
		// int ksize = (int) Math.ceil(sigma * DIMENSION) * 2 + 1;
		int ksize = 7;
		if (ksize == 1)
			return source.clone();
		GeometryGray geom = new GeometryGray(Geometry.TYPE_BICUBIC,
				Geometry.FILL_WITH_BLANK);
		GrayImage src = geom.scale(source, width, height);
		double[] kernel = new double[ksize];
		double scale = -.5 / (sigma * sigma);
		double cons = 1 / Math.sqrt(-scale / Math.PI);
		double sum = 0;
		int kcenter = ksize / 2;
		int i = 0, x, y;
		for (i = 0; i < ksize; i++)
		{
			x = i - kcenter;
			kernel[i] = cons * Math.exp(x * x * scale);
			sum += kernel[i];
		}
		for (i = 0; i < ksize; i++)
			kernel[i] /= sum;
		GrayImage dst = new GrayImage(width, height);
		GrayImage temp = new GrayImage(width, height);
		double mul = 0;
		// X-direction 1D Gaussian blur
		for (y = 0; y < height; y++)
			for (x = 0; x < width; x++)
			{
				sum = mul = 0;
				for (i = -kcenter; i <= kcenter; i++)
					if ((x + i) >= 0 && (x + i) < width)
					{
						mul += src.getPixel(x, y) * kernel[kcenter + i];
						sum += kernel[kcenter + i];
					}
				temp.setPixel(x, y, (int) Math.round(mul / sum));
			}
		// Y-direction 1D Gaussian blur
		for (x = 0; x < width; x++)
			for (y = 0; y < height; y++)
			{
				sum = mul = 0;
				for (i = -kcenter; i <= kcenter; i++)
					if ((y + i) >= 0 && (y + i) < height)
					{
						mul += temp.getPixel(x, y + i) * kernel[kcenter + i];
						sum += kernel[kcenter + i];
					}
				dst.setPixel(x, y, (int) Math.round(mul / sum));
			}
		return geom.scale(dst, source.width(), source.height());
	}

	/**
	 * @param src
	 * @return
	 */
	private ColorImage convolveColor(ColorImage source)
	{
		int width = source.width() / 2;
		int height = source.height() / 2;
		int ksize = (int) Math.ceil(sigma * DIMENSION) * 2 + 1;
		if (ksize == 1)
			return source.clone();
		GeometryColor geom = new GeometryColor(Geometry.TYPE_BICUBIC,
				Geometry.FILL_WITH_BLANK);
		ColorImage src = geom.scale(source, width, height);
		double[] kernel = new double[ksize];
		double scale = -.5f / (sigma * sigma);
		double cons = 1 / Math.sqrt(-scale / Math.PI);
		double sum = 0;
		int kcenter = ksize / 2;
		int i = 0, x, y;
		for (i = 0; i < ksize; i++)
		{
			x = i - kcenter;
			kernel[i] = cons * Math.exp(x * x * scale);
			sum += kernel[i];
		}
		for (i = 0; i < ksize; i++)
			kernel[i] /= sum;
		ColorImage dst = new ColorImage(width, height);
		ColorImage temp = new ColorImage(width, height);
		double bmul = 0, gmul = 0, rmul = 0;
		// X-direction 1D Gaussian blur
		for (y = 0; y < height; y++)
			for (x = 0; x < width; x++)
			{
				sum = bmul = gmul = rmul = 0;
				for (i = -kcenter; i <= kcenter; i++)
					if ((x + i) >= 0 && (x + i) < width)
					{
						rmul += src.getBlue(x + i, y) * kernel[kcenter + i];
						gmul += src.getBlue(x + i, y) * kernel[kcenter + i];
						bmul += src.getBlue(x + i, y) * kernel[kcenter + i];
						sum += kernel[kcenter + i];
					}
				temp.setPixel(x, y, src.getAlpha(x, y),
						(int) Math.round(rmul / sum),
						(int) Math.round(gmul / sum),
						(int) Math.round(bmul / sum));
			}
		// Y-direction 1D Gaussian blur
		for (x = 0; x < width; x++)
			for (y = 0; y < height; y++)
			{
				sum = bmul = gmul = rmul = 0;
				for (i = -kcenter; i <= kcenter; i++)
					if ((y + i) >= 0 && (y + i) < height)
					{
						rmul += temp.getRed(x, y + i) * kernel[kcenter + i];
						gmul += temp.getGreen(x, y + i) * kernel[kcenter + i];
						bmul += temp.getBlue(x, y + i) * kernel[kcenter + i];
						sum += kernel[kcenter + i];
					}
				dst.setPixel(x, y, src.getAlpha(x, y),
						(int) Math.round(rmul / sum),
						(int) Math.round(gmul / sum),
						(int) Math.round(bmul / sum));
			}
		return geom.scale(dst, source.width(), source.height());
	}

	/**
	 * Returns sigma.
	 * 
	 * @return the sigma
	 */
	public double getSigma()
	{
		return sigma;
	}

	/**
	 * Set the value of &sigma;.
	 * 
	 * @param sigma
	 *            the value of &sigma;
	 */
	public void setSigma(double sigma)
	{
		if (sigma <= 0)
			throw new IllegalArgumentException("Sigma value must be possitive.");
		this.sigma = sigma;
	}
}
