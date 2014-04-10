/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractGridFeature.java is PROPRIETARY/CONFIDENTIAL built in 8:02:44 PM, Apr
 * 10, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import com.frank.dip.BinaryImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.feature.FeatureExtractor;
import com.frank.dip.feature.Sample;
import com.frank.dip.threshold.GlobalThresholding;
import com.frank.dip.threshold.Otsu;
import com.frank.dip.threshold.Thresholding;

/**
 * TODO
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractGridFeature extends FeatureExtractor<Image>
{
	/**
	 * The thresholding method of transfer a other color type image to binary
	 * image.
	 */
	protected Thresholding	thresholding;
	/**
	 * The rows of the grid.
	 */
	protected int			rows;
	/**
	 * The columns of the grid.
	 */
	protected int			columns;
	/**
	 * If <code>true</code> do normalization to the result.
	 */
	protected boolean		isScaled;

	/**
	 * Construct an default instance of <tt>ExtractGridFeature</tt>.
	 * <p>
	 * The grid will be 6&times;6, with scaled values and use
	 * {@linkplain com.frank.dip.threshold.Otsu
	 * Otus method} as thresholding.
	 * </p>
	 */
	public ExtractGridFeature()
	{
		this(6, 6, new GlobalThresholding(new Otsu()), true);
	}

	/**
	 * Construct an instance of <tt>ExtractGridFeature</tt>.
	 * 
	 * @param rows
	 *            the rows of the grid
	 * @param columns
	 *            the columns of the grid
	 * @param thresholding
	 *            the thresholding method
	 * @param if <code>true</code> do normalization to the result
	 */
	public ExtractGridFeature(int rows, int columns, Thresholding thresholding,
			boolean isScaled)
	{
		this.thresholding = thresholding;
		this.rows = rows;
		this.columns = columns;
		this.isScaled = isScaled;
	}

	/**
	 * @see com.frank.dip.feature.FeatureExtractor#extract(com.frank.dip.Image,
	 *      java.lang.Double)
	 */
	@Override
	public Sample extract(Image image, Double target)
	{
		// prepare binary image
		int width = image.width();
		int height = image.height();
		if (width < columns || height < rows)
			throw new IllegalArgumentException(
					String.format(
							"Grid (rows:%d, columns:%d) cannot fit the image of size (width: %d, height:%d).",
							rows, columns, width, height));
		boolean[][] bi;
		if (image instanceof BinaryImage)
			bi = ((BinaryImage) image).getBinaryMatrix();
		else if (image instanceof GrayImage)
			bi = (thresholding.operate((GrayImage) image)).getBinaryMatrix();
		else
			bi = (thresholding.operate(new GrayImage(image))).getBinaryMatrix();
		Sample s = new Sample(rows * columns);
		s.setTarget(target);
		// build integral image
		double summary = 0.0;
		int counter;
		double[][] integral = new double[height + 1][width + 1];
		for (int y = 1; y <= height; y++, summary += counter)
		{
			counter = 0;
			for (int x = 1; x <= width; x++)
			{
				if (bi[y - 1][x - 1])
					counter++;
				integral[y][x] = integral[y - 1][x] + counter;
			}
		}
		// do normalization
		if (isScaled)
			for (int y = 1; y <= height; y++)
				for (int x = 1; x <= width; x++)
					integral[y][x] /= summary;
		// creating grids
		double stepX = width / (double) rows;
		double stepY = height / (double) columns;
		int x0, y0, x0t, y0t, x1, y1, x1t, y1t; // anchor points of center
		double x, y, xt, yt, dx, dy, dxt, dyt;// anchor points of floats
		double value, all, top, bottom, left, right, // side areas
		left_top, left_bottom, right_top, right_bottom;
		int index = 0;// index of the grid
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
			{
				x = column * stepX;
				y = row * stepY;
				xt = x + stepX;
				yt = y + stepY;
				// integer anchors
				x0 = floor(x, ACCURACY);
				y0 = floor(y, ACCURACY);
				x1 = x0 + 1;
				y1 = y0 + 1;
				x0t = ceil(xt, ACCURACY);
				y0t = ceil(yt, ACCURACY);
				x1t = x0t - 1;
				y1t = y0t - 1;
				// divisors
				dx = x - x0;
				dy = y - y0;
				dxt = x0t - xt;
				dyt = y0t - yt;
				// calculate areas
				// @formatter:off
				all	   = 		integral[y0t][x0t] - integral[y0t][x0]  - integral[y0] [x0t] + integral[y0] [x0];
				left   = dxt * (integral[y0t][x1]  - integral[y0t][x0]  - integral[y0] [x1]  + integral[y0] [x0]);
				right  = dxt * (integral[y0t][x0t] - integral[y0t][x1t] - integral[y0] [x0t] + integral[y0] [x1t]);
				top    = dy  * (integral[y1] [x0t] - integral[y1] [x0]  - integral[y0] [x0t] + integral[y0] [x0]);
				bottom = dyt * (integral[y0t][x0t] - integral[y0t][x0]  - integral[y1t][x0t] + integral[y1t][x0]);
				left_top 	 = dx  * dy  * (integral[y1] [x1]  - integral[y1] [x0] -  integral[y0] [x1]  + integral[y0] [x0]);
				left_bottom  = dx  * dyt * (integral[y0t][x1]  - integral[y0t][x0] -  integral[y1t][x1]  + integral[y1t][x0]);
				right_top 	 = dxt * dy  * (integral[y1] [x0t] - integral[y1] [x1t] - integral[y0] [x0t] + integral[y0] [x1t]);
				right_bottom = dxt * dyt * (integral[y0t][x0t] - integral[y0t][x1t] - integral[y1t][x0t] + integral[y1t][x1t]);
				// @formatter:on
				value = all - left - right - top - bottom + left_top
						+ left_bottom + right_top + right_bottom;
				s.insert(index++, value);
			}
		return s;
	}

	/**
	 * Returns thresholding method.
	 * 
	 * @return the thresholding method
	 */
	public Thresholding getThresholding()
	{
		return thresholding;
	}

	/**
	 * Set thresholding method.
	 * 
	 * @param thresholding
	 *            the value of thresholding method
	 */
	public void setThresholding(Thresholding thresholding)
	{
		this.thresholding = thresholding;
	}

	/**
	 * Returns rows of features.
	 * 
	 * @return the rows
	 */
	public int getRows()
	{
		return rows;
	}

	/**
	 * Set rows of features.
	 * 
	 * @param rows
	 *            the value of rows
	 */
	public void setRows(int rows)
	{
		this.rows = rows;
	}

	/**
	 * Returns columns of features.
	 * 
	 * @return the columns
	 */
	public int getColumns()
	{
		return columns;
	}

	/**
	 * Set columns of features.
	 * 
	 * @param columns
	 *            the value of columns
	 */
	public void setColumns(int columns)
	{
		this.columns = columns;
	}

	/**
	 * Returns if <code>true</code> do normalization to the result.
	 * 
	 * @return if <code>true</code> do normalization to the result.
	 */
	public boolean isScaled()
	{
		return isScaled;
	}

	/**
	 * Set if <code>true</code> do normalization to the result.
	 * 
	 * @param isScaled
	 *            if <code>true</code> do normalization to the result.
	 */
	public void setScaled(boolean isScaled)
	{
		this.isScaled = isScaled;
	}

	/**
	 * Accuracy for avoiding adding up error.
	 */
	private static final double	ACCURACY	= 1e-4;

	/**
	 * Returns the largest (closest to positive infinity) {@code double} value
	 * that is less than or equal to the
	 * argument and is equal to a mathematical integer. Special cases:
	 * <ul>
	 * <li>If the argument value is already equal to a mathematical integer,
	 * then the result is the same as the argument.</li>
	 * <li>If the argument is NaN or an infinity or positive zero or negative
	 * zero, then the result is the same as the argument.</li>
	 * </ul>
	 * <p>
	 * This method is different from the {@linkplain Math#floor(double) native
	 * floor method}, this method can avoid adding up error. If the value is has
	 * the smaller distance between <code>d</code> and <code>(int) a</code> than
	 * use (int) a to represent it.
	 * </p>
	 * 
	 * @param a
	 *            a value.
	 * @param accuracy
	 *            the accuracy for judging adding up error
	 * @return the largest (closest to positive infinity)
	 *         floating-point value that less than or equal to the argument
	 *         and is equal to a mathematical integer.
	 */
	public static int floor(double a, double accuracy)
	{
		if (accuracy > a - (int) a)
			return (int) a;
		else
			return (int) Math.floor(a);
	}

	/**
	 * Returns the smallest (closest to negative infinity) {@code double} value
	 * that is greater than or equal to the
	 * argument and is equal to a mathematical integer. Special cases:
	 * <ul>
	 * <li>If the argument value is already equal to a mathematical integer,
	 * then the result is the same as the argument.</li>
	 * <li>If the argument is NaN or an infinity or positive zero or negative
	 * zero, then the result is the same as the argument.</li>
	 * <li>If the argument value is less than zero but greater than -1.0, then
	 * the result is negative zero.</li>
	 * </ul>
	 * Note that the value of {@code Math.ceil(x)} is exactly the
	 * value of {@code -Math.floor(-x)}.
	 * <p>
	 * This method is different from the {@linkplain Math#floor(double) native
	 * floor method}, this method can avoid adding up error. If the value is has
	 * the smaller distance between <code>d</code> and <code>(int) a</code> than
	 * use (int) a to represent it.
	 * </p>
	 * 
	 * @param a
	 *            a value.
	 * @return the smallest (closest to negative infinity)
	 *         floating-point value that is greater than or equal to
	 *         the argument and is equal to a mathematical integer.
	 */
	public static int ceil(double a, double accuracy)
	{
		if (accuracy > a - (int) a)
			return (int) a;
		else
			return (int) Math.ceil(a);
	}
}
