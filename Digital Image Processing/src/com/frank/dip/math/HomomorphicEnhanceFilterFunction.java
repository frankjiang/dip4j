/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * HomomorphicEnhanceFilterFunction.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.math;

import java.util.Properties;

/**
 * The homomorphic enhance filter.
 * <p>
 * H<sup>*</sup>(u,v) = (&gamma;<sub>h</sub> - &gamma;<sub>l</sub>) * H(u,v) +
 * &gamma;<sub>l</sub><br>
 * H(u,v): original frequency filter<br>
 * &gamma;<sub>h</sub>: the limit parameter for high frequency<br>
 * &gamma;<sub>l</sub>: the limit parameter for low frequency
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class HomomorphicEnhanceFilterFunction implements ScalableFunction
{
	/**
	 * The parameters.
	 */
	public static final String	PARAM_H	= "h", PARAM_L = "l";
	/**
	 * The distance base filter function.
	 */
	protected Function			function;
	/**
	 * The limit parameter &gamma;<sub>h</sub> for high frequency.
	 */
	protected double			high;
	/**
	 * The limit parameter &gamma;<sub>l</sub> for low frequency.
	 */
	protected double			low;
	/**
	 * The scale parameter.
	 */
	protected double			scale;

	/**
	 * Construct an instance of <tt>HomomorphicEnhanceFilterFunction</tt>.
	 * 
	 * @param function
	 *            the frequency domain filter function
	 * @param high
	 *            the limit parameter &gamma;<sub>h</sub> for high frequency
	 * @param low
	 *            the limit parameter &gamma;<sub>l</sub> for low frequency
	 */
	public HomomorphicEnhanceFilterFunction(Function function, double high,
			double low)
	{
		this.function = function;
		this.high = high;
		this.low = low;
		scale = 128;
	}

	/**
	 * @see com.frank.dip.math.Function#function(double)
	 */
	@Override
	public double function(double r)
	{
		return (high - low) * function.function(r) + low;
	}

	/**
	 * @see com.frank.dip.math.Function#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		Properties p = function.getProperties();
		p.put(PARAM_H, high);
		p.put(PARAM_L, low);
		return p;
	}

	/**
	 * @see com.frank.dip.math.Function#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p)
	{
		Object obj = null;
		obj = p.get(PARAM_H);
		if (obj != null && obj instanceof Number)
			high = ((Number) obj).doubleValue();
		obj = p.get(PARAM_L);
		if (obj != null && obj instanceof Number)
			low = ((Number) obj).doubleValue();
		function.setProperties(p);
	}

	/**
	 * Returns the splitted function string.
	 * 
	 * @param s
	 *            the function string
	 * @return the splitted function
	 */
	private String split(String s)
	{
		int idx = s.indexOf("= ");
		if (idx != -1)
			return s.substring(idx + 2);
		{
			idx = s.indexOf('=');
			if (idx != -1)
				return s.substring(idx + 1);
			else
				return s;
		}
	}

	/**
	 * @see com.frank.dip.math.Function#getFunctionString()
	 */
	@Override
	public String getFunctionString()
	{
		return String
				.format("H(u,v) = (h - l) * ( %s ) + l", split(function.getFunctionString()));//$NON-NLS-1$
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return String
				.format("H(u,v) = %f*(%s)%+f", high - low, low, split(function.getFunctionString()));//$NON-NLS-1$
	}

	/**
	 * @see com.frank.dip.math.ScalableFunction#setScale(double)
	 */
	@Override
	public void setScale(double scale)
	{
		this.scale = scale;
	}

	/**
	 * @see com.frank.dip.math.ScalableFunction#getScale()
	 */
	@Override
	public double getScale()
	{
		return scale;
	}

	/**
	 * @see com.frank.dip.math.ScalableFunction#scaledFunction(double)
	 */
	@Override
	public double scaledFunction(double r)
	{
		return scale * function(r);
	}
}
