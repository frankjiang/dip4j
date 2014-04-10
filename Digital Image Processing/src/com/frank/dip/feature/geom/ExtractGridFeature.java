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
public class ExtractGridFeature<T extends Image> extends FeatureExtractor<T>
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
	public Sample extract(T image, Double target)
	{
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
		Sample s = new Sample();
		s.setTarget(target);
		double summary = 0.0;
		int counter = 0;
		double[][] integral = new double[width + 1][height + 1];
		for (int y = 1; y <= height; y++, summary += counter)
		{
			counter = 0;
			for (int x = 1; x <= width; x++)
			{
				if (bi[y][x])
					counter++;
				integral[y][x] = integral[y - 1][x] + counter;
			}
		}
		for (int y = 1; y <= height; y++)
			for (int x = 1; x <= width; x++)
				integral[y][x] /= summary;
		// OUTPUT
		for (int y = 1; y <= height; y++)
		{
			for (int x = 1; x <= width; x++)
				System.out.printf("%f\t", integral[y][x]);
			System.out.println();
		}
		return null;
	}

	/**
	 * Returns thresholding.
	 * 
	 * @return the thresholding
	 */
	public Thresholding getThresholding()
	{
		return thresholding;
	}

	/**
	 * Set thresholding.
	 * 
	 * @param thresholding
	 *            the value of thresholding
	 */
	public void setThresholding(Thresholding thresholding)
	{
		this.thresholding = thresholding;
	}

	/**
	 * Returns rows.
	 * 
	 * @return the rows
	 */
	public int getRows()
	{
		return rows;
	}

	/**
	 * Set rows.
	 * 
	 * @param rows
	 *            the value of rows
	 */
	public void setRows(int rows)
	{
		this.rows = rows;
	}

	/**
	 * Returns columns.
	 * 
	 * @return the columns
	 */
	public int getColumns()
	{
		return columns;
	}

	/**
	 * Set columns.
	 * 
	 * @param columns
	 *            the value of columns
	 */
	public void setColumns(int columns)
	{
		this.columns = columns;
	}

	/**
	 * Returns isScaled.
	 * 
	 * @return the isScaled
	 */
	public boolean isScaled()
	{
		return isScaled;
	}

	/**
	 * Set isScaled.
	 * 
	 * @param isScaled
	 *            the value of isScaled
	 */
	public void setScaled(boolean isScaled)
	{
		this.isScaled = isScaled;
	}
}
