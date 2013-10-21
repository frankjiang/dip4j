/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * Interpolation.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */

package com.frank.dip.geom;

/**
 * The implementations for interpolations.
 * <p>
 * </p>
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Interpolation
{
	/**
	 * Bilinear interpolation.
	 * 
	 * @param ex
	 *            the distance of X coordinate between new pixel position and
	 *            original pixel position
	 * @param ey
	 *            the distance of Y coordinate between new pixel position and
	 *            original pixel position
	 * @param p00
	 *            the top-left pixel value
	 * @param p01
	 *            the top-right pixel value
	 * @param p10
	 *            the bottom-left pixel value
	 * @param p11
	 *            the bottom-right pixel value
	 * @return the interpolation value
	 */
	public static int bilinear(float ex, float ey, int p00, int p01,
			int p10, int p11)
	{
		int p = Math.round((1 - ey) * ((1 - ex) * p00 + ex * p01) + ey
				* ((1 - ex) * p10 + ex * p11));
		return p > 255 ? 255 : (p < 0 ? 0 : p);
	}

	/**
	 * Bicubic interpolation.
	 * 
	 * @param ex
	 *            the distance of X coordinate between new pixel position and
	 *            original pixel position
	 * @param ey
	 *            the distance of Y coordinate between new pixel position and
	 *            original pixel position
	 * @param p00
	 *            the top-left pixel value
	 * @param p01
	 *            the top-right pixel value
	 * @param p10
	 *            the bottom-left pixel value
	 * @param p11
	 *            the bottom-right pixel value
	 * @return the interpolation value
	 */
	public static int bicubic(float ex, float ey, int p00, int p01, int p10,
			int p11)
	{
		int p = Math.round(((1 - ex) * (1 - ey) * p00 + (1 - ex) * ey * p01
				+ ex * (1 - ey) * p10 + ex * ey * p11));
		return p > 255 ? 255 : (p < 0 ? 0 : p);
	}
}
