/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Convolver.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.convolver;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.ColorScaleLevel;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;
import com.frank.dip.ImageOperate;

/**
 * The convolver implementation for any kernel.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Convolver<T extends Image> implements ImageOperate<T, T>,
		ColorScaleLevel
{
	/**
	 * Accuracy hint: Interrupt the out range values.
	 * <p>
	 * e.g. <br>
	 * -1 to 0<br>
	 * 267 to 255
	 * </p>
	 */
	public static final int	HINT_ACCURACY_INTERRUPT	= 0;
	/**
	 * Accuracy hint: Normalize the output value to [0,256).
	 */
	public static final int	HINT_ACCURACY_NORMALIZE	= 1;
	/**
	 * Edge hint: Fill the edge with zero.
	 */
	public static final int	HINT_EDGE_FILL_ZERO		= 0;
	/**
	 * Edge hint: Fill the edge with source pixels.
	 */
	public static final int	HINT_EDGE_SOURCE		= 1;
	/**
	 * The kernel of the convolver.
	 */
	protected Kernel		kernel;
	/**
	 * The hint of accuracy dealing.
	 * 
	 * @see #HINT_ACCURACY_INTERRUPT
	 * @see #HINT_ACCURACY_NORMALIZE
	 */
	protected int			accuracyHint;
	/**
	 * The hint of edge filling strategy.
	 * 
	 * @see #HINT_EDGE_FILL_ZERO
	 * @see #HINT_EDGE_SOURCE
	 */
	protected int			edgeHint;

	/**
	 * Construct an instance of <tt>Convolver</tt> with specified kernel and
	 * accuracy hint.
	 * 
	 * @param kernel
	 *            the kernel of convolver
	 * @param accuracyHint
	 *            the hint of accuracy for pixel value calculating
	 */
	public Convolver(Kernel kernel)
	{
		this(kernel, HINT_ACCURACY_INTERRUPT, HINT_EDGE_FILL_ZERO);
	}

	/**
	 * Construct an instance of <tt>Convolver</tt> with specified kernel.
	 * 
	 * @param kernel
	 *            the kernel of convolver
	 * @param accuracyHint
	 *            the hint of accuracy for pixel value calculating
	 * @param edgeHint
	 *            the hint of edge filling strategy
	 * @see #HINT_ACCURACY_INTERRUPT
	 * @see #HINT_ACCURACY_NORMALIZE
	 * @see #HINT_EDGE_FILL_ZERO
	 * @see #HINT_EDGE_SOURCE
	 */
	public Convolver(Kernel kernel, int accuracyHint, int edgeHint)
	{
		this.kernel = kernel;
		this.accuracyHint = accuracyHint;
		this.edgeHint = edgeHint;
	}

	/**
	 * @see com.frank.dip.ImageOperate#operate(com.frank.dip.Image)
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	@Override
	public T operate(T source) throws IllegalImageTypeException
	{
		if (source instanceof BinaryImage)
			return (T) source.clone();
		if (source instanceof GrayImage)
		{
			switch (accuracyHint)
			{
				default:
				case HINT_ACCURACY_INTERRUPT:
					return (T) convolveGrayWithInterrupt((GrayImage) source);
				case HINT_ACCURACY_NORMALIZE:
					return (T) convolveGrayWithNormalize((GrayImage) source);
			}
		}
		if (source instanceof ColorImage)
		{
			switch (accuracyHint)
			{
				default:
				case HINT_ACCURACY_INTERRUPT:
					return (T) convolveColorWithInterrupt((ColorImage) source);
				case HINT_ACCURACY_NORMALIZE:
					return (T) convolveColorWithNormalize((ColorImage) source);
			}
		}
		throw new IllegalImageTypeException(getClass(), source.getClass());
	}

	/**
	 * Perform convolution to the gray image. The accuracy policy will be
	 * interrupting the out of range values.
	 * 
	 * @param gi
	 *            the gray image
	 * @return the gray image after convolution
	 */
	private GrayImage convolveGrayWithInterrupt(GrayImage gi)
	{
		int width = gi.getWidth();
		int height = gi.getHeight();
		int w = kernel.width();
		int h = kernel.height();
		boolean fw = w % 2 == 0;// the flag whether the width is even
		boolean fh = h % 2 == 0;// the flag whether the height is even
		int dw = w / 2;
		int dh = h / 2;
		float[] pixels = new float[w * h];
		float pixel;
		GrayImage res = gi.recreate();
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				// check edge filling
				if (x < dw || x >= width - dw || y < dh || y >= height - dh)
				{
					switch (edgeHint)
					{
						default:
						case HINT_EDGE_FILL_ZERO:
							res.setPixel(x, y, 0);
							break;
						case HINT_EDGE_SOURCE:
							res.setPixel(x, y, gi.getPixel(x, y));
							break;
					}
				}
				else
				{
					for (int j = -dh; fh ? (j < dh) : (j <= dh); j++)
						for (int i = -dw; fw ? (i < dw) : (i <= dw); i++)
							pixels[(j + dh) * w + i + dw] = gi.getPixel(x + i,
									y + i);
					pixel = kernel.perform(pixels);
					if (pixel < 0)
						res.setPixel(x, y, 0);
					else if (pixel < COLOR_SCALE_LEVEL)
						res.setPixel(x, y, (int) pixel);
					else
						res.setPixel(x, y, COLOR_SCALE_LEVEL - 1);
				}
		return res;
	}

	/**
	 * Perform convolution to the gray image. The accuracy policy will be
	 * normalizing all of the pixel values.
	 * 
	 * @param gi
	 *            the gray image
	 * @return the gray image after convolution
	 */
	private GrayImage convolveGrayWithNormalize(GrayImage gi)
	{
		int width = gi.getWidth();
		int height = gi.getHeight();
		int w = kernel.width();
		int h = kernel.height();
		boolean fw = w % 2 == 0;// the flag whether the width is even
		boolean fh = h % 2 == 0;// the flag whether the height is even
		int dw = w / 2;
		int dh = h / 2;
		float[] pixels = new float[w * h];
		float pixel;
		float[][] gray = new float[height][width];
		float minGray = Integer.MAX_VALUE;
		float maxGray = Integer.MIN_VALUE;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				// check edge filling
				if (x < dw || x >= width - dw || y < dh || y >= height - dh)
				{
					switch (edgeHint)
					{
						default:
						case HINT_EDGE_FILL_ZERO:
							gray[y][x] = 0;
							break;
						case HINT_EDGE_SOURCE:
							gray[y][x] = gi.getPixel(x, y);
							break;
					}
				}
				else
				{
					for (int j = -dh; fh ? (j < dh) : (j <= dh); j++)
						for (int i = -dw; fw ? (i < dw) : (i <= dw); i++)
							pixels[(j + dh) * w + i + dw] = gi.getPixel(x + i,
									y + i);
					pixel = kernel.perform(pixels);
					if (minGray > pixel)
						minGray = pixel;
					if (maxGray < pixel)
						maxGray = pixel;
					gray[y][x] = Math.round(pixel);
				}
		GrayImage res = gi.recreate();
		float lenGray = maxGray - minGray;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				res.setPixel(x, y, (gray[y][x] - minGray) / lenGray * 255);
		return res;
	}

	/**
	 * Perform convolution to the gray image. The accuracy policy will be
	 * interrupting the out of range values.
	 * 
	 * @param ci
	 *            the color image
	 * @return the color image after convolution
	 */
	private ColorImage convolveColorWithInterrupt(ColorImage ci)
	{
		int width = ci.getWidth();
		int height = ci.getHeight();
		int w = kernel.width();
		int h = kernel.height();
		boolean fw = w % 2 == 0;// the flag whether the width is even
		boolean fh = h % 2 == 0;// the flag whether the height is even
		int dw = w / 2;
		int dh = h / 2;
		// neighbor pixels
		float[] reds = new float[w * h];
		float[] greens = new float[w * h];
		float[] blues = new float[w * h];
		ColorImage res = ci.recreate();
		// temporary pixels arrays
		float pixel;
		int ix, jy, index;
		for (int y = 0; y < ci.getHeight(); y++)
			for (int x = 0; x < ci.getWidth(); x++)
				// check edge filling
				if (x < dw || x >= width - dw || y < dh || y >= height - dh)
				{
					switch (edgeHint)
					{
						default:
						case HINT_EDGE_FILL_ZERO:
							res.setPixel(x, y, ci.getAlpha(x, y), 0, 0, 0);
							break;
						case HINT_EDGE_SOURCE:
							res.setPixel(x, y, ci.getPixel(x, y));
							break;
					}
				}
				else
				{
					for (int j = -dh; fh ? (j < dh) : (j <= dh); j++)
						for (int i = -dw; fw ? (i < dw) : (i <= dw); i++)
						{
							ix = x + i;
							jy = y + i;
							index = (j + dh) * w + i + dw;
							reds[index] = ci.getRed(ix, jy);
							greens[index] = ci.getGreen(ix, jy);
							blues[index] = ci.getBlue(ix, jy);
						}
					// alpha channel
					res.setAlpha(x, y, ci.getAlpha(x, y));
					// red channel
					pixel = kernel.perform(reds);
					if (pixel < 0)
						res.setRed(x, y, 0);
					else if (pixel < COLOR_SCALE_LEVEL)
						res.setRed(x, y, pixel);
					else
						res.setRed(x, y, COLOR_SCALE_LEVEL - 1);
					// green channel
					pixel = kernel.perform(greens);
					if (pixel < 0)
						res.setGreen(x, y, 0);
					else if (pixel < COLOR_SCALE_LEVEL)
						res.setGreen(x, y, pixel);
					else
						res.setGreen(x, y, COLOR_SCALE_LEVEL - 1);
					// blue channel
					pixel = kernel.perform(blues);
					if (pixel < 0)
						res.setBlue(x, y, 0);
					else if (pixel < COLOR_SCALE_LEVEL)
						res.setBlue(x, y, Math.round(pixel));
					else
						res.setBlue(x, y, COLOR_SCALE_LEVEL - 1);
				}
		return res;
	}

	/**
	 * Perform convolution to the gray image. The accuracy policy will be
	 * normalizing all of the pixel values.
	 * 
	 * @param gi
	 *            the color image
	 * @return the color image after convolution
	 */
	private ColorImage convolveColorWithNormalize(ColorImage ci)
	{
		int width = ci.getWidth();
		int height = ci.getHeight();
		int w = kernel.width();
		int h = kernel.height();
		boolean fw = w % 2 == 0;// the flag whether the width is even
		boolean fh = h % 2 == 0;// the flag whether the height is even
		int dw = w / 2;
		int dh = h / 2;
		// neighbor pixels
		float[] reds = new float[w * h];
		float[] greens = new float[w * h];
		float[] blues = new float[w * h];
		// temporary pixels arrays
		float[][] red = new float[height][width];
		float[][] green = new float[height][width];
		int[][] blue = new int[height][width];
		// maximums and minimums
		float minRed = Integer.MAX_VALUE;
		float maxRed = Integer.MIN_VALUE;
		float minGreen = Integer.MAX_VALUE;
		float maxGreen = Integer.MIN_VALUE;
		float minBlue = Integer.MAX_VALUE;
		float maxBlue = Integer.MIN_VALUE;
		float pixel;
		int ix, jy, index;
		for (int y = 0; y < ci.getHeight(); y++)
			for (int x = 0; x < ci.getWidth(); x++)
				// check edge filling
				if (x < dw || x >= width - dw || y < dh || y >= height - dh)
				{
					switch (edgeHint)
					{
						default:
						case HINT_EDGE_FILL_ZERO:
							red[y][x] = green[y][x] = blue[y][x] = 0;
							break;
						case HINT_EDGE_SOURCE:
							red[y][x] = ci.getRed(x, y);
							green[y][x] = ci.getGreen(x, y);
							blue[y][x] = ci.getBlue(x, y);
							break;
					}
				}
				else
				{
					for (int j = -dh; fh ? (j < dh) : (j <= dh); j++)
						for (int i = -dw; fw ? (i < dw) : (i <= dw); i++)
						{
							ix = x + i;
							jy = y + i;
							index = (j + dh) * w + i + dw;
							reds[index] = ci.getRed(ix, jy);
							greens[index] = ci.getGreen(ix, jy);
							blues[index] = ci.getBlue(ix, jy);
						}
					// red channel
					pixel = kernel.perform(reds);
					if (pixel < minRed)
						minRed = pixel;
					if (pixel > maxRed)
						maxRed = pixel;
					red[y][x] = Math.round(pixel);
					// green channel
					pixel = kernel.perform(greens);
					if (pixel < minGreen)
						minGreen = pixel;
					if (pixel > maxGreen)
						maxGreen = pixel;
					green[y][x] = Math.round(pixel);
					// blue channel
					pixel = kernel.perform(blues);
					if (pixel < minBlue)
						minBlue = pixel;
					if (pixel > maxBlue)
						maxBlue = pixel;
					blue[y][x] = Math.round(pixel);
				}
		ColorImage res = ci.recreate();
		float lenRed = maxRed - minRed;
		float lenGreen = maxGreen - minGreen;
		float lenBlue = maxBlue - minBlue;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				res.setPixel(x, y, ci.getAlpha(x, y),
						Math.round((red[y][x] - minRed) / lenRed * 255),
						Math.round((green[y][x] - minGreen) / lenGreen * 255),
						Math.round((blue[y][x] - minBlue) / lenBlue * 255));
		return res;
	}

	/**
	 * Returns the kernel of convolve.
	 * 
	 * @return the kernel of convolve
	 */
	public Kernel getConvolveKernel()
	{
		return kernel;
	}

	/**
	 * Returns the hint of accuracy dealing.
	 * 
	 * @see #HINT_ACCURACY_INTERRUPT
	 * @see #HINT_ACCURACY_NORMALIZE
	 * @return the hint of accuracy dealing
	 */
	public int getAccuracyHint()
	{
		return accuracyHint;
	}

	/**
	 * Set the hint of accuracy dealing.
	 * 
	 * @see #HINT_ACCURACY_INTERRUPT
	 * @see #HINT_ACCURACY_NORMALIZE
	 * @param accuracyHint
	 *            the hint of accuracy dealing
	 */
	public void setAccuracyHint(int accuracyHint)
	{
		this.accuracyHint = accuracyHint;
	}

	/**
	 * Returns the hint of edge filling strategy.
	 * 
	 * @see #HINT_EDGE_FILL_ZERO
	 * @see #HINT_EDGE_SOURCE
	 * @return the hint of edge filling strategy
	 */
	public int getEdgeHint()
	{
		return edgeHint;
	}

	/**
	 * Set the hint of edge filling strategy.
	 * 
	 * @see #HINT_EDGE_FILL_ZERO
	 * @see #HINT_EDGE_SOURCE
	 * @param edgeHint
	 *            the hint of edge filling strategy
	 */
	public void setEdgeHint(int edgeHint)
	{
		this.edgeHint = edgeHint;
	}
}
