/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * PPMImageReader.java is PROPRIETARY/CONFIDENTIAL built in 7:37:59 PM, Feb 25,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io.format;

import java.io.EOFException;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.stream.ImageInputStream;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.io.ImageReader;

/**
 * The image reader of format "ppm".
 * <p>
 * </p>
 * 
 * @see NetpbmFormat
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class PPMImageReader extends ImageReader implements NetpbmFormat
{
	/**
	 * The string delimiter.
	 */
	private static final String	DELIM	= " \r\n\t\f";

	/**
	 * @see com.frank.dip.io.ImageReader#read(javax.imageio.stream.ImageInputStream)
	 */
	@Override
	public Image read(ImageInputStream iis) throws IOException
	{
		// Resolve the header of the image.
		iis.mark();
		String typeStr = iis.readLine();
		int type = checkType(typeStr);
		if (type == -1)
		{
			iis.reset();
			throw new IOException("Unknow magic number for Netpbm format: "
					+ typeStr);
		}
		Integer width = null, height = null, max = null;
		String line = iis.readLine();
		StringBuilder description = new StringBuilder();
		boolean b = false;
		Image image = null;
		int idx;
		while (line != null
				&& (width == null || height == null || (type != 1 && type != 4 && max == null)))
		{
			idx = line.indexOf('#');
			if (idx != -1)
			{
				description.append(line.substring(idx + 1) + "\r\n");
				line = line.substring(0, idx);
			}
			if (line.length() == 0)
			{
				line = iis.readLine();
				continue;
			}
			StringTokenizer st = new StringTokenizer(line, " \r\n\t\f");
			if (width == null && st.hasMoreTokens())
				width = Integer.valueOf(st.nextToken());
			if (height == null && st.hasMoreTokens())
				height = Integer.valueOf(st.nextToken());
			if (max == null && type != 1 && type != 4 && st.hasMoreTokens())
				max = Integer.valueOf(st.nextToken());
			if (width == null || height == null
					|| (type != 1 && type != 4 && max == null))
				line = iis.readLine();
		}
		// Resolve the image data body.
		Exception exp = null;
		if (line == null || width == null || height == null
				|| (type != 1 && type != 4 && max == null))
			b = true;
		else
			try
			{
				switch (type)
				{
					case 1:
						image = readP1(iis, width, height);
						break;
					case 2:
						image = readP2(iis, width, height, max);
						break;
					case 3:
						image = readP3(iis, width, height, max);
						break;
					case 4:
						image = readP4(iis, width, height);
						break;
					case 5:
						image = readP5(iis, width, height, max);
						break;
					case 6:
						image = readP6(iis, width, height, max);
						break;
					default:
						b = true;
						break;
				}
			}
			catch (Exception e)
			{
				exp = e;
				b = true;
			}
		if (b)
		{
			IOException ioe = new IOException(
					"The image completeness is damaged.");
			if (exp != null)
				ioe.initCause(exp);
			throw ioe;
		}
		else
		{
			if (description.length() != 0)
				image.setDescription(description.toString());
			return image;
		}
	}

	// /**
	// * Read a line from the input stream, return the comment removed line and
	// * add the comment to comment builder.
	// *
	// * @param iis
	// * the image input stream
	// * @param description
	// * the comment builder
	// * @return the comment removed line
	// * @throws IOException
	// * if any IO error occurred
	// */
	// private String readLine(ImageInputStream iis, StringBuilder description)
	// throws IOException
	// {
	// String line = iis.readLine();
	// if (line == null)
	// return null;
	// int idx = line.indexOf('#');
	// if (idx == -1)
	// return line;
	// else
	// {
	// description.append(line.substring(idx));
	// return line.substring(0, idx);
	// }
	// }
	/**
	 * Read "P1" format image, which is defined as portable bitmap and
	 * encoded with ASCII.
	 * 
	 * @param iis
	 *            the image input stream.
	 * @param width
	 *            the image width
	 * @param height
	 *            the image height
	 * @param description
	 *            the comment description builder
	 * @return the "P1" format image
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected Image readP1(ImageInputStream iis, Integer width, Integer height)
			throws IOException
	{
		BinaryImage image = new BinaryImage(width, height);
		String line = iis.readLine();
		if (line == null)
			return image;
		StringTokenizer st = new StringTokenizer(line, DELIM);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (line == null)
				{
					x = width;
					y = height;
				}
				else if (!st.hasMoreTokens())
				{
					line = iis.readLine();
					st = new StringTokenizer(line, DELIM);
					x--;
				}
				else
					image.setPixel(x, y, "1".equals(st.nextToken()));
		return image;
	}

	/**
	 * Read "P2" format image, which is defined as portable graymap and
	 * encoded with ASCII.
	 * 
	 * @param iis
	 *            the image input stream.
	 * @param width
	 *            the image width
	 * @param height
	 *            the image height
	 * @param max
	 *            the max pixel scale value
	 * @param description
	 *            the comment description builder
	 * @return the "P2" format image
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected Image readP2(ImageInputStream iis, Integer width, Integer height,
			Integer max) throws IOException
	{
		GrayImage image = new GrayImage(width, height);
		String line = iis.readLine();
		if (line == null)
			return image;
		StringTokenizer st = new StringTokenizer(line, DELIM);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (line == null)
				{
					x = width;
					y = height;
				}
				else if (!st.hasMoreTokens())
				{
					line = iis.readLine();
					st = new StringTokenizer(line, DELIM);
					x--;
				}
				else
					image.setPixel(
							x,
							y,
							Math.round(Integer.valueOf(st.nextToken()) * 255f
									/ max));
		return image;
	}

	/**
	 * Read "P3" format image, which is defined as portable pixmap and
	 * encoded with ASCII.
	 * 
	 * @param iis
	 *            the image input stream.
	 * @param width
	 *            the image width
	 * @param height
	 *            the image height
	 * @param max
	 *            the max pixel scale value
	 * @param description
	 *            the comment description builder
	 * @return the "P3" format image
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected Image readP3(ImageInputStream iis, Integer width, Integer height,
			Integer max) throws IOException
	{
		ColorImage image = new ColorImage(width, height);
		String line = iis.readLine();
		if (line == null)
			return image;
		StringTokenizer st = new StringTokenizer(line, DELIM);
		int red, green, blue;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (line == null)
				{
					x = width;
					y = height;
				}
				else if (!st.hasMoreTokens())
				{
					line = iis.readLine();
					st = new StringTokenizer(line, DELIM);
					x--;
				}
				else
				{
					red = Math.round(Integer.valueOf(st.nextToken()) * 255f
							/ max);
					green = Math.round(Integer.valueOf(st.nextToken()) * 255f
							/ max);
					blue = Math.round(Integer.valueOf(st.nextToken()) * 255f
							/ max);
					image.setPixel(x, y, 255, red, green, blue);
				}
		return image;
	}

	/**
	 * Read "P4" format image, which is defined as portable bitmap and
	 * encoded with binary.
	 * 
	 * @param iis
	 *            the image input stream.
	 * @param width
	 *            the image width
	 * @param height
	 *            the image height
	 * @param description
	 *            the comment description builder
	 * @return the "P4" format image
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected Image readP4(ImageInputStream iis, Integer width, Integer height)
			throws IOException
	{
		BinaryImage image = new BinaryImage(width, height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				image.setPixel(x, y, iis.readBit() != 0);
		return image;
	}

	/**
	 * Read "P5" format image, which is defined as portable graymap and
	 * encoded with binary.
	 * 
	 * @param iis
	 *            the image input stream.
	 * @param width
	 *            the image width
	 * @param height
	 *            the image height
	 * @param max
	 *            the max pixel scale value
	 * @param description
	 *            the comment description builder
	 * @return the "P5" format image
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected Image readP5(ImageInputStream iis, Integer width, Integer height,
			Integer max) throws IOException
	{
		GrayImage image = new GrayImage(width, height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				image.setPixel(x, y,
						Math.round(iis.readUnsignedByte() * 255f / max));
		return image;
	}

	/**
	 * Read "P6" format image, which is defined as portable pixmap and
	 * encoded with binary.
	 * 
	 * @param iis
	 *            the image input stream.
	 * @param width
	 *            the image width
	 * @param height
	 *            the image height
	 * @param max
	 *            the max pixel scale value
	 * @param description
	 *            the comment description builder
	 * @return the "P6" format image
	 * @throws IOException
	 *             if any IO error occurred
	 */
	protected Image readP6(ImageInputStream iis, Integer width, Integer height,
			Integer max) throws IOException
	{
		ColorImage image = new ColorImage(width, height);
		if (max < 256)
		{
			byte[] buf = new byte[3];
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					if (iis.read(buf) == -1)
					{
						x = width;
						y = height;
					}
					else
						image.setPixel(x, y, 255, buf[0] & 0xff, buf[1] & 0xff,
								buf[2] & 0xff);
		}
		else
		{
			int red, green, blue;
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					try
					{
						red = iis.readShort() & 0xffff;
						green = iis.readShort() & 0xffff;
						blue = iis.readInt() & 0xffff;
						image.setPixel(x, y, 255, red, green, blue);
					}
					catch (EOFException e)
					{
						x = width;
						y = height;
					}
		}
		return image;
	}

	/**
	 * Returns 1-6 if the magic number is match by the system, otherwise returns
	 * <code>-1</code>.
	 * 
	 * @param type
	 *            the magic number string.
	 * @return magic number matched or -1 if not matched
	 */
	public int checkType(String type)
	{
		type = type.toUpperCase();
		if (TYPE_NETPBM_P1.equals(type))
			return 1;
		if (TYPE_NETPBM_P2.equals(type))
			return 2;
		if (TYPE_NETPBM_P3.equals(type))
			return 3;
		if (TYPE_NETPBM_P4.equals(type))
			return 4;
		if (TYPE_NETPBM_P5.equals(type))
			return 5;
		if (TYPE_NETPBM_P6.equals(type))
			return 6;
		return -1;
	}
}
