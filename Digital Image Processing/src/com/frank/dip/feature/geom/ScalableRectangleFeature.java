/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ScalableRectangleFeature.java is PROPRIETARY/CONFIDENTIAL built in 4:23:06
 * PM,
 * Apr 11, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.geom;

import com.frank.dip.threshold.Thresholding;

/**
 * The scalable rectangle feature.
 * <p>
 * This feature extends rectangle feature for the features can be scaled to 0 to
 * 1.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class ScalableRectangleFeature extends RectangleFeature
{
	/**
	 * Accuracy for avoiding adding up error.
	 */
	protected static final double	ACCURACY	= 1e-4;
	/**
	 * If <code>true</code> do normalization to the result.
	 */
	protected boolean				isScaled;

	/**
	 * Construct an instance of <tt>ScalableSquareFeature</tt>.
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
	public ScalableRectangleFeature(int rows, int columns,
			Thresholding thresholding, boolean isScaled)
	{
		super(rows, columns, thresholding);
		this.isScaled = isScaled;
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
		return String.format("%s [rows=%d, columns=%d, scaled=%b]", getClass()
				.toString(), rows, columns, isScaled);
	}
}
