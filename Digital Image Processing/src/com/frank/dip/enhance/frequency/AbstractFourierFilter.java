/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * AbstractFourierFilter.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.frequency;

import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.analyze.FourierTransformation;

/**
 * The abstract Fourier filter.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class AbstractFourierFilter extends FourierTransformation
{
	/**
	 * Perform Fourier distance filter to the source image.
	 * 
	 * @param source
	 *            the source image
	 * @return the image after filtered
	 */
	public GrayImage filter(Image source)
	{
		complex(source);
		return backward();
	}
}
