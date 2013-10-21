/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. ColorImage.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip;

/**
 * The color image.
 * <p>
 * A color image consists of 4 different channels of pixels as alpha, red,
 * green, blue. This is the color image of type ARGB. The data will be restored
 * in 4 different matrixes.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ColorImage extends Image implements ColorScaleLevel
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 5388221574139904418L;
	/**
	 * The pixels in alpha channel.
	 */
	protected byte[][]			alpha;
	/**
	 * The pixels in red channel.
	 */
	protected byte[][]			red;
	/**
	 * The pixels in green channel.
	 */
	protected byte[][]			green;
	/**
	 * The pixels in blue channel.
	 */
	protected byte[][]			blue;

	/**
	 * empty constructor
	 */
	private ColorImage()
	{
	}

	/**
	 * Construct an empty color image instance with specified dimension. The
	 * image will be a transparent pure black image.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 */
	public ColorImage(int width, int height)
	{
		alpha = new byte[height][width];
		red = new byte[height][width];
		green = new byte[height][width];
		blue = new byte[height][width];
		this.width = width;
		this.height = height;
	}

	/**
	 * Construct a color image instance with specified dimension and default RGB
	 * value.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param rgb
	 *            the default RGB value
	 */
	public ColorImage(int width, int height, int rgb)
	{
		this(width, height);
		if (width * height != 0 && rgb != 0)
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
				{
					alpha[y][x] = (byte) (rgb >> 24);
					red[y][x] = (byte) ((rgb >> 16) & 0xff);
					green[y][x] = (byte) ((rgb >> 8) & 0xff);
					blue[y][x] = (byte) (rgb & 0xff);
				}
	}

	/**
	 * Construct a color image instance with specified dimension and default RGB
	 * array. The initialization for image color will be performed according to
	 * the RGB array.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param rgbArray
	 *            the RGB array
	 */
	public ColorImage(int width, int height, int[] rgbArray)
	{
		if (rgbArray.length != width * height)
			throw new IllegalArgumentException(
					String.format(
							"The length of input RGB array %d is not match the specified dimension (%d, %d)",
							rgbArray.length, width, height));
		alpha = new byte[height][width];
		red = new byte[height][width];
		green = new byte[height][width];
		blue = new byte[height][width];
		this.width = width;
		this.height = height;
		int rgb;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				rgb = rgbArray[y * width + x];
				alpha[y][x] = (byte) (rgb >> 24);
				red[y][x] = (byte) ((rgb >> 16) & 0xff);
				green[y][x] = (byte) ((rgb >> 8) & 0xff);
				blue[y][x] = (byte) (rgb & 0xff);
			}
	}

	/**
	 * Construct a color image copy of specified image.
	 * 
	 * @param image
	 *            the specified image
	 */
	public ColorImage(Image image)
	{
		super(image);
	}

	/**
	 * Construct a color image instance with specified two dimensional RGB
	 * matrix. In the matrix, the width and height must be determined, each row
	 * shall contains the same columns.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param rgbMatrix
	 *            the RGB matrix
	 */
	public ColorImage(int[][] rgbMatrix)
	{
		initialImageByRGBMatrix(rgbMatrix);
	}

	/**
	 * Construct an empty color image instance according to specified image
	 * instance. If the image is instance of
	 * {@linkplain java.awt.image.BufferedImage}, the observer can be null.
	 * 
	 * @param image
	 *            the specified image.
	 * @param observer
	 *            an object waiting for the image to be loaded.
	 */
	public ColorImage(java.awt.Image image,
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
		alpha = new byte[height][width];
		red = new byte[height][width];
		green = new byte[height][width];
		blue = new byte[height][width];
		int rgb;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				rgb = rgbArray[y * width + x];
				alpha[y][x] = (byte) (rgb >> 24);
				red[y][x] = (byte) ((rgb >> 16) & 0xff);
				green[y][x] = (byte) ((rgb >> 8) & 0xff);
				blue[y][x] = (byte) (rgb & 0xff);
			}
	}

	/**
	 * Returns the pixel at the specified point <code>(x, y)</code>. In which
	 * 
	 * @see com.frank.dip.Image#getPixel(int, int)
	 */
	@Override
	public int getPixel(int x, int y)
	{
		checkBounds(x, y);
		return (alpha[y][x] & 0xff) << 24 | (red[y][x] & 0xff) << 16
				| (green[y][x] & 0xff) << 8 | (blue[y][x] & 0xff);
	}

	/**
	 * Returns the pixel value at specified position in alpha channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the alpha channel pixel value
	 */
	public int getAlpha(int x, int y)
	{
		checkBounds(x, y);
		return alpha[y][x] & 0xff;
	}

	/**
	 * Returns the pixel value at specified position in red channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the red channel pixel value
	 */
	public int getRed(int x, int y)
	{
		checkBounds(x, y);
		return red[y][x] & 0xff;
	}

	/**
	 * Returns the pixel value at specified position in green channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the green channel pixel value
	 */
	public int getGreen(int x, int y)
	{
		checkBounds(x, y);
		return green[y][x] & 0xff;
	}

	/**
	 * Returns the pixel value at specified position in blue channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the blue channel pixel value
	 */
	public int getBlue(int x, int y)
	{
		checkBounds(x, y);
		return blue[y][x] & 0xff;
	}

	/**
	 * @see com.frank.dip.Image#setPixel(int, int, int)
	 */
	@Override
	public void setPixel(int x, int y, int value)
	{
		checkBounds(x, y);
		alpha[y][x] = (byte) (value >> 24);
		red[y][x] = (byte) ((value >> 16) & 0xff);
		green[y][x] = (byte) ((value >> 8) & 0xff);
		blue[y][x] = (byte) (value & 0xff);
	}

	/**
	 * Set the RGB value in the specified position.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param alpha
	 *            the value of alpha channel
	 * @param red
	 *            the value of red channel
	 * @param green
	 *            the value of green channel
	 * @param blue
	 *            the value of blue channel
	 */
	public void setPixel(int x, int y, int alpha, int red, int green, int blue)
	{
		checkBounds(x, y);
		this.alpha[y][x] = (byte) (alpha & 0xff);
		this.red[y][x] = (byte) (red & 0xff);
		this.green[y][x] = (byte) (green & 0xff);
		this.blue[y][x] = (byte) (blue & 0xff);
	}

	/**
	 * Returns the pixel value at specified position in alpha channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param value
	 *            the alpha channel pixel value to set
	 */
	public void setAlpha(int x, int y, int value)
	{
		checkBounds(x, y);
		alpha[y][x] = (byte) value;
	}

	/**
	 * Returns the pixel value at specified position in red channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param value
	 *            the red channel pixel value to set
	 */
	public void setRed(int x, int y, int value)
	{
		checkBounds(x, y);
		red[y][x] = (byte) value;
	}

	/**
	 * Returns the pixel value at specified position in green channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param value
	 *            the green channel pixel value to set
	 */
	public void setGreen(int x, int y, int value)
	{
		checkBounds(x, y);
		green[y][x] = (byte) value;
	}

	/**
	 * Returns the pixel value at specified position in blue channel.
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @param value
	 *            the blue channel pixel value to set
	 */
	public void setBlue(int x, int y, int value)
	{
		checkBounds(x, y);
		blue[y][x] = (byte) value;
	}

	/**
	 * @see com.frank.dip.Image#getRGBArray()
	 */
	@Override
	public int[] getRGBArray()
	{
		int[] array = new int[width * height];
		int i = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				array[i++] = (alpha[y][x] & 0xff) << 24
						| (red[y][x] & 0xff) << 16 | (green[y][x] & 0xff) << 8
						| (blue[y][x] & 0xff);
		return array;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	public ColorImage clone()
	{
		ColorImage clone = new ColorImage();
		clone.width = width;
		clone.height = height;
		clone.alpha = new byte[height][];
		clone.red = new byte[height][];
		clone.green = new byte[height][];
		clone.blue = new byte[height][];
		for (int i = 0; i < height; i++)
		{
			clone.alpha[i] = alpha[i].clone();
			clone.red[i] = red[i].clone();
			clone.green[i] = green[i].clone();
			clone.blue[i] = blue[i].clone();
		}
		return clone;
	}

	/**
	 * Initialize current color image instance with specified two dimensional
	 * RGB matrix. In the matrix, the width and height must be determined, each
	 * row shall contains the same columns.
	 * 
	 * @see com.frank.dip.Image#initialImageByRGBMatrix(int[][])
	 */
	@Override
	protected void initialImageByRGBMatrix(int[][] rgbMatrix)
	{
		height = rgbMatrix.length;
		width = rgbMatrix[0].length;
		alpha = new byte[height][width];
		red = new byte[height][width];
		green = new byte[height][width];
		blue = new byte[height][width];
		int rgb;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				rgb = rgbMatrix[y][x];
				alpha[y][x] = (byte) (rgb >> 24);
				red[y][x] = (byte) ((rgb >> 16) & 0xff);
				green[y][x] = (byte) ((rgb >> 8) & 0xff);
				blue[y][x] = (byte) (rgb & 0xff);
			}
	}

	/**
	 * Returns the 256 gray scale value of the pixel in specified position. The
	 * gray scale will be translated by formula:
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * @param x
	 *            X-coordinate of the position
	 * @param y
	 *            Y-coordinate of the position
	 * @return the 256 gray scale value
	 */
	public int getGray(int x, int y)
	{
		checkBounds(x, y);
		return (int) Math.round(getRed(x, y) * 0.3 + getGreen(x, y) * 0.59
				+ getBlue(x, y) * 0.11);
	}

	/**
	 * Returns the gray scale pixels array of the current image.
	 * <p>
	 * The gray scale pixels array will be arranged as [(0,0), (1,0), ... , (w,
	 * 0), ... , (w, h)].
	 * </p>
	 * <p>
	 * The returned array will be a copy of current image. Changing the value of
	 * it will not change the source image.The gray scale will be translated by
	 * formula:
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @return the gray scale pixels array
	 */
	public int[] getGrayArray()
	{
		int[] gray = new int[width * height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				gray[y * width + x] = (int) Math.round((red[y][x] & 0xff) * 0.3
						+ (green[y][x] & 0xff) * 0.59 + (blue[y][x] & 0xff)
						* 0.11);
		return gray;
	}

	/**
	 * Returns the red channel pixels array of the current image.
	 * <p>
	 * The pixels array will be arranged as [(0,0), (1,0), ... , (w, 0), ... ,
	 * (w, h)].
	 * </p>
	 * 
	 * @return the pixels array
	 */
	public int[] getRedArray()
	{
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				pixels[y * width + x] = (red[y][x] & 0xff);
		return pixels;
	}

	/**
	 * Returns the exact pixels storage matrix of the red channel.
	 * <p>
	 * The pixels array will be arranged as
	 * 
	 * <pre>
	 * [(0,0), (1,0), ... , (w, 0)]
	 * [(0,1), (1,1), ... , (w, 1)]
	 *  ...
	 * [(0,h), (1,h), ... , (w, h)]
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @return red channel matrix
	 */
	public byte[][] getRedMatrix()
	{
		return red;
	}

	/**
	 * Returns the green channel pixels array of the current image.
	 * <p>
	 * The pixels array will be arranged as [(0,0), (1,0), ... , (w, 0), ... ,
	 * (w, h)].
	 * </p>
	 * 
	 * @return the pixels array
	 */
	public int[] getGreenArray()
	{
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				pixels[y * width + x] = (green[y][x] & 0xff);
		return pixels;
	}

	/**
	 * Returns the exact pixels storage matrix of the green channel.
	 * <p>
	 * The pixels array will be arranged as
	 * 
	 * <pre>
	 * [(0,0), (1,0), ... , (w, 0)]
	 * [(0,1), (1,1), ... , (w, 1)]
	 *  ...
	 * [(0,h), (1,h), ... , (w, h)]
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @return green channel matrix
	 */
	public byte[][] getGreenMatrix()
	{
		return green;
	}

	/**
	 * Returns the blue channel pixels array of the current image.
	 * <p>
	 * The pixels array will be arranged as [(0,0), (1,0), ... , (w, 0), ... ,
	 * (w, h)].
	 * </p>
	 * 
	 * @return the pixels array
	 */
	public int[] getBlueArray()
	{
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				pixels[y * width + x] = (blue[y][x] & 0xff);
		return pixels;
	}

	/**
	 * Returns the exact pixels storage matrix of the blue channel.
	 * <p>
	 * The pixels array will be arranged as
	 * 
	 * <pre>
	 * [(0,0), (1,0), ... , (w, 0)]
	 * [(0,1), (1,1), ... , (w, 1)]
	 *  ...
	 * [(0,h), (1,h), ... , (w, h)]
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @return red channel matrix
	 */
	public byte[][] getBlueMatrix()
	{
		return blue;
	}

	/**
	 * @see com.frank.dip.Image#recreate()
	 */
	@Override
	public ColorImage recreate()
	{
		return new ColorImage(width, height);
	}

	/**
	 * @see com.frank.dip.Image#getRGB(int, int)
	 */
	@Override
	public int getRGB(int x, int y)
	{
		checkBounds(x, y);
		return (alpha[y][x] & 0xff) << 24 | (red[y][x] & 0xff) << 16
				| (green[y][x] & 0xff) << 8 | (blue[y][x] & 0xff);
	}
}
