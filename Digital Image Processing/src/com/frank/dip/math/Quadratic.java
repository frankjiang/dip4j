/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Quadratic.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.math;

import java.util.Properties;

/**
 * The quadratic function.
 * 
 * <pre>
 * f(x) = Ax<sup>2</sup> + Bx + C
 * </pre>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Quadratic implements Function
{
	/**
	 * The parameter string.
	 */
	public static final String	PARAM_A	= "A", PARAM_B = "B", PARAM_C = "C";
	/**
	 * The quadratic parameters.
	 */
	protected double			a, b, c;

	/**
	 * Construct an instance of <tt>Quadratic</tt>.
	 * 
	 * <pre>
	 * f(x) = Ax<sup>2</sup> + Bx + C
	 * </pre>
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	public Quadratic(double a, double b, double c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
	 * @see com.frank.dip.math.Function#function(double)
	 */
	@Override
	public double function(double r)
	{
		return a * r * r + b * r + c;
	}

	/**
	 * @see com.frank.dip.math.Function#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		Properties p = new Properties();
		p.put(PARAM_A, a);
		p.put(PARAM_B, b);
		p.put(PARAM_C, c);
		return p;
	}

	/**
	 * @see com.frank.dip.math.Function#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p)
	{
		Object obj = null;
		obj = p.get(PARAM_A);
		if (obj != null && obj instanceof Number)
			a = ((Number) obj).doubleValue();
		obj = p.get(PARAM_B);
		if (obj != null && obj instanceof Number)
			b = ((Number) obj).doubleValue();
		obj = p.get(PARAM_C);
		if (obj != null && obj instanceof Number)
			c = ((Number) obj).doubleValue();
	}

	/**
	 * @see com.frank.dip.math.Function#getFunctionString()
	 */
	@Override
	public String getFunctionString()
	{
		return "Ax^2 + Bx + C = 0";
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
		return String.format("%f*x^2%+f * x%+f = 0", a, b, c);//$NON-NLS-1$
	}
}
