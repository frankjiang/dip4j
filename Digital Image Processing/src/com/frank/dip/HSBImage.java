/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * HSIImage.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip;

import java.awt.Color;

import com.frank.dip.math.Function;

/**
 * Image in HSB(hue, saturation & brightness) color space.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class HSBImage extends Image
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= -3645899595903084410L;
	/**
	 * The alpha channel.
	 */
	protected byte[][]			alpha;
	/**
	 * The HSB spaces.
	 */
	protected float[][]			hue, saturation, brightness;

	/**
	 * Construct an instance of <tt>HSIImage</tt>.
	 * 
	 * @param image
	 */
	public HSBImage(Image image)
	{
		super(image);
	}

	/**
	 * Construct an instance of <tt>HSIImage</tt>.
	 * 
	 * @param width
	 * @param height
	 */
	public HSBImage(int width, int height)
	{
		this.width = width;
		this.height = height;
		alpha = new byte[height][width];
		hue = new float[height][width];
		saturation = new float[height][width];
		brightness = new float[height][width];
	}

	/**
	 * empty constructor
	 */
	protected HSBImage()
	{
		// do nothing
	}

	/**
	 * @see com.frank.dip.Image#getPixel(int, int)
	 */
	@Override
	public int getPixel(int x, int y)
	{
		return getRGB(x, y);
	}

	/**
	 * @see com.frank.dip.Image#setPixel(int, int, int)
	 */
	@Override
	public void setPixel(int x, int y, int value)
	{
		checkBounds(x, y);
		int r, g, b;
		alpha[y][x] = (byte) (value >> 24);
		r = (value >> 16) & 0xff;
		g = (value >> 8) & 0xff;
		b = value & 0xff;
		float[] hsb = Color.RGBtoHSB(r, g, b, null);
		hue[y][x] = hsb[0];
		saturation[y][x] = hsb[1];
		brightness[y][x] = hsb[2];
		// ----------------------------
	}

	/**
	 * @see com.frank.dip.Image#getRGB(int, int)
	 */
	@Override
	public int getRGB(int x, int y)
	{
		checkBounds(x, y);
		float h, s, b;
		h = hue[y][x];
		s = saturation[y][x];
		b = brightness[y][x];
		return (alpha[y][x] & 0xff) << 24
				| (Color.HSBtoRGB(h, s, b) & 0xffffff);
	}

	/**
	 * @see com.frank.dip.Image#initialImageByRGBMatrix(int[][])
	 */
	@Override
	protected void initialImageByRGBMatrix(int[][] rgbMatrix)
	{
		int rgb, r, g, b;
		height = rgbMatrix.length;
		width = rgbMatrix[0].length;
		alpha = new byte[height][width];
		hue = new float[height][width];
		saturation = new float[height][width];
		brightness = new float[height][width];
		float[] hsb = new float[3];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				rgb = rgbMatrix[y][x];
				alpha[y][x] = (byte) (rgb >> 24);
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
				Color.RGBtoHSB(r, g, b, hsb);
				hue[y][x] = hsb[0];
				saturation[y][x] = hsb[1];
				brightness[y][x] = hsb[2];
			}
	}

	/**
	 * @see com.frank.dip.Image#restore()
	 */
	@Override
	public java.awt.image.BufferedImage restore()
	{
		return restore(java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
	}

	/**
	 * @see com.frank.dip.Image#clone()
	 */
	@Override
	public HSBImage clone()
	{
		HSBImage clone = new HSBImage();
		clone.width = width;
		clone.height = height;
		clone.alpha = new byte[height][];
		clone.hue = new float[height][];
		clone.saturation = new float[height][];
		clone.brightness = new float[height][];
		for (int i = 0; i < height; i++)
		{
			clone.alpha[i] = alpha[i].clone();
			clone.hue[i] = hue[i].clone();
			clone.saturation[i] = saturation[i].clone();
			clone.brightness[i] = brightness[i].clone();
		}
		return clone;
	}

	/**
	 * @see com.frank.dip.Image#recreate()
	 */
	@Override
	public Image recreate()
	{
		return new HSBImage(width, height);
	}

	/**
	 * Returns the intensity value of the specified image.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the intensity channel value
	 */
	public float getIntensity(int x, int y)
	{
		checkBounds(x, y);
		return brightness[y][x];
	}

	/**
	 * Set the intensity value of the specified image.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param i
	 *            the intensity channel value to set
	 */
	public void setIntensity(int x, int y, float i)
	{
		checkBounds(x, y);
		brightness[y][x] = i;
	}

	/**
	 * Transform the intensity according to the specified function.
	 * 
	 * @param f
	 *            The transform function.
	 * @param useHighAccuracyScheme
	 *            The flag for whether the high accuracy scheme is used. If this
	 *            scheme is used, the pixels values will be stored in matrixes,
	 *            they will be normalized before set to the image
	 */
	public void transformIntensity(Function f, boolean useHighAccuracyScheme)
	{
		int csl = 1;
		if (useHighAccuracyScheme)
		{
			float max = Float.NEGATIVE_INFINITY, min = Float.POSITIVE_INFINITY, d;
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
				{
					d = (float) f.function(brightness[y][x]);
					if (d > max)
						max = d;
					if (d < min)
						min = d;
				}
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
					brightness[y][x] = (brightness[y][x] - min) / (max - min);
		}
		else
		{
			float d;
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
				{
					d = (float) f.function(brightness[y][x]);
					if (d > csl)
						brightness[y][x] = csl;
					else if (d < 0)
						brightness[y][x] = 0;
					else
						brightness[y][x] = d;
				}
		}
	}

	/**
	 * @see com.frank.dip.AbstractImage#subImage(int, int, int, int)
	 */
	@Override
	public HSBImage subImage(int x0, int y0, int xt, int yt)
			throws ArrayIndexOutOfBoundsException
	{
		checkBounds(x0, y0);
		checkBounds(xt, yt);
		int width = xt - x0, height = yt - y0;
		HSBImage image = new HSBImage(width, height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				image.alpha[y][x] = alpha[y0 + y][x0 + x];
				image.hue[y][x] = hue[y0 + y][x0 + x];
				image.saturation[y][x] = saturation[y0 + y][x0 + x];
				image.brightness[y][x] = brightness[y0 + y][x0 + x];
			}
		return image;
	}

	/**
	 * @deprecated In HSB image no combined HSB pixel value, therefore, this
	 *             method has no effect.
	 * @see com.frank.dip.Image#setPixel(int, int, double)
	 */
	@Override
	public void setPixel(int x, int y, double value)
	{
	}
}
