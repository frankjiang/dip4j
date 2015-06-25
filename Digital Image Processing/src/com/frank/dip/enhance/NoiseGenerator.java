/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * NoiseGenerator.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance;

import java.util.Random;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.Operator;
import com.frank.dip.math.Function;

/**
 * The image noise generator.
 * <p>
 * In this class, the normal procedure of the image noise generatinng is
 * defined.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class NoiseGenerator extends Operator<Image, Image>
{
	/**
	 * The probability distribution function of the noise.
	 */
	protected Function	pdf;
	/**
	 * The random generator.
	 */
	protected Random	random;
	/**
	 * The minimum value of the generated noise.
	 */
	protected double	minimum;
	/**
	 * The maximum value of the generated nose.
	 */
	protected double	maximum;

	/**
	 * Construct an instance of <tt>NoiseGenerator</tt>.
	 * 
	 * @param pdf
	 *            the probability distribution function of the noise
	 */
	public NoiseGenerator(Function pdf)
	{
		this.pdf = pdf;
		random = new Random();
	}

	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public Image operate(Image source)
	{
		if (source instanceof GrayImage)
			return operateGray((GrayImage) source);
		if (source instanceof ColorImage)
			return operateColor((ColorImage) source);
		if (source instanceof GrayImage)
			return operateBinary((BinaryImage) source);
		throw new IllegalArgumentException(String.format(
				"Unsupported image type: %s", source.getClass()));
	}

	private GrayImage operateGray(GrayImage source)
	{
		GrayImage res = source.clone();
		for (int y = 0; y < res.getHeight(); y++)
			for (int x = 0; x < res.getWidth(); x++)
				res.setPixel(x, y, (int) (source.getPixel(x, y) + bias()));
		return res;
	}

	private BinaryImage operateBinary(BinaryImage source)
	{
		BinaryImage res = source.clone();
		for (int y = 0; y < res.getHeight(); y++)
			for (int x = 0; x < res.getWidth(); x++)
				res.setPixel(x, y, (int) (source.getPixel(x, y) + bias()));
		return res;
	}

	private ColorImage operateColor(ColorImage source)
	{
		ColorImage res = source.clone();
		for (int y = 0; y < res.getHeight(); y++)
			for (int x = 0; x < res.getWidth(); x++)
			{
				res.setRed(x, y, (int) (source.getBlue(x, y) + bias()));
				res.setGreen(x, y, (int) (source.getGreen(x, y) + bias()));
				res.setBlue(x, y, (int) (source.getBlue(x, y) + bias()));
			}
		return res;
	}

	private double rand(double min, double max)
	{
		return random.nextDouble() * (max - min) + min;
	}

	/**
	 * Returns the noise random bias.
	 * 
	 * @return the noise random bias.
	 */
	protected double bias()
	{
		double x, y, p;
		do
		{
			x = rand(minimum, maximum);
			y = pdf.function(x);
			p = rand(0, y);
		}
		while (p > y);
		random.nextGaussian();
		return x;
	}

	/**
	 * Getter for the minimum noise value.
	 * 
	 * @return the minimum noise value
	 */
	public double getMinimum()
	{
		return minimum;
	}

	/**
	 * Setter for the minimum noise value.
	 * 
	 * @param minimum
	 *            the minimum noise value
	 */
	public void setMinimum(double minimum)
	{
		this.minimum = minimum;
	}

	/**
	 * Returns the maximum noise value.
	 * 
	 * @return the maximum noise value
	 */
	public double getMaximum()
	{
		return maximum;
	}

	/**
	 * Setter for the maximum noise value.
	 * 
	 * @param maximum
	 *            the the maximum noise value
	 */
	public void setMaximum(double maximum)
	{
		this.maximum = maximum;
	}

	/**
	 * Returns the probability distribution function of the noise.
	 * 
	 * @return the probability distribution function of the noise
	 */
	public Function getPdf()
	{
		return pdf;
	}
}
