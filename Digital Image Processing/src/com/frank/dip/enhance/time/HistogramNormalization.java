/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. HistogramNormalization.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.enhance.time;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.ColorScaleLevel;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.Operator;
import com.frank.dip.analyze.Histogram;
import com.frank.dip.math.Function;
import com.frank.dip.math.InverseIntegralFunction;
import com.frank.dip.math.PiecewiseAverage;

/**
 * The histogram normalization.
 * <p>
 * Histogram normalization is a normal gray stretch algorithm. In this method,
 * the destination gray scale PDF is given by interface {@linkplain Function}.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class HistogramNormalization<T extends Image> extends
		Operator<T, T> implements ColorScaleLevel
{
	/**
	 * The gray scale stretch will be performed according to this function.
	 */
	protected InverseIntegralFunction	function;

	/**
	 * Construct an instance of <tt>HistogramNormalization</tt>.
	 * 
	 * @param function
	 *            the function to perform histogram stretch
	 */
	public HistogramNormalization(Function function)
	{
		this.function = new InverseIntegralFunction(function, 0,
				COLOR_SCALE_LEVEL, 1);
	}

	/**
	 * The binary histogram normalization.
	 * <p>
	 * This class is created for type compatibility. In this transformation, no
	 * transformation will be performed on the binary image, due to the
	 * histogram normalization cannot make any effect in a binary image.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Binary extends
			HistogramNormalization<BinaryImage>
	{
		/**
		 * Construct an instance of <tt>Binary</tt>.
		 * 
		 * @param function
		 */
		public Binary(Function function)
		{
			super(function);
		}

		/**
		 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
		 */
		@Override
		public BinaryImage operate(BinaryImage source)
		{
			return source.clone();
		}
	}

	/**
	 * Gray image histogram normalization.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Gray extends HistogramNormalization<GrayImage>
	{
		/**
		 * Construct an instance of <tt>Gray</tt>.
		 * 
		 * @param function
		 */
		public Gray(Function function)
		{
			super(function);
		}

		/**
		 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
		 */
		@Override
		public GrayImage operate(GrayImage source)
		{
			int height = source.height();
			int width = source.width();
			GrayImage result = source.clone();
			Histogram.Gray hist = (Histogram.Gray) Histogram.histogram(source);
			float[] gray = hist.getPDF();
			for (int i = 1; i < gray.length; i++)
				gray[i] += gray[i - 1];
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					result.setPixel(x, y, limit((int) function
							.function(gray[result.getPixel(x, y)])));
			return result;
		}
	}

	/**
	 * Color image histogram normalization.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Color extends HistogramNormalization<ColorImage>
	{
		/**
		 * Construct an instance of <tt>Color</tt>.
		 * 
		 * @param function
		 */
		public Color(Function function)
		{
			super(function);
		}

		/**
		 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
		 */
		@Override
		public ColorImage operate(ColorImage source)
		{
			int height = source.height();
			int width = source.width();
			ColorImage result = source.clone();
			Histogram.Color hist = (Histogram.Color) Histogram
					.histogram(source);
			float[] red = hist.getRedPDF();
			float[] blue = hist.getBluePDF();
			float[] green = hist.getGreenPDF();
			for (int i = 1; i < red.length; i++)
			{
				red[i] += red[i - 1];
				blue[i] += blue[i - 1];
				green[i] += green[i - 1];
			}
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
				{
					result.setRed(x, y, limit((int) function
							.function(red[result.getRed(x, y)])));
					result.setBlue(x, y, limit((int) function
							.function(blue[result.getBlue(x, y)])));
					result.setGreen(x, y, limit((int) function
							.function(green[result.getGreen(x, y)])));
				}
			return result;
		}
	}

	/**
	 * Returns the normalized image by the specified function according to the
	 * source image.
	 * 
	 * @param function
	 *            the function to perform histogram stretch
	 * @param source
	 *            the source image
	 * @return the image normalized
	 */
	public static <T extends Image> T histogramNormalize(Function function,
			T source)
	{
		if (source instanceof GrayImage)
			return (T) new Gray(function).operate((GrayImage) source);
		if (source instanceof ColorImage)
			return (T) new Color(function).operate((ColorImage) source);
		if (source instanceof BinaryImage)
			return (T) new Binary(function).operate((BinaryImage) source);
		throw new IllegalArgumentException(
				String.format(
						"Current histogram normalization cannot support image type: %s",
						source.getClass().toString()));
	}

	/**
	 * Returns the histogram normalization according to the specified image type
	 * and stretch function.
	 * 
	 * @param function
	 *            the specified stretch function
	 * @param type
	 *            the specified image type
	 * @return the histogram normalization
	 */
	public static <T extends Image> HistogramNormalization<T> getHistogramNormalizatoin(
			Function function, Class<T> type)
	{
		if (type == GrayImage.class)
			return (HistogramNormalization<T>) new Gray(function);
		if (type == ColorImage.class)
			return (HistogramNormalization<T>) new Color(function);
		if (type == BinaryImage.class)
			return (HistogramNormalization<T>) new Binary(function);
		throw new IllegalArgumentException(
				String.format(
						"Current histogram normalization cannot support image type: %s",
						type.toString()));
	}

	/**
	 * Returns the scale average stretch function.
	 * 
	 * @return the average stretch function
	 */
	public static PiecewiseAverage getAverage()
	{
		return new PiecewiseAverage(0, COLOR_SCALE_LEVEL, 1);
	}

	/**
	 * Limit the color scale level in the range of [0, {@code COLOR_SCALE_LEVEL}
	 * ].
	 * 
	 * @param value
	 *            the input color scale level
	 * @return the output value
	 */
	private static int limit(int value)
	{
		return value >= COLOR_SCALE_LEVEL ? (COLOR_SCALE_LEVEL - 1)
				: (value < 0 ? 0 : value);
	}
	// int height = pixels.length;
	// int width = pixels[0].length;
	// int[] histogram = new int[256];
	// // get histogram
	// for (int y = 0; y < height; y++)
	// for (int x = 0; x < width; x++)
	// histogram[pixels[y][x]]++;
	// // stretch histogram
	// double a = 255.0 / (height * width);
	// double[] c = new double[256];
	// c[0] = a * histogram[0];
	// for (int i = 1; i < 256; i++)
	// c[i] = c[i - 1] + a * histogram[i];
	// for (int y = 0; y < height; y++)
	// for (int x = 0; x < width; x++)
	// pixels[y][x] = (int) c[pixels[y][x]];
}
