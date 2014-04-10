/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageUtils.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The image utilities for Frank Jiang's image structure.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ImageUtils
{
	/**
	 * Create image from specified image filename.
	 * 
	 * @param filename
	 *            the name of image file
	 * @return the image instance
	 * @throws IOException
	 *             if I/O error occurs
	 */
	public static Image createImage(String filename) throws IOException
	{
		return createImage(ImageIO.read(new java.io.File(filename)));
	}

	/**
	 * Create image from specified image file.
	 * 
	 * @param file
	 *            the image file
	 * @return the image instance
	 * @throws IOException
	 *             if I/O error occurs
	 */
	public static Image createImage(java.io.File file) throws IOException
	{
		return createImage(ImageIO.read(file));
	}

	/**
	 * Create image from specified image input stream.
	 * 
	 * @param is
	 *            the image input stream
	 * @return the image instance
	 * @throws IOException
	 *             if I/O error occurs
	 */
	public static Image createImage(java.io.InputStream is) throws IOException
	{
		return createImage(ImageIO.read(is));
	}

	/**
	 * Create image instance from specified {@link BufferedImage} type.
	 * 
	 * @param image
	 *            the specified {@link BufferedImage} image
	 * @return the image instance
	 */
	public static Image createImage(BufferedImage image)
	{
		int type = image.getType();
		if (type == BufferedImage.TYPE_BYTE_GRAY
				|| type == BufferedImage.TYPE_BYTE_INDEXED)
			return new GrayImage(image, null);
		else if (type == BufferedImage.TYPE_BYTE_BINARY)
			return new BinaryImage(image, null);
		else
			return new ColorImage(image, null);
	}

	/**
	 * Create image instance from specified {@link java.awt.Image} type.
	 * 
	 * @param image
	 *            the specified {@link java.awt.Image} image
	 * @return the image instance
	 */
	public static Image createImage(java.awt.Image image)
	{
		if (image instanceof BufferedImage)
			return createImage((BufferedImage) image);
		else
			return new ColorImage(image, null);
	}
}
