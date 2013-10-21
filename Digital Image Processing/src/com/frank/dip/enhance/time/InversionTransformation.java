/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. InversionTransformation.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.enhance.time;

import java.util.Properties;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;

/**
 * Image inversion.
 * <p>
 * In this class defines the inverse action of image. Inverse transform obeys
 * the formula:
 * 
 * <pre>
 * {@code s} = {@code L} - 1 - {@code r}
 * {@code L}: the scale level
 * {@code r}: the pixel input
 * {@code s}: the pixel output
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @param <T>
 *            image type
 * @version 1.0.0
 */
public abstract class InversionTransformation<T extends Image> extends
		EnhanceTransformation<T> implements Transform
{
	/**
	 * Inverse the binary image.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	final static public class Binary extends
			InversionTransformation<BinaryImage>
	{
		/**
		 * @see com.frank.dip.enhance.Transformation#function(double)
		 */
		@Override
		public double function(double r)
		{
			return SCALE_LEVEL - 1 - r;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Transform getInverseTransform()
				throws UnsupportedOperationException
		{
			return new Binary();
		}
	}

	/**
	 * Inverse the gray scale image.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	final static public class Gray extends InversionTransformation<GrayImage>
	{
		/**
		 * @see com.frank.dip.enhance.Transformation#function(double)
		 */
		@Override
		public double function(double r)
		{
			return SCALE_LEVEL - 1 - r;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Transform getInverseTransform()
				throws UnsupportedOperationException
		{
			return new Gray();
		}
	}

	/**
	 * Inverse the color image.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	final static public class Color extends InversionTransformation<ColorImage>
	{
		/**
		 * @see com.frank.dip.enhance.EnhanceTransformationation#function(int)
		 */
		@Override
		public double function(double r)
		{
			int r0, alpha, red, green, blue;
			r0 = (int) r;
			alpha = r0 >> 24;
			red = (r0 >> 16) & 0xff;
			green = (r0 >> 8) & 0xff;
			blue = r0 & 0xff;
			red = SCALE_LEVEL - 1 - red;
			green = SCALE_LEVEL - 1 - green;
			blue = SCALE_LEVEL - 1 - blue;
			return alpha << 24 | red << 16 | green << 8 | blue;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Transform getInverseTransform()
				throws UnsupportedOperationException
		{
			return new Color();
		}
	}

	/**
	 * Inverse the specified image.
	 * 
	 * @param source
	 *            the source image
	 * @return the inverse image
	 * @throws IllegalArgumentException
	 *             if the image type not supported
	 */
	public static <T extends Image> T inverse(T source)
			throws IllegalArgumentException
	{
		if (source instanceof GrayImage)
			return (T) (new Gray().operate((GrayImage) source));
		if (source instanceof BinaryImage)
			return (T) (new Binary().operate((BinaryImage) source));
		if (source instanceof ColorImage)
			return (T) (new Color().operate((ColorImage) source));
		throw new IllegalArgumentException(String.format(
				"Current inversion cannot support image type: %s", source
						.getClass().toString()));
	}

	/**
	 * Returns the inversion transformation of the specified image type.
	 * 
	 * @param type
	 *            the type of source image
	 * @return the inversion transformation
	 */
	public static <T extends Image> InversionTransformation<T> getInverseTransformation(
			Class<T> type)
	{
		if (type == GrayImage.class)
			return (InversionTransformation<T>) new Gray();
		if (type == BinaryImage.class)
			return (InversionTransformation<T>) new Binary();
		if (type == ColorImage.class)
			return (InversionTransformation<T>) new Color();
		throw new IllegalArgumentException(String.format(
				"Current inversion cannot support image type: %s",
				type.toString()));
	}

	/**
	 * Returns an empty properties instance of current inversion transform, due
	 * to this transform needs no parameter.
	 * 
	 * @see com.frank.dip.enhance.Transformation#getProperties()
	 */
	public Properties getProperties()
	{
		return new Properties();
	}

	/**
	 * Do nothing, due to this transform needs no parameter.
	 * 
	 * @see com.frank.dip.enhance.Transformation#setProperties(java.util.Properties)
	 */
	public void setProperties(Properties p)
	{
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#toString()
	 */
	@Override
	public String toString()
	{
		return toFunction();
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return String.format("s = %d - r", SCALE_LEVEL - 1);//$NON-NLS-1$
	}

	/**
	 * @see com.frank.dip.enhance.Transformation#getFunctionString()
	 */
	public String getFunctionString()
	{
		return "s = L - 1 - r";//$NON-NLS-1$
	}
}
