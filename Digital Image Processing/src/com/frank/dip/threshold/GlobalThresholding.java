/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Thresholding.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.BinaryImage;
import com.frank.dip.GrayImage;

/**
 * The global thresholding operator.
 * <p>
 * In this operator, the any thresholding algorithm implementing
 * {@linkplain ThresholdFinder} can be used to perform the source image.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GlobalThresholding extends Thresholding
{
	/**
	 * The thresholding finder.
	 */
	protected ThresholdFinder	finder;

	/**
	 * Construct an instance of <tt>Thresholding</tt>.
	 * 
	 * @param source
	 * @param finder
	 */
	public GlobalThresholding(ThresholdFinder finder)
	{
		this.finder = finder;
	}

	/**
	 * Get the thresholding finder.
	 * 
	 * @return the thresholding finder
	 */
	public ThresholdFinder getFinder()
	{
		return finder;
	}

	/**
	 * Set the thresholding finder.
	 * 
	 * @param finder
	 *            the thresholding finder
	 */
	public void setFinder(ThresholdFinder finder)
	{
		this.finder = finder;
	}

	/**
	 * Perform thresholding algorithm to the source image. The thresholding
	 * algorithm is defined by {@linkplain ThresholdFinder}.
	 * 
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public BinaryImage operate(GrayImage source)
	{
		int width = source.width();
		int height = source.height();
		BinaryImage bi = new BinaryImage(width, height);
		int threshold = finder.threshold(source);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				bi.setPixel(x, y, source.getPixel(x, y) > threshold);
		return bi;
	}

	/**
	 * @see com.frank.dip.threshold.Thresholding#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("Global thresholding base on %s",
				finder.toString());
	}
}
