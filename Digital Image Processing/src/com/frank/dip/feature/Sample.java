/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Sample.java is PROPRIETARY/CONFIDENTIAL built in 7:20:59 PM, Apr 10, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature;

import com.frank.math.struct.sparse.SparseVector;

/**
 * The data structure for a sample data.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.1.0
 */
public class Sample
{
	/**
	 * The referenced information.
	 * <p>
	 * This can be any object.
	 * </p>
	 */
	protected Object				refInfo	= null;
	/**
	 * The feature vector.
	 */
	protected SparseVector<Double>	feature;
	/**
	 * The target value.
	 */
	protected Double				target;
	/**
	 * The feature size.
	 */
	protected int					size;

	/**
	 * Construct an default instance of <tt>Sample</tt>.
	 * <p>
	 * No specified target or feature value.
	 * </p>
	 * 
	 * @param size
	 *            the feature size
	 */
	public Sample(int size)
	{
		this.size = size;
		feature = new SparseVector.Double();
		target = null;
	}

	/**
	 * Construct an instance of <tt>Sample</tt>.
	 * 
	 * @param size
	 *            the feature size
	 * @param target
	 *            the target value
	 * @param features
	 *            the feature vector
	 */
	public Sample(int size, Double target, Double... features)
	{
		this.size = size;
		feature = new SparseVector.Double();
		this.target = target;
		for (double value : features)
			feature.add(value);
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

	/**
	 * Returns the feature size.
	 * 
	 * @return the feature size
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Set the feature size.
	 * 
	 * @param size
	 *            the value of feature size
	 */
	public void setSize(int size)
	{
		this.size = size;
	}

	/**
	 * Returns refInfo.
	 * 
	 * @return the refInfo
	 */
	public Object getRefInfo()
	{
		return refInfo;
	}

	/**
	 * Set refInfo.
	 * 
	 * @param refInfo
	 *            the value of refInfo
	 */
	public void setRefInfo(Object refInfo)
	{
		this.refInfo = refInfo;
	}
}
