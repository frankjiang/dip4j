/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * GuassNoiseGenerator.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance;

import java.util.Random;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;
import com.frank.dip.Operator;

/**
 * The Guass distribution subjected noise generator.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GuassNoiseGenerator extends Operator<Image, Image>
{
	/**
	 * The parameter of mean: <code>&mu;</code>
	 */
	protected double	mu;
	/**
	 * The parameter of standard deviation: <code>&sigma;</code>
	 */
	protected double	sigma;
	/**
	 * The random generator.
	 */
	protected Random	random;

	/**
	 * Construct an instance of <tt>GuassNoiseGenerator</tt>.
	 * 
	 * @param mu
	 *            the mean
	 * @param sigma
	 *            the standard deviation
	 */
	public GuassNoiseGenerator(double mu, double sigma)
	{
		this.mu = mu;
		this.sigma = sigma;
		random = new Random();
	}

	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public Image operate(Image source) throws IllegalImageTypeException
	{
		if (source instanceof GrayImage)
			return operateGray((GrayImage) source);
		if (source instanceof ColorImage)
			return operateColor((ColorImage) source);
		if (source instanceof GrayImage)
			return operateBinary((BinaryImage) source);
		throw new IllegalImageTypeException(getClass(), source.getClass());
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

	/**
	 * Returns the noise random bias.
	 * 
	 * @return the noise random bias.
	 */
	protected double bias()
	{
		double b = random.nextGaussian();
		b *= sigma;
		b += mu;
		return b;
	}

	/**
	 * Getter for mu.
	 * 
	 * @return the mu
	 */
	public double getMu()
	{
		return mu;
	}

	/**
	 * Setter for mu.
	 * 
	 * @param mu
	 *            the value of mu
	 */
	public void setMu(double mu)
	{
		this.mu = mu;
	}

	/**
	 * Getter for sigma.
	 * 
	 * @return the sigma
	 */
	public double getSigma()
	{
		return sigma;
	}

	/**
	 * Setter for sigma.
	 * 
	 * @param sigma
	 *            the value of sigma
	 */
	public void setSigma(double sigma)
	{
		this.sigma = sigma;
	}
}
