/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * PPMImageWriter.java is PROPRIETARY/CONFIDENTIAL built in 8:05:33 PM, Feb 25,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io.format;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.stream.ImageOutputStream;

import com.frank.dip.BinaryImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.io.ImageWriter;

/**
 * The image writer of format "ppm".
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class PPMImageWriter extends ImageWriter implements NetpbmFormat
{
	/**
	 * The default encode scheme.
	 */
	protected boolean	encodeASCII	= false;

	/**
	 * Returns the encode scheme, <code>true</code> if use ASCII, otherwise use
	 * binary.
	 * 
	 * @return <code>true</code> if use ASCII, otherwise use binary
	 */
	public boolean isEncodeASCII()
	{
		return encodeASCII;
	}

	/**
	 * Set encode scheme, <code>true</code> if use ASCII, otherwise use binary.
	 * 
	 * @param encodeASCII
	 *            the value of encodeASCII
	 */
	public void setEncodeScheme(boolean encodeASCII)
	{
		this.encodeASCII = encodeASCII;
	}

	/**
	 * @see com.frank.dip.io.ImageWriter#write(javax.imageio.stream.ImageOutputStream,
	 *      com.frank.dip.Image)
	 */
	@Override
	public void write(ImageOutputStream ios, Image image) throws IOException
	{
		// Write header.
		int type = -1;
		if (image instanceof BinaryImage)
			type = encodeASCII ? 1 : 4;
		else if (image instanceof GrayImage)
			type = encodeASCII ? 2 : 5;
		else
			type = encodeASCII ? 3 : 6;
		switch (type)
		{
			case 1:
				writeP1(ios, image);
				break;
			case 2:
				writeP2(ios, image);
				break;
			case 3:
				writeP3(ios, image);
				break;
			case 4:
				writeP4(ios, image);
				break;
			case 5:
				writeP5(ios, image);
				break;
			default:
			case 6:
				writeP6(ios, image);
				break;
		}
	}

	/**
	 * Write "P1" format image, which is defined as portable bitmap and
	 * encoded with ASCII.
	 * 
	 * @param ios
	 *            the image output stream
	 * @param image
	 *            the image to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected void writeP1(ImageOutputStream ios, Image image)
			throws IOException
	{
		String s = image.getDescription();
		StringTokenizer st = null;
		if (s != null)
			st = new StringTokenizer(s, "\r\n\f");//$NON-NLS-1$
		ios.writeBytes(TYPE_NETPBM_P1);
		ios.writeBytes("\r\n");//$NON-NLS-1$
		if (st != null)
			while (st.hasMoreTokens())
			{
				ios.writeByte('#');
				ios.writeBytes(st.nextToken());
				ios.write('\n');
			}
		int width = image.width();
		int height = image.height();
		ios.writeBytes(String.format("%d %d\n", width, height));//$NON-NLS-1$
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				ios.write(image.getRGB(x, y) > 127 ? '1' : '0');
				ios.write('\t');
			}
			ios.write('\n');
		}
	}

	/**
	 * Write "P2" format image, which is defined as portable graymap and
	 * encoded with ASCII.
	 * 
	 * @param ios
	 *            the image output stream
	 * @param image
	 *            the image to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected void writeP2(ImageOutputStream ios, Image image)
			throws IOException
	{
		String s = image.getDescription();
		StringTokenizer st = null;
		if (s != null)
			st = new StringTokenizer(s, "\r\n\f");//$NON-NLS-1$
		ios.writeBytes(TYPE_NETPBM_P2);
		ios.writeBytes("\r\n");//$NON-NLS-1$
		if (st != null)
			while (st.hasMoreTokens())
			{
				ios.writeByte('#');
				ios.writeBytes(st.nextToken());
				ios.write('\n');
			}
		int width = image.width();
		int height = image.height();
		ios.writeBytes(String.format("%d %d\n", width, height));//$NON-NLS-1$
		ios.writeBytes("255\n");//$NON-NLS-1$
		int rgb;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				rgb = image.getRGB(x, y);
				ios.writeBytes(Integer.toString(rgb >> 16));
				ios.write('\t');
			}
			ios.write('\n');
		}
	}

	/**
	 * Read "P3" format image, which is defined as portable pixmap and
	 * encoded with ASCII.
	 * 
	 * @param ios
	 *            the image output stream
	 * @param image
	 *            the image to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected void writeP3(ImageOutputStream ios, Image image)
			throws IOException
	{
		String s = image.getDescription();
		StringTokenizer st = null;
		if (s != null)
			st = new StringTokenizer(s, "\r\n\f");//$NON-NLS-1$
		ios.writeBytes(TYPE_NETPBM_P3);
		ios.writeBytes("\r\n");//$NON-NLS-1$
		if (st != null)
			while (st.hasMoreTokens())
			{
				ios.writeByte('#');
				ios.writeBytes(st.nextToken());
				ios.write('\n');
			}
		int width = image.width();
		int height = image.height();
		ios.writeBytes(String.format("%d %d\n", width, height));//$NON-NLS-1$
		ios.writeBytes("255\n");//$NON-NLS-1$
		int rgb;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				rgb = image.getRGB(x, y);
				ios.writeBytes(Integer.toString(rgb >> 16));
				ios.write('\t');
				ios.writeBytes(Integer.toString(rgb >> 8));
				ios.write('\t');
				ios.writeBytes(Integer.toString(rgb));
				ios.write('\t');
			}
			ios.write('\n');
		}
	}

	/**
	 * Read "P4" format image, which is defined as portable bitmap and
	 * encoded with binary.
	 * 
	 * @param ios
	 *            the image output stream
	 * @param image
	 *            the image to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected void writeP4(ImageOutputStream ios, Image image)
			throws IOException
	{
		String s = image.getDescription();
		StringTokenizer st = null;
		if (s != null)
			st = new StringTokenizer(s, "\r\n\f");//$NON-NLS-1$
		ios.writeBytes(TYPE_NETPBM_P4);
		ios.writeBytes("\r\n");//$NON-NLS-1$
		if (st != null)
			while (st.hasMoreTokens())
			{
				ios.writeByte('#');
				ios.writeBytes(st.nextToken());
				ios.write('\n');
			}
		int width = image.width();
		int height = image.height();
		ios.writeBytes(String.format("%d %d\n", width, height));//$NON-NLS-1$
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				ios.writeBit(image.getPixel(x, y) > 127 ? 1 : 0);
	}

	/**
	 * Read "P5" format image, which is defined as portable graymap and
	 * encoded with binary.
	 * 
	 * @param ios
	 *            the image output stream
	 * @param image
	 *            the image to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected void writeP5(ImageOutputStream ios, Image image)
			throws IOException
	{
		String s = image.getDescription();
		StringTokenizer st = null;
		if (s != null)
			st = new StringTokenizer(s, "\r\n\f");//$NON-NLS-1$
		ios.writeBytes(TYPE_NETPBM_P5);
		ios.writeBytes("\r\n");//$NON-NLS-1$
		if (st != null)
			while (st.hasMoreTokens())
			{
				ios.writeByte('#');
				ios.writeBytes(st.nextToken());
				ios.write('\n');
			}
		int width = image.width();
		int height = image.height();
		ios.writeBytes(String.format("%d %d\n", width, height));//$NON-NLS-1$
		ios.writeBytes("255\n");//$NON-NLS-1$
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				ios.writeByte(image.getPixel(x, y));
	}

	/**
	 * Read "P6" format image, which is defined as portable pixmap and
	 * encoded with binary.
	 * 
	 * @param ios
	 *            the image output stream
	 * @param image
	 *            the image to write
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected void writeP6(ImageOutputStream ios, Image image)
			throws IOException
	{
		String s = image.getDescription();
		StringTokenizer st = null;
		if (s != null)
			st = new StringTokenizer(s, "\r\n\f");//$NON-NLS-1$
		ios.writeBytes(TYPE_NETPBM_P6);
		ios.writeBytes("\r\n");//$NON-NLS-1$
		if (st != null)
			while (st.hasMoreTokens())
			{
				ios.writeByte('#');
				ios.writeBytes(st.nextToken());
				ios.write('\n');
			}
		int width = image.width();
		int height = image.height();
		ios.writeBytes(String.format("%d %d\n", width, height));//$NON-NLS-1$
		ios.writeBytes("255\n");//$NON-NLS-1$
		int rgb;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				rgb = image.getRGB(x, y);
				ios.writeByte(rgb >> 16);
				ios.writeByte(rgb >> 8);
				ios.write(rgb);
			}
	}
}
