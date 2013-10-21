/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. InverseIntegralFunction.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.math;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

/**
 * The inverse function for the integral function of a specified function.
 * <p>
 * This inverse function build the inverse integral function F<sup>-1</sup>(x)
 * for the original function f(x).<br>
 * F(x) = &int;f(x)dx
 * </p>
 * <p>
 * In this function, the inverse function using {@linkplain TreeMap} to build
 * the inverse integral function.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class InverseIntegralFunction implements Function
{
	/**
	 * The inverse values of inverse integral.
	 */
	protected TreeMap<Double, Double>	inverseIntegral;

	/**
	 * Construct an instance of <tt>InverseIntegralFunction</tt>. An inverse
	 * integral function mappings the input region {@code R} to values, offering
	 * the mapping from value to input.
	 * 
	 * @param function
	 *            the original function f(x)
	 * @param begin
	 *            the begin of the specified region {@code R}
	 * @param end
	 *            the end of the specified region {@code R}
	 * @param step
	 *            the precision value for build the inverse integral function
	 */
	public InverseIntegralFunction(Function function, double begin, double end,
			double step)
	{
		inverseIntegral = new TreeMap<Double, Double>();
		int size = (int) Math.floor((end - begin) / step) + 1;
		double[] keys = new double[size];// the result of integral
		double[] values = new double[size];// the value of inputs
		int i = 0;
		double r = begin;
		for (i = 0; i < values.length; r += step, i++)
		{
			values[i] = r;
			keys[i] = function.function(r);
		}
		// build integral image
		for (i = 1; i < values.length; i++)
			keys[i] += keys[i - 1];
		// mapping integral inputs and outputs
		for (i = 0; i < values.length; i++)
			inverseIntegral.put(keys[i], values[i]);
	}

	/**
	 * @see com.frank.dip.math.Function#function(double)
	 */
	@Override
	public double function(double r)
	{
		if (inverseIntegral.isEmpty())
			return 0;
		Entry<Double, Double> e = inverseIntegral.floorEntry(r);
		if (e == null)
		{
			Entry<Double, Double> first = inverseIntegral.firstEntry();
			if (r < first.getKey())
				e = first;
			else
				e = inverseIntegral.lastEntry();
		}
		return e.getValue();
	}

	/**
	 * @see com.frank.dip.math.Function#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		return new Properties();
	}

	/**
	 * @see com.frank.dip.math.Function#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p)
	{
	}

	/**
	 * @see com.frank.dip.math.Function#getFunctionString()
	 */
	@Override
	public String getFunctionString()
	{
		return "Inverse integral: " + inverseIntegral.toString();//$NON-NLS-1$
	}

	@Override
	public String toString()
	{
		return getFunctionString();
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return getFunctionString();
	}
}
