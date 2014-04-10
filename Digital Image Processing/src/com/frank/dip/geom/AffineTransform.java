/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * AffineTransformKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * The kernel defined for affine transformation.
 * 
 * <pre>
 *      [ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
 *      [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
 *      [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
 * </pre>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class AffineTransform implements Cloneable, GeometryTransform
{
	/**
	 * The affine transform matrix elements.
	 */
	protected double	m00, m10, m01, m11, m02, m12;

	/**
	 * Construct an instance of <tt>AffineTransformKernel</tt>.
	 * 
	 * <pre>
	 *      [  m00  m01  m02  ]
	 *      [  m10  m11  m12  ]
	 *      [   0    0    1   ]
	 * </pre>
	 * 
	 * @param m00
	 *            the X coordinate scaling element of the 3x3 matrix
	 * @param m10
	 *            the Y coordinate shearing element of the 3x3 matrix
	 * @param m01
	 *            the X coordinate shearing element of the 3x3 matrix
	 * @param m11
	 *            the Y coordinate scaling element of the 3x3 matrix
	 * @param m02
	 *            the X coordinate translation element of the 3x3 matrix
	 * @param m12
	 *            the Y coordinate translation element of the 3x3 matrix
	 */
	public AffineTransform(double m00, double m10, double m01, double m11,
			double m02, double m12)
	{
		setKernel(m00, m10, m01, m11, m02, m12);
	}

	/**
	 * Construct an instance of <tt>AffineTransformKernel</tt> according to
	 * {@linkplain AffineTransform} instance.
	 * 
	 * @param xform
	 *            the {@linkplain AffineTransform} instance
	 */
	public AffineTransform(java.awt.geom.AffineTransform xform)
	{
		double[] flatmatrix = new double[6];
		xform.getMatrix(flatmatrix);
		setKernel(flatmatrix[0], flatmatrix[1], flatmatrix[2], flatmatrix[3],
				flatmatrix[4], flatmatrix[5]);
	}

	/**
	 * Set the elements in the affine transform kernel.
	 * 
	 * <pre>
	 *      [  m00  m01  m02  ]
	 *      [  m10  m11  m12  ]
	 *      [   0    0    1   ]
	 * </pre>
	 * 
	 * @param m00
	 *            the X coordinate scaling element of the 3x3 matrix
	 * @param m10
	 *            the Y coordinate shearing element of the 3x3 matrix
	 * @param m01
	 *            the X coordinate shearing element of the 3x3 matrix
	 * @param m11
	 *            the Y coordinate scaling element of the 3x3 matrix
	 * @param m02
	 *            the X coordinate translation element of the 3x3 matrix
	 * @param m12
	 *            the Y coordinate translation element of the 3x3 matrix
	 */
	public void setKernel(double m00, double m10, double m01, double m11,
			double m02, double m12)
	{
		this.m00 = m00;
		this.m10 = m10;
		this.m01 = m01;
		this.m11 = m11;
		this.m02 = m02;
		this.m12 = m12;
	}

	/**
	 * Returns {@code AffineTransform} instance related to this kernel.
	 * 
	 * @return the {@code AffineTransform}
	 */
	public java.awt.geom.AffineTransform getAffineTransform()
	{
		return new java.awt.geom.AffineTransform(m00, m10, m01, m11, m02, m12);
	}

	/**
	 * Getter for m00.
	 * 
	 * @return the m00
	 */
	public double getM00()
	{
		return m00;
	}

	/**
	 * Getter for m10.
	 * 
	 * @return the m10
	 */
	public double getM10()
	{
		return m10;
	}

	/**
	 * Getter for m01.
	 * 
	 * @return the m01
	 */
	public double getM01()
	{
		return m01;
	}

	/**
	 * Getter for m11.
	 * 
	 * @return the m11
	 */
	public double getM11()
	{
		return m11;
	}

	/**
	 * Getter for m02.
	 * 
	 * @return the m02
	 */
	public double getM02()
	{
		return m02;
	}

	/**
	 * Getter for m12.
	 * 
	 * @return the m12
	 */
	public double getM12()
	{
		return m12;
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
		double x0 = src.getX(), y0 = src.getY();
		dst.setLocation(m00 * x0 + m01 * y0 + m02, m10 * x0 + m11 * y0 + m12);
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
		double det = m00 * m11 - m01 * m10;
		if (Math.abs(det) <= Double.MIN_VALUE)
			throw new NoninvertibleTransformException(String.format(
					"The determine is %f.", det));
		double xt = src.getX(), yt = src.getY();
		dst.setLocation((m01 * m12 - m02 * m11 + m11 * xt - m01 * yt) / det,
				-(m00 * m12 - m02 * m10 + m10 * xt - m00 * yt) / det);
		return dst;
	}

	/**
	 * @see com.frank.dip.geom.GeometryTransform#createTransformedShape(java.awt.Shape)
	 */
	@Override
	public Rectangle createTransformedShape(Shape shape)
	{
		if (shape == null)
			return null;
		return new Path2D.Double(shape, getAffineTransform()).getBounds();
	}
}
