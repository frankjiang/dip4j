/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Kernel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.convolver;

import com.frank.dip.ColorScaleLevel;

/**
 * The kernel interface is the definition for a structure with several matrix
 * inside.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface Kernel extends Cloneable, ColorScaleLevel
{

	/**
	 * Perform the specified convolve to the pixels.
	 * 
	 * @param pixels
	 *            the line first order pixels input
	 * @return the pixel value after convolution
	 */
	public float perform(int... pixels);

	/**
	 * Returns the width of the kernel.
	 * 
	 * @return the kernel width
	 */
	public int width();

	/**
	 * Returns the height of the kernel.
	 * 
	 * @return the kernel height
	 */
	public int height();
}
