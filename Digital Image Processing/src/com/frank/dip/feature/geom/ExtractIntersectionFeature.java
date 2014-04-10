/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractIntersectionFeature.java is PROPRIETARY/CONFIDENTIAL built in 10:44:20
 * PM, Apr 10, 2014.
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
public class ExtractIntersectionFeature extends FeatureExtractor<Image>
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
	 * Construct an instance of <tt>ExtractIntersectionFeature</tt>.
	 * <p>
	 * The intersection will be 4&times;4, with scaled values and use
	 * {@linkplain com.frank.dip.threshold.Otsu
	 * Otus method} as thresholding.
	 * </p>
	 */
	public ExtractIntersectionFeature()
	{
		this(4, 4, new GlobalThresholding(new Otsu()));
	}

	/**
	 * Construct an instance of <tt>ExtractIntersectionFeature</tt>.
	 * 
	 * @param rows
	 *            the rows of the grid
	 * @param columns
	 *            the columns of the grid
	 * @param thresholding
	 *            the thresholding method
	 */
	public ExtractIntersectionFeature(int rows, int columns,
			Thresholding thresholding)
	{
		this.rows = rows;
		this.columns = columns;
		this.thresholding = thresholding;
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
		// creating grids
		double stepX = width / (double) rows;
		double stepY = height / (double) columns;
		Sample s = new Sample(rows * columns);
		s.setTarget(target);
		int counter, index = 0;
		boolean b;
		for (int x, row = 0; row < rows; row++)
		{
			x = (int) (row * stepX);
			counter = 0;
			b = false;
			for (int y = 0; y < height; y++)
				if (bi[y][x] != b)
				{
					b = !b;
					counter++;
				}
			if (counter % 2 != 0)
				counter++;
			s.insert(index++, counter / 2);
		}
		for (int y, column = 0; column < columns; column++)
		{
			y = (int) (column * stepY);
			counter = 0;
			b = false;
			for (int x = 0; x < width; x++)
				if (bi[y][x] != b)
				{
					b = !b;
					counter++;
				}
			if (counter % 2 != 0)
				counter++;
			s.insert(index++, counter / 2);
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
}
