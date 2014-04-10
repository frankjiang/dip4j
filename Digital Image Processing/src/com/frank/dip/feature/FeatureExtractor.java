/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * FeatureExtractor.java is PROPRIETARY/CONFIDENTIAL built in 7:19:28 PM, Apr
 * 10, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature;

import com.frank.dip.Image;

/**
 * The feature extractor.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class FeatureExtractor<T extends Image>
{
	/**
	 * Extract features from specified image.
	 * 
	 * @param image
	 *            the specified image
	 * @param target
	 *            the target value, <code>null</code> if not determined
	 * @return the extracted features
	 */
	abstract public Sample extract(T image, Double target);
}
