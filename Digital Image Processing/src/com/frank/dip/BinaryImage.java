/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * GrayImage.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip;

import java.awt.Color;

/**
 * The binary image.
 * <p>
 * A binary image consists of only black(0) and white(255) pixels. In order to
 * low down the storage of the image, the source image pixel data will be stored
 * in a boolean matrix, in which <tt>true</tt> represents white and
 * <tt>false</tt> for black.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class BinaryImage extends Image
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= -4600445899600944082L;
	/**
	 * The source image pixels.
	 */
	protected boolean[][]		data;

	/**
	 * empty constructor
	 */
	private BinaryImage()
	{
	};

	/**
	 * Construct a binary image copy of specified image.
	 * 
	 * @param image
	 *            the specified image
	 */
	public BinaryImage(Image image)
	{
		super(image);
	}

	/**
	 * Construct an empty binary image instance with specified dimension.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 */
	public BinaryImage(int width, int height)
	{
		data = new boolean[height][width];
		this.width = width;
		this.height = height;
	}

	/**
	 * Construct a binary image instance with specified dimension and default
	 * value.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param value
	 *            the default value
	 */
	public BinaryImage(int width, int height, boolean value)
	{
		this(width, height);
		if (width * height != 0 && value != data[0][0])
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					data[y][x] = value;
	}

	/**
	 * Construct a binary image instance with specified dimension and default
	 * RGB array. The initialization for image color will be performed according
	 * to the RGB array. If the input image is a color image, the image will be
	 * transformed to gray image by formula
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * Then the gray image will be transformed to binary image according to the
	 * threshold of 127.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param rgbArray
	 *            the RGB array
	 */
	public BinaryImage(int width, int height, int[] rgbArray)
	{
		if (rgbArray.length != width * height)
			throw new IllegalArgumentException(
					String.format(
							"The length of input RGB array %d is not match the specified dimension (%d, %d)",
							rgbArray.length, width, height));
		data = new boolean[height][width];
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
				data[y][x] = (0.3f * r + 0.59f * g + 0.11f * b) > 127;
			}
	}

	/**
	 * Construct a binary image instance with specified two dimensional RGB
	 * matrix. In the matrix, the width and height must be determined, each row
	 * shall contains the same columns. If the input image is a color image, the
	 * image will be transformed to gray image by formula
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * Then the gray image will be transformed to binary image according to the
	 * threshold of 127.
	 * 
	 * @param rgbMatrix
	 *            the RGB matrix
	 */
	public BinaryImage(int[][] rgbMatrix)
	{
		initialImageByRGBMatrix(rgbMatrix);
	}

	/**
	 * Construct a binary image instance with specified two dimensional binary
	 * matrix. In the matrix, the width and height must be determined, each row
	 * shall contains the same columns.
	 * 
	 * @param binaryMatrix
	 *            the RGB matrix
	 */
	public BinaryImage(boolean[][] binaryMatrix)
	{
		this.width = binaryMatrix.length;
		this.height = binaryMatrix[0].length;
		data = new boolean[height][width];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				data[y][x] = binaryMatrix[y][x];
	}

	/**
	 * Construct a binary image instance according to specified image instance.
	 * If the input image is a color image, the image will be transformed to
	 * gray image by formula
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * Then the gray image will be transformed to binary image according to the
	 * threshold of 127. If the image is instance of
	 * {@linkplain java.awt.image.BufferedImage}, the observer can be null.
	 * 
	 * @param image
	 *            the specified image.
	 * @param observer
	 *            an object waiting for the image to be loaded.
	 */
	public BinaryImage(java.awt.Image image,
			java.awt.image.ImageObserver observer)
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
					java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
			java.awt.Graphics g = bi.createGraphics();
			g.drawImage(image, 0, 0, null);
		}
		int[] rgbArray = new int[width * height];
		bi.getRGB(0, 0, width, height, rgbArray, 0, width);
		data = new boolean[height][width];
		int p, r, g, b;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				p = rgbArray[y * width + x];
				r = (p >> 16) & 0xff;
				g = (p >> 8) & 0xff;
				b = p & 0xff;
				data[y][x] = (0.3f * r + 0.59f * g + 0.11f * b) > 127;
			}
	}

	/**
	 * Returns the pixels value 0 for black or 255 for white.
	 * 
	 * @see com.frank.dip.Image#getPixel(int, int)
	 */
	@Override
	public int getPixel(int x, int y)
	{
		checkBounds(x, y);
		return data[y][x] ? 255 : 0;
	}

	/**
	 * @see com.frank.dip.Image#setPixel(int, int, int)
	 */
	@Override
	public void setPixel(int x, int y, int value)
	{
		checkBounds(x, y);
		data[y][x] = value > 127;
	}

	/**
	 * Set the specified value to specified pixel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param value
	 *            the value to set, <tt>true</tt> for white, otherwise, black
	 */
	public void setPixel(int x, int y, boolean value)
	{
		checkBounds(x, y);
		data[y][x] = value;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	public BinaryImage clone()
	{
		BinaryImage clone = new BinaryImage();
		clone.width = width;
		clone.height = height;
		clone.data = new boolean[height][];
		for (int i = 0; i < height; i++)
			clone.data[i] = data[i].clone();
		return clone;
	}

	/**
	 * Initialize current binary image instance with specified two dimensional
	 * RGB matrix. In the matrix, the width and height must be determined, each
	 * row shall contains the same columns. If the input image is a color image,
	 * the image will be transformed to gray image by formula
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * Then the gray image will be transformed to binary image according to the
	 * threshold of 127.
	 * 
	 * @see com.frank.dip.Image#initialImageByRGBMatrix(int[][])
	 */
	@Override
	protected void initialImageByRGBMatrix(int[][] rgbMatrix)
	{
		this.height = rgbMatrix.length;
		this.width = rgbMatrix[0].length;
		data = new boolean[height][width];
		int p, r, g, b;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				p = rgbMatrix[y][x];
				r = (p >> 16) & 0xff;
				g = (p >> 8) & 0xff;
				b = p & 0xff;
				data[y][x] = (0.3f * r + 0.59f * g + 0.11f * b) > 127;
			}
	}

	/**
	 * @see com.frank.dip.Image#recreate()
	 */
	@Override
	public BinaryImage recreate()
	{
		return new BinaryImage(width, height);
	}

	/**
	 * @see com.frank.dip.Image#restore()
	 */
	public java.awt.image.BufferedImage restore()
	{
		return restore(java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
	}

	/**
	 * Returns the {@code boolean} matrix which stores the binary pixels data.
	 * 
	 * <pre>
	 * The pixels are organized as pixels[y][x]. 
	 * x&isin;[0,width)
	 * y&isin;[0,height)
	 * 
	 * </pre>
	 * 
	 * @return the {@code boolean} matrix
	 */
	public boolean[][] getBinaryMatrix()
	{
		return data;
	}

	/**
	 * @see com.frank.dip.Image#getRGB(int, int)
	 */
	@Override
	public int getRGB(int x, int y)
	{
		checkBounds(x, y);
		return data[y][x] ? Color.WHITE.getRGB() : Color.BLACK.getRGB();
	}
}
