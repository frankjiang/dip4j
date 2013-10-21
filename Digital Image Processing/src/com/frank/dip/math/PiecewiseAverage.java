/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. PiecewiseAverage.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.math;

import java.util.Properties;

/**
 * A piecewise average function.
 * 
 * <pre>
 * f(x) = 
 * weight / size, x&isin;[begin, end)
 * 0, otherwise
 * 
 * <strong>weight</strong>: the weight of average calculating
 * <strong>begin</strong>: begin value of the region
 * <strong>end</strong>: end value of the region
 * </pre>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class PiecewiseAverage implements Function
{
	/**
	 * The parameter strings.
	 */
	public static final String	PARAM_BEGIN	= "begin", PARAM_END = "end", PARAM_WEIGHT = "weight";	//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	/**
	 * The begin value of the region.
	 */
	protected double			begin;
	/**
	 * The end value of the region.
	 */
	protected double			end;
	/**
	 * The weight of average calculating.
	 */
	protected double			weight;
	/**
	 * The division value of {@code weight} / ({@code end} - {@code begin}).
	 */
	protected double			division;

	/**
	 * Construct an instance of <tt>PiecewiseAverage</tt> with specified begin
	 * value, end value and weight.
	 * 
	 * @param begin
	 *            the begin value of the region
	 * @param end
	 *            the end value of the region
	 * @param weight
	 *            the weight of average calculating
	 */
	public PiecewiseAverage(double begin, double end, double weight)
	{
		this.begin = begin;
		this.end = end;
		this.weight = weight;
		division = weight / (end - begin);
	}

	/**
	 * @see com.frank.dip.math.Function#function(double)
	 */
	@Override
	public double function(double r)
	{
		if (r < begin || r >= end)
			return 0;
		else
			return division;
	}

	/**
	 * @see com.frank.dip.math.Function#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		Properties p = new Properties();
		p.put(PARAM_BEGIN, begin);
		p.put(PARAM_END, end);
		p.put(PARAM_WEIGHT, weight);
		return p;
	}

	/**
	 * @see com.frank.dip.math.Function#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p)
	{
		Object obj = null;
		obj = p.get(PARAM_BEGIN);
		if (obj != null && obj instanceof Number)
			begin = ((Number) obj).doubleValue();
		obj = p.get(PARAM_END);
		if (obj != null && obj instanceof Number)
			end = ((Number) obj).doubleValue();
		obj = p.get(PARAM_WEIGHT);
		if (obj != null && obj instanceof Number)
			weight = ((Number) obj).doubleValue();
		division = weight / (end - begin);
	}

	/**
	 * @see com.frank.dip.math.Function#getFunctionString()
	 */
	@Override
	public String getFunctionString()
	{
		return "f(x) = {weight / (end - begin), x∈[begin,end) | 0, otherwise}";//$NON-NLS-1$
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return toFunction();
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return String.format("f(x) = {%f, x∈[%f,%f) | 0, otherwise}"//$NON-NLS-1$
				, division, begin, end);
	}
}
