/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * FloatImage.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.entropy;

import com.frank.dip.AbstractImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;

/**
 * TODO FloatImage: Implement and add comments.
 * <p>
 * Unimplemented image.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class FloatImage extends AbstractImage
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= -3043256573834292338L;
	/**
	 * The image data.
	 */
	protected double[][]		data;

	/**
	 * Construct an instance of <tt>FloatImage</tt>.
	 * 
	 * @param image
	 * @param scaleLevel
	 */
	public FloatImage(Image image, double scaleLevel)
	{
		width = image.width();
		height = image.height();
		data = new double[height][width];
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				data[y][x] = (image.getPixel(x, y) & 0xff) / scaleLevel;
	}

	/**
	 * Returns the pixel value at the specified point <code>(x, y)</code>.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the pixel value
	 */
	public double getPixel(int x, int y)
	{
		checkBounds(x, y);
		return data[y][x];
	}

	/**
	 * Set the specified value to specified pixel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param value
	 *            the value to set
	 */
	public void setPixel(int x, int y, double value)
	{
		checkBounds(x, y);
		data[y][x] = value;
	}

	/**
	 * Returns the value of scaled pixel.
	 * 
	 * @param x
	 * @param y
	 * @param scaleLevel
	 * @return
	 */
	protected int scale(int x, int y, double scaleLevel)
	{
		int p = (int) (data[y][x] * scaleLevel);
		if (p >= scaleLevel)
			return (int) (scaleLevel - 1);
		else if (p < 0)
			return 0;
		else
			return p;
	}

	public void normalize(double min, double max)
	{
		double tmax = data[0][0], tmin = data[0][0];
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
			{
				if (data[y][x] > tmax)
					tmax = data[y][x];
				if (data[y][x] < tmin)
					tmin = data[y][x];
			}
		double size = max - min;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				data[y][x] = (data[y][x] - tmin) / (tmax - tmin) * size + min;
	}

	public GrayImage toGrayImage()
	{
		GrayImage gi = new GrayImage(width, height);
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				gi.setPixel(x, y, (int) data[y][x]);
		return gi;
	}

	public void entropy(int w, int h)
	{
		int x, x0, xt, y, y0, yt;
		double size;
		double[][] d = new double[height + 1][width + 1];
		for (x = 1; x <= width; x++)
			for (y = 1; y <= height; y++)
				d[y][x] = d[y - 1][x - 1] + data[y - 1][x - 1];
		for (x = 0; x < width; x++)
			for (y = 0; y < height; y++)
			{
				x0 = x < w / 2 ? 0 : (x - w / 2);
				y0 = y < h / 2 ? 0 : (y - h / 2);
				xt = x0 + w;
				if (xt >= width)
					xt = width - 1;
				yt = y0 + h;
				if (yt >= height)
					yt = height - 1;
				size = (xt - x0) * (yt - y0);
				data[y][x] /= (d[yt + 1][xt + 1] - d[y0][x0]) / size;
				data[y][x] = Math.pow(data[y][x], 2);
			}
		for (x = 1; x <= width; x++)
			for (y = 1; y <= height; y++)
				d[y][x] = d[y - 1][x - 1] + data[y - 1][x - 1];
		for (x = 0; x < width; x++)
			for (y = 0; y < height; y++)
			{
				x0 = x < w / 2 ? 0 : (x - w / 2);
				y0 = y < h / 2 ? 0 : (y - h / 2);
				xt = x0 + w;
				if (xt >= width)
					xt = width - 1;
				yt = y0 + h;
				if (yt >= height)
					yt = height - 1;
				size = (xt - x0) * (yt - y0);
				data[y][x] = (d[yt + 1][xt + 1] - d[y0][x0]) / size;
			}
	}

	/**
	 * @see com.frank.dip.AbstractImage#subImage(int, int, int, int)
	 */
	@Override
	public AbstractImage subImage(int x0, int y0, int xt, int yt)
			throws ArrayIndexOutOfBoundsException
	{
		// TODO not implemented.
		return null;
	}
}
