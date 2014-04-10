/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Overlying.java is PROPRIETARY/CONFIDENTIAL built in 2:27:07 PM, Mar 24, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.arithmetic;

/**
 * The implementation of image channel overlying.
 * <p>
 * The overlying method obeys the following formula:<br>
 * <code><strong>p</strong> = &alpha;&times;<strong>left</strong> + &beta;&times;<strong>right</strong></code>
 * <code>&alpha; + &beta; = 1</code>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Overlying implements Arithmetic
{
	/**
	 * The &alpha; parameter.
	 */
	protected double	alpha;
	/**
	 * The &beta; parameter.
	 */
	protected double	beta;

	/**
	 * Construct an instance of <tt>Overlying</tt>.
	 */
	public Overlying()
	{
		alpha = beta = 0.5;
	}

	/**
	 * Returns alpha.
	 * 
	 * @return the alpha
	 */
	public double getAlpha()
	{
		return alpha;
	}

	/**
	 * Set alpha.
	 * 
	 * @param alpha
	 *            the value of alpha
	 */
	public void setAlpha(double alpha)
	{
		this.alpha = alpha;
	}

	/**
	 * @see com.frank.dip.enhance.arithmetic.Arithmetic#calculate(int, int)
	 */
	@Override
	public int calculate(int left, int right)
	{
		return (int) Math.round(left * alpha + right * beta);
	}
}
