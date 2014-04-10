/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * SineFunction.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.color;

import java.util.Properties;

import com.frank.dip.math.Function;

/**
 * The function of sine:
 * 
 * <pre>
 * <code>f(x) = |A&times;sin(&omega;x+&phi;)+B|</code>
 * </pre>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class AbsoluteSineFunction implements Function
{
	/**
	 * The strings representing the parameters.
	 */
	public static final String	PARAM_A	= "A", PARAM_B = "B",
			PARAM_OMEGA = "omega", PARAM_PHI = "phi";
	/**
	 * The parameter A.
	 */
	protected double			a;
	/**
	 * The parameter B.
	 */
	protected double			b;
	/**
	 * The parameter &omega;.
	 */
	protected double			omega;
	/**
	 * The parameter &phi;.
	 */
	protected double			phi;

	/**
	 * Construct an instance of <tt>SineFunction</tt>.
	 * 
	 * <pre>
	 * <code>f(x) = A&times;sin(&omega;x+&phi;)+B</code>
	 * </pre>
	 * 
	 * @param a
	 *            A
	 * @param omega
	 *            &omega;
	 * @param phi
	 *            &phi;
	 * @param b
	 *            B
	 */
	public AbsoluteSineFunction(double a, double omega, double phi, double b)
	{
		this.a = a;
		this.b = b;
		this.phi = phi;
		this.omega = omega;
	}

	/**
	 * @see com.frank.dip.math.Function#function(double)
	 */
	@Override
	public double function(double r)
	{
		return Math.abs(a * Math.sin(omega * r + phi) + b);
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
		p.put(PARAM_OMEGA, omega);
		p.put(PARAM_PHI, phi);
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
		obj = p.get(PARAM_OMEGA);
		if (obj != null && obj instanceof Number)
			omega = ((Number) obj).doubleValue();
		obj = p.get(PARAM_PHI);
		if (obj != null && obj instanceof Number)
			phi = ((Number) obj).doubleValue();
	}

	/**
	 * @see com.frank.dip.math.Function#getFunctionString()
	 */
	@Override
	public String getFunctionString()
	{
		return "f(x) = |A * sin(\u03c9 * x + \u03c6) + B|";//$NON-NLS-1$
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return String.format("f(x) = |%f*sin(%f*x%+f)%+f|", a, omega, phi, b);
	}
}
