/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Thresholding.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.BinaryImage;
import com.frank.dip.GrayImage;
import com.frank.dip.ImageOperate;

/**
 * A super class definition for all the thresholding algorithms.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class Thresholding implements ImageOperate<GrayImage, BinaryImage>
{
	/**
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
