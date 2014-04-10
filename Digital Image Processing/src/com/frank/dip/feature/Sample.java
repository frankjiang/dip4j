/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Sample.java is PROPRIETARY/CONFIDENTIAL built in 7:20:59 PM, Apr 10, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature;

import com.frank.math.struct.SparseVector;

/**
 * The data structure for a sample data.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Sample
{
	/**
	 * The feature vector.
	 */
	protected SparseVector<Double>	feature;
	/**
	 * The target value.
	 */
	protected Double				target;

	/**
	 * Construct an default instance of <tt>Sample</tt>.
	 * <p>
	 * No specified target or feature value.
	 * </p>
	 */
	public Sample()
	{
		feature = new SparseVector.Double();
		target = null;
	}

	/**
	 * Construct an instance of <tt>Sample</tt>.
	 * 
	 * @param target
	 *            the target value
	 * @param features
	 *            the feature vector
	 */
	public Sample(Double target, Double... features)
	{
		feature = new SparseVector.Double();
		this.target = target;
		int index = 0;
		for (double value : features)
			feature.insert(index++, value);
	}

	/**
	 * Returns the specified feature value.
	 * 
	 * @param index
	 *            the feature index
	 * @return the feature value
	 */
	public double get(int index)
	{
		return feature.get(index);
	}

	/**
	 * Insert a specified feature value
	 * 
	 * @param index
	 *            the specified feature index
	 * @param value
	 *            the specified feature value
	 */
	public void insert(int index, double value)
	{
		feature.insert(index, value);
	}

	/**
	 * Returns the feature vector.
	 * 
	 * @return the feature vector
	 */
	public SparseVector<Double> getFeature()
	{
		return feature;
	}

	/**
	 * Set the target value.
	 * 
	 * @param target
	 *            the target value
	 */
	public void setTarget(Double target)
	{
		this.target = target;
	}

	/**
	 * Returns the target value.
	 * 
	 * @return the target value
	 */
	public Double getTarget()
	{
		return target;
	}
}
