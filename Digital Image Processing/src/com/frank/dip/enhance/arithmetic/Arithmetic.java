/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Arithmetic.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.arithmetic;

import com.frank.dip.ColorScaleLevel;

/**
 * Definition of any arithmetic calculation {@code left} &oplus; {@code right}.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface Arithmetic extends ColorScaleLevel
{

	/**
	 * Returns the result of the calculation {@code left} &oplus; {@code right}.
	 * 
	 * @param left
	 * @param right
	 * @return the result
	 */
	public int calculate(int left, int right);
}
