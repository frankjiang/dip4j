/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Geometry.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;

/**
 * The abstract geometry operators.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @param <T>
 * @version 1.0.0
 */
public abstract class Geometry<T extends Image>
{
	/**
	 * The edge filling scheme.
	 * <p>
	 * Fill the blank pixels with transparent pixels. If the image type cannot
	 * support transparent pixel, then fill the pixels with black pixels.
	 * </p>
	 */
	public static final int	FILL_WITH_BLANK			= 0;
	/**
	 * The edge filling scheme.
	 * <p>
	 * Fill the blank pixels with black pixels.
	 * </p>
	 */
	public static final int	FILL_WITH_BLACK			= -1;
	/**
	 * The edge filling scheme.
	 * <p>
	 * Fill the blank pixels with white pixels.
	 * </p>
	 */
	public static final int	FILL_WITH_WHITE			= 1;
	/**
	 * Nearest-neighbor interpolation type.
	 * 
	 * @see java.awt.image.AffineTransformOp#TYPE_NEAREST_NEIGHBOR
	 */
	public static final int	TYPE_NEAREST_NEIGHBOR	= 1;
	/**
	 * Bilinear interpolation type.
	 * 
	 * @see java.awt.image.AffineTransformOp#TYPE_BILINEAR
	 */
	public static final int	TYPE_BILINEAR			= 2;
	/**
	 * Bicubic interpolation type.
	 * 
	 * @see java.awt.image.AffineTransformOp#TYPE_BICUBIC
	 */
	public static final int	TYPE_BICUBIC			= 3;
	/**
	 * The interpolation type.
	 * 
	 * @see #TYPE_BICUBIC
	 * @see #TYPE_BILINEAR
	 * @see #TYPE_NEAREST_NEIGHBOR
	 */
	protected int			type;
	/**
	 * The edge filling scheme.
	 */
	protected int			fillScheme;

	/**
	 * Construct an instance of <tt>Geometry</tt>.
	 * 
	 * @param type
	 *            the type of interpolation
	 * @param fillScheme
	 *            the edge filling scheme
	 */
	protected Geometry(int type, int fillScheme)
	{
		this.type = type;
		this.fillScheme = fillScheme;
	}

	/**
	 * Perform affine transform to the specified image.
	 * 
	 * @param image
	 *            the image to perform
	 * @param transform
	 *            the affine kernel
	 * @return the result image
	 * @throws NoninvertibleTransformException
	 */
	public abstract T transform(T image, GeometryTransform transform)
			throws NoninvertibleTransformException;

	/**
	 * Returns the result image of the affine transformation for a
	 * {@linkplain java.awt.image.BufferedImage}.
	 * 
	 * @param image
	 *            the {@linkplain java.awt.image.BufferedImage}
	 * @param transform
	 *            the affine transformation kernel
	 * @return the result image
	 */
	public java.awt.image.BufferedImage affineTransform(
			java.awt.image.BufferedImage image, AffineTransform transform)
	{
		return new AffineTransformOp(transform.getAffineTransform(), type)
				.filter(image, null);
	}

	/**
	 * Rotate the image with angle <code>&theta;<code>.
	 * 
	 * @param image
	 *            the image to rotate
	 * @param theta
	 *            <code>&theta;<code> in radians
	 * @return the rotated image
	 */
	public T rotate(T image, double theta)
	{
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		try
		{
			return transform(image, new AffineTransform(cos, sin, -sin, cos, 0,
					0));
		}
		catch (NoninvertibleTransformException e)
		{
			return null;
		}
	}

	/**
	 * Rotate the image with angle <code>&theta;<code>.
	 * 
	 * @param image
	 *            the image to rotate
	 * @param theta
	 *            <code>&theta;<code> in degree
	 * @return the rotated image
	 */
	public T rotateByDegree(T image, double theta)
	{
		return rotate(image, Math.toDegrees(theta));
	}

	/**
	 * Scale the image with specified scale factors.
	 * 
	 * @param image
	 *            the image to scale
	 * @param sx
	 *            the factor by which coordinates are scaled along the X axis
	 *            direction
	 * @param sy
	 *            the factor by which coordinates are scaled along the Y axis
	 *            direction
	 * @return the scaled image
	 */
	public T scaleByRate(T image, double sx, double sy)
	{
		try
		{
			// m00 = sx;
			// m10 = 0.0;
			// m01 = 0.0;
			// m11 = sy;
			// m02 = 0.0;
			// m12 = 0.0;
			return transform(image, new AffineTransform(sx, 0, 0, sy, 0, 0));
		}
		catch (NoninvertibleTransformException e)
		{
			return null;
		}
	}

