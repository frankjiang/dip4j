/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractIntersectionFeature.java is PROPRIETARY/CONFIDENTIAL built in 10:44:20
 * PM, Apr 10, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import com.frank.dip.feature.Sample;
import com.frank.dip.threshold.GlobalThresholding;
import com.frank.dip.threshold.Otsu;
import com.frank.dip.threshold.Thresholding;

/**
 * Extract intersection feature.
 * <p>
 * Intersection feature is a kind of binary image feature which indicates the
 * times that horizontal and vertical scale lines will intersect the foreground.
 * The width of the intersected part will be ignored.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractIntersection extends RectangleFeature
{
	/**
	 * The thresholding method of transfer a other color type image to binary
	 * image.
	 */
	protected Thresholding	thresholding;

	/**
	 * Construct an instance of <tt>ExtractIntersectionFeature</tt>.
	 * <p>
	 * The intersection will be 4&times;4, with scaled values and use
	 * {@linkplain com.frank.dip.threshold.Otsu
	 * Otus method} as thresholding.
	 * </p>
	 */
	public ExtractIntersection()
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
	public ExtractIntersection(int rows, int columns, Thresholding thresholding)
	{
		super(rows, columns, thresholding);
	}

	/**
	 * @see com.frank.dip.feature.ExtractBinaryImageFeature#extract(com.frank.dip.feature.Sample,
	 *      boolean[][], int, int, int)
	 */
	@Override
	protected int extract(Sample s, boolean[][] bi, int width, int height,
			int beginIndex)
	{
		double stepX = width / (double) rows;
		double stepY = height / (double) columns;
		int counter, index = beginIndex;
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
		return index;
	}
}
