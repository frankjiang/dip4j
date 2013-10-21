/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Histogram.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.analyze;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.math.MathUtils;

/**
 * The image histogram.
 * <p>
 * In this class, the image histogram is defined, the gray scale histogram is
 * processed default. A new type of histogram can be processed by implementing
 * this abstract class.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class Histogram
{
	/**
	 * The normal color scale level 256.
	 */
	public static final int	SCALE_LEVEL	= 256;
	/**
	 * The area of the source image.
	 */
	protected float			area;
	/**
	 * The gray scale histogram.
	 */
	protected int[]			data;

	/**
	 * Gray scale histogram.
	 * <p>
	 * In this histogram, the image histogram will be performed according to 256
	 * gray scale image type. If a image is a color image, the pixels will be
	 * transformed to 256 gray scale according to formula:
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Gray extends Histogram
	{
		/**
		 * Construct gray scale histogram for the specified image.
		 * 
		 * @param image
		 *            the specified image
		 */
		public Gray(Image image)
		{
			super(image);
		}
	}

	/**
	 * RGB Color histogram.
	 * <p>
	 * In this histogram, the image histogram will be performed according to 256
	 * gray scale image type. If a image is a color image, the pixels will be
	 * transformed to 256 gray scale according to formula:
	 * 
	 * <pre>
	 * gray = 0.3 * red + 0.59 * green + 0.11 * blue
	 * </pre>
	 * 
	 * In the meanwhile, the histogram of red, green and blue channel will also
	 * be calculated.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Color extends Histogram
	{
		/**
		 * Histogram of red channel.
		 */
		protected int[]	red;
		/**
		 * Histogram of green channel.
		 */
		protected int[]	green;
		/**
		 * Histogram of blue channel.
		 */
		protected int[]	blue;

		/**
		 * Construct an instance of <tt>HistogramColor</tt>.
		 * 
		 * @param image
		 */
		public Color(Image image)
		{
			super(image);
			if (image instanceof ColorImage)
			{
				ColorImage ci = (ColorImage) image;
				// data = histogram(ci.getGrayArray());
				red = histogram(ci.getRedArray());
				green = histogram(ci.getGreenArray());
				blue = histogram(ci.getBlueArray());
			}
		}

		/**
		 * Get the source histogram data of red channel.
		 * 
		 * @return histogram data
		 */
		public int[] getRed()
		{
			return red;
		}

		/**
		 * Get the source histogram data of green channel.
		 * 
		 * @return histogram data
		 */
		public int[] getGreen()
		{
			return green;
		}

		/**
		 * Get the source histogram data of blue channel.
		 * 
		 * @return histogram data
		 */
		public int[] getBlue()
		{
			return blue;
		}

		/**
		 * Get the probability distribution function array of the red channel
		 * histogram.
		 * 
		 * @return probability distribution function array
		 */
		public float[] getRedPDF()
		{
			return calculatePDF(red, area);
		}

		/**
		 * Get the probability distribution function array of the green channel
		 * histogram.
		 * 
		 * @return probability distribution function array
		 */
		public float[] getGreenPDF()
		{
			return calculatePDF(green, area);
		}

		/**
		 * Get the probability distribution function array of the blue channel
		 * histogram.
		 * 
		 * @return probability distribution function array
		 */
		public float[] getBluePDF()
		{
			return calculatePDF(blue, area);
		}

		/**
		 * Get the normalized source histogram data of red channel.
		 * 
		 * @param max
		 *            the maximum value of histogram data
		 * @param min
		 *            the minimum value of histogram data
		 * @return the normalized histogram data
		 */
		public int[] getNormalizedRed(int max, int min)
		{
			int[] copy = red.clone();
			MathUtils.normalize(copy, max, min);
			return copy;
		}

		/**
		 * Get the normalized source histogram data of green channel.
		 * 
		 * @param max
		 *            the maximum value of histogram data
		 * @param min
		 *            the minimum value of histogram data
		 * @return the normalized histogram data
		 */
		public int[] getNormalizedGreen(int max, int min)
		{
			int[] copy = green.clone();
			MathUtils.normalize(copy, max, min);
			return copy;
		}

		/**
		 * Get the normalized source histogram data of blue channel.
		 * 
		 * @param max
		 *            the maximum value of histogram data
		 * @param min
		 *            the minimum value of histogram data
		 * @return the normalized histogram data
		 */
		public int[] getNormalizedBlue(int max, int min)
		{
			int[] copy = blue.clone();
			MathUtils.normalize(copy, max, min);
			return copy;
		}
	}

	/**
	 * Construct an instance of <tt>Histogram</tt>.
	 * 
	 * @param image
	 */
	public Histogram(Image image)
	{
		int width = image.width();
		int height = image.height();
		area = width * height;
		int[] pixels = null;
		if (image instanceof ColorImage)
			pixels = ((ColorImage) image).getGrayArray();
		else if (image instanceof BinaryImage || image instanceof GrayImage)
			pixels = image.getPixelsArray();
		if (pixels == null)
			throw new IllegalArgumentException(String.format(
					"Image type of class \"%s\" is not support yet.", image
							.getClass().toString()));
		data = histogram(pixels);
	}

	/**
	 * Get the source histogram data of gray scale.
	 * 
	 * @return histogram data
	 */
	public int[] getData()
	{
		return data;
	}

	/**
	 * Get the probability distribution function array of the gray scale
	 * histogram.
	 * 
	 * @return probability distribution function array
	 */
	public float[] getPDF()
	{
		return calculatePDF(getData(), area);
	}

	/**
	 * Get the normalized source histogram data of gray scale.
	 * 
	 * @param max
	 *            the maximum value of histogram data
	 * @param min
	 *            the minimum value of histogram data
	 * @return the normalized histogram data
	 */
	public int[] getNormalizedData(int max, int min)
	{
		int[] copy = data.clone();
		MathUtils.normalize(copy, max, min);
		return copy;
	}

	/**
	 * Returns the PDF histogram of the specified original histogram. The PDF
	 * calculating obey the formula:
	 * 
	 * <pre>
	 * PDF(i) = histogram(i) / base
	 * </pre>
	 * 
	 * @param histogram
	 *            the specified original histogram
	 * @param base
	 *            PDF base
	 * @return
	 */
	protected static float[] calculatePDF(int[] histogram, float base)
	{
		float[] pdf = new float[histogram.length];
		for (int i = 0; i < pdf.length; i++)
			pdf[i] = histogram[i] / base;
		return pdf;
	}

	/**
	 * Returns the histogram of pixels array. The value in the pixels should be
	 * in the range of [0, {@linkplain #SCALE_LEVEL}).
	 * 
	 * @param pixels
	 *            the histogram original data
	 * @return the histogram
	 */
	protected static int[] histogram(int[] pixels)
	{
		int[] hist = new int[SCALE_LEVEL];
		for (int pixel : pixels)
			hist[pixel]++;
		return hist;
	}

	/**
	 * Returns histogram result of specified image.
	 * 
	 * @param image
	 *            the specified image
	 * @return histogram result
	 */
	public static Histogram histogram(Image image)
	{
		if (image instanceof ColorImage)
			return new Color(image);
		return new Gray(image);
	}

	/**
	 * Returns the area value of the source image.
	 * 
	 * @return the source image area
	 */
	public float getArea()
	{
		return area;
	}
}
