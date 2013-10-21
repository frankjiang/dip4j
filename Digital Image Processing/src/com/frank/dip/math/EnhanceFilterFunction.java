/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * EnhanceFilterFunction.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.math;

import java.util.Properties;

/**
 * The enhance filter.
 * <p>
 * H<sup>*</sup>(u,v) = a + b * H(u,v)<br>
 * H(u,v): original frequency filter<br>
 * a: the amplification of self image<br>
 * b: the sharpen degree of target frequency domain
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class EnhanceFilterFunction implements ScalableFunction
{
	/**
	 * The parameters.
	 */
	public static final String	PARAM_A	= "amplification", PARAM_B = "sharpen";
	/**
	 * The distance base filter function.
	 */
	protected Function			function;
	/**
	 * The amplification {@code a} of self image.
	 */
	protected double			amplification;
	/**
	 * The sharpen degree {@code b} of target frequency domain.
	 */
	protected double			sharpen;
	/**
	 * The scale parameter.
	 */
	protected double			scale;

	/**
	 * Construct an instance of <tt>EnhanceFilterFunction</tt>.
	 * 
	 * @param function
	 *            the frequency domain filter function
	 * @param amplification
	 *            the amplification {@code a} of self image
	 * @param sharpen
	 *            the sharpen degree {@code b} of target frequency domain
	 */
	public EnhanceFilterFunction(Function function, double amplification,
			double sharpen)
	{
		this.function = function;
		this.amplification = amplification;
		this.sharpen = sharpen;
		scale = 255;
	}

	/**
	 * @see com.frank.dip.math.Function#function(double)
	 */
	@Override
	public double function(double r)
	{
		return amplification + sharpen * function.function(r);
	}

	/**
	 * @see com.frank.dip.math.Function#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		Properties p = function.getProperties();
		p.put(PARAM_A, amplification);
		p.put(PARAM_B, sharpen);
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
			amplification = ((Number) obj).doubleValue();
		obj = p.get(PARAM_B);
		if (obj != null && obj instanceof Number)
			sharpen = ((Number) obj).doubleValue();
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
		return String.format(
				"H(u,v) = a + b * ( %s )", split(function.getFunctionString()));//$NON-NLS-1$
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return String
				.format("H(u,v) = %f%+f*(%s)", amplification, sharpen, split(function.getFunctionString()));//$NON-NLS-1$
	}

	/**
	 * Returns the amplification {@code a} of self image.
	 * 
	 * @return the amplification {@code a} of self image
	 */
	public double getAmplification()
	{
		return amplification;
	}

	/**
	 * Set the amplification {@code a} of self image.
	 * 
	 * @param amplification
	 *            the amplification {@code a} of self image
	 */
	public void setAmplification(double amplification)
	{
		this.amplification = amplification;
	}

	/**
	 * Returns the sharpen degree {@code b} of target frequency domain.
	 * 
	 * @return the sharpen degree {@code b} of target frequency domain
	 */
	public double getSharpen()
	{
		return sharpen;
	}

	/**
	 * Set the sharpen degree {@code b} of target frequency domain
	 * 
	 * @param sharpen
	 *            the sharpen degree {@code b} of target frequency domain
	 */
	public void setSharpen(double sharpen)
	{
		this.sharpen = sharpen;
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
