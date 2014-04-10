/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. GrayImage.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip;

/**
 * The 256 gray scale image.
 * <p>
 * A gray image consists of a byte array whose value is arranged from [0,256). <br>
 * In order to low down the storage cost of the image, the pixels data will be
 * stored in byte array.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GrayImage extends Image implements ColorScaleLevel
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= -5700113814726736976L;
	/**
	 * The source image pixels.
	 */
	protected byte[][]			data;

	/**
	 * Construct an empty 256 gray scale image instance with specified
	 * dimension.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 */
	public GrayImage(int width, int height)
	{
		data = new byte[height][width];
		this.width = width;
		this.height = height;
	}

	/**
	 * Construct a 256 gray scale image instance with specified dimension and
	 * default value.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param value
	 *            the default value
	 */
	public GrayImage(int width, int height, int value)
	{
		this(width, height);
		if (width * height != 0 && value != data[0][0])
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					data[y][x] = (byte) value;
	}

	/**
	 * Construct a 256 gray scale image instance with specified dimension and
	 * default RGB array. The initialization for image color will be performed
	 * according to the RGB array. If the input image is a color image, the
	 * image will be transformed to gray image by formula
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param rgbArray
	 *            the RGB array
	 */
	public GrayImage(int width, int height, int[] rgbArray)
	{
		if (rgbArray.length != width * height)
			throw new IllegalArgumentException(
					String.format(
							"The length of input RGB array %d is not match the specified dimension (%d, %d)",
							rgbArray.length, width, height));
		data = new byte[height][width];
		this.width = width;
		this.height = height;
		int p, r, g, b;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				p = rgbArray[y * width + x];
				r = (p >> 16) & 0xff;
				g = (p >> 8) & 0xff;
				b = p & 0xff;
				data[y][x] = (byte) (Math.round(0.3 * r + 0.59 * g + 0.11 * b));
			}
	}

	/**
	 * Construct a 256 gray scale image instance with specified two dimensional
	 * RGB matrix. In the matrix, the width and height must be determined, each
	 * row shall contains the same columns. If the input image is a color image,
	 * the image will be transformed to gray image by formula
	 * 
	 * <pre>
	 * gray = 0.33 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * @param rgbMatrix
	 *            the RGB matrix
	 */
	public GrayImage(int[][] rgbMatrix)
	{
		initialImageByRGBMatrix(rgbMatrix);
	}

	/**
	 * Construct a 256 gray scale image instance with specified two dimensional
	 * gray scale matrix. In the matrix, the width and height must be
	 * determined, each row shall contains the same columns.
	 * 
	 * @param grayMatrix
	 *            the 256 gray scale matrix
	 */
	public GrayImage(byte[][] grayMatrix)
	{
		this.height = grayMatrix.length;
		this.width = grayMatrix[0].length;
		data = new byte[height][width];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				data[y][x] = grayMatrix[y][x];
	}

	/**
	 * Construct an empty 256 gray scale image instance according to specified
	 * image instance. If the input image is a color image, the image will be
	 * transformed to gray image by formula
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * If the image is instance of {@linkplain java.awt.image.BufferedImage},
	 * the observer can be null.
	 * 
	 * @param image
	 *            the specified image.
	 * @param observer
	 *            an object waiting for the image to be loaded.
	 */
	public GrayImage(java.awt.Image image, java.awt.image.ImageObserver observer)
	{
		java.awt.image.BufferedImage bi = null;
		if (image instanceof java.awt.image.BufferedImage)
		{
			bi = (java.awt.image.BufferedImage) image;
			width = bi.getWidth();
			height = bi.getHeight();
		}
		else
		{
			width = image.getWidth(observer);
			height = image.getHeight(observer);
			bi = new java.awt.image.BufferedImage(width, height,
					java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
			java.awt.Graphics g = bi.createGraphics();
			g.drawImage(image, 0, 0, null);
		}
		int[] rgbArray = new int[width * height];
		bi.getRGB(0, 0, width, height, rgbArray, 0, width);
		data = new byte[height][width];
		int p, r, g, b;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				p = rgbArray[y * width + x];
				r = (p >> 16) & 0xff;
				g = (p >> 8) & 0xff;
				b = p & 0xff;
				data[y][x] = (byte) Math.round(0.3 * r + 0.59 * g + 0.11 * b);
			}
	}

	/**
	 * empty constructor
	 */
	private GrayImage()
	{
	}

	/**
	 * Construct a gray image copy of specified image.
	 * 
	 * @param image
	 *            the specified image
	 */
	public GrayImage(Image image)
	{
		super(image);
	}

	/**
	 * @see com.frank.dip.Image#getPixel(int, int)
	 */
	@Override
	public int getPixel(int x, int y)
	{
		checkBounds(x, y);
		return data[y][x] & 0xff;
	}

	/**
	 * @see com.frank.dip.Image#setPixel(int, int, int)
	 */
	@Override
	public void setPixel(int x, int y, int value)
	{
		checkBounds(x, y);
		if (value < 0)
			data[y][x] = 0;
		else if (value < COLOR_SCALE_LEVEL)
			data[y][x] = (byte) value;
		else
			data[y][x] = (byte) (COLOR_SCALE_LEVEL - 1);
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	public GrayImage clone()
	{
		GrayImage clone = new GrayImage();
		clone.width = width;
		clone.height = height;
		clone.data = new byte[height][];
		for (int i = 0; i < height; i++)
			clone.data[i] = data[i].clone();
		return clone;
	}

	/**
	 * Initialize current 256 gray scale image instance with specified two
	 * dimensional RGB matrix. In the matrix, the width and height must be
	 * determined, each row shall contains the same columns. If the input image
	 * is a color image, the image will be transformed to gray image by formula
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * @see com.frank.dip.Image#initialImageByRGBMatrix(int[][])
	 */
	@Override
	protected void initialImageByRGBMatrix(int[][] rgbMatrix)
	{
		this.height = rgbMatrix.length;
		this.width = rgbMatrix[0].length;
		data = new byte[height][width];
		int p, r, g, b;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				p = rgbMatrix[y][x];
				r = (p >> 16) & 0xff;
				g = (p >> 8) & 0xff;
				b = p & 0xff;
				data[y][x] = (byte) Math.round(0.3 * r + 0.59 * g + 0.11 * b);
				// data[y][x] = (byte) Math.round((r+g+b)/3.0);
			}
	}

	/**
	 * @see com.frank.dip.Image#recreate()
	 */
	@Override
	public GrayImage recreate()
	{
		return new GrayImage(width, height);
	}

	/**
	 * @see com.frank.dip.Image#restore()
	 */
	public java.awt.image.BufferedImage restore()
	{
		return restore(java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
	}

	/**
	 * @see com.frank.dip.Image#getRGB(int, int)
	 */
	@Override
	public int getRGB(int x, int y)
	{
		int g = data[y][x] & 0xff;
		return 255 << 24 | g << 16 | g << 8 | g;
	}

	/**
	 * @see com.frank.dip.AbstractImage#subImage(int, int, int, int)
	 */
	@Override
	public GrayImage subImage(int x0, int y0, int xt, int yt)
			throws ArrayIndexOutOfBoundsException
	{
		checkBounds(x0, y0);
		checkBounds(xt - 1, yt - 1);
		int width = xt - x0, height = yt - y0;
		GrayImage image = new GrayImage(width, height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				image.data[y][x] = data[y0 + y][x0 + x];
		return image;
	}

	/**
	 * @see com.frank.dip.Image#setPixel(int, int, double)
	 */
	@Override
	public void setPixel(int x, int y, double value)
	{
		checkBounds(x, y);
		data[x][y] = (byte) Math.round(value);
	}
}
