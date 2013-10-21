/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. ThresholdFinder.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use
 * is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * The threshold finder interface defines an algorithm for finding a global
 * threshold of a specified gray scale image.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface ThresholdFinder
{
	/**
	 * Returns the found threshold of the specified gray scale image.
	 * 
	 * @param image
	 *            the gray scale image
	 * @return the threshold
	 */
	public int threshold(GrayImage image);

	/**
	 * Returns a string to representing the threshold finder.
	 * 
	 * @return the string
	 */
	public String getFinderName();
}
