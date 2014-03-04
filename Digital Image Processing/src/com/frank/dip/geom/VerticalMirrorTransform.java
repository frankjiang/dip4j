/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * HorizontalMirrorTransform.java is PROPRIETARY/CONFIDENTIAL built in 9:46:51
 * AM, Mar 4, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * The vertical mirror transform.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class VerticalMirrorTransform implements GeometryTransform
{
	/**
	 * The width of source image.
	 */
	protected int	width;
	/**
	 * The height of source image.
	 */
	protected int	height;

	/**
	 * Construct an instance of <tt>VerticalMirrorTransform</tt>.
	 * 
	 * @param width
	 *            the width of source image
	 * @param height
	 */
	public VerticalMirrorTransform(int width, int height)
	{
		this.width = width;
		this.height = height;
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
		dst.setLocation(src.getX(), height - src.getY());
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
		return transform(src, dst);
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#createTransformedShape(java.awt.Shape)
	 */
	@Override
	public Rectangle createTransformedShape(Shape shape)
	{
		return new Rectangle(width, height);
	}
}