	/**
	 * Scale the image with specified scale factor.
	 * 
	 * @param image
	 *            the image to scale
	 * @param rate
	 *            the factor by which both X and Y coordinates are scaled along
	 *            the axis direction
	 * @return the scaled image
	 */
	public T scaleByRate(T image, double rate)
	{
		try
		{
			return transform(image, new AffineTransform(rate, 0, 0, rate, 0, 0));
		}
		catch (NoninvertibleTransformException e)
		{
			return null;
		}
	}

	/**
	 * Scale the image with specified dimension.
	 * 
	 * @param image
	 *            the image to scale
	 * @param width
	 *            the width factor for scaled image
	 * @param height
	 *            the height factor for scaled image
	 * @return the scaled image
	 */
	public T scale(T image, int width, int height)
	{
		double sx = width / (double) image.width(), sy = height
				/ (double) image.height();
		try
		{
			return transform(image, new AffineTransform(
					java.awt.geom.AffineTransform.getScaleInstance(sx, sy)));
		}
		catch (NoninvertibleTransformException e)
		{
			return null;
		}
	}

	/**
	 * Scale the image with specified {@code size}. The size factor means the
	 * minimum one in width and height will be scaled to the {@code size}, and
	 * the other one will be scaled relatively.
	 * 
	 * @param image
	 *            the image to scale
	 * @param size
	 *            the size factor for scaled image
	 * @return the scaled image
	 */
	public T scale(T image, int size)
	{
		double w = image.width();
		double h = image.height();
		double rate = size / (w < h ? w : h);
		try
		{
			return transform(image, new AffineTransform(rate, 0, 0, rate, 0, 0));
		}
		catch (NoninvertibleTransformException e)
		{
			return null;
		}
	}

	/**
	 * Perform spatial transformation to the specified image, correct the
	 * specified image to specified width and height.
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
	 * @return the image corrected by spatial transform
	 */
	public T spatialTransform(T image, Point2D p00, Point2D p01, Point2D p11,
			Point2D p10, double width, double height)
	{
		try
		{
			return transform(image, new SpatialTransform(p00, p01, p11, p10,
					width, height));
		}
		catch (NoninvertibleTransformException e)
		{
			return null;
		}
	}

	/**
	 * Perform spatial transformation to the specified image. The width and
	 * height of the corrected image will be generated by the anchors.
	 * 
	 * @param p00
	 *            left-top anchor
	 * @param p01
	 *            right-top anchor
	 * @param p11
	 *            right-bottom anchor
	 * @param p10
	 *            left-bottom anchor
	 * @return the image corrected by spatial transform
	 */
	public T spatialTransform(T image, Point2D p00, Point2D p01, Point2D p11,
			Point2D p10)
	{
		return spatialTransform(image, p00, p01, p11, p10, 0, 0);
	}

	/**
	 * Getter for the interpolation type.
	 * 
	 * @see #TYPE_BICUBIC
	 * @see #TYPE_BILINEAR
	 * @see #TYPE_NEAREST_NEIGHBOR
	 * @return the interpolation type
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Setter for the interpolation type.
	 * 
	 * @see #TYPE_BICUBIC
	 * @see #TYPE_BILINEAR
	 * @see #TYPE_NEAREST_NEIGHBOR
	 * @param type
	 *            the interpolation type
	 */
	public void setType(int type)
	{
		this.type = type;
	}

	/**
	 * Returns the geometry operator according to the image instance.
	 * 
	 * @param image
	 *            the image instance
	 * @param type
	 *            the interpolation type
	 * @param fillScheme
	 *            the edge filling scheme
	 * @return the geometry operator
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static Geometry getGeometry(Image image, int type, int fillScheme)
			throws IllegalImageTypeException
	{
		if (image instanceof BinaryImage)
			return new GeometryBinary(type, fillScheme);
		if (image instanceof GrayImage)
			return new GeometryGray(type, fillScheme);
		if (image instanceof ColorImage)
			return new GeometryColor(type, fillScheme);
		throw new IllegalImageTypeException(Geometry.class, image.getClass());
	}

	/**
	 * Returns the geometry operator according to the image class.
	 * 
	 * @param c
	 *            the image class
	 * @param type
	 *            the interpolation type
	 * @param fillScheme
	 *            the edge filling scheme
	 * @return the geometry operator
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static Geometry getGeometry(Class<? extends Image> c, int type,
			int fillScheme) throws IllegalImageTypeException
	{
		if (c == BinaryImage.class)
			return new GeometryBinary(type, fillScheme);
		if (c == GrayImage.class)
			return new GeometryGray(type, fillScheme);
		if (c == ColorImage.class)
			return new GeometryColor(type, fillScheme);
		throw new IllegalImageTypeException(Geometry.class, c);
	}
}
