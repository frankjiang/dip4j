/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractProjection.java is PROPRIETARY/CONFIDENTIAL built in 4:02:14 PM, Apr
 * 11, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import com.frank.dip.feature.Sample;
import com.frank.dip.threshold.GlobalThresholding;
import com.frank.dip.threshold.Otsu;
import com.frank.dip.threshold.Thresholding;
import com.frank.math.MathUtils;

/**
 * Extract projection feature.
 * <p>
 * This feature displays the projection value of the foreground as the
 * horizontal and vertical histograms.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractProjection extends ScalableRectangleFeature
{
	/**
	 * Construct an instance of <tt>ExtractProjection</tt>.
	 * <p>
	 * The default projection dimension will be 6&times;6, with scaled values
	 * and use {@linkplain com.frank.dip.threshold.Otsu
	 * Otus method} as thresholding.
	 * </p>
	 */
	public ExtractProjection()
	{
		this(6, 6, new GlobalThresholding(new Otsu()), true);
	}

	/**
	 * Construct an instance of <tt>ExtractProjection</tt>.
	 * 
	 * @param rows
	 *            the rows of the feature
	 * @param columns
	 *            the columns of the feature
	 * @param thresholding
	 *            the thresholding method
	 * @param isScaled
	 *            if <code>true</code> do normalization to the result
	 */
	public ExtractProjection(int rows, int columns, Thresholding thresholding,
			boolean isScaled)
	{
		super(rows, columns, thresholding, isScaled);
	}

	/**
	 * @see com.frank.dip.feature.ExtractBinaryImageFeature#extract(com.frank.dip.feature.Sample,
	 *      boolean[][], int, int, int)
	 */
	@Override
	protected int extract(Sample s, boolean[][] bi, int width, int height,
			int beginIndex)
	{
		// do projection
		int[] xs = new int[width + 1];// projection to X-axis
		int[] ys = new int[height + 1]; // projection to Y-axis
		for (int y = 1; y <= height; y++)
			for (int x = 1; x <= width; x++)
				if (bi[y - 1][x - 1])
				{
					xs[x]++;
					ys[y]++;
				}
		// build integral
		for (int i = 1; i < xs.length; i++)
			xs[i] += xs[i - 1];
		for (int i = 1; i < ys.length; i++)
			ys[i] += ys[i - 1];
		double summary = xs[xs.length - 1];
		// build sample
		int index = beginIndex;
		double stepX = width / (double) rows;
		double stepY = height / (double) columns;
		double value; // value to insert in
		// calculate & insert row projections
		double y, yt;
		int y0, y0t;
		for (int row = 0; row < rows; row++)
		{
			// double anchors
			y = stepY * row;
			yt = y + stepY;
			// integer anchors
			y0 = (int) MathUtils.floor(y, ACCURACY);
			y0t = (int) MathUtils.ceil(yt, ACCURACY);
			// calculate & insert
			value = ys[y0t] - ys[y0] - (y - y0) * (ys[y0 + 1] - ys[y0])
					- (y0t - yt) * (ys[y0t] - ys[y0t - 1]);
			s.insert(index++, isScaled ? (value / summary) : value);
		}
		// calculate & insert column projections
		double x, xt;
		int x0, x0t;
		for (int column = 0; column < columns; column++)
		{
			// double anchors
			x = stepX * column;
			xt = x + stepX;
			// integer anchors
			x0 = (int) MathUtils.floor(x, ACCURACY);
			x0t = (int) MathUtils.ceil(xt, ACCURACY);
			// calculate & insert
			value = xs[x0t] - xs[x0] - (x - x0) * (xs[x0 + 1] - xs[x0])
					- (x0t - xt) * (xs[x0t] - xs[x0t - 1]);
			s.insert(index++, isScaled ? (value / summary) : value);
		}
		return 0;
	}
}
