/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractGridFeature.java is PROPRIETARY/CONFIDENTIAL built in 8:02:44 PM, Apr
 * 10, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import com.frank.dip.feature.Sample;
import com.frank.dip.threshold.GlobalThresholding;
import com.frank.dip.threshold.Otsu;
import com.frank.dip.threshold.Thresholding;
import com.frank.math.MathUtils;

/**
 * Extract grid feature.
 * <p>
 * A grid feature divides the original image into specified
 * <code>rows&times;columns</code> dimension of grids. In each grid, the summary
 * of the foreground will be counted.
 * </p>
 * <p>
 * In this implementation, the image <code>width</code> and <code>height</code>
 * need not to be the times of <code>rows</code> or <code>columns</code>.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractGrid extends ScalableRectangleFeature
{
	/**
	 * Construct an default instance of <tt>ExtractGridFeature</tt>.
	 * <p>
	 * The grid will be 6&times;6, with scaled values and use
	 * {@linkplain com.frank.dip.threshold.Otsu
	 * Otus method} as thresholding.
	 * </p>
	 */
	public ExtractGrid()
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
	public ExtractGrid(int rows, int columns, Thresholding thresholding,
			boolean isScaled)
	{
		super(rows, columns, thresholding, isScaled);
	}

	/**
	 * @see com.frank.dip.feature.ExtractBinaryImageFeature#extract(com.frank.dip.feature.Sample,
	 *      boolean[][], int, int, int)
	 */
	@Override
//	protected int extract(Sample s, boolean[][] bi, int width, int height,
//			int beginIndex)
//	{
//		// build integral image
//		double summary = 0.0;
//		int counter;
//		double[][] integral = new double[height + 1][width + 1];
//		for (int y = 1; y <= height; y++, summary += counter)
//		{
//			counter = 0;
//			for (int x = 1; x <= width; x++)
//			{
//				if (bi[y - 1][x - 1])
//					counter++;
//				integral[y][x] = integral[y - 1][x] + counter;
//			}
//		}
//		// do normalization
//		if (isScaled)
//			for (int y = 1; y <= height; y++)
//				for (int x = 1; x <= width; x++)
//					integral[y][x] /= summary;
//		// creating grids
//		double stepX = width / (double) columns;
//		double stepY = height / (double) rows;
//		double x, y, xt, yt;// anchor points of floats
//		double v00, v01, v10, v11;
//		int index = beginIndex;// index of the grid
//		for (int row = 0; row < rows; row++)
//			for (int column = 0; column < columns; column++)
//			{
//				x = stepX * column;
//				y = stepY * row;
//				xt = x + stepX;
//				yt = y + stepY;
//				// integer anchors
//				v00 = MathUtils.interpolate(integral, x, y);
//				v01 = MathUtils.interpolate(integral, x, yt);
//				v10 = MathUtils.interpolate(integral, xt, y);
//				v11 = MathUtils.interpolate(integral, xt, yt);
//				s.insert(index++, v11 - v10 - v01 + v00);
//				System.out.println(Arrays.toString(new double[] {
//						v11 - v10 - v01 + v00, v00, v01, v10, v11 }));
//			}
//		return index;
//	}
	protected int extract(Sample s, boolean[][] bi, int width, int height,
			int beginIndex)
	{
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
		double stepX = width / (double) columns;
		double stepY = height / (double) rows;
		int x0, y0, x0t, y0t, x1, y1, x1t, y1t; // anchor points of center
		double x, y, xt, yt, dx, dy, dxt, dyt;// anchor points of floats
		double value, all, top, bottom, left, right, // side areas
		left_top, left_bottom, right_top, right_bottom;
		int index = beginIndex;// index of the grid
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
			{
				x = stepX * column;
				y = stepY * row;
				xt = x + stepX;
				yt = y + stepY;
				// integer anchors
				x0 = (int) MathUtils.floor(x, ACCURACY);
				y0 = (int) MathUtils.floor(y, ACCURACY);
				x1 = x0 + 1;
				y1 = y0 + 1;
				x0t = (int) MathUtils.ceil(xt, ACCURACY);
				y0t = (int) MathUtils.ceil(yt, ACCURACY);
				x1t = x0t - 1;
				y1t = y0t - 1;
				// divisors
				dx = x - x0;
				dy = y - y0;
				dxt = x0t - xt;
				dyt = y0t - yt;
				// try{
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
				//				}catch(Exception exp)
				//				{
				//					exp.printStackTrace();
				//					return 0;
				//				}
				value = all - left - right - top - bottom + left_top
						+ left_bottom + right_top + right_bottom;
				s.insert(index++, value);
			}
		return index;
	}
}
