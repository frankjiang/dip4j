/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * RectangleFeature.java is PROPRIETARY/CONFIDENTIAL built in 4:19:53 PM, Apr
 * 11,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import com.frank.dip.Image;
import com.frank.dip.feature.ExtractBinaryImageFeature;
import com.frank.dip.threshold.Thresholding;

/**
 * The rectangle feature indicates that the feature is arranged as a rectangle.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class RectangleFeature extends ExtractBinaryImageFeature
{
	/**
	 * The rows of the feature.
	 */
	protected int	rows;
	/**
	 * The columns of the feature.
	 */
	protected int	columns;

	/**
	 * Construct an instance of <tt>SquareFeature</tt>.
	 * 
	 * @param rows
	 *            the rows of the feature
	 * @param columns
	 *            the columns of the feature
	 * @param thresholding
	 *            the thresholding method
	 */
	public RectangleFeature(int rows, int columns, Thresholding thresholding)
	{
		super(thresholding);
		this.rows = rows;
		this.columns = columns;
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
	 * @see com.frank.dip.feature.FeatureExtractor#size()
	 */
	@Override
	public int size()
	{
		return rows * columns;
	}

	/**
	 * @see com.frank.dip.feature.ExtractBinaryImageFeature#checkValid(com.frank.dip.Image)
	 */
	@Override
	protected void checkValid(Image image) throws IllegalArgumentException
	{
		int width = image.getWidth();
		int height = image.getHeight();
		if (width < columns || height < rows)
			throw new IllegalArgumentException(
					String.format(
							"Grid (rows:%d, columns:%d) cannot fit the image of size (width: %d, height:%d).",
							rows, columns, width, height));
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return String.format("%s [rows=%d, columns=%d]", getClass().toString(),
				rows, columns);
	}
}
