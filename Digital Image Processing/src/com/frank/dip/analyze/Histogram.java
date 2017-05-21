/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Histogram.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.analyze;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.ColorScaleLevel;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
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
public abstract class Histogram implements ColorScaleLevel
{
	/**
	 * The area of the source image.
	 */
	protected float	area;
	/**
	 * The gray scale histogram.
	 */
	protected int[]	data;
	/**
	 * The maximum scale value.
	 */
	protected int	maximum;
	/**
	 * The minimum scale value.
	 */
	protected int	minimum;

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
		 * The maximum and minimum scale values of the RGB channel.
		 */
		protected int	maximumRed, maximumGreen, maximumBlue, minimumRed,
				minimumGreen, minimumBlue;

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
				int[] pair = new int[2];
				data = histogram(ci.getGrayArray(), pair);
				minimum = pair[0];
				maximum = pair[1];
				red = histogram(ci.getRedArray(), pair);
				minimumRed = pair[0];
				maximumRed = pair[1];
				green = histogram(ci.getGreenArray(), pair);
				minimumGreen = pair[0];
				maximumGreen = pair[1];
				blue = histogram(ci.getBlueArray(), pair);
				minimumBlue = pair[0];
				maximumBlue = pair[1];
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
		public float[] getPDFRed()
		{
			return calculatePDF(red, area);
		}

		/**
		 * Get the probability distribution function array of the green channel
		 * histogram.
		 * 
		 * @return probability distribution function array
		 */
		public float[] getPDFGreen()
		{
			return calculatePDF(green, area);
		}

		/**
		 * Get the probability distribution function array of the blue channel
		 * histogram.
		 * 
		 * @return probability distribution function array
		 */
		public float[] getPDFBlue()
		{
			return calculatePDF(blue, area);
		}

		/**
		 * Returns the accumulated (integral) array of the probability
		 * distribution function (PDF) array of the red channel histogram.
		 * <p>
		 * The value of the array
		 * <code>INT(k) = &int;PDF(x)dx = &sum;PDF(x), x&isin;[0,k];
		 * </p>
		 * 
		 * @return the probability distribution accumulated function (integral)
		 *         array
		 */
		public float[] getIntegralRed()
		{
			return calculateIntegral(getPDFRed());
		}

		/**
		 * Returns the accumulated (integral) array of the probability
		 * distribution function (PDF) array of the green channel histogram.
		 * <p>
		 * The value of the array
		 * <code>INT(k) = &int;PDF(x)dx = &sum;PDF(x), x&isin;[0,k];
		 * </p>
		 * 
		 * @return the probability distribution accumulated function (integral)
		 *         array
		 */
		public float[] getIntegralGreen()
		{
			return calculateIntegral(getPDFGreen());
		}

		/**
		 * Returns the accumulated (integral) array of the probability
		 * distribution function (PDF) array of the blue channel histogram.
		 * <p>
		 * The value of the array
		 * <code>INT(k) = &int;PDF(x)dx = &sum;PDF(x), x&isin;[0,k];
		 * </p>
		 * 
		 * @return the probability distribution accumulated function (integral)
		 *         array
		 */
		public float[] getIntegralBlue()
		{
			return calculateIntegral(getPDFBlue());
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

		/**
		 * Returns the maximum red scale value.
		 * 
		 * @return the maximum red scale value.
		 */
		public int getMaximumRed()
		{
			return maximumRed;
		}

		/**
		 * Returns the minimum red scale value.
		 * 
		 * @return the minimum red scale value.
		 */
		public int getMinimumRed()
		{
			return minimumRed;
		}

		/**
		 * Returns the maximum green scale value.
		 * 
		 * @return the maximum green scale value.
		 */
		public int getMaximumGreen()
		{
			return maximumGreen;
		}

		/**
		 * Returns the minimum green scale value.
		 * 
		 * @return the minimum green scale value.
		 */
		public int getMinimumGreen()
		{
			return minimumGreen;
		}

		/**
		 * Returns the maximum blue scale value.
		 * 
		 * @return the maximum blue scale value.
		 */
		public int getMaximumBlue()
		{
			return maximumBlue;
		}

		/**
		 * Returns the minimum blue scale value.
		 * 
		 * @return the minimum blue scale value.
		 */
		public int getMinimumBlue()
		{
			return minimumBlue;
		}
	}

	/**
	 * empty constructor 
	 */
	protected Histogram() {}
	
	/**
	 * Construct an instance of <tt>Histogram</tt>.
	 * 
	 * @param image
	 *            the source image
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public Histogram(Image image) throws IllegalImageTypeException
	{
		int width = image.getWidth();
		int height = image.getHeight();
		area = width * height;
		int[] pixels = null;
		if (image instanceof ColorImage)
			pixels = ((ColorImage) image).getGrayArray();
		else if (image instanceof BinaryImage || image instanceof GrayImage)
			pixels = image.getPixelsArray();
		if (pixels == null)
			throw new IllegalImageTypeException(getClass(), image.getClass());
		int[] pair = new int[2];
		data = histogram(pixels, pair);
		minimum = pair[0];
		maximum = pair[1];
	}

	/**
	 * Returns the source histogram data of gray scale.
	 * 
	 * @return histogram data
	 */
	public int[] getData()
	{
		return data;
	}

	/**
	 * Returns the probability distribution function (PDF) array of the gray
	 * scale histogram.
	 * 
	 * @return probability distribution array
	 */
	public float[] getPDF()
	{
		return calculatePDF(getData(), area);
	}

	/**
	 * Returns the accumulated (integral) array of the probability distribution
	 * function (PDF) array of the gray scale histogram.
	 * <p>
	 * The value of the array
	 * <code>INT(k) = &int;PDF(x)dx = &sum;PDF(x), x&isin;[0,k];
	 * </p>
	 * 
	 * @return the probability distribution accumulated function (integral)
	 *         array
	 */
	public float[] getIntegral()
	{
		return calculateIntegral(calculatePDF(getData(), area));
	}

	/**
	 * Calculate the accumulated (integral) array according to the specified
	 * probability distribution function (PDF) array.
	 * 
	 * @param pdf
	 *            the probability distribution function (PDF) array
	 * @return the accumulated (integral) array
	 */
	protected float[] calculateIntegral(float[] pdf)
	{
		float[] integral = new float[pdf.length];
		if (pdf.length <= 0)
			return integral;
		integral[0] = pdf[0];
		for (int i = 1; i < pdf.length; i++)
			integral[i] = integral[i - 1] + pdf[i];
		return integral;
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
	 * @param pair
	 *            the entry of minimum scale value and maximum scale value, if
	 *            the array is <code>null</code> or its size is less than 2, the
	 *            statistics has no effect.
	 * @return the histogram
	 */
	protected static int[] histogram(int[] pixels, int[] pair)
	{
		int[] hist = new int[COLOR_SCALE_LEVEL];
		int min = COLOR_SCALE_LEVEL, max = 0;
		for (int pixel : pixels)
		{
			hist[pixel]++;
			if (pixel < min)
				min = pixel;
			if (pixel > max)
				max = pixel;
		}
		if (pair != null && pair.length > 1)
		{
			pair[0] = min;
			pair[1] = max;
		}
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

	/**
	 * Returns the maximum scale value.
	 * 
	 * @return the maximum scale value.
	 */
	public int getMaximum()
	{
		return maximum;
	}

	/**
	 * Returns the minimum scale value.
	 * 
	 * @return the minimum scale value.
	 */
	public int getMinimum()
	{
		return minimum;
	}
}
