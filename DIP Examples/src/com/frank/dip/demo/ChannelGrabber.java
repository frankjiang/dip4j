/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * ChannelGrabber.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo;

import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;

/**
 * RGB channel grabber.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ChannelGrabber
{
	public static final int	RED		= 1;
	public static final int	GREEN	= 2;
	public static final int	BLUE	= 3;
	public static final int	GRAY	= 0;

	/**
	 * Grab the pixels in one channel.
	 * 
	 * @param ci
	 *            the color image to grab
	 * @param type
	 *            the grab type
	 * @return the grabbed image
	 */
	public static GrayImage grab(ColorImage ci, int type)
	{
		switch (type)
		{
			default:
			case GRAY:
				return new GrayImage(ci);
			case RED:
				return new GrayImage(ci.getRedMatrix());
			case GREEN:
				return new GrayImage(ci.getGreenMatrix());
			case BLUE:
				return new GrayImage(ci.getBlueMatrix());
		}
	}
}
