/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractHistogram.java is PROPRIETARY/CONFIDENTIAL built in 下午10:51:52,
 * 2014年11月2日.
 * Use is subject to license terms.
 */
package com.frank.dip.feature.texture;

import java.util.ArrayList;

import com.frank.dip.ColorScaleLevel;
import com.frank.dip.Image;
import com.frank.dip.analyze.Histogram;
import com.frank.dip.feature.FeatureExtractor;
import com.frank.dip.feature.Sample;

/**
 * TODO
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractHistogram extends FeatureExtractor<Image> implements ColorScaleLevel
{
	/**
	 * If <code>true</code> the feature is used for color image.
	 */
	protected boolean	isColorImage;
	/**
	 * The size of histogram feature.
	 */
	protected int		size;

	/**
	 * Construct an instance of <tt>ExtractHistogram</tt>.
	 * 
	 * @param isColorImage
	 *            if <code>true</code> the feature is used for color image
	 */
	public ExtractHistogram(boolean isColorImage)
	{
		this(isColorImage, -1);
	}

	/**
	 * Construct an instance of <tt>ExtractHistogram</tt>.
	 * 
	 * @param isColorImage
	 *            if <code>true</code> the feature is used for color image
	 * @param size
	 *            the size of destinated feature, or nonpositive value for
	 *            compress the histogram.<br>
	 *            If size is specified (positive), the result of histogram will
	 *            be compressed;<br>
	 */
	public ExtractHistogram(boolean isColorImage, int size)
	{
		this.isColorImage = isColorImage;
		if (size < 0)
		{
			if (isColorImage)
				this.size = COLOR_SCALE_LEVEL * 3;
			else
				this.size = COLOR_SCALE_LEVEL;
		}
		else
		{
			if ((isColorImage && (size > COLOR_SCALE_LEVEL * 3)) || (!isColorImage && (size > COLOR_SCALE_LEVEL)))
				throw new IllegalArgumentException(String.format("Illegal feature size: %d", size));
			this.size = size;
		}
	}

	/**
	 * @see com.frank.dip.feature.FeatureExtractor#extract(com.frank.dip.feature.Sample,
	 *      java.lang.Object, int)
	 */
	@Override
	public int extract(Sample s, Image data, int beginIndex)
	{
		Histogram hist = Histogram.histogram(data);
		ArrayList<Float> list;
		if (isColorImage)
		{
			list = new ArrayList<Float>(COLOR_SCALE_LEVEL * 3);
			if (hist instanceof Histogram.Color)
			{
				Histogram.Color hist0 = (Histogram.Color) hist;
				for (float e : hist0.getPDFRed())
					list.add(e);
				for (float e : hist0.getPDFGreen())
					list.add(e);
				for (float e : hist0.getPDFBlue())
					list.add(e);
			}
			else
			{
				float[] fs = hist.getPDF();
				for (float e : fs)
					list.add(e);
				for (float e : fs)
					list.add(e);
				for (float e : fs)
					list.add(e);
			}
		}
		else
		{
			list = new ArrayList<Float>(COLOR_SCALE_LEVEL);
			float[] fs = hist.getPDF();
			for (float e : fs)
				list.add(e);
		}
		int step = (int) Math.floor(list.size() / (double) size);
		double sum;
		for (int i = 0; i < list.size(); i += step)
		{
			sum = 0.0;
			for (int j = 0; j < step; j++)
				sum += list.get(i + j);
			s.insert(beginIndex++, sum);
		}
		return beginIndex;
	}

	/**
	 * @see com.frank.dip.feature.FeatureExtractor#size()
	 */
	@Override
	public int size()
	{
		return size;
	}
}
