/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. GrayScale.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance;

import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.ImageOperate;

/**
 * The color image to gray image transform.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class GrayScale implements ImageOperate<Image, GrayImage>
{
	/**
	 * Transform the color image to gray image.
	 * 
	 * @param ci
	 *            the color image
	 * @return the gray image
	 */
	public abstract GrayImage operate(Image ci);
}
