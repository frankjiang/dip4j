/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageReader.java is PROPRIETARY/CONFIDENTIAL built in 7:20:29 PM, Feb 25,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.frank.dip.Image;

/**
 * The image reader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class ImageReader
{
	/**
	 * Read the image input stream and returns the image instance.
	 * 
	 * @param file
	 *            the image file to read
	 * @return the image instance
	 * @throws IOException
	 *             if any I/O error occurs
	 */
	public Image read(File file) throws IOException
	{
		return read(new FileImageInputStream(file));
	}

	/**
	 * Read the image input stream and returns the image instance.
	 * 
	 * @param in
	 *            the image input stream
	 * @return the image instance
	 * @throws IOException
	 *             if any I/O error occurs
	 */
	public Image read(InputStream in) throws IOException
	{
		return read(new MemoryCacheImageInputStream(in));
	}

	/**
	 * Read the image input stream and returns the image instance.
	 * 
	 * @param iis
	 *            the image input stream
	 * @return the image instance
	 * @throws IOException
	 *             if any I/O error occurs
	 */
	abstract public Image read(ImageInputStream iis) throws IOException;
}
