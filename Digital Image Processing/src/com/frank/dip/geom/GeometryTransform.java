/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * GeometryTransform.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Geometry transform provides an interface which defines the point to point
 * geometry transformation.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface GeometryTransform
{
	/**
	 * Perform the transformation to code>src</code> and stores the result in
	 * code>dst</code>.
	 * <p>
	 * If <code>src</code> and <code>dst</code> are the same object, the input
	 * point is correctly overwritten with the transformed point.
	 * </p>
	 * 
	 * @param src
	 *            the point to be transformed
	 * @param dst
	 *            the resulting transformed point
	 * @return <code>dst</code>, which contains the result of the
	 */
	public Point2D transform(Point2D src, Point2D dst);

	/**
	 * Perform the inverse transformation to code>src</code> and stores the
	 * result in code>dst</code>.
	 * <p>
	 * If <code>src</code> and <code>dst</code> are the same object, the input
	 * point is correctly overwritten with the transformed point.
	 * </p>
	 * 
	 * @param src
	 *            the point to be inverse transformed
	 * @param dst
	 *            the resulting transformed point
	 * @return <code>dst</code>, which contains the result of the
	 * @exception NoninvertibleTransformException
	 *                if the matrix cannot be inverted.
	 */
	public Point2D inverseTransform(Point2D src, Point2D dst)
			throws NoninvertibleTransformException;

	/**
	 * Returns the bound rectangle of the transformed {@code shape}.
	 * 
	 * @param shape
	 *            the shape to transform
	 * @return the bound of the transformed shape
	 */
	public Rectangle createTransformedShape(Shape shape);
}
