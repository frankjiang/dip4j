/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageWriter.java is PROPRIETARY/CONFIDENTIAL built in 7:25:08 PM, Feb 25,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.frank.dip.Image;

/**
 * The image writer.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class ImageWriter
{
	/**
	 * Write the specified image to the specified image output stream.
	 * 
	 * @param out
	 *            the specified image output stream
	 * @param image
	 *            the image instance to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	public void write(OutputStream out, Image image) throws IOException
	{
		write(new MemoryCacheImageOutputStream(out), image);
	}
	/**
	 * Write the specified image to the specified image output stream.
	 * 
	 * @param ios
	 *            the specified image output stream
	 * @param image
	 *            the image instance to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	public void write(File file, Image image) throws IOException
	{
		if(file.exists())
			file.createNewFile();
		write(new FileImageOutputStream(file), image);
	}
	/**
	 * Write the specified image to the specified image output stream.
	 * 
	 * @param ios
	 *            the specified image output stream
	 * @param image
	 *            the image instance to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	public abstract void write(ImageOutputStream ios, Image image)
			throws IOException;
}
