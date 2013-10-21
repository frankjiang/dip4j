/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Minus.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.arithmetic;

/**
 * Implementation of minus: a - b.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Minus implements Arithmetic
{
	/**
	 * Returns the result of the calculation {@code left} - {@code right}.
	 * 
	 * @see com.frank.dip.enhance.arithmetic.Arithmetic#calculate(int, int)
	 */
	@Override
	public int calculate(int left, int right)
	{
		int r = left + right;
		if (r < 0)
			return 0;
		else if (r < COLOR_SCALE_LEVEL)
			return r;
		else
			return COLOR_SCALE_LEVEL - 1;
	}
}
