/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. PiecewiseLinearTransformation.java is PROPRIETARY/CONFIDENTIAL
 * built in 2013. Use is subject to license terms.
 */
package com.frank.dip.enhance.time;

import java.awt.geom.Point2D;
import java.util.Properties;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;
import com.frank.math.StraightLine;

/**
 * The piecewise linear transformation.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class PiecewiseLinearTransformation<T extends Image> extends
		EnhanceTransformation<T> implements Transform
{
	/**
	 * Parameter strings.
	 */
	public static final String	PARAM_A_X	= "ax", PARAM_A_Y = "ay", PARAM_B_X = "bx", PARAM_B_Y = "by";	//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
	/**
	 * The piecewise points.
	 */
	protected Point2D			o, t, a, b;
	/**
	 * The piecewise lines.
	 */
	protected StraightLine		oa, ab, bt;

	/**
	 * Construct an instance of <tt>PiecewiseLinearTransformation</tt>.
	 * 
	 * @param ax
	 *            x-coordinate of the first point
	 * @param ay
	 *            y-coordinate of the first point
	 * @param bx
	 *            x-coordinate of the second point
	 * @param by
	 *            y-coordinate of the second point
	 */
	public PiecewiseLinearTransformation(double ax, double ay, double bx,
			double by)
	{
		initialize(ax, ay, bx, by);
	}

	/**
	 * Initialize the piecewise linear transformation points and lines.
	 * 
	 * @param ax
	 *            x-coordinate of the first point
	 * @param ay
	 *            y-coordinate of the first point
	 * @param bx
	 *            x-coordinate of the second point
	 * @param by
	 *            y-coordinate of the second point
	 */
	protected void initialize(double ax, double ay, double bx, double by)
	{
		if (!checkBound(ax) || !checkBound(ay) || !checkBound(bx)
				|| !checkBound(by))
			throw new IllegalArgumentException(String.format(
					"Bounds(%f,%f) to (%f,%f) is out of (0,0) to (255,255)",
					ax, ay, bx, by));
		a = new Point2D.Double(ax, ay);
		b = new Point2D.Double(bx, by);
		o = new Point2D.Double(0, 0);
		t = new Point2D.Double(SCALE_LEVEL - 1, SCALE_LEVEL - 1);
		oa = new StraightLine(o, a);
		ab = new StraightLine(a, b);
		bt = new StraightLine(b, t);
	}

	/**
	 * Returns <tt>true</tt> if the point coordinate value input is legal.
	 * 
	 * @param d
	 *            the point coordinate value
	 * @return <tt>true</tt> if the point coordinate value input is legal
	 */
	protected boolean checkBound(double d)
	{
		return d > 0 && d < SCALE_LEVEL - 1;
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#function(double)
	 */
	@Override
	public double function(double r)
	{
		if (r < a.getX())
			return oa.y(r);
		else if (r < b.getX())
			return ab.y(r);
		else
			return bt.y(r);
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		Properties p = new Properties();
		p.put(PARAM_A_X, a.getX());
		p.put(PARAM_A_Y, a.getY());
		p.put(PARAM_B_X, b.getX());
		p.put(PARAM_B_Y, b.getY());
		return p;
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p)
	{
		Object obj = null;
		double ax = 0, ay = 0, bx = 0, by = 0;
		obj = p.get(PARAM_A_X);
		if (obj != null && obj instanceof Number)
			ax = ((Number) obj).doubleValue();
		obj = p.get(PARAM_A_Y);
		if (obj != null && obj instanceof Number)
			ay = ((Number) obj).doubleValue();
		obj = p.get(PARAM_B_X);
		if (obj != null && obj instanceof Number)
			bx = ((Number) obj).doubleValue();
		obj = p.get(PARAM_B_Y);
		if (obj != null && obj instanceof Number)
			by = ((Number) obj).doubleValue();
		initialize(ax, ay, bx, by);
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#getFunctionString()
	 */
	@Override
	public String getFunctionString()
	{
		return oa.toString() + "," + ab.toString() + "," + bt.toString();//$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#toString()
	 */
	@Override
	public String toString()
	{
		return getFunctionString();
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return getFunctionString();
	}

	/**
	 * The piecewise linear transformation for binary image.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Binary extends
			PiecewiseLinearTransformation<BinaryImage>
	{
		/**
		 * Construct an instance of <tt>PiecewiseLinearTransformationBinary</tt>
		 * .
		 * 
		 * @param ax
		 *            x-coordinate of the first point
		 * @param ay
		 *            y-coordinate of the first point
		 * @param bx
		 *            x-coordinate of the second point
		 * @param by
		 *            y-coordinate of the second point
		 */
		public Binary(double ax, double ay, double bx, double by)
		{
			super(ax, ay, bx, by);
		}

		/**
		 * Due to the piecewise linear transformation cannot make any effect in
		 * a binary image, this method returns a copy of the current binary
		 * image without any transform.
		 * 
		 * @see com.frank.dip.enhance.EnhanceTransformationation#operate(com.frank.dip.Image)
		 */
		@Override
		public BinaryImage operate(BinaryImage source)
		{
			return source.clone();
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#function(double)
		 */
		@Override
		public double function(double r)
		{
			return r;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Binary getInverseTransform()
				throws UnsupportedOperationException
		{
			return new Binary(a.getY(), a.getX(), b.getY(), b.getX());
		}
	}

	/**
	 * The piecewise linear transformation for gray image.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Gray extends
			PiecewiseLinearTransformation<GrayImage>
	{
		/**
		 * Construct an instance of <tt>PiecewiseLinearTransformationBinary</tt>
		 * .
		 * 
		 * @param ax
		 *            x-coordinate of the first point
		 * @param ay
		 *            y-coordinate of the first point
		 * @param bx
		 *            x-coordinate of the second point
		 * @param by
		 *            y-coordinate of the second point
		 */
		public Gray(double ax, double ay, double bx, double by)
		{
			super(ax, ay, bx, by);
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Gray getInverseTransform() throws UnsupportedOperationException
		{
			return new Gray(a.getY(), a.getX(), b.getY(), b.getX());
		}
	}

	/**
	 * The piecewise linear transformation for color image.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Color extends
			PiecewiseLinearTransformation<ColorImage>
	{
		/**
		 * Construct an instance of <tt>PiecewiseLinearTransformationBinary</tt>
		 * .
		 * 
		 * @param ax
		 *            x-coordinate of the first point
		 * @param ay
		 *            y-coordinate of the first point
		 * @param bx
		 *            x-coordinate of the second point
		 * @param by
		 *            y-coordinate of the second point
		 */
		public Color(double ax, double ay, double bx, double by)
		{
			super(ax, ay, bx, by);
		}

		/**
		 * @see com.frank.dip.enhance.time.EnhanceTransformation#perform(double)
		 */
		@Override
		public int perform(double r)
		{
			int r0, alpha, red, green, blue;
			r0 = (int) r;
			alpha = r0 >> 24;
			red = (r0 >> 16) & 0xff;
			green = (r0 >> 8) & 0xff;
			blue = r0 & 0xff;
			red = (int) Math.round(function(red));
			green = (int) Math.round(function(green));
			blue = (int) Math.round(function(blue));
			return alpha << 24 | red << 16 | green << 8 | blue;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Gray getInverseTransform() throws UnsupportedOperationException
		{
			return new Gray(a.getY(), a.getX(), b.getY(), b.getX());
		}
	}

	/**
	 * Perform piecewise linear transformation to the specified image.
	 * 
	 * @param source
	 *            the source image
	 * @param ax
	 *            x-coordinate of the first point
	 * @param ay
	 *            y-coordinate of the first point
	 * @param bx
	 *            x-coordinate of the second point
	 * @param by
	 *            y-coordinate of the second point
	 * @return the transformed image
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> T piecewiseLinear(T source, double ax,
			double ay, double bx, double by) throws IllegalImageTypeException
	{
		if (source instanceof GrayImage)
			return (T) (new Gray(ax, ay, bx, by).operate((GrayImage) source));
		if (source instanceof BinaryImage)
			return (T) (new Binary(ax, ay, bx, by)
					.operate((BinaryImage) source));
		if (source instanceof ColorImage)
			return (T) (new Color(ax, ay, bx, by).operate((ColorImage) source));
		throw new IllegalImageTypeException(
				PiecewiseLinearTransformation.class, source.getClass());
	}

	/**
	 * Get piecewise linear transformation according to the specified image
	 * type.
	 * 
	 * @param type
	 *            the type of source image
	 * @param ax
	 *            x-coordinate of the first point
	 * @param ay
	 *            y-coordinate of the first point
	 * @param bx
	 *            x-coordinate of the second point
	 * @param by
	 *            y-coordinate of the second point
	 * @return the transformation
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> PiecewiseLinearTransformation<T> getPiecewiseLinearTransformation(
			Class<T> type, double ax, double ay, double bx, double by)
			throws IllegalImageTypeException
	{
		if (type == GrayImage.class)
			return (PiecewiseLinearTransformation<T>) new Gray(ax, ay, bx, by);
		if (type == BinaryImage.class)
			return (PiecewiseLinearTransformation<T>) new Binary(ax, ay, bx, by);
		if (type == ColorImage.class)
			return (PiecewiseLinearTransformation<T>) new Color(ax, ay, bx, by);
		throw new IllegalImageTypeException(
				PiecewiseLinearTransformation.class, type);
	}

	/**
	 * Get inverse piecewise linear transformation according to the specified
	 * image type.
	 * 
	 * @param type
	 *            the type of source image
	 * @param ax
	 *            x-coordinate of the first point
	 * @param ay
	 *            y-coordinate of the first point
	 * @param bx
	 *            x-coordinate of the second point
	 * @param by
	 *            y-coordinate of the second point
	 * @return the transformation
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> PiecewiseLinearTransformation<T> getInversePiecewiseLinearTransformation(
			Class<T> type, double ax, double ay, double bx, double by)
			throws IllegalImageTypeException
	{
		if (type == GrayImage.class)
			return (PiecewiseLinearTransformation<T>) new Gray(ay, ax, by, bx);
		if (type == BinaryImage.class)
			return (PiecewiseLinearTransformation<T>) new Binary(ay, ax, by, bx);
		if (type == ColorImage.class)
			return (PiecewiseLinearTransformation<T>) new Color(ay, ax, by, bx);
		throw new IllegalImageTypeException(
				PiecewiseLinearTransformation.class, type);
	}

	/**
	 * Perform inverse piecewise linear transformation to the specified image.
	 * 
	 * @param source
	 *            the source image
	 * @param ax
	 *            x-coordinate of the first point
	 * @param ay
	 *            y-coordinate of the first point
	 * @param bx
	 *            x-coordinate of the second point
	 * @param by
	 *            y-coordinate of the second point
	 * @return the transformed image
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> T inversePiecewiseLinear(T source,
			double ax, double ay, double bx, double by)
			throws IllegalImageTypeException
	{
		if (source instanceof GrayImage)
			return (T) (new Gray(ay, ax, by, bx).operate((GrayImage) source));
		if (source instanceof BinaryImage)
			return (T) (new Binary(ay, ax, by, bx)
					.operate((BinaryImage) source));
		if (source instanceof ColorImage)
			return (T) (new Color(ay, ax, by, bx).operate((ColorImage) source));
		throw new IllegalImageTypeException(
				PiecewiseLinearTransformation.class, source.getClass());
	}
}
