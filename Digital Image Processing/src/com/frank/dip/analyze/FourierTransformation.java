/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. FastFourierTransfrom.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.analyze;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;
import com.frank.dip.enhance.GrayScaleCoefficient;
import com.frank.math.Complex;
import com.frank.math.FFT2D;

/**
 * The Fourier transformation kernel.
 * <p>
 * In this class, the normal procedure of Fourier transformation is defined.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class FourierTransformation
{
	/**
	 * The complex matrix.
	 */
	protected Complex[][]	complex;
	/**
	 * The logarithm parameter of the Fourier transformation.
	 * <p>
	 * The logarithm parameter is used for scaling the energy output
	 * &radic;(real<sup>2</sup>+imaginary<sup>2</sup>) of the transformed
	 * frequency values.
	 * </p>
	 */
	protected double		logarithm	= 0.0;
	/**
	 * The source image width.
	 */
	protected int			width;
	/**
	 * The source image height.
	 */
	protected int			height;

	/**
	 * Create the complex matrix of the source image.
	 * 
	 * @param source
	 *            the source image
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public void complex(Image source) throws IllegalImageTypeException
	{
		width = source.width();
		height = source.height();
		if (source instanceof GrayImage || source instanceof BinaryImage)
			complex = FFT2D.fft(createComplex(source));
		else if (source instanceof ColorImage)
			complex = FFT2D.fft(createComplex(new GrayScaleCoefficient()
					.operate((ColorImage) source)));
		else
			throw new IllegalImageTypeException(getClass(), source.getClass());
	}

	/**
	 * Perform forward Fourier transformation to the source image.
	 * 
	 * @param source
	 *            the source image
	 * @return the frequency image
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public GrayImage forward(Image source) throws IllegalImageTypeException
	{
		width = source.width();
		height = source.height();
		if (source instanceof GrayImage || source instanceof BinaryImage)
			return perform(source);
		if (source instanceof ColorImage)
			return perform(new GrayScaleCoefficient()
					.operate((ColorImage) source));
		throw new IllegalImageTypeException(getClass(), source.getClass());
	}

	/**
	 * Perform forward discrete fast Fourier transformation to the source image.
	 * 
	 * @param source
	 *            the source image
	 * @return the frequency image
	 */
	protected GrayImage perform(Image source)
	{
		complex(source);
		double[][] map = new double[height][width];
		double max = -1.0, t;
		double base = Math.sqrt(width * height);
		for (int y = 0; y < complex.length; y++)
			for (int x = 0; x < complex[y].length; x++)
			{
				t = complex[y][x].abs() / base;
				map[y][x] = t;
				if (t > max)
					max = t;
			}
		logarithm = Math.log1p(max) / 255;
		GrayImage res = new GrayImage(width, height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				res.setPixel(x, y, (int) (Math.log1p(map[y][x]) / logarithm));
		return res;
	}

	/**
	 * Perform backward Fourier transformation according the parameters in the
	 * transformation.
	 * 
	 * @return the restored image
	 */
	public GrayImage backward()
	{
		GrayImage gi = new GrayImage(width, height);
		Complex[][] c = FFT2D.ifft(complex);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				gi.setPixel(
						x,
						y,
						((x + y) % 2 == 0 ? 1 : -1)
								* (int) Math.round(c[y][x].real));
		return gi;
	}

	/**
	 * Create the complex matrix.
	 * <p>
	 * The real part of the complex will be the value of pixel and the imaginary
	 * part will be 0.
	 * </p>
	 * 
	 * @param source
	 *            the source image
	 * @return the complex matrix of the source image
	 */
	protected Complex[][] createComplex(Image source)
	{
		Complex[][] c = new Complex[height][width];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				c[y][x] = new Complex(((x + y) % 2 == 0 ? 1 : -1)
						* source.getPixel(x, y));
		return c;
	}

	/**
	 * Returns the complex matrix.
	 * 
	 * @return the complex matrix
	 */
	public Complex[][] getComplex()
	{
		return complex;
	}

	/**
	 * Set the complex matrix.
	 * 
	 * @param complex
	 *            the complex matrix
	 */
	public void setComplex(Complex[][] complex)
	{
		this.complex = complex;
	}

	/**
	 * Returns the image width.
	 * 
	 * @return the image width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Set the image width.
	 * 
	 * @param width
	 *            the image width
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * Returns the image height.
	 * 
	 * @return the image height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Set the image height.
	 * 
	 * @param height
	 *            the image height
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
}
