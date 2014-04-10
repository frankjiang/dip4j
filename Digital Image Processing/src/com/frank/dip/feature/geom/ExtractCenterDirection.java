/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractCenterDirection.java is PROPRIETARY/CONFIDENTIAL built in 5:04:40 PM,
 * Apr 12, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import com.frank.dip.Image;
import com.frank.dip.feature.ExtractBinaryImageFeature;
import com.frank.dip.feature.Sample;
import com.frank.dip.threshold.Thresholding;
import com.frank.math.MathUtils;

/**
 * Extract center direction feature.
 * <p>
 * Center direction feature divide the circle of the center point into
 * <code>size</code> areas in angle, statistic the point count in each area as
 * the features.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractCenterDirection extends ExtractBinaryImageFeature
{
	/**
	 * The feature size.
	 */
	protected int		size;
	/**
	 * If <code>true</code> do normalization to the result.
	 */
	protected boolean	isScaled;

	/**
	 * Construct an instance of <tt>ExtractCenterDirection</tt>.
	 * 
	 * @param size
	 *            the size of feature
	 * @param thresholding
	 *            the thresholding method
	 * @param isScaled
	 *            if <code>true</code> do normalization to the result
	 */
	public ExtractCenterDirection(int size, Thresholding thresholding,
			boolean isScaled)
	{
		super(thresholding);
		setSize(size);
		this.isScaled = isScaled;
	}

	/**
	 * @see com.frank.dip.feature.ExtractBinaryImageFeature#extract(com.frank.dip.feature.Sample,
	 *      boolean[][], int, int, int)
	 */
	@Override
	protected int extract(Sample s, boolean[][] bi, int width, int height,
			int beginIndex)
	{
		int summary = 0, sumX = 0, sumY = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (bi[y][x])
				{
					sumX += x;
					sumY += y;
					summary++;
				}
		double centerX = sumX / (double) summary, centerY = sumY
				/ (double) summary;
		double circle = 2 * Math.PI;
		double dx, dy;
		double[] feature = new double[size];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (bi[y][x])
				{
					dx = x - centerX;
					dy = y - centerY;
					// exactly center point
					if (dx == 0.0 && dy == 0.0)
						continue;
					// X-axis point
					if (dy == 0.0)
					{
						if (dx > 0)
							feature[0] += 1;
						else
							feature[size / 2] += 1;
					}
					// Y-axis point
					if (dx == 0.0)
					{
						if (dy > 0)
							feature[size / 4] += 1;
						else
							feature[size * 3 / 4] += 1;
					}
					// normal point
					feature[(int) (size * MathUtils.angle(dx, dy) / circle)] += 1;
				}
		if (isScaled)
			for (int i = 0; i < size; i++)
				s.insert(beginIndex + i, feature[i] / summary);
		else
			for (int i = 0; i < size; i++)
				s.insert(beginIndex + i, feature[i]);
		return beginIndex + size;
	}

	/**
	 * @see com.frank.dip.feature.ExtractBinaryImageFeature#checkValid(com.frank.dip.Image)
	 */
	@Override
	protected void checkValid(Image image) throws IllegalArgumentException
	{
	}

	/**
	 * @see com.frank.dip.feature.FeatureExtractor#size()
	 */
	@Override
	public int size()
	{
		return size;
	}

	/**
	 * Returns the feature size.
	 * 
	 * @return the feature size
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Set feature size.
	 * 
	 * @param size
	 *            the value of feature size
	 */
	public void setSize(int size)
	{
		if (size <= 1)
			throw new IllegalArgumentException(String.format(
					"The size (%d) of the feature must be greater than 1.",
					size));
		this.size = size;
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
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return String.format("%s [size=%d, scaled=%b]", getClass().toString(),
				size, isScaled);
	}
}
