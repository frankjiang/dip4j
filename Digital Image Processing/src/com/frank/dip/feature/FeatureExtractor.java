/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * FeatureExtractor.java is PROPRIETARY/CONFIDENTIAL built in 7:19:28 PM, Apr
 * 10, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature;

/**
 * The feature extractor.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class FeatureExtractor<T>
{
	/**
	 * Extract features from specified data.
	 * 
	 * @param data
	 *            the specified data
	 * @param target
	 *            the target value, <code>null</code> if not determined
	 * @return the extracted features
	 */
	public Sample extract(T data, Double target)
	{
		Sample s = new Sample(size());
		s.setTarget(target);
		extract(s, data, 0);
		return s;
	}

	/**
	 * Extract features from specified data and append to the last of the
	 * specified sample.
	 * 
	 * @param s
	 *            the specified sample
	 * @param data
	 *            the specified data
	 * @param beginIndex
	 *            the begin index of the features to insert in
	 * @return the end index of the inserted features
	 */
	abstract public int extract(Sample s, T data, int beginIndex);

	/**
	 * Returns the size of features.
	 * 
	 * @return the size of features
	 */
	abstract public int size();
}
