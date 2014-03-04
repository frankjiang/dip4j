/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * MultiGeometryTransform.java is PROPRIETARY/CONFIDENTIAL built in 10:15:58 AM,
 * Mar 4, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * The multiplied geometry transform operator.
 * <p>
 * In this operator, the geometry transformations will be stored in one list and
 * be performed orderly.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MultiGeometryTransform implements GeometryTransform
{
	/**
	 * The geometry transform operator list.
	 */
	protected GeometryTransform[]	transforms;

	/**
	 * Construct an instance of <tt>MultiGeometryTransform</tt>.
	 * 
	 * @param transforms
	 *            the geometry transform operator list, cannot be less than 1
	 * @throws IllegalArgumentException
	 *             if the length transforms is less than 1
	 */
	public MultiGeometryTransform(GeometryTransform... transforms)
			throws IllegalArgumentException
	{
		if (transforms.length < 1)
			throw new IllegalArgumentException(
					"The transformation list must contains at least one transformation.");
		this.transforms = transforms;
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#transform(java.awt.geom.Point2D,
	 *      java.awt.geom.Point2D)
	 */
	@Override
	public Point2D transform(Point2D src, Point2D dst)
	{
		for (int i = 0; i < transforms.length; i++)
			transforms[i].transform(src, dst);
		return src;
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#inverseTransform(java.awt.geom.Point2D,
	 *      java.awt.geom.Point2D)
	 */
	@Override
	public Point2D inverseTransform(Point2D src, Point2D dst)
			throws NoninvertibleTransformException
	{
		for (int i = transforms.length - 1; i >= 0; i--)
			transforms[i].transform(src, dst);
		return dst;
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#createTransformedShape(java.awt.Shape)
	 */
	@Override
	public Rectangle createTransformedShape(Shape shape)
	{
		Rectangle rect = transforms[0].createTransformedShape(shape);
		for (int i = 1; i < transforms.length; i++)
			rect = transforms[i].createTransformedShape(rect);
		return rect;
	}
}
