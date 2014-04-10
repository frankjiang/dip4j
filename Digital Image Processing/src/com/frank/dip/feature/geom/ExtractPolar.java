/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractPolar.java is PROPRIETARY/CONFIDENTIAL built in 下午10:12:59,
 * 2014年4月15日.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;

import com.frank.dip.Image;
import com.frank.dip.feature.Sample;
import com.frank.dip.threshold.GlobalThresholding;
import com.frank.dip.threshold.Otsu;
import com.frank.dip.threshold.Thresholding;
import com.frank.math.MathUtils;

/**
 * The feature of the polar.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractPolar extends ScalableRectangleFeature
{
	/**
	 * Construct an instance of <tt>ExtractPolar</tt>.
	 */
	public ExtractPolar()
	{
		this(5, 6, new GlobalThresholding(new Otsu()), true);
	}

	/**
	 * Construct an instance of <tt>ExtractPolar</tt>.
	 * 
	 * @param rhoGrid
	 *            the division count of &rho;, &rho; will be divided into
	 *            <code>rhoGrid</code> parts
	 * @param thetaGrid
	 *            the division count of &theta;, the circle angle (
	 *            <code>2&Pi;</code>) will be divided into
	 *            <code>thetaGrid</code> parts
	 * @param thresholding
	 *            the thresholding method
	 * @param isScaled
	 *            if <code>true</code> do normalization to the result
	 */
	public ExtractPolar(int rhoGrid, int thetaGrid, Thresholding thresholding,
			boolean isScaled)
	{
		super(rhoGrid, thetaGrid, thresholding, isScaled);
	}

	/**
	 * @see com.frank.dip.feature.ExtractBinaryImageFeature#extract(com.frank.dip.feature.Sample,
	 *      boolean[][], int, int, int)
	 */
	@Override
	protected int extract(Sample s, boolean[][] bi, int width, int height,
			int beginIndex)
	{
		LinkedList<Point> points = new LinkedList<Point>();
		int summary = 0, sumX = 0, sumY = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (bi[y][x])
				{
					points.add(new Point(x, y));
					sumX += x;
					sumY += y;
					summary++;
				}
		double centerX = sumX / (double) summary, centerY = sumY
				/ (double) summary;
		double[] distances = { Point.distance(centerX, centerX, 0, 0),
				Point.distance(centerX, centerX, width, 0),
				Point.distance(centerX, centerX, 0, height),
				Point.distance(centerX, centerX, width, height) };
		Arrays.sort(distances);
		double rhoMax = distances[distances.length - 1], thetaMax = 2 * Math.PI;
		int[][] features = new int[rows][columns];
		for (Point p : points)
			features[(int) (Point.distance(centerX, centerY, p.x, p.y) / rhoMax * rows)][(int) (MathUtils
					.angle(centerX - p.x, centerY - p.y) / thetaMax * columns)] += 1.0;
		int index = beginIndex;
		if (isScaled)
			for (int[] list : features)
				for (int f : list)
					s.insert(index++, f / (double) summary);
		else
			for (int[] list : features)
				for (int f : list)
					s.insert(index++, f);
		return index;
	}

	/**
	 * @see com.frank.dip.feature.stat.RectangleFeature#checkValid(com.frank.dip.Image)
	 */
	@Override
	protected void checkValid(Image image) throws IllegalArgumentException
	{
		if (rows <= 0 || columns <= 0)
			throw new IllegalArgumentException(String.format(
					"rho gird(%d) and theta grid(%d) must be positive.", rows,
					columns));
	}
}
