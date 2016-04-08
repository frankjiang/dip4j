/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Image.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip;

/**
 * The abstract image of digital image.
 * <p>
 * In this interface the normal parameters of image is defined, such as width,
 * height and pixel type.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class Image extends AbstractImage implements Cloneable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -7657370378084638265L;

	/**
	 * Construct an empty instance of <tt>Image</tt>.
	 */
	protected Image()
	{
	}

	/**
	 * Construct a copy of specified image.
	 * 
	 * @param image
	 *            the specified image
	 */
	public Image(Image image)
	{
		initialImageByRGBMatrix(image.getRGBMatrix());
	}

	/**
	 * Returns the pixel value at the specified point <code>(x, y)</code>.
	 * <p>
	 * This value may change according to different types of images.
	 * </p>
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the pixel value
	 */
	public abstract int getPixel(int x, int y);

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
	public abstract void setPixel(int x, int y, int value);

	/**
	 * Set the specified value to specified pixel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param value
	 *            the high precision value to set
	 */
	public abstract void setPixel(int x, int y, double value);

	/**
	 * Returns the pixels array of the current image.
	 * <p>
	 * The pixels array will be arranged as [(0,0), (1,0), ... , (w, 0), ... ,
	 * (w, h)].
	 * </p>
	 * The returned array will be a copy of current image. Changing the value of
	 * it will not change the source image.
	 * 
	 * @return the pixels array
	 */
	public int[] getPixelsArray()
	{
		int[] array = new int[width * height];
		int i = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				array[i++] = getPixel(x, y);
		return array;
	}

	/**
	 * Returns the ARGB pixels matrix of the current image.
	 * <p>
	 * The pixels array will be arranged as
	 * 
	 * <pre>
	 * [(0,0), (1,0), ... , (w, 0)]
	 * [(0,1), (1,1), ... , (w, 1)]
	 *  ...
	 * [(0,h), (1,h), ... , (w, h)]
	 * </pre>
	 * </p>
	 * <strong>Warning:</strong> The returned matrix will be a copy of current
	 * image. Changing the value of it will not change the source image.
	 * 
	 * @return the pixels matrix
	 */
	public int[][] getRGBMatrix()
	{
		int[][] matrix = new int[height][width];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				matrix[y][x] = getRGB(x, y);
		return matrix;
	}

	/**
	 * Returns the instance of {@linkplain java.awt.image.BufferedImage}.
	 * 
	 * @return the buffered image instance
	 */
	public java.awt.image.BufferedImage restore()
	{
		return restore(java.awt.image.BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Returns the instance of {@linkplain java.awt.image.BufferedImage} with
	 * speicified image type.
	 * 
	 * @param type
	 *            type of the restored image
	 * @return the buffered image instance
	 * @see java.awt.image.BufferedImage#TYPE_INT_RGB
	 * @see java.awt.image.BufferedImage#TYPE_INT_ARGB
	 * @see java.awt.image.BufferedImage#TYPE_INT_ARGB_PRE
	 * @see java.awt.image.BufferedImage#TYPE_INT_BGR
	 * @see java.awt.image.BufferedImage#TYPE_3BYTE_BGR
	 * @see java.awt.image.BufferedImage#TYPE_4BYTE_ABGR
	 * @see java.awt.image.BufferedImage#TYPE_4BYTE_ABGR_PRE
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_GRAY
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_GRAY
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_BINARY
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_INDEXED
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_565_RGB
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_555_RGB
	 */
	public java.awt.image.BufferedImage restore(int type)
	{
		java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(width, height, type);
		bi.setRGB(0, 0, width, height, getRGBArray(), 0, width);
		return bi;
	}

	/**
	 * Returns the RGB pixel at the specified point <code>(x, y)</code>.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the pixel value
	 */
	public abstract int getRGB(int x, int y);

	/**
	 * Returns the RGB array of the current image.
	 * <p>
	 * The RGB array will be arranged as [(0,0), (1,0), ... , (w, 0), ... , (w,
	 * h)].
	 * </p>
	 * The returned array will be a copy of current image. Changing the value of
	 * it will not change the source image.
	 * 
	 * @return the RGB array
	 */
	public int[] getRGBArray()
	{
		int[] array = new int[width * height];
		int i = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				array[i++] = getRGB(x, y);
		return array;
	}

	/**
	 * Initialize the current image by a specified RGB matrix.
	 * 
	 * @param rgbMatrix
	 *            the specified RGB matrix
	 */
	protected abstract void initialImageByRGBMatrix(int[][] rgbMatrix);

	/**
	 * @see java.lang.Object#clone()
	 */
	public abstract Image clone();

	/**
	 * Recreate an empty image with current image dimension.
	 * 
	 * @return the empty image
	 */
	public abstract Image recreate();

	/**
	 * Recreate an empty image with specified image dimension.
	 * 
	 * @param width
	 *            the specified width
	 * @param height
	 *            the specified height
	 * @return the empty image
	 */
	public abstract Image recreate(int width, int height);
}
