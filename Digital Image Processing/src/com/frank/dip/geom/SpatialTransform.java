/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * SpatialTransform.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * Perform a spatial transform from distorted rectangle to a normal rectangle.
 * <p>
 * The inverse distortion function is built base on bilinear functions:
 * 
 * <pre>
 * <code>x' = c<sub>1</sub>*x + c<sub>2</sub>*y + c<sub>3</sub>*x*y + c<sub>4</sub></code>
 * <code>y' = c<sub>5</sub>*x + c<sub>6</sub>*y + c<sub>7</sub>*x*y + c<sub>8</sub></code>
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SpatialTransform implements GeometryTransform
{
	/**
	 * The width of corrected image.
	 */
	protected double	width;
	/**
	 * The height of corrected image.
	 */
	protected double	height;
	/**
	 * The parameters for bilinear distortion.
	 */
	protected double	c1, c2, c3, c4, c5, c6, c7, c8;

	/**
	 * Construct an instance of <tt>SpatialTransform</tt>.
	 * 
	 * @param p00
	 *            left-top anchor
	 * @param p01
	 *            right-top anchor
	 * @param p11
	 *            right-bottom anchor
	 * @param p10
	 *            left-bottom anchor
	 * @param width
	 *            the width of corrected image, if &le; 0 create by the anchors
	 * @param height
	 *            the height of corrected image, if &le; 0 create by the anchors
	 */
	public SpatialTransform(Point2D p00, Point2D p01, Point2D p11, Point2D p10,
			double width, double height)
	{
		// the distorted image anchors
		double x1 = p00.getX(), y1 = p00.getY();
		double x2 = p01.getX(), y2 = p01.getY();
		double x3 = p11.getX(), y3 = p11.getY();
		double x4 = p10.getX(), y4 = p10.getY();
		// calculate the standard image anchors
		if (width <= Double.MIN_VALUE || height <= Double.MIN_VALUE)
		{
			double[] xs = { x1, x2, x3, x4 };
			double[] ys = { y1, y2, y3, y4 };
			Arrays.sort(xs);
			Arrays.sort(ys);
			this.width = (xs[3] - xs[0]) > 480 ? 480 : (xs[3] - xs[0]);
			this.height = this.width * (ys[3] - ys[0]) / (xs[3] - xs[0]);
		}
		else
		{
			this.width = width;
			this.height = height;
		}
		// standard image
		double xt1 = 0, yt1 = 0;
		double xt2 = this.width, yt2 = 0;
		double xt3 = this.width, yt3 = this.height;
		double xt4 = 0, yt4 = this.height;
		// the distortion correcting parameters
		// @formatter:off
		double base = (xt1*xt2*yt1*yt3 - xt1*xt3*yt1*yt2 - xt1*xt2*yt1*yt4 - xt1*xt2*yt2*yt3 + xt1*xt4*yt1*yt2 + xt2*xt3*yt1*yt2 + xt1*xt2*yt2*yt4 + xt1*xt3*yt1*yt4 + xt1*xt3*yt2*yt3 - xt1*xt4*yt1*yt3 - xt2*xt3*yt1*yt3 - xt2*xt4*yt1*yt2 - xt1*xt3*yt3*yt4 - xt1*xt4*yt2*yt4 - xt2*xt3*yt2*yt4 + xt2*xt4*yt1*yt4 + xt2*xt4*yt2*yt3 + xt3*xt4*yt1*yt3 + xt1*xt4*yt3*yt4 + xt2*xt3*yt3*yt4 - xt3*xt4*yt1*yt4 - xt3*xt4*yt2*yt3 - xt2*xt4*yt3*yt4 + xt3*xt4*yt2*yt4);
		c1 = (x2*xt1*yt1*yt3 - x3*xt1*yt1*yt2 - x1*xt2*yt2*yt3 - x2*xt1*yt1*yt4 + x3*xt2*yt1*yt2 + x4*xt1*yt1*yt2 + x1*xt2*yt2*yt4 + x1*xt3*yt2*yt3 - x2*xt3*yt1*yt3 + x3*xt1*yt1*yt4 - x4*xt1*yt1*yt3 - x4*xt2*yt1*yt2 - x1*xt3*yt3*yt4 - x1*xt4*yt2*yt4 + x2*xt4*yt1*yt4 - x3*xt2*yt2*yt4 + x4*xt2*yt2*yt3 + x4*xt3*yt1*yt3 + x1*xt4*yt3*yt4 + x2*xt3*yt3*yt4 - x3*xt4*yt1*yt4 - x4*xt3*yt2*yt3 - x2*xt4*yt3*yt4 + x3*xt4*yt2*yt4)/base;
		c2 =-(x2*xt1*xt3*yt1 - x3*xt1*xt2*yt1 - x1*xt2*xt3*yt2 - x2*xt1*xt4*yt1 + x3*xt1*xt2*yt2 + x4*xt1*xt2*yt1 + x1*xt2*xt3*yt3 + x1*xt2*xt4*yt2 - x2*xt1*xt3*yt3 + x3*xt1*xt4*yt1 - x4*xt1*xt2*yt2 - x4*xt1*xt3*yt1 - x1*xt2*xt4*yt4 - x1*xt3*xt4*yt3 + x2*xt1*xt4*yt4 - x3*xt2*xt4*yt2 + x4*xt1*xt3*yt3 + x4*xt2*xt3*yt2 + x1*xt3*xt4*yt4 + x2*xt3*xt4*yt3 - x3*xt1*xt4*yt4 - x4*xt2*xt3*yt3 - x2*xt3*xt4*yt4 + x3*xt2*xt4*yt4)/base;
		c3 = (x1*xt2*yt3 - x1*xt3*yt2 - x2*xt1*yt3 + x2*xt3*yt1 + x3*xt1*yt2 - x3*xt2*yt1 - x1*xt2*yt4 + x1*xt4*yt2 + x2*xt1*yt4 - x2*xt4*yt1 - x4*xt1*yt2 + x4*xt2*yt1 + x1*xt3*yt4 - x1*xt4*yt3 - x3*xt1*yt4 + x3*xt4*yt1 + x4*xt1*yt3 - x4*xt3*yt1 - x2*xt3*yt4 + x2*xt4*yt3 + x3*xt2*yt4 - x3*xt4*yt2 - x4*xt2*yt3 + x4*xt3*yt2)/base;
		c4 = (x2*xt1*xt3*yt1*yt4 - x2*xt1*xt4*yt1*yt3 - x3*xt1*xt2*yt1*yt4 + x3*xt1*xt4*yt1*yt2 + x4*xt1*xt2*yt1*yt3 - x4*xt1*xt3*yt1*yt2 - x1*xt2*xt3*yt2*yt4 + x1*xt2*xt4*yt2*yt3 + x3*xt1*xt2*yt2*yt4 - x3*xt2*xt4*yt1*yt2 - x4*xt1*xt2*yt2*yt3 + x4*xt2*xt3*yt1*yt2 + x1*xt2*xt3*yt3*yt4 - x1*xt3*xt4*yt2*yt3 - x2*xt1*xt3*yt3*yt4 + x2*xt3*xt4*yt1*yt3 + x4*xt1*xt3*yt2*yt3 - x4*xt2*xt3*yt1*yt3 - x1*xt2*xt4*yt3*yt4 + x1*xt3*xt4*yt2*yt4 + x2*xt1*xt4*yt3*yt4 - x2*xt3*xt4*yt1*yt4 - x3*xt1*xt4*yt2*yt4 + x3*xt2*xt4*yt1*yt4)/base;
		c5 = (xt1*y2*yt1*yt3 - xt1*y3*yt1*yt2 - xt1*y2*yt1*yt4 + xt1*y4*yt1*yt2 - xt2*y1*yt2*yt3 + xt2*y3*yt1*yt2 + xt1*y3*yt1*yt4 - xt1*y4*yt1*yt3 + xt2*y1*yt2*yt4 - xt2*y4*yt1*yt2 + xt3*y1*yt2*yt3 - xt3*y2*yt1*yt3 - xt2*y3*yt2*yt4 + xt2*y4*yt2*yt3 - xt3*y1*yt3*yt4 + xt3*y4*yt1*yt3 - xt4*y1*yt2*yt4 + xt4*y2*yt1*yt4 + xt3*y2*yt3*yt4 - xt3*y4*yt2*yt3 + xt4*y1*yt3*yt4 - xt4*y3*yt1*yt4 - xt4*y2*yt3*yt4 + xt4*y3*yt2*yt4)/base;
		c6 = (xt1*xt2*y3*yt1 - xt1*xt3*y2*yt1 - xt1*xt2*y3*yt2 - xt1*xt2*y4*yt1 + xt1*xt4*y2*yt1 + xt2*xt3*y1*yt2 + xt1*xt2*y4*yt2 + xt1*xt3*y2*yt3 + xt1*xt3*y4*yt1 - xt1*xt4*y3*yt1 - xt2*xt3*y1*yt3 - xt2*xt4*y1*yt2 - xt1*xt3*y4*yt3 - xt1*xt4*y2*yt4 - xt2*xt3*y4*yt2 + xt2*xt4*y1*yt4 + xt2*xt4*y3*yt2 + xt3*xt4*y1*yt3 + xt1*xt4*y3*yt4 + xt2*xt3*y4*yt3 - xt3*xt4*y1*yt4 - xt3*xt4*y2*yt3 - xt2*xt4*y3*yt4 + xt3*xt4*y2*yt4)/base;
		c7 =-(xt1*y2*yt3 - xt1*y3*yt2 - xt2*y1*yt3 + xt2*y3*yt1 + xt3*y1*yt2 - xt3*y2*yt1 - xt1*y2*yt4 + xt1*y4*yt2 + xt2*y1*yt4 - xt2*y4*yt1 - xt4*y1*yt2 + xt4*y2*yt1 + xt1*y3*yt4 - xt1*y4*yt3 - xt3*y1*yt4 + xt3*y4*yt1 + xt4*y1*yt3 - xt4*y3*yt1 - xt2*y3*yt4 + xt2*y4*yt3 + xt3*y2*yt4 - xt3*y4*yt2 - xt4*y2*yt3 + xt4*y3*yt2)/base;
		c8 =-(xt1*xt2*y3*yt1*yt4 - xt1*xt2*y4*yt1*yt3 - xt1*xt3*y2*yt1*yt4 + xt1*xt3*y4*yt1*yt2 + xt1*xt4*y2*yt1*yt3 - xt1*xt4*y3*yt1*yt2 - xt1*xt2*y3*yt2*yt4 + xt1*xt2*y4*yt2*yt3 + xt2*xt3*y1*yt2*yt4 - xt2*xt3*y4*yt1*yt2 - xt2*xt4*y1*yt2*yt3 + xt2*xt4*y3*yt1*yt2 + xt1*xt3*y2*yt3*yt4 - xt1*xt3*y4*yt2*yt3 - xt2*xt3*y1*yt3*yt4 + xt2*xt3*y4*yt1*yt3 + xt3*xt4*y1*yt2*yt3 - xt3*xt4*y2*yt1*yt3 - xt1*xt4*y2*yt3*yt4 + xt1*xt4*y3*yt2*yt4 + xt2*xt4*y1*yt3*yt4 - xt2*xt4*y3*yt1*yt4 - xt3*xt4*y1*yt2*yt4 + xt3*xt4*y2*yt1*yt4)/base;
		// @formatter:on
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#transform(java.awt.geom.Point2D,
	 *      java.awt.geom.Point2D)
	 */
	@Override
	public Point2D transform(Point2D src, Point2D dst)
	{
		if (dst == null)
		{
			if (src instanceof Point2D.Float)
				dst = new Point2D.Float();
			else
				dst = new Point2D.Double();
		}
		double x = src.getX(), y = src.getY();
		dst.setLocation(c1 * x + c2 * y + c3 * x * y + c4, c5 * x + c6 * y + c7
				* x * y + c8);
		return dst;
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#inverseTransform(java.awt.geom.Point2D,
	 *      java.awt.geom.Point2D)
	 */
	@Override
	public Point2D inverseTransform(Point2D src, Point2D dst)
			throws NoninvertibleTransformException
	{
		if (dst == null)
		{
			if (src instanceof Point2D.Float)
				dst = new Point2D.Float();
			else
				dst = new Point2D.Double();
		}
		double x = src.getX(), y = src.getY();
		dst.setLocation(c1 * x + c2 * y + c3 * x * y + c4, c5 * x + c6 * y + c7
				* x * y + c8);
		return dst;
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#createTransformedShape(java.awt.Shape)
	 */
	@Override
	public Rectangle createTransformedShape(Shape shape)
	{
		return new Rectangle((int) Math.round(width), (int) Math.round(height));
	}
}
