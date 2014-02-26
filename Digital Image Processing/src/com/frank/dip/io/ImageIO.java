/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageIO.java is PROPRIETARY/CONFIDENTIAL built in 11:05:46 AM, Feb 25, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.io.format.ppm.PPMImageReader;
import com.frank.dip.io.format.ppm.PPMImageWriter;

/**
 * The image I/O utilities.
 * <p>
 * In this class, normal image I/O processing will be handled.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ImageIO
{
	/**
	 * The extended service supported reading format.
	 */
	private static final String[]	supportedFormatRead		= { "ppm" };
	/**
	 * The extended service supported writing format.
	 */
	private static final String[]	supportedFormatWrite	= { "ppm" };

	/**
	 * Returns the suffix of the specified filename string.
	 * <p>
	 * The suffix will be in the format of &lt;filename&gt;.&lt;suffix&gt;.
	 * </p>
	 * 
	 * @param s
	 *            the specified filename string
	 * @return the suffix or the whole filename if the character '.' is not
	 *         found.
	 */
	public static String getSuffix(String s)
	{
		int idx = s.indexOf('.');
		return idx != -1 ? s.substring(idx + 1) : s;
	}

	/**
	 * Returns <code>true</code> if the specified image format is supported
	 * reading by the current system.
	 * 
	 * @param formatName
	 *            the suffix of the image format
	 * @return <code>true</code> if supported, <code>false</code> if not
	 */
	public static boolean isReadSupported(String formatName)
	{
		return isSystemReadSupported(formatName)
				|| isExtendedReadSupported(formatName);
	}

	/**
	 * Returns <code>true</code> if the specified image format is supported
	 * reading by the basic Java system.
	 * 
	 * @param formatName
	 *            the suffix of the image format
	 * @return <code>true</code> if supported, <code>false</code> if not
	 */
	public static boolean isSystemReadSupported(String formatName)
	{
		String[] suffixes = javax.imageio.ImageIO.getReaderFileSuffixes();
		for (String suffix : suffixes)
			if (suffix.equals(formatName))
				return true;
		return false;
	}

	/**
	 * Returns <code>true</code> if the specified image format is supported
	 * reading by the extended services.
	 * 
	 * @param formatName
	 *            the suffix of the image format
	 * @return <code>true</code> if supported, <code>false</code> if not
	 */
	public static boolean isExtendedReadSupported(String formatName)
	{
		for (String suffix : supportedFormatRead)
			if (suffix.equals(formatName))
				return true;
		return false;
	}

	/**
	 * Returns <code>true</code> if the specified image format is supported
	 * write by current system.
	 * 
	 * @param formatName
	 *            the suffix of the image format
	 * @return <code>true</code> if supported, <code>false</code> if not
	 */
	public static boolean isWriteSupported(String formatName)
	{
		String[] suffixes = javax.imageio.ImageIO.getReaderFileSuffixes();
		for (String suffix : suffixes)
			if (suffix.equals(formatName))
				return true;
		return false;
	}

	/**
	 * Returns <code>true</code> if the specified image format is supported
	 * write by the basic Java system.
	 * 
	 * @param formatName
	 *            the suffix of the image format
	 * @return <code>true</code> if supported, <code>false</code> if not
	 */
	public static boolean isSystemWriteSupported(String formatName)
	{
		String[] suffixes = javax.imageio.ImageIO.getReaderFileSuffixes();
		for (String suffix : suffixes)
			if (suffix.equals(formatName))
				return true;
		return false;
	}

	/**
	 * Returns <code>true</code> if the specified image format is supported
	 * write by the extended services.
	 * 
	 * @param formatName
	 *            the suffix of the image format
	 * @return <code>true</code> if supported, <code>false</code> if not
	 */
	public static boolean isExtendedWriteSupported(String formatName)
	{
		for (String suffix : supportedFormatWrite)
			if (suffix.equals(formatName))
				return true;
		return false;
	}

	/**
	 * Read the image file and return the image instance it represents.
	 * 
	 * @param file
	 *            the image file
	 * @return the image instance
	 * @throws IOException
	 *             if I/O error occurred
	 */
	public static Image read(File file) throws IOException
	{
		return read(new FileImageInputStream(file), getSuffix(file.getName().toLowerCase()));
	}

	/**
	 * Read the image input stream and return the image instance it represents.
	 * 
	 * @param in
	 *            the image input stream
	 * @return the image instance
	 * @throws IOException
	 *             if I/O error occurred
	 */
	public static Image read(InputStream in, String formatName)
			throws IOException
	{
		return read(new MemoryCacheImageInputStream(in), formatName);
	}

	/**
	 * Returns the image instance according to the image input stream and the
	 * specified image format.
	 * 
	 * @param iis
	 *            the image input stream
	 * @param cs
	 *            the character set of the input stream
	 * @param formatName
	 *            the format name
	 * @return the image instance
	 * @throws IOException
	 *             if I/O error occurred
	 */
	public static Image read(ImageInputStream iis, String formatName)
			throws IOException
	{
		if (isSystemReadSupported(formatName))
		{
			BufferedImage bi = javax.imageio.ImageIO.read(iis);
			switch (bi.getType())
			{
				case BufferedImage.TYPE_BYTE_BINARY:
					return new BinaryImage(bi, null);
				case BufferedImage.TYPE_BYTE_GRAY:
					return new GrayImage(bi, null);
				default:
					return new ColorImage(bi, null);
			}
		}
		if (isExtendedReadSupported(formatName))
		{
			ImageReader reader = getImageReader(formatName);
			if (reader == null)
				throw new NullPointerException(
						"The image reader of extended is not implemented yet!!!");
			return reader.read(iis);
		}
		else
			throw new IOException(
					String.format(
							"The image format \"%s\" is not supported reading by the current system.",
							formatName));
	}

	/**
	 * Returns the correlated <code>ImageReader</code> instance according to the
	 * specified format name.
	 * 
	 * @param formatName
	 *            the specified format name
	 * @return the <code>ImageReader</code> instance
	 */
	public static ImageReader getImageReader(String formatName)
	{
		if ("ppm".equals(formatName))
			return new PPMImageReader();
		return null;
	}

	/**
	 * Write specified formated image to the specified file.
	 * 
	 * @param ios
	 *            the specified image output stream
	 * @param image
	 *            the image output stream
	 * @throws IOException
	 *             if any IO error occurred
	 */
	public static void write(File file, Image image) throws IOException
	{
		write(new FileImageOutputStream(file), image, getSuffix(file.getName().toLowerCase()));
	}

	/**
	 * Write specified formated image to the specified image output stream.
	 * 
	 * @param out
	 *            the specified image output stream
	 * @param image
	 *            the image output stream
	 * @param formatName
	 *            the image format name
	 * @throws IOException
	 *             if any IO error occurred
	 */
	public static void write(OutputStream out, Image image, String formatName)
			throws IOException
	{
		write(new MemoryCacheImageOutputStream(out), image, formatName);
	}

	/**
	 * Write specified formated image to the specified image output stream.
	 * 
	 * @param ios
	 *            the specified image output stream
	 * @param image
	 *            the image output stream
	 * @param formatName
	 *            the image format name
	 * @throws IOException
	 *             if any IO error occurred
	 */
	public static void write(ImageOutputStream ios, Image image,
			String formatName) throws IOException
	{
		if (isSystemWriteSupported(formatName))
		{
			javax.imageio.ImageIO.write(image.restore(), formatName, ios);
			return;
		}
		if (isExtendedWriteSupported(formatName))
		{
			ImageWriter writer = getImageWriter(formatName);
			if (writer == null)
				throw new NullPointerException(
						"The image writer of extended is not implemented yet!!!");
			writer.write(ios, image);
		}
		else
			throw new IOException(
					String.format(
							"The image format \"%s\" is not supported reading by the current system.",
							formatName));
	}

	/**
	 * Returns the correlated <code>ImageWriter</code> instance according to the
	 * specified format name.
	 * 
	 * @param formatName
	 *            the specified format name
	 * @return the <code>ImageWriter</code> instance
	 */
	public static ImageWriter getImageWriter(String formatName)
	{
		if ("ppm".equals(formatName))
			return new PPMImageWriter();
		return null;
	}
}
